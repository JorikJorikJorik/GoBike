package com.jorik.gobike.ViewModel;

import android.content.Context;
import android.databinding.ObservableField;
import com.jorik.gobike.Model.POJO.WeekStatisticsModel;
import com.jorik.gobike.Network.DTO.DetailsWeekStatisticsDTO;
import com.jorik.gobike.Utils.WorkStatisticsUtils;
import com.jorik.gobike.View.Adapter.GraphAdapter;
import com.jorik.gobike.View.Adapter.TotalStatisticsAdapter;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class DetailsStatisticsViewModel extends BaseViewModel {


  public final ObservableField<TotalStatisticsAdapter> totalAdapter;
  public final ObservableField<GraphAdapter> graphAdapter;

  private Context mContext;
  private CompositeSubscription mSubscription;
  private WorkStatisticsUtils mWorkStatisticsUtils;

  public DetailsStatisticsViewModel(Context context, DetailsWeekStatisticsDTO detailsStatisticsDTO) {
    mContext = context;
    mWorkStatisticsUtils = new WorkStatisticsUtils(mContext);
    mSubscription = new CompositeSubscription();
    super.initCompositeSubscription(mSubscription);

    totalAdapter = new ObservableField<>();
    graphAdapter = new ObservableField<>();

    convertDataAndFillAdapter(detailsStatisticsDTO);
  }

  private void convertDataAndFillAdapter(DetailsWeekStatisticsDTO detailsWeekStatistics) {
    Subscription subscriptionPrepare = Observable
        .just(detailsWeekStatistics)
        .flatMap(statistics -> mWorkStatisticsUtils.constructWeekStatisticsModel(statistics))
        .doOnNext(this::fillAdapter)
        .subscribe(new Subscriber<WeekStatisticsModel>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(WeekStatisticsModel weekStatisticsModel) {

          }
        });

    mSubscription.add(subscriptionPrepare);
  }

  private void fillAdapter(WeekStatisticsModel model){
    totalAdapter.set(new TotalStatisticsAdapter(mContext, model.getTotalStatisticsModels()));
    graphAdapter.set(new GraphAdapter(mContext, model.getGraphModels()));
  }

}
