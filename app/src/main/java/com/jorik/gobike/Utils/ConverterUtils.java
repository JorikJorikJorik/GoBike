package com.jorik.gobike.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class ConverterUtils {

  private static final String DECIMAL_FORMAT = "0.0";
  public static final int COUNT_TICK = 10000000;

  public static int[] convertObjectToBaseInt(Integer[] convertMas) {
    int[] result = new int[convertMas.length];
    for (int i = 0; i < convertMas.length; i++) {
      result[i] = convertMas[i];
    }
    return result;
  }

  public static Integer[] convertBaseToObjectInt(int[] convertMas) {
    Integer[] result = new Integer[convertMas.length];
    for (int i = 0; i < convertMas.length; i++) {
      result[i] = convertMas[i];
    }
    return result;
  }

  public static Double decimal(Double value){
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
    DecimalFormat decimalFormat = (DecimalFormat)numberFormat;
    decimalFormat.applyPattern(DECIMAL_FORMAT);
    return Double.valueOf(decimalFormat.format(value));
  }
  
}
