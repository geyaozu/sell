package com.gyz.sell.repository;

import com.gyz.sell.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {


    @Autowired
    private ProductCategoryRepository productCategoryRepository;


    @Test
    public void findOne(){
        /*
        springboot版本2.0以上使用findOne方法需要example
         */
//        ProductCategory productCategory = new ProductCategory();
//        productCategory.setCategoryId(1);
//
//        Example<ProductCategory> example = Example.of(productCategory);
//
//        ProductCategory productCategorys = productCategoryRepository.findOne(example).get();
//        System.out.println(productCategorys);

    }

    @Test
    @Transactional //测试数据 测试完成之后直接回滚 不会存到数据库中
    public void saveTest(){
//        ProductCategory productCategory = new ProductCategory();
//        productCategory.setCategoryId(1);
//        Example<ProductCategory> example = Example.of(productCategory);
//        ProductCategory productCategorys = productCategoryRepository.findOne(example).get();

       // ProductCategory productCategory = new ProductCategory();
        //productCategory.setCategoryId(1);
        //productCategory.setCategoryName("男生销榜");
        ProductCategory productCategorys = new ProductCategory("男生最爱",4);
        ProductCategory productCategory = productCategoryRepository.save(productCategorys);
        Assert.assertNotNull(productCategory);
    }

    @Test
    public void findByCategoryTypeInTest(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<ProductCategory> results = productCategoryRepository.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0,results.size());
    }
}