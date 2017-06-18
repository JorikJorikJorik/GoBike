package com.jorik.gobike.View.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import com.jorik.gobike.View.Fragment.BaseFragment;

public abstract class BaseActivityFragment<T extends BaseFragment> extends BaseActivity {

  public abstract int getFrameLayoutId();

  public abstract T getFragment();

  public abstract boolean isHomeToolbar();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    FragmentManager manager = getSupportFragmentManager();
    manager
        .beginTransaction()
        .replace(getFrameLayoutId(), getFragment())
        .commit();

    super.homeToolbarButton(isHomeToolbar());
  }

}

