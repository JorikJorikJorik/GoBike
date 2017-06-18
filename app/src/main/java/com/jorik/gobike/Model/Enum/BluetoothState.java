package com.jorik.gobike.Model.Enum;

public enum BluetoothState {
  NONE(0),
  NOT_WORK(1),
  NOT_ENABLE(2),
  ENABLE(3);

  private int value;

  BluetoothState(int value) {
    this.value = value;
  }

  public static BluetoothState fromValue(int value){
    for(BluetoothState item : values()){
      if(item.getValue() == value) return item;
    }
    return NONE;
  }

  public int getValue() {
    return value;
  }
}


