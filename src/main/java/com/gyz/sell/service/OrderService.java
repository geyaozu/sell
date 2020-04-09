package com.gyz.sell.service;

import com.gyz.sell.dataobject.OrderMaster;
import com.gyz.sell.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    /**
     * 创建订单
     * @param orderMaster
     * @return
     */
    OrderDTO create(OrderDTO orderDTO);


    /**
     * 查询单个订单
     */
    OrderDTO findOne(String orderId);

    /**
     * 查询订单列表
     */
    Page<OrderDTO> findList(String buyerOpenId, Pageable pageable);

    /**
     * 取消订单
     */
    OrderDTO cancel (OrderDTO orderDTO);

    /**
     * 完结订单
     */
    OrderDTO finsh (OrderDTO orderDTO);

    /**
     * 支付订单
     */
    OrderDTO paid (OrderDTO orderDTO);

}
