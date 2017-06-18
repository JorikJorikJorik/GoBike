package com.jorik.gobike.View.Fragment;

import static android.Manifest.permission.CAMERA;

import android.Manifest;
import android.Manifest.permission;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jorik.gobike.BR;
import com.jorik.gobike.Bluetooth.BluetoothUtils;
import com.jorik.gobike.R;
import com.jorik.gobike.ViewModel.ArduinoViewModel;
import com.jorik.gobike.databinding.FragmentArduinoBinding;

public class ArduinoFragment extends BaseFragment<FragmentArduinoBinding, ArduinoViewModel> {

  public static ArduinoFragment newInstance() {
    ArduinoFragment fragment = new ArduinoFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public int getLayoutId() {
    return R.layout.fragment_arduino;
  }

  @Override
  public int getVariable() {
    return BR.arduino;
  }

  @Override
  public ArduinoViewModel createViewModel() {
    return new ArduinoViewModel(getActivity());
  }

}
