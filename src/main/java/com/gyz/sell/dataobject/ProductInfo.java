package com.gyz.sell.dataobject;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * 商品表
 */
@Entity
@Data
public class ProductInfo {

    /**
     *     `product_id` VARCHAR(32) NOT NULL,
     *     `product_name` VARCHAR(64) NOT NULL COMMENT '商品名称',
     *     `product_price` DECIMAL(8,2) NOT NULL COMMENT '单价',
     *     `product_stock` INT NOT NULL COMMENT '库存',
     *     `product_description` VARCHAR(64) COMMENT '描述',
     *     `product_icon` VARCHAR(512) COMMENT '小图',
     *     `product_status` TINYINT(3) DEFAULT '0' COMMENT '商品状态,0正常1下架',
     *     `category_type` INT NOT NULL COMMENT '类目编号',
     *     `create_time` DATETIME  NOT NULL  COMMENT '创建时间',
     *     `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
     *     PRIMARY KEY (`product_id`)
     */
    @javax.persistence.Id
    private String productId;

    private String productName;

    private BigDecimal productPrice;

    private Integer productStock;

    private String productDescription;

    private String productIcon;

    //商品状态 0正常  1 下架
    private Integer productStatus;

    private Integer categoryType;



}
