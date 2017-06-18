package com.jorik.gobike.View.Activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.jorik.gobike.ViewModel.BaseViewModel;

public abstract class BaseActivityViewModel<B extends BaseViewModel, V extends ViewDataBinding> extends AppCompatActivity {

  private B viewModel;
  private V binding;

  public abstract int getLayoutId();
  public abstract int getVariableId();
  public abstract B createViewModel();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, getLayoutId());
    viewModel = createViewModel();
    binding.setVariable(getVariableId(), viewModel);
    binding.executePendingBindings();
  }

  @Override
  protected void onStart() {
    super.onStart();
    viewModel.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
    viewModel.onResume();
  }

  @Override
  protected void onPause() {
    viewModel.onPause();
    super.onPause();
  }

  @Override
  protected void onStop() {
    viewModel.onStop();
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    viewModel.onDestroy();
    super.onDestroy();
  }

  public V getBinding() {
    return binding;
  }

  public B getViewModel() {
    return viewModel;
  }
}
