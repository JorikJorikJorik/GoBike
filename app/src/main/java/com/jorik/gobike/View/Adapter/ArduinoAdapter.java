package com.jorik.gobike.View.Adapter;

import android.content.Context;
import com.jorik.gobike.BR;
import com.jorik.gobike.Network.DTO.ArduinoDTO;
import com.jorik.gobike.R;
import com.jorik.gobike.ViewModel.ArduinoViewModel;
import com.jorik.gobike.ViewModel.Callback.DeleteArduinoCallback;
import com.jorik.gobike.databinding.ItemArduinoBinding;
import java.util.List;

public class ArduinoAdapter extends BaseAdapter<ArduinoDTO, ItemArduinoBinding> {

  private DeleteArduinoCallback mArduinoCallback;

  public ArduinoAdapter(Context context, List<ArduinoDTO> dataArrayList) {
    super(context, dataArrayList);
  }

  @Override
  public int getLayoutId() {
    return R.layout.item_arduino;
  }

  @Override
  public int getVariable() {
    return BR.arduino_item;
  }

  @Override
  public Integer getVariableAdapter() {
    return null;
  }

  @Override
  public void onBindViewHolder(BaseAdapter.ViewHolder holder, int position) {
    super.onBindViewHolder(holder, position);
    ItemArduinoBinding binding = (ItemArduinoBinding) holder.getBinding();
    binding.imageViewArduinoDelete.setOnClickListener(v -> {
      if(mArduinoCallback != null)
        mArduinoCallback.deleteArduino(getDataArrayList().get(holder.getAdapterPosition()).getArduinoId());
    });
  }

  @Override
  public void setDataArrayList(List<ArduinoDTO> dataArrayList) {
    super.setDataArrayList(dataArrayList);
  }

  @Override
  public List<ArduinoDTO> getDataArrayList() {
    return super.getDataArrayList();
  }


  public void registerCallback(DeleteArduinoCallback callback) {
    mArduinoCallback = callback;
  }
}
