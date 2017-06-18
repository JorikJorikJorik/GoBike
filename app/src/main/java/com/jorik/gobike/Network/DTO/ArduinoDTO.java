package com.jorik.gobike.Network.DTO;


import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArduinoDTO implements Parcelable {

  @SerializedName("ArduinoId")
  @Expose
  private Integer arduinoId;

  @SerializedName("Name")
  @Expose
  private String name;

  @SerializedName("Mac")
  @Expose
  private String mac;

  public ArduinoDTO() {
  }

  protected ArduinoDTO(Parcel in) {
    arduinoId = in.readInt();
    name = in.readString();
    mac = in.readString();
  }

  public static final Creator<ArduinoDTO> CREATOR = new Creator<ArduinoDTO>() {
    @Override
    public ArduinoDTO createFromParcel(Parcel in) {
      return new ArduinoDTO(in);
    }

    @Override
    public ArduinoDTO[] newArray(int size) {
      return new ArduinoDTO[size];
    }
  };

  public Integer getArduinoId() {
    return arduinoId;
  }

  public void setArduinoId(Integer arduinoId) {
    this.arduinoId = arduinoId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(arduinoId);
    dest.writeString(name);
    dest.writeString(mac);
  }
}
