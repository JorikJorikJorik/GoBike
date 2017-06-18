package com.jorik.gobike.View.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.databinding.library.baseAdapters.BR;
import com.jorik.gobike.R;
import com.jorik.gobike.ViewModel.LogInViewModel;
import com.jorik.gobike.databinding.FragmentLoginBinding;

public class LogInFragment extends BaseFragment<FragmentLoginBinding, LogInViewModel> {

  public static LogInFragment newInstance() {
    LogInFragment fragment = new LogInFragment();
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
    return R.layout.fragment_login;
  }

  @Override
  public int getVariable() {
    return BR.login;
  }

  @Override
  public LogInViewModel createViewModel() {
    return new LogInViewModel(getActivity());
  }

}
