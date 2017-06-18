package com.jorik.gobike.Model.Enum;

public enum ErrorNetworkType {

  BAD_REQUEST("400"),
  UNAUTHORIZED("401"),
  FORBIDDEN("403"),
  NOT_FOUND("404"),
  NOT_ACCEPTABLE("406"),
  GONE("410"),
  ENHANCE_YOUR_CALM("420"),
  TOO_MANY_REQUESTS("429"),
  INTERNAL_SERVER_ERROR("500"),
  BAD_GATEWAY("502"),
  SERVICE_UNAVAILABLE("503"),
  GATEWAY_TIMEOUT("504"),
  JSON_ERROR("lang"),
  HOSTNAME_ERROR("hostname"),
  TIMEOUT_ERROR("timeout"),
  CONNECT_ERROR("connect"),
  NETWORK_ERROR("network"),
  UNKNOWN_ERROR("unknown");

  private String value;

  ErrorNetworkType(String value) {
    this.value = value;
  }

  public static ErrorNetworkType fromValue(String value, boolean searchInValue){
    for(ErrorNetworkType item : ErrorNetworkType.values()){
      String first = searchInValue ? value.toLowerCase() : item.getValue();
      String second = searchInValue ? item.getValue() : value.toLowerCase();
      if(first.contains(second)) return item;
    }
    return UNKNOWN_ERROR;
  }

  public String getValue() {
    return value;
  }
}
