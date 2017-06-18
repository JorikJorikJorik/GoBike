package com.jorik.gobike.View.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.jorik.gobike.R;
import com.jorik.gobike.View.Fragment.SignUpFragment;

public class SignUpActivity extends BaseActivityFragment<SignUpFragment> {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);
  }

  @Override
  public int getFrameLayoutId() {
    return R.id.frameLayoutSignup;
  }

  @Override
  public SignUpFragment getFragment() {
    return SignUpFragment.newInstance();
  }

  @Override
  public boolean isHomeToolbar() {
    return true;
  }

}
