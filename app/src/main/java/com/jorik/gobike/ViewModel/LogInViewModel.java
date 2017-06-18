package com.jorik.gobike.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import com.jorik.gobike.DataBase.DataBaseUtils;
import com.jorik.gobike.Network.RestClient;
import com.jorik.gobike.Network.Service.LoginService;
import com.jorik.gobike.R;
import com.jorik.gobike.Utils.AuthorizationUtils;
import com.jorik.gobike.Utils.CryptoUtils;
import com.jorik.gobike.Utils.ErrorViewUtils;
import com.jorik.gobike.Utils.LoadingViewUtils;
import com.jorik.gobike.Utils.ValidationUtils;
import com.jorik.gobike.View.Activity.SignUpActivity;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class LogInViewModel extends BaseViewModel {

  private final static int MIN_LENGTH_LOGIN = 5;
  private final static int MIN_LENGTH_PASSWORD = 7;

  public final ObservableBoolean enableLoginButton;
  public final ObservableField<String> login;
  public final ObservableField<String> password;

  private Context mContext;
  private CompositeSubscription mSubscription;
  private ErrorViewUtils mErrorViewUtils;
  private ValidationUtils mValidationUtils;
  private AuthorizationUtils mAuthorizationUtils;
  private LoadingViewUtils mLoadingViewUtils;

  public LogInViewModel(Context context) {
    mContext = context;
    DataBaseUtils dataBaseUtils = new DataBaseUtils(mContext);
    mSubscription = new CompositeSubscription();
    mErrorViewUtils = new ErrorViewUtils(mContext);
    mValidationUtils = new ValidationUtils();
    mLoadingViewUtils = new LoadingViewUtils(mContext);
    mAuthorizationUtils = new AuthorizationUtils(mContext, dataBaseUtils);

    super.initDataBase(dataBaseUtils);
    super.initCompositeSubscription(mSubscription);

    enableLoginButton = new ObservableBoolean(false);
    login = new ObservableField<>();
    password = new ObservableField<>();
  }

  public void logIn() {

    mLoadingViewUtils.loading();

    LoginService service = RestClient.createClientService(LoginService.class);
    Subscription subscriptionLogin = service
        .login(generateBasic())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .flatMap(profile -> mAuthorizationUtils.prepareData(profile, login.get(), password.get()))
        .observeOn(Schedulers.io())
        .flatMap(mAuthorizationUtils::addToken)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(this::errorHandle)
        .doOnCompleted(this::completeHandle)
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

    mSubscription.add(subscriptionLogin);
  }

  public void signUp() {
    Intent moveToSignInIntent = new Intent(mContext, SignUpActivity.class);
    mContext.startActivity(moveToSignInIntent);
  }

  public void checkEnableLogin() {
    Subscription subscriptionEnable = Observable
        .combineLatest(validationLogin(), validationPassword(),
            (login, password) -> !login && !password)
        .subscribe(new Subscriber<Boolean>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(Boolean enableButton) {
            enableLoginButton.set(enableButton);
          }
        });

    mSubscription.add(subscriptionEnable);
  }

  private Observable<Boolean> validationLogin() {
    return Observable
        .just(login.get())
        .map(String::trim)
        .filter(login -> login.length() > MIN_LENGTH_LOGIN)
        .filter(login -> mValidationUtils.validationEmail(login))
        .isEmpty();
  }

  private Observable<Boolean> validationPassword() {
    return Observable
        .just(password.get())
        .map(String::trim)
        .filter(password -> password.length() > MIN_LENGTH_PASSWORD)
        .filter(password -> mValidationUtils.validationPassword(password))
        .isEmpty();
  }

  private void errorHandle(Throwable error) {
    mLoadingViewUtils.cancel();
    mErrorViewUtils.createErrorDialog(R.string.login_error_dialog_title, error);
  }

  private void completeHandle() {
    mLoadingViewUtils.cancel();
    mAuthorizationUtils.moveToEnter();
    super.finishActivity();
  }

  private String generateBasic() {
    CryptoUtils cryptoUtils = new CryptoUtils(mContext);
    return cryptoUtils.generateBasic(login.get(), password.get());
  }
}
