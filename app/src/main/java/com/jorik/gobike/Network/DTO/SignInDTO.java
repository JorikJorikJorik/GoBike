package com.jorik.gobike.Network.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jorik on 25.05.17.
 */

public class SignInDTO {

  public SignInDTO(String login, String password, String name, String lastName,
      String photoURL, String city, String phone, String email) {
    this.login = login;
    this.password = password;
    this.name = name;
    this.lastName = lastName;
    this.photoURL = photoURL;
    this.city = city;
    this.phone = phone;
    this.email = email;
  }

  @SerializedName("Login")
  @Expose
  private String login;

  @SerializedName("Password")
  @Expose
  private String password;

  @SerializedName("Name")
  @Expose
  private String name;

  @SerializedName("LastName")
  @Expose
  private String lastName;

  @SerializedName("PhotoURL")
  @Expose
  private String photoURL;

  @SerializedName("City")
  @Expose
  private String city;

  @SerializedName("Phone")
  @Expose
  private String phone;

  @SerializedName("Email")
  @Expose
  private String email;

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhotoURL() {
    return photoURL;
  }

  public void setPhotoURL(String photoURL) {
    this.photoURL = photoURL;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}


