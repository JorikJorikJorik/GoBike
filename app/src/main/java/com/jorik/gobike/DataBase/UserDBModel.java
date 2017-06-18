package com.jorik.gobike.DataBase;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class UserDBModel extends RealmObject {

  @PrimaryKey
  private long userId;
  private String fullName;
  private String photoUrl;
  private String photoPath;
  private String basicAuthorization;
  private int stateApplication;

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public String getPhotoPath() {
    return photoPath;
  }

  public void setPhotoPath(String photoPath) {
    this.photoPath = photoPath;
  }

  public String getBasicAuthorization() {
    return basicAuthorization;
  }

  public void setBasicAuthorization(String basicAuthorization) {
    this.basicAuthorization = basicAuthorization;
  }

  public int getStateApplication() {
    return stateApplication;
  }

  public void setStateApplication(int stateApplication) {
    this.stateApplication = stateApplication;
  }
}
