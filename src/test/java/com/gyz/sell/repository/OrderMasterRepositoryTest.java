package com.gyz.sell.repository;

import com.gyz.sell.dataobject.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {


    @Autowired
    private OrderMasterRepository orderMasterRepository;

    public final String openId = "123123";

    @Test
    public void saveTest(){
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("12231231");
        orderMaster.setBuyerName("张三");
        orderMaster.setBuyerPhone("12333333333");
        orderMaster.setBuyerAddress("beijing");
        orderMaster.setBuyerOpenid("123123");
        orderMaster.setOrderAmount(new BigDecimal(2.3));

        OrderMaster result = orderMasterRepository.save(orderMaster);
        Assert.assertNotEquals(null,result);
    }

    @Test
    public void findByBuyerOpenid() {
        PageRequest pageRequest = PageRequest.of(0,1);
        Page<OrderMaster> page = orderMasterRepository.findByBuyerOpenid(openId,pageRequest);
        Assert.assertNotEquals(0,page.getTotalElements());
    }
}