package com.gyz.sell.service.impl;


import com.gyz.sell.dataobject.ProductInfo;
import com.gyz.sell.dto.CartDTO;
import com.gyz.sell.enums.ProductStatusEnum;
import com.gyz.sell.enums.ResultEnum;
import com.gyz.sell.exception.SellException;
import com.gyz.sell.repository.ProductInfoRepository;
import com.gyz.sell.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {


    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public ProductInfo findOne(String productId) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId(productId);
        Example example = Example.of(productInfo);
        Optional<ProductInfo> optional = productInfoRepository.findOne(example);
        if (!optional.isPresent()){
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        return optional.get();
    }

    @Override
    public List<ProductInfo> findUpAll() {

        return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDto: cartDTOList) {
            ProductInfo productInfo = this.findOne(cartDto.getProductId());
            if (productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            productInfo.setProductStock(productInfo.getProductStock()+cartDto.getProductQuantity());
            this.save(productInfo);
        }


    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDto: cartDTOList) {
            ProductInfo productInfo = this.findOne(cartDto.getProductId());
            if (productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }


            if (productInfo.getProductStock()<cartDto.getProductQuantity()){
                throw new SellException(ResultEnum.INVENTORY_SHORTAGE);
            }
            productInfo.setProductStock(productInfo.getProductStock()-cartDto.getProductQuantity());
            this.save(productInfo);
        }
    }
}
