package com.gyz.sell.utils;

import com.gyz.sell.vo.ResultVO;

public class ResultVOUtils {


    public static ResultVO success(Object  o){
        ResultVO resultVO = new ResultVO();
        resultVO.setMsg("成功");
        resultVO.setCode(0);
        resultVO.setData(o);
        return resultVO;
    }
    public static ResultVO success(){
        return success(null);
    }
    public static ResultVO error(Integer integer,String  msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setMsg(msg);
        resultVO.setCode(integer);
//        resultVO.setData(o);
        return resultVO;
    }


}
