package com.jorik.gobike.View.Adapter;

import android.content.Context;
import android.content.Intent;
import com.jorik.gobike.BR;
import com.jorik.gobike.Network.DTO.DetailsWeekStatisticsDTO;
import com.jorik.gobike.Network.DTO.EveryDayProfileStatisticsDTO;
import com.jorik.gobike.R;
import com.jorik.gobike.View.Activity.DetailsStatisticsActivity;
import com.jorik.gobike.ViewModel.Callback.DetailStatisticsCallback;
import com.jorik.gobike.databinding.ItemPeriodStatisticsBinding;
import java.util.ArrayList;
import java.util.List;

public class PeriodStatisticsAdapter extends
    BaseAdapter<EveryDayProfileStatisticsDTO, ItemPeriodStatisticsBinding>{

  public static final String DETAILS_STATISTICS_NAME_EXTRA = "details_statistics_name_extra";

  private Context mContext;
  private DetailStatisticsCallback mDetailStatisticsCallback;

  public PeriodStatisticsAdapter(Context context,
      List<EveryDayProfileStatisticsDTO> dataArrayList) {
    super(context, dataArrayList);
    mContext = context;
  }

  @Override
  public void onBindViewHolder(BaseAdapter.ViewHolder holder, int position) {
    super.onBindViewHolder(holder, position);
    ItemPeriodStatisticsBinding binding = (ItemPeriodStatisticsBinding) holder.getBinding();
    binding.constrainLayoutPeriodStatistics.setOnClickListener(v -> chooseDate(position));
  }

  @Override
  public int getLayoutId() {
    return R.layout.item_period_statistics;
  }

  @Override
  public int getVariable() {
    return BR.period;
  }

  @Override
  public Integer getVariableAdapter() {
    return BR.period_adapter;
  }

  @Override
  public void setDataArrayList(List<EveryDayProfileStatisticsDTO> dataArrayList) {
    super.setDataArrayList(dataArrayList);
  }

  @Override
  public List<EveryDayProfileStatisticsDTO> getDataArrayList() {
    return super.getDataArrayList();
  }

  public void registerCallback(DetailStatisticsCallback callback) {
    mDetailStatisticsCallback = callback;
  }

  private void chooseDate(int position) {
    if (mDetailStatisticsCallback != null) {
      mDetailStatisticsCallback.detailStatisticsRequest(super.getDataArrayList().get(position).getNameDate());
    }
  }

  public void openDetails(ArrayList<DetailsWeekStatisticsDTO> detailsStatistics){
    Intent moveToDetailsStatistics = new Intent(mContext, DetailsStatisticsActivity.class);
    moveToDetailsStatistics.putParcelableArrayListExtra(DETAILS_STATISTICS_NAME_EXTRA, detailsStatistics);
    mContext.startActivity(moveToDetailsStatistics);
  }

}
