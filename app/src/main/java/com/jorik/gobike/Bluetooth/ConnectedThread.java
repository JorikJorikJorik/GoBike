package com.jorik.gobike.Bluetooth;

import static com.jorik.gobike.Bluetooth.BluetoothWorker.STATE_CONNECTED;

import android.bluetooth.BluetoothSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ConnectedThread extends Thread {

  private static final String UTF_8 = "UTF-8";
  private static final int BUFFER_SIZE = 1024;

  private final BluetoothSocket mSocket;
  private final InputStream mInStream;
  private final OutputStream mOutStream;
  private BluetoothWorker mWorker;

  public ConnectedThread(BluetoothSocket socket, BluetoothWorker worker) {
    mSocket = socket;
    mWorker = worker;

    InputStream tmpIn = null;
    OutputStream tmpOut = null;

    try {
      tmpIn = socket.getInputStream();
      tmpOut = socket.getOutputStream();
    } catch (IOException ignored) {
    }

    mInStream = tmpIn;
    mOutStream = tmpOut;

    mWorker.setState(STATE_CONNECTED);
  }

  public void run() {
    byte[] buffer = new byte[BUFFER_SIZE];
    int bytes;

    StringBuilder builder = new StringBuilder();
//    while (mWorker.getState() == STATE_CONNECTED) {
//      try {
//        bytes = mInStream.read();
//        String result = new String(buffer, UTF_8);
//        builder.append(result);
//      } catch (IOException e) {
//        mWorker.connectionLost();
//        break;
//      }
//    }
    try {

      BufferedReader input = new BufferedReader(new InputStreamReader(mInStream));
    String inputLine = null;
      if (input.ready()) {
        inputLine = input.readLine();
        System.out.println(inputLine);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void write(byte[] buffer) {
    try {
      mOutStream.write(buffer);
    } catch (IOException ignored) {
    }
  }

  public void cancel() {
    try {
      mSocket.close();
    } catch (IOException ignored) {
    }
  }
}

