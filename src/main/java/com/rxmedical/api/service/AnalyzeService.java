package com.rxmedical.api.service;

import com.rxmedical.api.model.dto.CallAdviceDto;
import com.rxmedical.api.model.dto.SafetyRatioDto;
import com.rxmedical.api.model.po.History;
import com.rxmedical.api.model.po.Product;
import com.rxmedical.api.model.po.Record;
import com.rxmedical.api.repository.HistoryRepository;
import com.rxmedical.api.repository.ProductRepository;
import com.rxmedical.api.repository.RecordRepository;
import com.rxmedical.api.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AnalyzeService {

    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private ProductRepository productRepository;

    /**
     * [後台 - 分析] 取得並計算勞動積分
     * @return Map "WHO" has "HOW MUCH" LABOR SCORE
     */
    public Map<String, Integer> getLaborScore() {
        // 分數加成，可自行調整
        final int TRANSPORTER_FACTOR = 2;
        final int TAKER_FACTOR = 5;

        // 找出近30日，完成的申請單
        // 獲取當前日期和時間
        Calendar calendar = Calendar.getInstance();

        // 將日期設置為30天前
        calendar.add(Calendar.DAY_OF_YEAR, -30);

        // 獲取計算後的日期
        Date monthAgoDate = calendar.getTime();
        List<Record> finishRecords = recordRepository.findByStatusAndUpdateDateAfter("finish", monthAgoDate);

        Map<String, Integer> map = new HashMap<>();
        finishRecords.forEach(record -> {
            List<History> recordDetails = historyRepository.findByRecord(record);

            // 计算撿貨人分數
            recordDetails.stream()
                    .map(history -> history.getUser().getName())
                    .forEach(taker -> map.merge(taker, TAKER_FACTOR, Integer::sum));

            // 计算運送人分數
            String transporterName = record.getTransporter().getName();
            // 運送分 = 訂單明細數 * 運送加成
            int transportScore = recordDetails.size() * TRANSPORTER_FACTOR;
            map.merge(transporterName, transportScore, Integer::sum);
        });
        return map;
    }

    /**
     * [後台 - 分析] 取得庫存比率過低的前三名
     * @return List
     */
    public List<SafetyRatioDto> getMaterialSafetyRatio() {
        // 取出所有產品，並利用庫存量和safetyThreshold計算庫存比例，取出最低的前3名並回傳
        List<Product> allMaterials = productRepository.findAll();

        return allMaterials.stream()
                .sorted(Comparator.comparingDouble(material -> (double) material.getStock() / material.getSafetyThreshold()))
                .limit(3)
                .map(material -> new SafetyRatioDto(material.getName(), material.getCode(),
                        (float) material.getStock() / (material.getSafetyThreshold() * 4)))  // safetyThreshold * "4" 計算一個月的安全比率
                .collect(Collectors.toList());
    }

    /**
     * [後台 - 分析] 取得特定商品建議進貨量趨勢圖
     * @param productId
     * @return
     */
    public List<CallAdviceDto> getCallMaterialDiagram(Integer productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            return null;
        }

        Product product = optionalProduct.get();

        // 取得當週的週日，以及八週前的日期
        Date startDateOfWeek = DateUtil.getStartOfWeek(new Date());
        Date eightWeeksAgo = DateUtil.getDateWeeksAgo(startDateOfWeek, 7);  // 7 是因為上面已經包含一週了

        // 取得該商品八週內所有紀錄
        List<History> historyList = historyRepository.findByProductAndUpdateDateAfter(product, eightWeeksAgo);
        historyList = historyList.stream()
                // 進貨和銷毀都是由管理員執行的，要考慮
                .filter(history -> "進".equals(history.getFlow()) || "銷".equals(history.getFlow()) ||
                        !"rejected".equals(history.getRecord().getStatus())) // 取消的訂單不用計算
                .sorted(Comparator.comparing(History::getUpdateDate).reversed())// 依照更新日期降序排列
                .toList();


        // ---------------- 資料分析 -----------------
        Integer currStock = product.getStock();
        int maxStock = product.getSafetyThreshold() * 6;

        // 建立一個結構來儲存每週的庫存資料
        // index 0 代表當前週，index 7 代表八週前，以此類推
        int[] weeklyData = new int[8];
        weeklyData[0] = currStock;
        int weekAgo = 0;

        // 將資料分配到對應的週
        for (History history : historyList) {
            Date historyDate = history.getUpdateDate();

            // 交易紀錄發生在更早之前，需要調整 weekAgo(index) 數值
            while (historyDate.before(DateUtil.getDateWeeksAgo(startDateOfWeek, weekAgo))) {
                // 當週庫存往前一週推
                weeklyData[weekAgo + 1] = weeklyData[weekAgo];
                // 計算當週建議進貨量
                weeklyData[weekAgo] = Math.max(maxStock - weeklyData[weekAgo], 0);
                // 加一週
                weekAgo++;
            }

            // 因為是在計算過去，所以進銷的加減要反過來
            if ("進".equals(history.getFlow())) {
                weeklyData[weekAgo] -= history.getQuantity();
            } else {
                weeklyData[weekAgo] += history.getQuantity();
            }
        }

        // 如果有好幾週沒進銷資料，建議庫存要往後遞延
        while (weekAgo < 8) {
            weeklyData[weekAgo] = weeklyData[weekAgo - 1];
            System.out.println(weekAgo + " " + weeklyData[weekAgo]);
            weekAgo++;
        }

        return IntStream.range(0, weeklyData.length)
                .mapToObj(index -> new CallAdviceDto(DateUtil.getDateWeeksAgo(startDateOfWeek, index), weeklyData[index]))
                .sorted(Comparator.comparing(CallAdviceDto::startOfWeek))
                .toList();
    }


}
