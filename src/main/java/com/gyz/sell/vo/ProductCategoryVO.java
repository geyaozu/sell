package com.gyz.sell.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gyz.sell.dataobject.ProductCategory;
import lombok.Data;

import java.util.List;

@Data
public class ProductCategoryVO {

    @JsonProperty("name")
    private String productCategoryName;

    @JsonProperty("type")
    private Integer productCategoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;
}
