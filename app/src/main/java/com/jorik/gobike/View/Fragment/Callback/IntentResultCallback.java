package com.jorik.gobike.View.Fragment.Callback;


import android.content.Intent;

public interface IntentResultCallback {
  void startBaseActivityForResult(Intent intent, int key);
  void setBaseResult(int key, Intent intent);
}
