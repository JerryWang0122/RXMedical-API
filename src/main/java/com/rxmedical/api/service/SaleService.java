package com.rxmedical.api.service;

import com.rxmedical.api.model.dto.*;
import com.rxmedical.api.model.po.History;
import com.rxmedical.api.model.po.Product;
import com.rxmedical.api.model.po.Record;
import com.rxmedical.api.model.po.User;
import com.rxmedical.api.repository.HistoryRepository;
import com.rxmedical.api.repository.ProductRepository;
import com.rxmedical.api.repository.RecordRepository;
import com.rxmedical.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class SaleService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecordRepository recordRepository;

    /**
     * [後台] 將訂單狀態從待確認往待撿貨推送
     * @param recordId 操作訂單ID
     * @return 正常: null, 不正常: [errorMsg]
     */
    public synchronized String pushToPicking(Integer recordId) {
        Optional<Record> optionalRecord = recordRepository.findById(recordId);
        if (optionalRecord.isEmpty()) {
            return "找不到訂單";
        }
        Record record = optionalRecord.get();
        if (!record.getStatus().equals("unchecked")) {
            return "訂單狀態已轉移";
        }
        record.setStatus("picking");
        recordRepository.save(record);
        return null;
    }

    /**
     * [後台] 將訂單狀態從待確認往取消推送
     * @param recordId 操作訂單ID
     * @return 正常: null, 不正常: [errorMsg]
     */
    @Transactional
    public synchronized String pushToRejected(Integer recordId) {
        Optional<Record> optionalRecord = recordRepository.findById(recordId);
        if (optionalRecord.isEmpty()) {
            return "找不到訂單";
        }
        Record record = optionalRecord.get();
        if (!record.getStatus().equals("unchecked")) {
            return "訂單狀態已轉移";
        }
        record.setStatus("rejected");
        recordRepository.save(record);
        // 把該訂單的History quantity 還給 Product stock
        List<History> recordDetails = historyRepository.findByRecord(record);
        recordDetails.forEach(history -> {
                         Product product = history.getProduct();
                         product.setStock(product.getStock() + history.getQuantity());
                         productRepository.save(product);
                     });
        return null;
    }

    /**
     * [後台] 取得訂單明細
     * @param recordId 訂單ID
     * @return (null 代表沒這個訂單)，List為明細資料
     */
    public synchronized List<OrderDetailDto> getOrderDetails(Integer recordId) {
        Optional<Record> optionalRecord = recordRepository.findById(recordId);
        if (optionalRecord.isEmpty()){ // 因為有檢查過，所以訂單內不可能為空，回傳null代表沒有這一個訂單編號
            return null;
        }
        List<History> recordDetails = historyRepository.findByRecord(optionalRecord.get());
        return recordDetails.stream()
                .map(history -> new OrderDetailDto(history.getProduct().getName(), history.getQuantity(), null))
                .toList();
    }

    /**
     * [後台] 取得所有未確認訂單概況
     * @return List 未確認訂單列表
     */
    public synchronized List<OrderListDto> getUncheckedOrderList() {
        List<Record> records = recordRepository.findByStatus("unchecked");
        return records.stream()
                      .map(r -> new OrderListDto(
                                        r.getId(),
                                        r.getCode(),
                                        historyRepository.countByRecord(r),
                                        new OrderDemanderDto(
                                                r.getDemander().getDept(),
                                                r.getDemander().getTitle(),
                                                r.getDemander().getName()
                                        ),
                                        null))
                      .toList();
    }

    /**
     * [後台] 取得所有取消訂單概況
     * @return List 取消訂單列表
     */
    public synchronized List<OrderListDto> getRejectedOrderList() {
        List<Record> records = recordRepository.findByStatus("rejected");
        return records.stream()
                .map(r -> new OrderListDto(
                            r.getId(),
                            r.getCode(),
                            historyRepository.countByRecord(r),
                            new OrderDemanderDto(
                                    r.getDemander().getDept(),
                                    r.getDemander().getTitle(),
                                    r.getDemander().getName()
                            ),
                            null))
                .toList();
    }

    /**
     * [前台] 產生衛材申請單
     * @param recordDto 申請資訊[申請人及申請項目]
     * @return String -> 檢查狀況，若為null則代表通過，生成訂單
     */
    @Transactional
    public synchronized String checkOrder(ApplyRecordDto recordDto) {
        if (recordDto.applyItems().isEmpty()) {
            return "沒有申請項目";
        }

        // 錯誤清單
        List<String> errorList = new ArrayList<>();

        // 先檢查有沒有不存在的貨號
        for (ApplyItemDto item : recordDto.applyItems()) {
            Optional<Product> optionalProduct = productRepository.findById(item.productId());
            if (optionalProduct.isEmpty()) {
                return "貨號不存在";
            } else {
                Product p = optionalProduct.get();
                // 如果貨不夠
                if (p.getStock() < item.applyQty()) {
                    errorList.add("[" + p.getName() + "]庫存: " + p.getStock());
                }
            }
        }
        // 如果errorList裡面有東西，回傳
        if (!errorList.isEmpty()) {
            return String.join("<br>", errorList);
        }
        //---------------- 檢查完成，生成訂單 ---------------
        // 已經通過aop檢查了，直接拿
        User demander = userRepository.findById(recordDto.userId()).get();

        Record record = new Record();
        record.setCode(generateCode());
        record.setStatus("unchecked");
        record.setDemander(demander);

        Record r = recordRepository.save(record);

        recordDto.applyItems().stream().forEach(item -> {
            // 因為上面檢查過了，直接拿
            Product p = productRepository.findById(item.productId()).get();
            // 更新庫存
            p.setStock(p.getStock() - item.applyQty());
            productRepository.save(p);

            // 產生歷史紀錄
            History history = new History();
            history.setQuantity(item.applyQty());
            history.setPrice(0);
            history.setFlow("售");
            history.setProduct(p);
            history.setRecord(r);
            historyRepository.save(history);
        });

        return null;

    }

    /**
     * [後台 - 衛材進銷] 進貨
     * @param callDto 進貨資料
     * @return Integer 最新庫存
     */
    @Transactional
    public synchronized Integer callMaterial(SaleMaterialDto callDto) {

        Optional<Product> optionalProduct = productRepository.findById(callDto.materialId());
        // 因為有過aop了，所以直接拿
        User user = userRepository.findById(callDto.userId()).get();

        // 商品不存在則直接退回
        if (optionalProduct.isEmpty()) {
            return null;
        }

        Product p = optionalProduct.get();

        History history = new History();
        history.setQuantity(callDto.quantity());
        history.setPrice(callDto.price());
        history.setFlow("進");
        history.setProduct(p);
        history.setUser(user);
        historyRepository.save(history);

        // 更新庫存
        p.setStock(p.getStock() + callDto.quantity());
        productRepository.save(p);
        return p.getStock();
    }

    /**
     * [後台 - 衛材進銷] 銷毀貨品
     * @param destroyDto 銷毀貨物資料
     * @return Integer 最新庫存，null表示找不到貨，-[number]表示庫存不足 -> number 表示目前庫存量
     */
    @Transactional
    public synchronized Integer destroyMaterial(SaleMaterialDto destroyDto) {
        Optional<Product> optionalProduct = productRepository.findById(destroyDto.materialId());
        // 因為有過aop了，所以直接拿
        User user = userRepository.findById(destroyDto.userId()).get();

        // 商品不存在則直接退回
        if (optionalProduct.isEmpty()) {
            return null;
        }

        Product p = optionalProduct.get();

        // 檢查存貨量
        if (p.getStock() < destroyDto.quantity()) {
            return -p.getStock();
        }

        History history = new History();
        history.setQuantity(destroyDto.quantity());
        history.setPrice(destroyDto.price());
        history.setFlow("銷");
        history.setProduct(p);
        history.setUser(user);
        historyRepository.save(history);

        // 更新庫存
        p.setStock(p.getStock() - destroyDto.quantity());
        productRepository.save(p);
        return p.getStock();
    }


    /**
     * [工具] 產生訂單編號
     * 產生的code：格式為當天日期加四位的大寫英文和數字的組合亂數 [YYYYMMDDXXXX]
     * @return String 訂單編號
     */
    private synchronized String generateCode() {
        // 获取当前日期并格式化为YYYYMMDD
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        String code = date + generateRandomCode(4);

        // 判断是否已经存在该订单
        while (recordRepository.existsByCode(code)) {
            code = date + generateRandomCode(4);
        }

        // 返回日期和乱数组合的字符串
        return code;
    }

    /**
     * [工具] 輔助generateCode()方法，生成四位的大寫英文和数字的组合乱数
     * @param length 產成數字的位數
     * @return String 4位的大寫英文和數字的組合
     */
    private synchronized String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        return sb.toString();
    }
}
