package com.rxmedical.api.service;

import com.rxmedical.api.model.dto.MaterialFileUploadDto;
import com.rxmedical.api.model.dto.MaterialInfoDto;
import com.rxmedical.api.model.dto.MaterialUpdateInfoDto;
import com.rxmedical.api.model.dto.ShowMaterialDto;
import com.rxmedical.api.model.po.History;
import com.rxmedical.api.model.po.Product;
import com.rxmedical.api.model.po.User;
import com.rxmedical.api.repository.HistoryRepository;
import com.rxmedical.api.repository.ProductRepository;
import com.rxmedical.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private HistoryRepository historyRepository;

    // 上傳圖片的儲存位置
    final private String FILE_PATH = "/Users/jerrywang/Intellij-WorkSpace/RXMedical-Web/img/products/";

    /**
     * [搜索] 取得所有產品資訊
     * @return List 商品列表
     */
    public List<ShowMaterialDto> getMaterialList() {
        return productRepository.findAll().stream()
                .map(p -> new ShowMaterialDto(p.getId(), p.getCode(), p.getName(), p.getStock(),
                                                p.getStorage(), p.getCategory()))
                .toList();
    }

    /**
     * [搜索] 取得單一產品的詳細資訊
     * @return MaterialInfoDto 單一商品詳細資料
     */
    public MaterialInfoDto getMaterialInfo(Integer id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product p = optionalProduct.get();
            return new MaterialInfoDto(p.getId(), p.getCode(), p.getName(),
                    p.getCategory(), p.getStorage(), p.getDescription(),
                    p.getPicture());
        }
        return null;
    }

    /**
     * [更新] 更新後台產品的資料
     * @param infoDto 產品要更新的資訊
     * @return
     */
    public Boolean updateMaterialInfo(MaterialUpdateInfoDto infoDto) {
        Optional<Product> optionalProduct = productRepository.findById(infoDto.productId());
        if (optionalProduct.isPresent()) {
            Product p = optionalProduct.get();
            p.setName(infoDto.name());
            p.setCategory(infoDto.category());
            p.setStorage(infoDto.storage());
            p.setDescription(infoDto.description());

            if (infoDto.updatePicture() != null) {
                String imgPath = uploadPicture(infoDto.updatePicture());
                if (imgPath == null) {
                    return false;
                }
                p.setPicture(imgPath);
            }

            productRepository.save(p);
            return true;
        }
        return false;
    }

    /**
     * [新增] 註冊產品資訊
     * @param infoDto 產品資訊及第一筆進貨資料
     * @return Boolean 是否成功
     */
    @Transactional
    public Boolean registerProduct(MaterialFileUploadDto infoDto) {

        // 因為經過aop，所以直接get
        User user = userRepository.findById(infoDto.userId()).get();

        // 上傳檔案並取得檔案路徑
        String imgPath = uploadPicture(infoDto.picture());
        // 上傳的檔案有問題
        if (imgPath == null) {
            return false;
        }

        // 產品資料寫入資料庫
        Product product = new Product();
        product.setCode(infoDto.code());
        product.setName(infoDto.name());
        product.setStock(0);
        product.setDescription(infoDto.description());
        product.setStorage(infoDto.storage());
        product.setPicture(imgPath);
        product.setCategory(infoDto.category());
        Product result = productRepository.save(product);

        // 產品資料寫入歷史紀錄
        History history = new History();
        history.setQuantity(infoDto.quantity());
        history.setPrice(infoDto.price());
        history.setFlow("進");
        history.setProduct(result);
        history.setRecord(null);
        history.setUser(user);
        historyRepository.save(history);

        // 加入第一筆產品庫存
        result.setStock(infoDto.quantity());
        productRepository.save(result);
        return true;
    }

    /**
     * [工具] 上傳圖片
     * @param file 圖片資料
     * @return String fileName
     */
    private String uploadPicture(MultipartFile file) {
        if (file.isEmpty()) {
            System.out.println("沒有檔案");
            return null;
        }
        String fileName = file.getOriginalFilename();   // 得到文件名
        File dest = new File(FILE_PATH + fileName);
        try {
            file.transferTo(dest);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("上傳失敗");
            return null;
        }

    }

}
