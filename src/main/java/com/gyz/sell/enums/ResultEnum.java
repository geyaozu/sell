package com.gyz.sell.enums;


import lombok.Getter;

@Getter
public enum ResultEnum {

    PARAM_ERROR(1,"参数不正确"),

    PRODUCT_NOT_EXIST(10,"商品不存在"),

    INVENTORY_SHORTAGE(11,"库存不足"),

    ORDER_NOT_EXIST(12,"订单不存在"),

    ORDERDETAIL_NOT_EXIST(13,"订单详情不存在"),

    ORDERDETAIL_STATUS_ERROR(14,"订单状态不正确"),

    ORDERDETAIL_STATUS_UPDATE_ERROR(15,"取消订单失败"),

    ORDERDETAIL_EMPTY(16,"订单商品详情为空"),

    ORDER_FINSH_ERROR(17,"完结订单失败"),

    PAY_STATUS_ERROR(18,"支付状态不正确"),

    ORDER_UPDATE_FAIL(19, "订单更新失败"),

    PRODUCT_CATEGORY_NOT_EXIST(20, "商品类目不存在"),

    ORDER_OWNER_ERROR(21,"该订单不属于当前用户")
    ;

    private Integer code;

    private String message;


    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
