package com.gyz.sell.service.impl;

import com.gyz.sell.dataobject.ProductInfo;
import com.gyz.sell.enums.ProductStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.print.Pageable;
import java.math.BigDecimal;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoServiceImplTest {

    @Autowired
    private ProductInfoServiceImpl productInfoService;

    @Test
    public void findOne() {
//        productInfoService.findOne("123456");
        Assert.assertEquals("123456",productInfoService.findOne("123456").getProductId());
    }

    @Test
    public void findUpAll() {

        Assert.assertNotEquals(0,productInfoService.findUpAll().size());
    }

    @Test
    public void findAll() {
        PageRequest pageRequest= new PageRequest(0,2);
        Page<ProductInfo> page = productInfoService.findAll(pageRequest);
        //System.out.println(page.getTotalElements());
        Assert.assertNotEquals(0,page.getTotalElements());
    }

    @Test
    public void save() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("1234564323");
        productInfo.setProductName("龙虾");
        productInfo.setProductPrice(new BigDecimal(3));
        productInfo.setProductStock(100);
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        productInfo.setProductDescription("好喝不贵");
        productInfo.setProductIcon("http://xxxxxxx.com");
        productInfo.setCategoryType(1);
        ProductInfo productInfo1 = productInfoService.save(productInfo);
        Assert.assertNotNull(productInfo1);

    }
}