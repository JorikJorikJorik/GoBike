package com.jorik.gobike.Bluetooth;


import static com.jorik.gobike.Bluetooth.BluetoothWorker.STATE_CONNECTING;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {

  private final BluetoothSocket mSocket;
  private BluetoothWorker mWorker;
//  private final BluetoothDevice mDevice;

  public ConnectThread(BluetoothDevice device, BluetoothWorker worker) {
//    mDevice = device;
    mWorker = worker;
    BluetoothSocket tmp = null;

    try {
      tmp = device.createRfcommSocketToServiceRecord(
          UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
    } catch (IOException ignored) {
    }
    mSocket = tmp;
    mWorker.setState(STATE_CONNECTING);
  }

  public void run() {

    mWorker.getAdapter().cancelDiscovery();

    try {
      mSocket.connect();
    } catch (IOException e) {
      try {
        mSocket.close();
      } catch (IOException ignored) {
      }

      mWorker.connectionFailed();
      return;
    }

    synchronized (mWorker) {
      mWorker.setConnectThread(null);
    }

    mWorker.connected(mSocket);
  }

  public void cancel() {
    try {
      mSocket.close();
    } catch (IOException ignored) {
    }
  }
}
