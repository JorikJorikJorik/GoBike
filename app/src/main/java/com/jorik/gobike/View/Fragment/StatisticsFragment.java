package com.jorik.gobike.View.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ProgressBar;
import android.widget.Spinner;
import com.jorik.gobike.BR;
import com.jorik.gobike.Network.DTO.PreviewProfileDTO;
import com.jorik.gobike.R;
import com.jorik.gobike.Utils.LoadingViewUtils;
import com.jorik.gobike.ViewModel.Callback.LoadProgressStateCallback;
import com.jorik.gobike.ViewModel.StatisticsViewModel;
import com.jorik.gobike.databinding.FragmentStatisticsBinding;

public class StatisticsFragment extends
    BaseFragment<FragmentStatisticsBinding, StatisticsViewModel> implements
    LoadProgressStateCallback {

  private static final String PREVIEW_PROFILE_ARGS = "preview_profile_args";

  private ProgressBar mProgressBarLoadData;
  private PreviewProfileDTO mPreviewProfileDTO;
  private StatisticsViewModel mStatisticsViewModel;

  public static StatisticsFragment newInstance(PreviewProfileDTO previewProfileDTO) {
    StatisticsFragment fragment = new StatisticsFragment();
    Bundle args = new Bundle();
    args.putParcelable(PREVIEW_PROFILE_ARGS, previewProfileDTO);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    mPreviewProfileDTO = getArguments().getParcelable(PREVIEW_PROFILE_ARGS);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mStatisticsViewModel = new StatisticsViewModel(getActivity(), mPreviewProfileDTO);
    mStatisticsViewModel.registerCallback(this);
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.statistics_menu, menu);
    createMenuItem(menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public int getLayoutId() {
    return R.layout.fragment_statistics;
  }

  @Override
  public int getVariable() {
    return BR.profile;
  }

  @Override
  public StatisticsViewModel createViewModel() {
    return mStatisticsViewModel;
  }

  private void createMenuItem(Menu menu) {
    loadSpinnerMenu(menu);
    loadProgressMenu(menu);
  }

  private void loadSpinnerMenu(Menu menu) {
    Spinner spinner = (Spinner) MenuItemCompat
        .getActionView(menu.findItem(R.id.statistics_item_choose_period));
    spinner.setAdapter(mStatisticsViewModel.buildPeriodAdapter());
    spinner.setSelection(mStatisticsViewModel.getSavedPeriodForSpinner());
    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mStatisticsViewModel.savePeriod(position);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

  private void loadProgressMenu(Menu menu) {
    LoadingViewUtils loadingViewUtils = new LoadingViewUtils(getActivity());
    mProgressBarLoadData = loadingViewUtils.createLoadingProgress();
    menu.findItem(R.id.statistics__item_choose_period_progress).setActionView(mProgressBarLoadData);
  }

  @Override
  public void loadProgressState(int state) {
    mProgressBarLoadData.setVisibility(state);
  }
}
