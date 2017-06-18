package com.jorik.gobike.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

  private static final String REG_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
  private static final String REG_PASSWORD = "^[A-Za-z0-9]{8,}$";

  public boolean validationEmail(String login) {
    return validationStringByReg(login, REG_EMAIL);
  }

  public boolean validationPassword(String password) {
    return validationStringByReg(password, REG_PASSWORD);
  }

  private boolean validationStringByReg(String data, String reg) {
    Pattern pattern = Pattern.compile(reg);
    Matcher matcher = pattern.matcher(data);
    return matcher.matches();
  }
}
