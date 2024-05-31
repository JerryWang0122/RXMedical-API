package com.rxmedical.api.service;

import com.rxmedical.api.model.dto.SaleMaterialDto;
import com.rxmedical.api.model.po.History;
import com.rxmedical.api.model.po.Product;
import com.rxmedical.api.model.po.User;
import com.rxmedical.api.repository.HistoryRepository;
import com.rxmedical.api.repository.ProductRepository;
import com.rxmedical.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

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
}
