package com.gyz.sell.dataobject;


import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 类目表
 * 如果表名和类名没有一直的话“例如s_produc_category”
 * 可以使用@Table(name="s_produc_category")
 *
 */
//@Table(name="s_produc_category")
@Entity
@DynamicUpdate
@Data
public class ProductCategory {

    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    //类目名字
    private String categoryName;
    //类目类别
    private Integer categoryType;

//    private Date createTime;
//
//    private Date updateTime;


    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }

    public ProductCategory() {
    }
}
