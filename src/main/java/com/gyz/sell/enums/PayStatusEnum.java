package com.gyz.sell.enums;


import lombok.Getter;

@Getter
public enum PayStatusEnum {

    WAIT(0,"等待支付"),
    SUCCESS(1,"支付成功"),

    ;
    private String message;

    private Integer code;

    PayStatusEnum(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
