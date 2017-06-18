package com.jorik.gobike.Model.POJO;

public class TotalStatisticsModel {

  private String title;
  private int resourceIcon;
  private String value;
  private String index;

  public TotalStatisticsModel(String title, int resourceIcon, String value, String index) {
    this.title = title;
    this.resourceIcon = resourceIcon;
    this.value = value;
    this.index = index;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getResourceIcon() {
    return resourceIcon;
  }

  public void setResourceIcon(int resourceIcon) {
    this.resourceIcon = resourceIcon;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getIndex() {
    return index;
  }

  public void setIndex(String index) {
    this.index = index;
  }
}
