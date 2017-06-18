package com.jorik.gobike.View.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jorik.gobike.BR;
import com.jorik.gobike.R;
import com.jorik.gobike.ViewModel.SignUpViewModel;
import com.jorik.gobike.databinding.FragmentSignupBinding;

public class SignUpFragment extends BaseFragment<FragmentSignupBinding, SignUpViewModel> {

  public static SignUpFragment newInstance() {
    SignUpFragment fragment = new SignUpFragment();
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
    return R.layout.fragment_signup;
  }

  @Override
  public int getVariable() {
    return BR.signup;
  }

  @Override
  public SignUpViewModel createViewModel() {
    return new SignUpViewModel(getActivity());
  }

}
