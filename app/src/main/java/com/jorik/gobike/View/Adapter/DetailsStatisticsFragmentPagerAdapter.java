package com.jorik.gobike.View.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.jorik.gobike.Network.DTO.DetailsWeekStatisticsDTO;
import com.jorik.gobike.View.Fragment.DetailsStatisticsFragment;
import java.util.ArrayList;

public class DetailsStatisticsFragmentPagerAdapter extends FragmentPagerAdapter {

  private ArrayList<DetailsWeekStatisticsDTO> mDetailsStatisticsDTOs;

  public DetailsStatisticsFragmentPagerAdapter(FragmentManager fragmentManager, ArrayList<DetailsWeekStatisticsDTO> detailsWeekStatisticsDTOs) {
    super(fragmentManager);
    mDetailsStatisticsDTOs = detailsWeekStatisticsDTOs;
  }

  @Override
  public Fragment getItem(int position) {
    return DetailsStatisticsFragment.newInstance(mDetailsStatisticsDTOs.get(position));
  }

  @Override
  public int getCount() {
    return mDetailsStatisticsDTOs.size();
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return mDetailsStatisticsDTOs.get(position).getPeriodName();
  }
}
