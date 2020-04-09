package com.gyz.sell.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyz.sell.dataobject.OrderDetail;
import com.gyz.sell.dto.OrderDTO;
import com.gyz.sell.enums.ResultEnum;
import com.gyz.sell.exception.SellException;
import com.gyz.sell.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class OrderForm2OrderDTOConverter {

    public static OrderDTO convert(OrderForm orderForm){
        OrderDTO orderDTO = new OrderDTO();
        Gson gson = new Gson();
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        List<OrderDetail> details = new ArrayList<>();
        try {
            details = gson.fromJson(orderForm.getItems(),new TypeToken<List<OrderDetail>>(){}.getType());
        }catch (Exception s){
            log.error("【对象转换】错误, string={}", orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        orderDTO.setOrderDetails(details);

        return orderDTO;
    }
}
