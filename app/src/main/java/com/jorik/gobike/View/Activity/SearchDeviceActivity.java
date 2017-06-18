package com.jorik.gobike.View.Activity;

import static com.jorik.gobike.ViewModel.ArduinoViewModel.BLUETOOTH_STATE_NAME_EXTRA;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import com.jorik.gobike.R;
import com.jorik.gobike.View.Fragment.BaseFragment;
import com.jorik.gobike.View.Fragment.SearchDeviceFragment;

public class SearchDeviceActivity extends BaseActivityFragment<SearchDeviceFragment> {

  private static final int DEFAULT_VALUE = 0;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_device);
  }

  @Override
  public int getFrameLayoutId() {
    return R.id.frameLayoutSearchDevice;
  }

  @Override
  public SearchDeviceFragment getFragment() {
    return SearchDeviceFragment.newInstance(getIntent().getIntExtra(BLUETOOTH_STATE_NAME_EXTRA, DEFAULT_VALUE));
  }

  @Override
  public boolean isHomeToolbar() {
    return true;
  }
}
