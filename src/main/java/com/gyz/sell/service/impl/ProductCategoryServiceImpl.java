package com.gyz.sell.service.impl;

import com.gyz.sell.dataobject.ProductCategory;
import com.gyz.sell.enums.ResultEnum;
import com.gyz.sell.exception.SellException;
import com.gyz.sell.repository.ProductCategoryRepository;
import com.gyz.sell.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;


    @Override
    public ProductCategory findOne(Integer categoryId) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryId(categoryId);
        Example<ProductCategory> example = Example.of(productCategory);
        Optional<ProductCategory> optional = productCategoryRepository.findOne(example);
        if (!optional.isPresent()){
            throw new SellException(ResultEnum.PRODUCT_CATEGORY_NOT_EXIST);
        }
        return optional.get();
    }

    @Override
    public List<ProductCategory> findAll() {
        return productCategoryRepository.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return productCategoryRepository.findByCategoryTypeIn(categoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }
}
