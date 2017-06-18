package com.jorik.gobike.Network.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ArduinoListDTO {

  @SerializedName("ArduinoList")
  @Expose
  private List<ArduinoDTO> mArduinoDTOs;

  public List<ArduinoDTO> getArduinoDTOs() {
    return mArduinoDTOs;
  }

  public void setArduinoDTOs(List<ArduinoDTO> arduinoDTOs) {
    mArduinoDTOs = arduinoDTOs;
  }
}
