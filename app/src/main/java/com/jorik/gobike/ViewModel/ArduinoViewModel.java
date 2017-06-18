package com.jorik.gobike.ViewModel;

import static android.app.Activity.RESULT_OK;
import static com.jorik.gobike.Model.Enum.ErrorViewType.EMPTY;
import static com.jorik.gobike.ViewModel.SearchDeviceViewModel.DEVICE_INFO_NAME_EXTRA;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import com.jorik.gobike.Bluetooth.BluetoothUtils;
import com.jorik.gobike.DataBase.DataBaseUtils;
import com.jorik.gobike.Model.Enum.BluetoothState;
import com.jorik.gobike.Model.Enum.CashDataType;
import com.jorik.gobike.Model.POJO.ErrorViewModel;
import com.jorik.gobike.Network.DTO.ArduinoDTO;
import com.jorik.gobike.Network.DTO.ArduinoListDTO;
import com.jorik.gobike.Network.RestClient;
import com.jorik.gobike.Network.Service.ArduinoService;
import com.jorik.gobike.R;
import com.jorik.gobike.Utils.CacheUtils;
import com.jorik.gobike.Utils.ErrorViewUtils;
import com.jorik.gobike.Utils.ResourceUtils;
import com.jorik.gobike.View.Activity.SearchDeviceActivity;
import com.jorik.gobike.View.Adapter.ArduinoAdapter;
import com.jorik.gobike.ViewModel.Callback.DeleteArduinoCallback;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ArduinoViewModel extends BaseViewModel implements DeleteArduinoCallback {

  public static final String BLUETOOTH_STATE_NAME_EXTRA = "bluetooth_state_name_extra";
  private static final int CONNECTED_DEVICE_RESULT = 2;

  public final ObservableField<ArduinoAdapter> arduinoAdapter;
  public final ObservableField<ErrorViewModel> mErrorModel;
  public final ObservableField<Boolean> stateResult;
  public final ObservableField<Boolean> stateError;
  public final ObservableField<Boolean> stateProgress;
  public final ObservableField<Boolean> stateFab;
  public final ObservableField<Boolean> swipeResult;
  public final ObservableField<Boolean> swipeError;

  private Context mContext;
  private CacheUtils<ArduinoListDTO> mCacheUtils;
  private DataBaseUtils mDataBaseUtils;
  private CompositeSubscription mSubscription;
  private ErrorViewUtils mErrorViewUtils;
  private ArduinoService mArduinoService;
  private ArduinoAdapter mAdapter;
  private boolean stateLoadListByCreated;

  public ArduinoViewModel(Context context) {

    mContext = context;
    mCacheUtils = new CacheUtils<>(mContext, CashDataType.RESPONSE);
    mDataBaseUtils = new DataBaseUtils(mContext);
    mSubscription = new CompositeSubscription();
    mErrorViewUtils = new ErrorViewUtils(mContext, this::loadArduinoList);
    mArduinoService = RestClient.createClientService(ArduinoService.class);
    stateLoadListByCreated = true;

    initDataBase(mDataBaseUtils);
    initCompositeSubscription(mSubscription);

    arduinoAdapter = new ObservableField<>();
    mErrorModel = new ObservableField<>();
    stateResult = new ObservableField<>();
    stateError = new ObservableField<>();
    stateProgress = new ObservableField<>();
    swipeResult = new ObservableField<>();
    swipeError = new ObservableField<>();
    stateFab = new ObservableField<>();

  }

  @Override
  public void onResume() {
    super.onResume();
    if (stateLoadListByCreated) {
      loadArduinoList();
    }
  }

  public void loadArduinoList() {
    showProgress();
    Subscription subscriptionArduinoList = mArduinoService
        .getArduinoList(mDataBaseUtils.getBasic())
        .delay(1, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnCompleted(this::defaultUpdateToolbar)
        .doOnError(this::handleError)
        .onErrorResumeNext(mCacheUtils.asyncRead(ArduinoListDTO.class))
        .flatMap(this::handelList)
        .doOnTerminate(() -> stateLoadListByCreated = true)
        .subscribe(new Subscriber<ArduinoListDTO>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(ArduinoListDTO arduinoDTOs) {

          }
        });
    mSubscription.add(subscriptionArduinoList);
  }

  private void showProgress() {
    ErrorViewModel errorModel = mErrorModel.get();
    boolean emptyError = false;
    if (errorModel != null) {
      emptyError = errorModel.getErrorType().equals(EMPTY);
    }
    boolean emptyAdapter = mAdapter == null && !emptyError;
    if (emptyAdapter || (errorModel != null && emptyAdapter)) {
      stateScreen(false, false, true);
    }
  }

  private void defaultUpdateToolbar() {
    updateToolbarMessage(ResourceUtils.with(mContext).string(R.string.navigation_item_arduino));
  }

  private Observable<ArduinoListDTO> handelList(ArduinoListDTO arduinoListDTO) {
    if (arduinoListDTO == null) {
      return handelListByState(false, true, false, arduinoListDTO);
    }
    boolean emptyData = arduinoListDTO.getArduinoDTOs().isEmpty();
    actionByEmpty(emptyData);
    return handelListByState(!emptyData, emptyData, true, arduinoListDTO);
  }

  private Observable<ArduinoListDTO> handelListByState(boolean result, boolean error, boolean fab, ArduinoListDTO arduinoListDTO) {
    stateScreenAndFab(result, error, fab);
    if (error) {
      defaultUpdateToolbar();
    }
    return !fab ? Observable.just(arduinoListDTO) :
        Observable.just(arduinoListDTO)
            .flatMap(data -> mCacheUtils.asyncWrite(data))
            .doOnNext(data -> fillAdapter(data.getArduinoDTOs()));
  }

  private void stateScreenAndFab(boolean result, boolean error, boolean fab) {
    stateScreen(result, error, false);
    stateFab.set(new Boolean(fab));
  }

  private void actionByEmpty(boolean emptyData) {
    if (emptyData) {
      mErrorModel.set(mErrorViewUtils
          .formationErrorEmpty(R.mipmap.ic_error_empty_arduino, R.string.arduino_error_empty));
    }
  }

  private void handleError(Throwable throwable) {
    mErrorModel.set(mErrorViewUtils.formationError(throwable));
    updateToolbarMessage(mErrorViewUtils.formationShortError(throwable));
  }

  private void stateScreen(boolean result, boolean error, boolean progress) {
    stateResult.set(result);
    stateError.set(error);
    stateProgress.set(progress);

    if (result) {
      swipeResult.set(new Boolean(false));
      swipeError.set(new Boolean(false));
    }
  }

  private void fillAdapter(List<ArduinoDTO> data) {
    if (mAdapter == null) {
      mAdapter = new ArduinoAdapter(mContext, data);
      mAdapter.registerCallback(this);
      arduinoAdapter.set(mAdapter);
    } else {
      mAdapter.setDataArrayList(data);
      mAdapter.notifyDataSetChanged();
    }
  }

  private void deleteArduinoData(int arduinoId) {
    Subscription subscriptionDelete = mArduinoService
        .deleteArduino(mDataBaseUtils.getBasic(), arduinoId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(this::handleError)
        .doOnCompleted(() -> updateList(arduinoId))
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
    mSubscription.add(subscriptionDelete);
  }


  private void updateList(int arduinoId) {
    List<ArduinoDTO> arduinoList = mAdapter.getDataArrayList();
    for (int i = 0; i < arduinoList.size(); i++) {
      if (arduinoList.get(i).getArduinoId() == arduinoId) {
        mAdapter.notifyItemRemoved(i);
        arduinoList.remove(i);
        break;
      }
    }

    boolean empty = arduinoList.isEmpty();
    if (empty) {
      actionByEmpty(empty);
      stateScreenAndFab(false, true, true);
    }

//    List<ArduinoDTO> arduinoList = mAdapter.getDataArrayList();
//    Observable
//        .from(arduinoList)
//        .filter(data -> data.getArduinoId() == arduinoId)
//        .doOnNext(data -> {
//          arduinoList.remove(data);
//          mAdapter.notifyItemRemoved(arduinoList.indexOf(data));
//        });
  }

  private void createArduino(ArduinoDTO arduinoDTO) {
    Subscription subscriptionAdd =
        checkNeededAddArduino(arduinoDTO)
            .doOnNext((need) -> stateLoadListByCreated = false)
            .flatMap(isNeed -> isNeed ? createRequest(arduinoDTO) : Observable.just(null))
            .doOnError(this::handleError)
            .doOnCompleted(this::loadArduinoList)
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

    mSubscription.add(subscriptionAdd);
  }

  private Observable<Boolean> checkNeededAddArduino(ArduinoDTO arduinoDTO) {
    return mAdapter != null && !mAdapter.getDataArrayList().isEmpty() ?
        Observable
            .from(mAdapter.getDataArrayList())
            .map(ArduinoDTO::getMac)
            .contains(arduinoDTO.getMac())
            .map(same -> !same) : Observable.just(true);
  }

  private Observable<Void> createRequest(ArduinoDTO arduinoDTO) {
    String basic = mDataBaseUtils.getBasic();
    return mArduinoService
        .createArduino(basic, arduinoDTO)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  public void moveToSearch() {
    BluetoothState state = new BluetoothUtils(mContext).checkBluetoothEnable();
    if (!state.equals(BluetoothState.NOT_WORK)) {
      Intent moveToSearch = new Intent(mContext, SearchDeviceActivity.class);
      moveToSearch.putExtra(BLUETOOTH_STATE_NAME_EXTRA, state.getValue());
      super.startActivityForResult(moveToSearch, CONNECTED_DEVICE_RESULT);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == CONNECTED_DEVICE_RESULT) {
      if (resultCode == RESULT_OK) {
        createArduino(data.getParcelableExtra(DEVICE_INFO_NAME_EXTRA));
      }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void deleteArduino(int arduinoId) {
    deleteArduinoData(arduinoId);
  }
}
