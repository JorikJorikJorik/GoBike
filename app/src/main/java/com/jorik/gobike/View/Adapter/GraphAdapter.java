package com.jorik.gobike.View.Adapter;

import android.content.Context;
import com.jorik.gobike.BR;
import com.jorik.gobike.Model.POJO.GraphModel;
import com.jorik.gobike.R;
import com.jorik.gobike.databinding.ItemWeekGraphBinding;
import java.util.List;

public class GraphAdapter extends BaseAdapter<GraphModel, ItemWeekGraphBinding> {

  public GraphAdapter(Context context, List<GraphModel> dataArrayList) {
    super(context, dataArrayList);
  }

  @Override
  public int getLayoutId() {
    return R.layout.item_week_graph;
  }

  @Override
  public int getVariable() {
    return BR.week_data;
  }

  @Override
  public Integer getVariableAdapter() {
    return null;
  }

}
