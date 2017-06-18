package com.jorik.gobike.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import com.jorik.gobike.DataBase.DataBaseUtils;
import com.jorik.gobike.Network.DTO.SignInDTO;
import com.jorik.gobike.Network.RestClient;
import com.jorik.gobike.Network.Service.SignInService;
import com.jorik.gobike.R;
import com.jorik.gobike.Utils.AuthorizationUtils;
import com.jorik.gobike.Utils.ErrorViewUtils;
import com.jorik.gobike.Utils.LoadingViewUtils;
import com.jorik.gobike.Utils.ResourceUtils;
import com.jorik.gobike.Utils.ValidationUtils;
import com.jorik.gobike.View.Activity.MainActivity;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SignUpViewModel extends BaseViewModel {

  private static final int MIN_LENGTH_FIELD = 3;
  private final static int MIN_LENGTH_LOGIN = 5;
  private final static int MIN_LENGTH_PASSWORD = 7;
  private final static String PHOTO_URL = "";

  public final ObservableField<String> name;
  public final ObservableField<String> nameError;
  public final ObservableField<String> lastName;
  public final ObservableField<String> lastNameError;
  public final ObservableField<String> city;
  public final ObservableField<String> cityError;
  public final ObservableField<String> phone;
  public final ObservableField<String> phoneError;
  public final ObservableField<String> email;
  public final ObservableField<String> emailError;
  public final ObservableField<String> password;
  public final ObservableField<String> passwordError;
  public final ObservableField<String> confirmPassword;
  public final ObservableField<String> confirmPasswordError;
  public final ObservableBoolean enableSignIn;

  private Context mContext;
  private CompositeSubscription mSubscription;
  private ValidationUtils mValidationUtils;
  private ErrorViewUtils mErrorViewUtils;
  private AuthorizationUtils mAuthorizationUtils;
  private LoadingViewUtils loadingViewUtils;


  public SignUpViewModel(Context context) {

    mContext = context;
    mSubscription = new CompositeSubscription();
    mValidationUtils = new ValidationUtils();
    mErrorViewUtils = new ErrorViewUtils(mContext);
    DataBaseUtils dataBaseUtils = new DataBaseUtils(mContext);
    mAuthorizationUtils = new AuthorizationUtils(mContext, dataBaseUtils);
    loadingViewUtils = new LoadingViewUtils(mContext);

    super.initDataBase(dataBaseUtils);
    super.initCompositeSubscription(mSubscription);

    name = new ObservableField<>();
    nameError = new ObservableField<>();
    lastName = new ObservableField<>();
    lastNameError = new ObservableField<>();
    city = new ObservableField<>();
    cityError = new ObservableField<>();
    phone = new ObservableField<>();
    phoneError = new ObservableField<>();
    email = new ObservableField<>();
    emailError = new ObservableField<>();
    password = new ObservableField<>();
    passwordError = new ObservableField<>();
    confirmPassword = new ObservableField<>();
    confirmPasswordError = new ObservableField<>();
    enableSignIn = new ObservableBoolean();
  }

  public void signIn() {
    SignInService service = RestClient.createClientService(SignInService.class);

    loadingViewUtils.loading();

    Subscription subscriptionSignIn = service
        .signIn(constructSignInDTO())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .flatMap(profile -> mAuthorizationUtils.prepareData(profile, email.get(), password.get()))
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
          public void onNext(Void empty) {
          }
        });

      mSubscription.add(subscriptionSignIn);
  }

  public void enableSignIn() {
    Subscription subscriptionEnable = Observable
        .combineLatest(
            validationField(name, nameError, MIN_LENGTH_FIELD),
            validationField(lastName, lastNameError, MIN_LENGTH_FIELD),
            validationField(city, cityError, MIN_LENGTH_FIELD),
            validationField(phone, phoneError, MIN_LENGTH_FIELD),
            validationEmail(),
            validationPassword(password, passwordError),
            validationPassword(confirmPassword, confirmPasswordError),
            equalPassword(),
            (name, lastName, city, phone, email, password, confirmPassword, passwordIsEqual) ->
                !name && !lastName && !city && !phone && !email && !password && !confirmPassword
                    && passwordIsEqual)
        .subscribe(new Subscriber<Boolean>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(Boolean enableButton) {
            enableSignIn.set(enableButton);
          }
        });

    mSubscription.add(subscriptionEnable);
  }

  private Observable<Boolean> validationField(ObservableField<String> field,
      ObservableField<String> fieldError, int minLength) {
    String trimString = trimData(field);
    return trimString.isEmpty() ? Observable.just(true) : Observable
        .just(trimString)
        .doOnNext(fieldData -> errorMessageUpdate(fieldError,
            R.string.sign_up_text_input_edit_text_error_size))
        .filter(fieldString -> fieldString.length() > minLength)
        .isEmpty()
        .doOnNext(emptySequence -> clearError(emptySequence, fieldError));
  }

  private Observable<Boolean> validationEmail() {
    String trimString = trimData(email);
    return trimString.isEmpty() ? Observable.just(true) : Observable
        .just(trimString)
        .doOnNext(fieldData -> errorMessageUpdate(emailError,
            R.string.sign_up_text_input_edit_text_error_size))
        .filter(email -> email.length() > MIN_LENGTH_LOGIN)
        .doOnNext(fieldData -> errorMessageUpdate(emailError,
            R.string.sign_up_text_input_edit_text_email_error_validation))
        .filter(email -> mValidationUtils.validationEmail(email))
        .isEmpty()
        .doOnNext(emptySequence -> clearError(emptySequence, emailError));
  }

  private Observable<Boolean> validationPassword(ObservableField<String> fieldPassword,
      ObservableField<String> fieldPasswordError) {
    String trimString = trimData(fieldPassword);
    return trimString.isEmpty() ? Observable.just(true) : Observable
        .just(trimString)
        .doOnNext(fieldData -> errorMessageUpdate(fieldPasswordError,
            R.string.sign_up_text_input_edit_text_error_size))
        .filter(password -> password.length() > MIN_LENGTH_PASSWORD)
        .doOnNext(fieldData -> errorMessageUpdate(fieldPasswordError,
            R.string.sign_up_text_input_edit_text_password_error_validation))
        .filter(password -> mValidationUtils.validationPassword(password))
        .isEmpty()
        .doOnNext(emptySequence -> clearError(emptySequence, fieldPasswordError));
  }

  private Observable<Boolean> equalPassword() {
    return Observable
        .sequenceEqual(Observable.just(password.get()), Observable.just(confirmPassword.get()))
        .doOnNext(equal -> changeErrorByCondition(equal, confirmPasswordError,
            R.string.sign_up_text_input_edit_text_password_error_equal));
  }

  private String trimData(ObservableField<String> data) {
    String fieldData = data.get();
    return fieldData != null ? fieldData.trim()
        : ResourceUtils.with(mContext).string(R.string.empty_string);
  }

  private void clearError(boolean isEmpty, ObservableField<String> fieldError) {
    if (!isEmpty) {
      errorMessageUpdate(fieldError, R.string.empty_string);
    }
  }

  private void changeErrorByCondition(boolean condition, ObservableField<String> fieldError,
      int resourceError) {
    errorMessageUpdate(fieldError, condition ? R.string.empty_string : resourceError);
  }

  private void errorMessageUpdate(ObservableField<String> field, int resourceError) {
    field.set(ResourceUtils.with(mContext).string(resourceError));
  }

  private SignInDTO constructSignInDTO() {
    return new SignInDTO(email.get(), password.get(), name.get(), lastName.get(), PHOTO_URL,
        city.get(), phone.get(), email.get());
  }

  private void errorHandle(Throwable error){
    loadingViewUtils.cancel();
    mErrorViewUtils.createErrorDialog(R.string.sign_up_error_dialog_title, error);
  }

  private void completeHandle(){
    loadingViewUtils.cancel();
    mAuthorizationUtils.moveToEnter();
    super.finishActivity();
  }

}
