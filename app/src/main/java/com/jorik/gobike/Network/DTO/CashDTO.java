package com.jorik.gobike.Network.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CashDTO<T> {

  @SerializedName("Type")
  @Expose
  private String type;

  @SerializedName("Data")
  @Expose
  private T data;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
