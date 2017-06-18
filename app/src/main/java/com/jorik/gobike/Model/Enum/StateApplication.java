package com.jorik.gobike.Model.Enum;

public enum StateApplication {

  LOGIN(1),
  ENTER(2);

  private int value;

  StateApplication(int i) {
    value = i;
  }

  public  static StateApplication fromValue(int value){
      for(StateApplication item : StateApplication.values()){
        if(item.value == value) return item;
      }
      return LOGIN;
  }

  public int getValue() {
    return value;
  }
}
