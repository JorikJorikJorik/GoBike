package com.jorik.gobike.ViewModel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v4.app.FragmentActivity;
import com.jorik.gobike.DataBase.DataBaseUtils;
import com.jorik.gobike.View.Activity.Callback.FinishActivityCallback;
import com.jorik.gobike.View.Fragment.Callback.FinishFragmentCallback;
import com.jorik.gobike.View.Fragment.Callback.IntentResultCallback;
import com.jorik.gobike.View.Fragment.Callback.UpdateToolbarMessageCallback;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseViewModel extends BaseObservable {

  private DataBaseUtils mDataBaseUtils;
  private CompositeSubscription mCompositeSubscription;
  private FinishActivityCallback mFinishActivityCallback;
  private FinishFragmentCallback mFinishFragmentCallback;
  private UpdateToolbarMessageCallback mUpdateToolbarMessageCallback;
  private IntentResultCallback mIntentResultCallback;

  public BaseViewModel() {
  }

  public void initDataBase(DataBaseUtils dataBaseUtils) {
    mDataBaseUtils = dataBaseUtils;
  }

  public void initCompositeSubscription(CompositeSubscription subscription) {
    mCompositeSubscription = subscription;
  }

  public void registerFinishCallback(FinishActivityCallback activityCallback, FinishFragmentCallback fragmentCallback, UpdateToolbarMessageCallback messageCallback, IntentResultCallback intentCallback){
    mFinishActivityCallback = activityCallback;
    mFinishFragmentCallback = fragmentCallback;
    mUpdateToolbarMessageCallback = messageCallback;
    mIntentResultCallback = intentCallback;
  }

  public void onStart() {

  }

  public void onResume() {

  }

  public void onPause() {

  }

  public void onStop() {

  }

  public void updateToolbarMessage(String message){
    if(mUpdateToolbarMessageCallback != null)
      mUpdateToolbarMessageCallback.updateToolbarMessage(message);
  }

  public void finishActivity(){
    if(mFinishActivityCallback != null)
      mFinishActivityCallback.finishActivity();
  }

  public void finishFragment(){
    if(mFinishFragmentCallback != null)
      mFinishFragmentCallback.finishFragment();
  }

  public void startActivityForResult(Intent intent, int key){
    if(mIntentResultCallback != null)
      mIntentResultCallback.startBaseActivityForResult(intent, key);
  }

  public void setResult(int key, Intent intent){
    if(mIntentResultCallback != null)
      mIntentResultCallback.setBaseResult(key, intent);
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data){
  }

  public void onDestroy() {
    if (mDataBaseUtils != null) {
      mDataBaseUtils.close();
    }
    if (mCompositeSubscription != null) {
      mCompositeSubscription.unsubscribe();
    }
  }

}
