package com.jorik.gobike.View.Adapter;

import android.content.Context;
import com.jorik.gobike.BR;
import com.jorik.gobike.Model.POJO.TotalStatisticsModel;
import com.jorik.gobike.R;
import com.jorik.gobike.databinding.ItemTotalStatisticsBinding;
import java.util.List;

public class TotalStatisticsAdapter extends
    BaseAdapter<TotalStatisticsModel, ItemTotalStatisticsBinding> {

  public TotalStatisticsAdapter(Context context, List<TotalStatisticsModel> dataArrayList) {
    super(context, dataArrayList);
  }

  @Override
  public int getLayoutId() {
    return R.layout.item_total_statistics;
  }

  @Override
  public int getVariable() {
    return BR.total;
  }

  @Override
  public Integer getVariableAdapter() {
    return null;
  }

  @Override
  public void setDataArrayList(List<TotalStatisticsModel> dataArrayList) {
    super.setDataArrayList(dataArrayList);
  }
}
