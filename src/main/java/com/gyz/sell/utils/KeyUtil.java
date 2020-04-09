package com.gyz.sell.utils;

import jdk.internal.dynalink.beans.StaticClass;

import java.util.Random;

public class KeyUtil {

    public static String getUniqueKey(){
        Random random = new Random();
        Integer q = random.nextInt(90)+10;
        return q+System.currentTimeMillis()+"";
    }
}
