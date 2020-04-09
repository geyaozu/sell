package com.gyz.sell.controller;


import com.gyz.sell.converter.OrderForm2OrderDTOConverter;
import com.gyz.sell.dto.OrderDTO;
import com.gyz.sell.enums.ResultEnum;
import com.gyz.sell.exception.SellException;
import com.gyz.sell.form.OrderForm;
import com.gyz.sell.service.BuyerService;
import com.gyz.sell.service.OrderService;
import com.gyz.sell.utils.ResultVOUtils;
import com.gyz.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/buyer/order")
public class BuyerOrderController {


    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    //创建订单
    @PostMapping("/create")
    public ResultVO<Map<String,String>> create(@Valid OrderForm orderForm, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            log.error("【创建订单】参数不正确，orderForm",orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        OrderDTO result = orderService.create(orderDTO);
        Map<String, String> map = new HashMap<>();
        map.put("orderId", result.getOrderId());

        return ResultVOUtils.success(map);
    }


    //查询订单列表
    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                         @RequestParam(value = ("page"),defaultValue = "0") Integer page,
                                         @RequestParam(value = ("size"),defaultValue = "10") Integer size){
        if (StringUtils.isEmpty(openid)){
            log.error("【查询订单列表】openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);

        }
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<OrderDTO> page1 = orderService.findList(openid,pageRequest);
        return ResultVOUtils.success(page1.getContent());
    }

    //查询订单详情
    @GetMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestParam("openid") String openid,
                                     @RequestParam("orderId") String orderId){
        if (StringUtils.isEmpty(openid)||StringUtils.isEmpty(orderId)){
            log.error("【查询订单详情】openid或orderId为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        OrderDTO orderDTO = buyerService.findOrderOne(openid,orderId);
        return ResultVOUtils.success(orderDTO);
    }

    @PostMapping("cancel")
    public ResultVO cancel(@RequestParam("openid") String openid,
                           @RequestParam("orderId") String orderId){
        if (StringUtils.isEmpty(openid)||StringUtils.isEmpty(orderId)){
            log.error("【查询订单详情】openid或orderId为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        buyerService.cancelOrder(openid,orderId);
        return ResultVOUtils.success();
    }
}
