package com.jorik.gobike.Firebase;

import static com.jorik.gobike.Model.Enum.StateApplication.ENTER;
import static com.jorik.gobike.Model.Enum.StateApplication.LOGIN;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.jorik.gobike.DataBase.DataBaseUtils;
import com.jorik.gobike.Network.RestClient;
import com.jorik.gobike.Network.Service.FirebaseService;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GoBikeFirebaseInstanceIdService extends FirebaseInstanceIdService {

  private Subscription mSubscription;

  @Override
  public void onTokenRefresh() {
    super.onTokenRefresh();
    DataBaseUtils mDataBaseUtils = new DataBaseUtils(getBaseContext());
    if (mDataBaseUtils.getStateApplication().equals(ENTER)) {
      refreshToken(mDataBaseUtils.getBasic());
    }
  }

  private void refreshToken(String basic) {
    FirebaseService service = RestClient.createClientService(FirebaseService.class);
    mSubscription = service
        .refreshToken(basic, FirebaseInstanceId.getInstance().getToken())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Void>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(Void aVoid) {

          }
        });
  }

  @Override
  public void onDestroy() {
    if (mSubscription != null) {
      mSubscription.unsubscribe();
    }
    super.onDestroy();
  }
}
