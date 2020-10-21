package com.fuel.consumption.util;

import java.math.BigDecimal;

public class BigDecimalUtil {

    public static final int SCALE = 4;
    public static final int ROUNDING_MODE = BigDecimal.ROUND_CEILING;


    public static BigDecimal summing(BigDecimal firstParam, BigDecimal secondParam) {
        return scale(BigDecimal.ZERO.add(firstParam).add(secondParam));
    }

    public static BigDecimal multiplying(BigDecimal firstParam, BigDecimal secondParam) {
        return firstParam.multiply(secondParam);
    }

    public static BigDecimal scale(BigDecimal param) {
        return param.setScale(SCALE, ROUNDING_MODE).stripTrailingZeros();
    }
}
