package com.jorik.gobike.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.util.UUID;


public class BluetoothWorker {

  public static final int STATE_NONE = 0;
  public static final int STATE_LISTEN = 1;
  public static final int STATE_CONNECTING = 2;
  public static final int STATE_CONNECTED = 3;
  public static final UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

  private final BluetoothAdapter mAdapter;
  private ConnectThread mConnectThread;
  private ConnectedThread mConnectedThread;
  private int mState;


  public BluetoothWorker(/*Handler handler*/) {
    mAdapter = BluetoothAdapter.getDefaultAdapter();
    mState = STATE_NONE;

  }

  private synchronized void updateUserInterfaceTitle() {
    mState = getState();
  }

  public synchronized void start() {

    if (mConnectThread != null) {
      mConnectThread.cancel();
      mConnectThread = null;
    }
    if (mConnectedThread != null) {
      mConnectedThread.cancel();
      mConnectedThread = null;
    }

    updateUserInterfaceTitle();
  }

  public synchronized void connect(BluetoothDevice device, boolean secure) {

    if (mState == STATE_CONNECTING) {
      if (mConnectThread != null) {
        mConnectThread.cancel();
        mConnectThread = null;
      }
    }

    if (mConnectedThread != null) {
      mConnectedThread.cancel();
      mConnectedThread = null;
    }

    mConnectThread = new ConnectThread(device, this);
    mConnectThread.start();

    updateUserInterfaceTitle();
  }

  public synchronized void connected(BluetoothSocket socket/*, BluetoothDevice device*/) {

    if (mConnectThread != null) {
      mConnectThread.cancel();
      mConnectThread = null;
    }

    if (mConnectedThread != null) {
      mConnectedThread.cancel();
      mConnectedThread = null;
    }

    mConnectedThread = new ConnectedThread(socket, this);
    mConnectedThread.start();

    updateUserInterfaceTitle();
  }


  public synchronized void stop() {

    if (mConnectThread != null) {
      mConnectThread.cancel();
      mConnectThread = null;
    }

    if (mConnectedThread != null) {
      mConnectedThread.cancel();
      mConnectedThread = null;
    }

    setState(STATE_NONE);

    updateUserInterfaceTitle();
  }

  public void write(byte[] out) {

    ConnectedThread r;

    synchronized (this) {
      if (getState() != STATE_CONNECTED) {
        return;
      }
      r = mConnectedThread;
    }
    r.write(out);
  }

  public void connectionFailed() {
    setState(STATE_NONE);
    updateUserInterfaceTitle();
    start();
  }

  public void connectionLost() {
    setState(STATE_NONE);
    updateUserInterfaceTitle();
    start();
  }

  public synchronized int getState() {
    return mState;
  }

  public void setState(int state) {
    mState = state;
  }

  public BluetoothAdapter getAdapter() {
    return mAdapter;
  }

  public void setConnectThread(ConnectThread connectThread) {
    mConnectThread = connectThread;
  }
}