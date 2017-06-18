package com.jorik.gobike.View.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.jorik.gobike.R;
import com.jorik.gobike.View.Fragment.LogInFragment;

public class LogInActivity extends BaseActivityFragment<LogInFragment> {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
  }

  @Override
  public int getFrameLayoutId() {
    return R.id.frameLayoutLogin;
  }

  @Override
  public LogInFragment getFragment() {
    return LogInFragment.newInstance();
  }

  @Override
  public boolean isHomeToolbar() {
    return false;
  }
}
