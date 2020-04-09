package com.gyz.sell.exception;

import com.gyz.sell.enums.ResultEnum;
import sun.plugin2.message.Message;

public class SellException extends RuntimeException {

    private Integer code;

    //private String message;


    public SellException(ResultEnum resultEnum) {
       super(resultEnum.getMessage());
       this.code = resultEnum.getCode();
    }
    public SellException(Integer code, String message) {
       super(message);
       this.code = code ;
    }
}
