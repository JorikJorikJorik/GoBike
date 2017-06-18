package com.jorik.gobike.View.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BaseView {

  public abstract int layoutId();

  public View createView(Context context) {
    return LayoutInflater.from(context).inflate(layoutId(), null);
  }
}
