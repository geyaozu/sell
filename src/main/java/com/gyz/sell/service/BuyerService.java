package com.gyz.sell.service;

import com.gyz.sell.dto.OrderDTO;

public interface BuyerService {

    //查询订单
    OrderDTO findOrderOne(String openid,String orderId);

    OrderDTO cancelOrder(String openid,String orderId);
}
