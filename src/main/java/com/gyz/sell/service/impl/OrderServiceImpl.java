package com.gyz.sell.service.impl;

import com.gyz.sell.converter.OrderMaster2OrderDTOConverter;
import com.gyz.sell.dataobject.OrderDetail;
import com.gyz.sell.dataobject.OrderMaster;
import com.gyz.sell.dataobject.ProductInfo;
import com.gyz.sell.dto.CartDTO;
import com.gyz.sell.dto.OrderDTO;
import com.gyz.sell.enums.OrderStatusEnum;
import com.gyz.sell.enums.PayStatusEnum;
import com.gyz.sell.enums.ResultEnum;
import com.gyz.sell.exception.SellException;
import com.gyz.sell.repository.OrderDetailRepository;
import com.gyz.sell.repository.OrderMasterRepository;
import com.gyz.sell.service.OrderService;
import com.gyz.sell.service.ProductInfoService;
import com.gyz.sell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.StringHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.PrimitiveIterator;


@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductInfoService productInfoService;


    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        //查询商品数量/价格
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        OrderMaster orderMaster = new OrderMaster();
        String orderId = KeyUtil.getUniqueKey();

        //减库存集合
        List<CartDTO> cartDTOList = new ArrayList<>();

        for (OrderDetail orderDetail:orderDTO.getOrderDetails()) {
            ProductInfo productInfo = productInfoService.findOne(orderDetail.getProductId());
            if (productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //计算总价
            orderAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())) .add( orderAmount);
            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetail.setDetailId(KeyUtil.getUniqueKey());
            orderDetail.setOrderId(orderId);
            //详情入库
            orderDetailRepository.save(orderDetail);

            CartDTO cartDTO = new CartDTO(orderDetail.getProductId(),orderDetail.getProductQuantity());
            cartDTOList.add(cartDTO);
        }
        //写入数据库(orderMaster orderDetail)
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMaster.setOrderAmount(orderAmount);
        orderMasterRepository.save(orderMaster);


        //扣库存
        productInfoService.decreaseStock(cartDTOList);

        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMasters = new OrderMaster();
        orderMasters.setOrderId(orderId);
        Example example = Example.of(orderMasters);
        Optional<OrderMaster> optionalOrderMaster  =orderMasterRepository.findOne(example);
        OrderMaster orderMaster = null;
        if (optionalOrderMaster.isPresent()){
            orderMaster = optionalOrderMaster.get();
        }
//        OrderMaster orderMaster = (OrderMaster) orderMasterRepository.findOne(example).get();
        if (orderMaster == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        orderDTO.setOrderDetails(orderDetailList);
        return orderDTO;

    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenId, Pageable pageable) {

        Page<OrderMaster> page = orderMasterRepository.findByBuyerOpenid(buyerOpenId,pageable);
        Page<OrderDTO> pageOrderDTO = new PageImpl<OrderDTO>(OrderMaster2OrderDTOConverter.orderMasterList2OrderDTOList(page.getContent()));
//        for (OrderMaster orderMaster: page.getContent()) {
//            if (orderMaster!=null){
//                OrderDTO orderDTO = new OrderDTO();
//                BeanUtils.copyProperties(orderMaster,orderDTO);
//            }
//        }
        return pageOrderDTO;
    }

    @Override
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【取消订单】取消订单不正确,orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDERDETAIL_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        //判断是否修改成功
        OrderMaster orderMasterUpdate = orderMasterRepository.save(orderMaster);
        if (orderMasterUpdate == null){
            log.error("【取消订单】取消订单失败,orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDERDETAIL_STATUS_UPDATE_ERROR);
        }
        //返回库存
        //判断是否有商品
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetails())){
            log.error("【取消订单】商品详情为空，orderDto={}",orderDTO);
            throw new SellException(ResultEnum.ORDERDETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = new ArrayList<>();
        for (OrderDetail orderDetail: orderDTO.getOrderDetails()) {
            CartDTO cartDTO = new CartDTO(orderDetail.getProductId(),orderDetail.getProductQuantity());
//            cartDTO.setProductId();
//            cartDTO.setProductQuantity();
            cartDTOList.add(cartDTO);
        }
        productInfoService.increaseStock(cartDTOList);

        //如果已支付,退款
        if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS)){
            //TODO

        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finsh(OrderDTO orderDTO) {
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【完结订单】订单状态不正确，orderId={}，orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDERDETAIL_STATUS_ERROR);
        }
        //修改订单状态
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster results = orderMasterRepository.save(orderMaster);
        if (results == null){
            log.error("【完结订单】更新失败，orderMaster={}",orderMaster);
           throw new SellException(ResultEnum.ORDER_FINSH_ERROR);
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【订单支付完成】 订单状态不正确，orderDto={}",orderDTO);
            throw new SellException(ResultEnum.ORDERDETAIL_STATUS_ERROR);
        }
        //修改支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
            log.error("【订单支付完成】 订单支付状态不正确，orderDto={}",orderDTO);
            throw new SellException(ResultEnum.PAY_STATUS_ERROR);
        }
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if (result == null){
            log.error("【订单支付完成】 更新订单状态失败，orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }
}
