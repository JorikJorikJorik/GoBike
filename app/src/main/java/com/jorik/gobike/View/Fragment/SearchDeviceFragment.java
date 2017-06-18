package com.jorik.gobike.View.Fragment;

import static android.app.Activity.RESULT_OK;
import static com.jorik.gobike.ViewModel.SearchDeviceViewModel.REQUEST_ENABLE_BLUETOOTH;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jorik.gobike.BR;
import com.jorik.gobike.Model.Enum.BluetoothState;
import com.jorik.gobike.R;
import com.jorik.gobike.ViewModel.SearchDeviceViewModel;
import com.jorik.gobike.databinding.FragmentSearchDeviceBinding;

public class SearchDeviceFragment extends BaseFragment<FragmentSearchDeviceBinding, SearchDeviceViewModel> {

  private static final String BLUETOOTH_STATE_ARGS = "bluetooth_state_args";

  private BluetoothState mState;
  private SearchDeviceViewModel mViewModel;

  public static SearchDeviceFragment newInstance(int stateBluetooth) {
    SearchDeviceFragment fragment = new SearchDeviceFragment();
    Bundle args = new Bundle();
    args.putInt(BLUETOOTH_STATE_ARGS, stateBluetooth);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    mState = BluetoothState.fromValue(getArguments().getInt(BLUETOOTH_STATE_ARGS));
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mViewModel = new SearchDeviceViewModel(getActivity(), mState);
    View view = super.onCreateView(inflater, container, savedInstanceState);
    mViewModel.checkState();
    return view;
  }

  @Override
  public int getLayoutId() {
    return R.layout.fragment_search_device;
  }

  @Override
  public int getVariable() {
    return BR.search_device;
  }

  @Override
  public SearchDeviceViewModel createViewModel() {
    return mViewModel;
  }
}
