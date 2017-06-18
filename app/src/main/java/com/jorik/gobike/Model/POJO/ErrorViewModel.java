package com.jorik.gobike.Model.POJO;


import com.jorik.gobike.Model.Enum.ErrorViewType;

public class ErrorViewModel {

  private ErrorViewType errorType;
  private int iconResource;
  private String textError;
  private boolean isRefresh;
  private Runnable action;

  public ErrorViewType getErrorType() {
    return errorType;
  }

  public void setErrorType(ErrorViewType errorType) {
    this.errorType = errorType;
  }

  public int getIconResource() {
    return iconResource;
  }

  public void setIconResource(int iconResource) {
    this.iconResource = iconResource;
  }

  public String getTextError() {
    return textError;
  }

  public void setTextError(String textError) {
    this.textError = textError;
  }

  public boolean isRefresh() {
    return isRefresh;
  }

  public void setRefresh(boolean refresh) {
    isRefresh = refresh;
  }

  public Runnable getAction() {
    return action;
  }

  public void setAction(Runnable action) {
    this.action = action;
  }
}
