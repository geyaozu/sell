package com.gyz.sell.vo;

import lombok.Data;

import java.util.List;

/**
 * 最终返回vo
 */
@Data
public class ResultVO<T> {

    /*返回信息 */

    private String msg;

    private Integer code;

    private T data;


}
