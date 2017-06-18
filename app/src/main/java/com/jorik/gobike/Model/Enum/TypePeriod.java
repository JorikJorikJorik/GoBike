package com.jorik.gobike.Model.Enum;

public enum TypePeriod {
  NONE(0),
  YEAR(1),
  MONTH(2),
  WEEK(3);

  private int value;

  TypePeriod(int value) {
    this.value = value;
  }

  public static TypePeriod fromValue(int value) {
    for (TypePeriod item : values()) {
      if (item.getValue() == value) {
        return item;
      }
    }
    return NONE;
  }

  public int getValue() {
    return value;
  }
}
