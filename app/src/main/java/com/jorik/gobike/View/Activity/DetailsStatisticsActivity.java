package com.jorik.gobike.View.Activity;


import static com.jorik.gobike.View.Adapter.PeriodStatisticsAdapter.DETAILS_STATISTICS_NAME_EXTRA;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import com.jorik.gobike.Network.DTO.DetailsWeekStatisticsDTO;
import com.jorik.gobike.R;
import com.jorik.gobike.View.Adapter.DetailsStatisticsFragmentPagerAdapter;
import java.util.ArrayList;

public class DetailsStatisticsActivity extends BaseActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details_statistics);

    TabLayout mTabLayout = (TabLayout) findViewById(R.id.tableLayoutDetailsStatistics);
    ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPagerDetailsStatistics);

    Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbarDetailStatistics);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    ArrayList<DetailsWeekStatisticsDTO> detailsWeekStatisticsDTOs = getIntent()
        .getParcelableArrayListExtra(DETAILS_STATISTICS_NAME_EXTRA);

    mViewPager.setAdapter(new DetailsStatisticsFragmentPagerAdapter(getSupportFragmentManager(),
        detailsWeekStatisticsDTOs));
    mTabLayout.setupWithViewPager(mViewPager);
  }

  @Override
  public void homeToolbarButton(Boolean isEnable) {
    super.homeToolbarButton(true);
  }
}
