package com.jorik.gobike.Bluetooth;

import static android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
import static android.bluetooth.BluetoothAdapter.getDefaultAdapter;
import static android.bluetooth.BluetoothDevice.ACTION_FOUND;
import static android.bluetooth.BluetoothDevice.EXTRA_DEVICE;
import static com.jorik.gobike.Model.Enum.BluetoothState.ENABLE;
import static com.jorik.gobike.Model.Enum.BluetoothState.NOT_ENABLE;
import static com.jorik.gobike.Model.Enum.BluetoothState.NOT_WORK;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;
import com.jorik.gobike.Bluetooth.Callback.UpdateBluetoothDeviceListener;
import com.jorik.gobike.Model.Enum.BluetoothState;
import com.jorik.gobike.Network.DTO.ArduinoDTO;
import com.jorik.gobike.R;
import com.jorik.gobike.Utils.ResourceUtils;
import java.util.ArrayList;
import java.util.List;

public class BluetoothUtils {

  private static final int EMPTY_ARDUINO_ID = 0;

  private Context mContext;
  private Toast mToastBluetoothInfo;
  private BluetoothAdapter mBluetoothAdapter;
  private BroadcastReceiver mBroadcastReceiverFoundDevice;
  private UpdateBluetoothDeviceListener mDeviceListener;

  public BluetoothUtils(Context context) {
    mContext = context;
    mBluetoothAdapter = getDefaultAdapter();
  }

  public BluetoothState checkBluetoothEnable() {
    if (mBluetoothAdapter == null) {
      toastMessage(R.string.bluetooth_utils_error_not_work);
      return NOT_WORK;
    }
    return !mBluetoothAdapter.isEnabled() ? NOT_ENABLE : ENABLE;
  }


  public void registerReceiver() {

    mBroadcastReceiverFoundDevice = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_FOUND)) {
          BluetoothDevice foundDevice = intent.getParcelableExtra(EXTRA_DEVICE);
          if (foundDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
            if (mDeviceListener != null) {
              mDeviceListener.updateBluetoothDevice(foundDevice);
            }
          }
        } else if (action.equals(ACTION_DISCOVERY_FINISHED)) {
          mDeviceListener.updateBluetoothDevice(null);
        }
      }
    };

    IntentFilter intentFilterFound = new IntentFilter(ACTION_FOUND);
    mContext.registerReceiver(mBroadcastReceiverFoundDevice, intentFilterFound);

    IntentFilter intentFilterDiscover = new IntentFilter(ACTION_DISCOVERY_FINISHED);
    mContext.registerReceiver(mBroadcastReceiverFoundDevice, intentFilterDiscover);

  }

  public List<BluetoothDevice> searchDevice() {
    searchConnectedDevice();
    return searchConnectableDevice();
  }

  private void searchConnectedDevice() {
    if (mBluetoothAdapter.isDiscovering()) {
      mBluetoothAdapter.cancelDiscovery();
    }
    mBluetoothAdapter.startDiscovery();
  }

  private List<BluetoothDevice> searchConnectableDevice() {
    return new ArrayList<>(mBluetoothAdapter.getBondedDevices());
  }

  public void setUpdateBluetoothDeviceListener(UpdateBluetoothDeviceListener mListener) {
    mDeviceListener = mListener;
  }

  public void toastMessage(int message) {
    if (mToastBluetoothInfo != null) {
      mToastBluetoothInfo.cancel();
    }
    mToastBluetoothInfo = Toast
        .makeText(mContext, ResourceUtils.with(mContext).string(message), Toast.LENGTH_SHORT);
    mToastBluetoothInfo.show();
  }

  public void unregisterReceiver() {
    if (mBroadcastReceiverFoundDevice != null) {
      mContext.unregisterReceiver(mBroadcastReceiverFoundDevice);
    }
  }

  public ArduinoDTO convertToArduinoDTO(BluetoothDevice bluetoothDevice) {
    ArduinoDTO arduinoDTO = new ArduinoDTO();
    arduinoDTO.setArduinoId(EMPTY_ARDUINO_ID);
    arduinoDTO.setName(bluetoothDevice.getName());
    arduinoDTO.setMac(bluetoothDevice.getAddress());
    return arduinoDTO;
  }

  public void destroyTools() {
    if (mBluetoothAdapter != null) {
      mBluetoothAdapter.cancelDiscovery();
    }
  }
}
