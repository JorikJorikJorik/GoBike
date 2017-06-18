package com.jorik.gobike.View.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jorik.gobike.BR;
import com.jorik.gobike.Network.DTO.DetailsWeekStatisticsDTO;
import com.jorik.gobike.R;
import com.jorik.gobike.ViewModel.DetailsStatisticsViewModel;
import com.jorik.gobike.databinding.FragmentDetailsStatisticsBinding;

public class DetailsStatisticsFragment extends
    BaseFragment<FragmentDetailsStatisticsBinding, DetailsStatisticsViewModel> {

  private static final String DETAILS_STATISTICS_ARGS = "details_statistics_args";

  private DetailsWeekStatisticsDTO mDetailsStatisticsDTO;

  public static DetailsStatisticsFragment newInstance(
      DetailsWeekStatisticsDTO detailsWeekStatisticsDTO) {
    DetailsStatisticsFragment fragment = new DetailsStatisticsFragment();
    Bundle args = new Bundle();
    args.putParcelable(DETAILS_STATISTICS_ARGS, detailsWeekStatisticsDTO);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    mDetailsStatisticsDTO = getArguments().getParcelable(DETAILS_STATISTICS_ARGS);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public int getLayoutId() {
    return R.layout.fragment_details_statistics;
  }

  @Override
  public int getVariable() {
    return BR.details_statistics;
  }

  @Override
  public DetailsStatisticsViewModel createViewModel() {
    return new DetailsStatisticsViewModel(getActivity(), mDetailsStatisticsDTO);
  }
}
