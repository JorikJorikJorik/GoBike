package com.jorik.gobike.ViewModel;

import static android.app.Activity.RESULT_OK;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import com.jorik.gobike.Bluetooth.BluetoothUtils;
import com.jorik.gobike.Bluetooth.BluetoothWorker;
import com.jorik.gobike.Model.Enum.BluetoothState;
import com.jorik.gobike.Model.POJO.ErrorViewModel;
import com.jorik.gobike.R;
import com.jorik.gobike.Utils.ErrorViewUtils;
import com.jorik.gobike.View.Adapter.SearchDeviceAdapter;
import com.jorik.gobike.ViewModel.Callback.BluetoothDeviceCallback;
import java.util.List;

public class SearchDeviceViewModel extends BaseViewModel implements BluetoothDeviceCallback {

  public static final String DEVICE_INFO_NAME_EXTRA = "device_info_name_extra";
  public static final int REQUEST_ENABLE_BLUETOOTH = 1;

  public final ObservableField<SearchDeviceAdapter> searchAdapter;
  public final ObservableField<Boolean> swipeResult;
  public final ObservableField<Boolean> swipeError;
  public final ObservableField<Boolean> showResult;
  public final ObservableField<Boolean> showError;
  public final ObservableField<Boolean> showProgress;
  public final ObservableField<ErrorViewModel> mErrorModel;

  private Context mContext;
  private BluetoothUtils mBluetoothUtils;
  private ErrorViewUtils mErrorViewUtils;
  private SearchDeviceAdapter mSearchDeviceAdapter;
  private BluetoothWorker mWorker;
  private BluetoothState mBluetoothState;

  public SearchDeviceViewModel(Context context, BluetoothState state) {
    mContext = context;
    mBluetoothUtils = new BluetoothUtils(mContext);
    mErrorViewUtils = new ErrorViewUtils(mContext, this::checkState);
    mBluetoothState = state;
    updateSearchListListener();

    searchAdapter = new ObservableField<>();
    swipeResult = new ObservableField<>();
    swipeError = new ObservableField<>();
    showResult = new ObservableField<>();
    showError = new ObservableField<>();
    showProgress = new ObservableField<>();
    mErrorModel = new ObservableField<>();
  }

  private void updateSearchListListener() {
    mBluetoothUtils.setUpdateBluetoothDeviceListener(device -> {
      List<BluetoothDevice> searchedList = mSearchDeviceAdapter.getDataArrayList();
      if (device != null) {
        searchedList.add(device);
        mSearchDeviceAdapter.setDataArrayList(searchedList);
        mSearchDeviceAdapter.notifyItemInserted(searchedList.size() + 1);
        changeStateScreen(true, false, false);
      } else {
        changeStateScreen(!searchedList.isEmpty(), searchedList.isEmpty(), false);
      }
    });
  }

  @Override
  public void onStart() {
    super.onStart();
    mWorker = new BluetoothWorker();
  }

  @Override
  public void onResume() {
    super.onResume();
    mBluetoothUtils.registerReceiver();
    if (mWorker.getState() == BluetoothWorker.STATE_NONE) {
      mWorker.start();
    }
  }

  @Override
  public void onPause() {
    mBluetoothUtils.unregisterReceiver();
    super.onPause();
  }

  @Override
  public void onDestroy() {
    mWorker.stop();
    mBluetoothUtils.destroyTools();
    super.onDestroy();
  }

  public void checkState() {
    if (mBluetoothState.equals(BluetoothState.NOT_ENABLE)) {
      Intent requestBluetoothOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(requestBluetoothOn, REQUEST_ENABLE_BLUETOOTH);
    } else {
      requestListDevice();
    }
  }

  public void requestListDevice() {
    List<BluetoothDevice> connactableDeviceList = mBluetoothUtils.searchDevice();
    if (mSearchDeviceAdapter == null) {
      changeStateScreen(false, false, true);
      mSearchDeviceAdapter = new SearchDeviceAdapter(mContext, connactableDeviceList);
      mSearchDeviceAdapter.registerCallback(this);
      searchAdapter.set(mSearchDeviceAdapter);
    } else {
      mSearchDeviceAdapter.setDataArrayList(connactableDeviceList);
      mSearchDeviceAdapter.notifyDataSetChanged();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
      if (resultCode == RESULT_OK) {
        requestListDevice();
      } else {
        bluetoothNotTurnOn();
      }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  private void bluetoothNotTurnOn() {
    mBluetoothUtils.toastMessage(R.string.bluetooth_utils_error_not_turn_on);
    changeStateScreen(false, true, false);
  }

  private void changeStateScreen(boolean result, boolean error, boolean progress) {
    showResult.set(result);
    showError.set(error);
    showProgress.set(progress);
    if (result) {
      swipeError.set(new Boolean(false));
    }
    if (error) {
      mErrorModel.set(mErrorViewUtils.formationErrorEmpty(R.mipmap.ic_error_bluetooth_empty,
          R.string.bluetooth_utils_error_not_search_device));
    }
    swipeResult.set(new Boolean(progress));
    showProgress.set(new Boolean(progress));
  }

  @Override
  public void bluetoothDevice(BluetoothDevice bluetoothDevice) {
//    mWorker.connect(bluetoothDevice, true);
    Intent intent = new Intent();
    intent.putExtra(DEVICE_INFO_NAME_EXTRA, mBluetoothUtils.convertToArduinoDTO(bluetoothDevice));
    setResult(RESULT_OK, intent);
    finishActivity();
  }
}
