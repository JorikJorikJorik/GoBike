package com.jorik.gobike.View.Fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jorik.gobike.View.Activity.BaseActivity;
import com.jorik.gobike.View.Activity.Callback.FinishActivityCallback;
import com.jorik.gobike.View.Fragment.Callback.FinishFragmentCallback;
import com.jorik.gobike.View.Fragment.Callback.IntentResultCallback;
import com.jorik.gobike.View.Fragment.Callback.UpdateToolbarMessageCallback;
import com.jorik.gobike.ViewModel.BaseViewModel;

public abstract class BaseFragment<B extends ViewDataBinding, V extends BaseViewModel> extends
    Fragment implements
    FinishFragmentCallback, UpdateToolbarMessageCallback, IntentResultCallback {

  private V viewModel;
  private B binding;

  public abstract int getLayoutId();

  public abstract int getVariable();

  public abstract V createViewModel();

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(getLayoutId(), container, false);
    viewModel = createViewModel();
    viewModel.registerFinishCallback(custToBaseActivity(), this, this, this);
    binding = DataBindingUtil.bind(view);
    binding.setVariable(getVariable(), viewModel);
    binding.executePendingBindings();
    return view;
  }

  @Override
  public void onStart() {
    super.onStart();
    viewModel.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    viewModel.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    viewModel.onPause();

  }

  @Override
  public void onStop() {
    viewModel.onStop();
    super.onStop();
  }

  @Override
  public void onDestroy() {
    viewModel.onDestroy();
    super.onDestroy();
  }

  @Override
  public void finishFragment() {
    getActivity().finish();
  }

  @Override
  public void updateToolbarMessage(String message) {
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(message);
    }
  }

  public B getBinding() {
    return binding;
  }

  public V getViewModel() {
    return viewModel;
  }

  private FinishActivityCallback custToBaseActivity() {
    if (getActivity() instanceof BaseActivity) {
      return (BaseActivity) getActivity();
    }
    return null;
  }

  @Override
  public void startBaseActivityForResult(Intent intent, int key){
    startActivityForResult(intent, key);
  }

  @Override
  public void setBaseResult(int key, Intent intent) {
    getActivity().setResult(key, intent);
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    viewModel.onActivityResult(requestCode, resultCode, data);
  }
}
