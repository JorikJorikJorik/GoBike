package com.jorik.gobike.View.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jorik.gobike.Network.DTO.DetailsWeekStatisticsDTO;
import com.jorik.gobike.View.Adapter.BaseAdapter.ViewHolder;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, B extends ViewDataBinding> extends
    RecyclerView.Adapter<ViewHolder> {

  private Context mContext;
  private List<T> dataArrayList;

  public BaseAdapter(Context context, List<T> dataArrayList) {
    mContext = context;
    this.dataArrayList = dataArrayList;
  }

  public abstract int getLayoutId();

  public abstract int getVariable();

  public abstract Integer getVariableAdapter();

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(mContext);
    B binding = DataBindingUtil.inflate(inflater, getLayoutId(), parent, false);
    return new ViewHolder(binding.getRoot());
  }

  @Override
  public void onBindViewHolder(BaseAdapter.ViewHolder holder, int position) {
    T item = dataArrayList.get(position);
    B binding = (B) holder.getBinding();
    binding.setVariable(getVariable(), item);
    if (getVariableAdapter() != null) {
      binding.setVariable(getVariableAdapter(), this);
    }
    binding.executePendingBindings();
  }

  @Override
  public int getItemCount() {
    return dataArrayList.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    private B binding;

    ViewHolder(View itemView) {
      super(itemView);

      binding = DataBindingUtil.bind(itemView);
    }

    B getBinding() {
      return binding;
    }
  }

  public void setDataArrayList(List<T> dataArrayList) {
    this.dataArrayList = new ArrayList<>(dataArrayList);
  }

  public List<T> getDataArrayList() {
    return dataArrayList;
  }
}