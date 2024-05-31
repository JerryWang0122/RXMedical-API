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

    @Transactional
    public Integer callMaterial(SaleMaterialDto callDto) {

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
}
