package com.gyz.sell.controller;


import com.gyz.sell.dataobject.ProductCategory;
import com.gyz.sell.dataobject.ProductInfo;
import com.gyz.sell.service.ProductCategoryService;
import com.gyz.sell.service.ProductInfoService;
import com.gyz.sell.utils.ResultVOUtils;
import com.gyz.sell.vo.ProductCategoryVO;
import com.gyz.sell.vo.ProductInfoVO;
import com.gyz.sell.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

//    @Autowired
//    private
    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductInfoService productInfoService;

    @GetMapping("/list")
    public ResultVO list(){

        //查出所有上线商品
        List<ProductInfo> productInfoList = productInfoService.findUpAll();
        List<Integer> categoryTypeList = new ArrayList<>();
        System.out.println("");
        for (ProductInfo productInfo : productInfoList) {
            categoryTypeList.add(productInfo.getCategoryType());
        }
        //差类目
        List<ProductCategoryVO> productCategoryVOList = new ArrayList<>();
        List<ProductCategory> productCategoryList = productCategoryService.findByCategoryTypeIn(categoryTypeList);
        for (ProductCategory productCategory: productCategoryList) {
            ProductCategoryVO productCategoryVO = new ProductCategoryVO();
            productCategoryVO.setProductCategoryName(productCategory.getCategoryName());
            productCategoryVO.setProductCategoryType(productCategory.getCategoryType());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo:productInfoList ) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productCategoryVO.setProductInfoVOList(productInfoVOList);
            productCategoryVOList.add(productCategoryVO);
        }


        return ResultVOUtils.success(productCategoryVOList);
    }
}
