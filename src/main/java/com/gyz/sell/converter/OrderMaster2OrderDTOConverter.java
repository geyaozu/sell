package com.gyz.sell.converter;

import com.gyz.sell.dataobject.OrderMaster;
import com.gyz.sell.dto.OrderDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderMaster2OrderDTOConverter {

    public static OrderDTO orderMaster2OrderDTO(OrderMaster orderMaster){

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        return orderDTO;
    }

    public static List<OrderDTO> orderMasterList2OrderDTOList(List<OrderMaster> orderMasterList){
        List<OrderDTO> orderDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderMasterList)){
            for (OrderMaster orderMaster: orderMasterList) {
                if (orderMaster!=null){
                    OrderDTO orderDTO = new OrderDTO();
                    BeanUtils.copyProperties(orderMaster,orderDTO);
                    orderDTOList.add(orderDTO);
                }
            }
        }
        return orderDTOList;
    }
}
