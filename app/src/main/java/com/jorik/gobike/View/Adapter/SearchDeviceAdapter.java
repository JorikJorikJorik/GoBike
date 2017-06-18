package com.jorik.gobike.View.Adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import com.jorik.gobike.BR;
import com.jorik.gobike.R;
import com.jorik.gobike.ViewModel.Callback.BluetoothDeviceCallback;
import com.jorik.gobike.databinding.ItemSearchDeviceBinding;
import java.util.List;

public class SearchDeviceAdapter extends
    BaseAdapter<BluetoothDevice, ItemSearchDeviceBinding> {

  private BluetoothDeviceCallback mBluetoothDeviceCallback;

  public SearchDeviceAdapter(Context context, List<BluetoothDevice> deviceList) {
    super(context, deviceList);
  }

  @Override
  public int getLayoutId() {
    return R.layout.item_search_device;
  }

  @Override
  public int getVariable() {
    return BR.bluetooth_device;
  }

  @Override
  public Integer getVariableAdapter() {
    return null;
  }

  @Override
  public List<BluetoothDevice> getDataArrayList() {
    return super.getDataArrayList();
  }

  @Override
  public void setDataArrayList(List<BluetoothDevice> dataArrayList) {
    super.setDataArrayList(dataArrayList);
  }

  @Override
  public void onBindViewHolder(BaseAdapter.ViewHolder holder, int position) {
    super.onBindViewHolder(holder, position);
    ItemSearchDeviceBinding binding = (ItemSearchDeviceBinding) holder.getBinding();
    binding.constrainLayoutSearchDevice.setOnClickListener((v) -> {
      if (mBluetoothDeviceCallback != null) {
        mBluetoothDeviceCallback.bluetoothDevice(getDataArrayList().get(position));
      }
    });
  }

  public void registerCallback(BluetoothDeviceCallback callback) {
    mBluetoothDeviceCallback = callback;
  }
}
