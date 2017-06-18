package com.jorik.gobike.ViewModel;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.jorik.gobike.Model.Enum.CashDataType.RESPONSE;
import static com.jorik.gobike.Utils.ResourceUtils.with;
import static com.jorik.gobike.Utils.SharedPreferencesUtils.PERIOD_DIRECTION;
import static com.jorik.gobike.Utils.SharedPreferencesUtils.PERIOD_TYPE_KEY;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.widget.ArrayAdapter;
import com.jorik.gobike.DataBase.DataBaseUtils;
import com.jorik.gobike.Model.POJO.StatisticsModel;
import com.jorik.gobike.Model.POJO.TotalStatisticsModel;
import com.jorik.gobike.Network.DTO.DetailsWeekStatisticsDTO;
import com.jorik.gobike.Network.DTO.EveryDayProfileStatisticsDTO;
import com.jorik.gobike.Network.DTO.PreviewProfileDTO;
import com.jorik.gobike.Network.DTO.ProfileStatisticsDTO;
import com.jorik.gobike.Network.RestClient;
import com.jorik.gobike.Network.Service.ProfileService;
import com.jorik.gobike.R;
import com.jorik.gobike.Utils.CacheUtils;
import com.jorik.gobike.Utils.DateUtils;
import com.jorik.gobike.Utils.ErrorViewUtils;
import com.jorik.gobike.Utils.ResourceUtils;
import com.jorik.gobike.Utils.SharedPreferencesUtils;
import com.jorik.gobike.Utils.WorkStatisticsUtils;
import com.jorik.gobike.View.Adapter.PeriodStatisticsAdapter;
import com.jorik.gobike.View.Adapter.TotalStatisticsAdapter;
import com.jorik.gobike.ViewModel.Callback.DetailStatisticsCallback;
import com.jorik.gobike.ViewModel.Callback.LoadProgressStateCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class StatisticsViewModel extends BaseViewModel implements DetailStatisticsCallback {

  private static final int EMPTY_VALUE = 0;
  private static final int BIAS_VALUE = 1;

  public final ObservableField<TotalStatisticsAdapter> totalAdapter;
  public final ObservableField<PeriodStatisticsAdapter> periodAdapter;
  public final ObservableField<Boolean> stateSwipeRefresh;
  public final ObservableBoolean stateScreen;

  private Context mContext;
  private DataBaseUtils mDataBaseUtils;
  private CompositeSubscription mSubscription;
  private ResourceUtils mResourceUtils;
  private ErrorViewUtils mErrorViewUtils;
  private SharedPreferencesUtils mSharedPreferencesUtils;
  private WorkStatisticsUtils mWorkStatisticsUtils;
  private CacheUtils<ProfileStatisticsDTO> mCacheUtils;

  private ProfileService mService;
  private PreviewProfileDTO mPreviewProfileDTO;
  private TotalStatisticsAdapter mTotalStatisticsAdapter;
  private PeriodStatisticsAdapter mPeriodStatisticsAdapter;
  private LoadProgressStateCallback mLoadProgressStateCallback;

  public StatisticsViewModel(Context context, PreviewProfileDTO previewProfileDTO) {
    mContext = context;
    mDataBaseUtils = new DataBaseUtils(mContext);
    mSubscription = new CompositeSubscription();
    mResourceUtils = with(mContext);
    mErrorViewUtils = new ErrorViewUtils(mContext);
    mSharedPreferencesUtils = new SharedPreferencesUtils(mContext);
    mWorkStatisticsUtils = new WorkStatisticsUtils(mContext, mSharedPreferencesUtils);
    mCacheUtils = new CacheUtils<>(mContext, RESPONSE);

    mService = RestClient.createClientService(ProfileService.class);
    mPreviewProfileDTO = previewProfileDTO;

    super.initDataBase(mDataBaseUtils);
    super.initCompositeSubscription(mSubscription);

    totalAdapter = new ObservableField<>();
    periodAdapter = new ObservableField<>();
    stateSwipeRefresh = new ObservableField<>();
    stateScreen = new ObservableBoolean();
  }

  public void registerCallback(LoadProgressStateCallback callback) {
    mLoadProgressStateCallback = callback;
  }

  @Override
  public void onResume() {
    super.onResume();
    updateData();
  }

  public void updateData() {
    getAndFormationStatisticsData();
    workWithEveryDayStatistics();
  }

  private void getAndFormationStatisticsData() {
    Observable<ProfileStatisticsDTO> profileStatisticsObservable =
        mPreviewProfileDTO != null ? savedProfileStatistics() : getProfileStatistics();

    Subscription subscriptionStatistics = profileStatisticsObservable
        .flatMap(statistics -> mCacheUtils.asyncWrite(statistics))
        .doOnNext(statistics -> clearPreviewData())
        .flatMap(statistics -> mWorkStatisticsUtils.constructStatisticsModel(statistics))
        .doOnNext(this::fillAdaptersData)
        .doAfterTerminate(this::stopRefreshing)
        .subscribe(new Subscriber<StatisticsModel>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(StatisticsModel statisticsModel) {

          }
        });

    mSubscription.add(subscriptionStatistics);
  }

  private Observable<ProfileStatisticsDTO> savedProfileStatistics() {
    return Observable.just(mPreviewProfileDTO.getProfileStatistics());
  }

  private Observable<ProfileStatisticsDTO> getProfileStatistics() {
    return mService
        .getProfileStatistics(mDataBaseUtils.getBasic(), getSavedPeriod())
        .delay(1, TimeUnit.SECONDS)//for imitation delay server!!!
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnCompleted(() -> updateTitle(mResourceUtils.string(R.string.navigation_item_statistics)))
        .doOnError(error -> updateTitle(mErrorViewUtils.formationShortError(error)))
        .onErrorResumeNext(mCacheUtils.asyncRead(ProfileStatisticsDTO.class));
  }

  private void updateTitle(String message) {
    super.updateToolbarMessage(message);
  }

  private void clearPreviewData() {
    if (mPreviewProfileDTO != null) {
      mPreviewProfileDTO = null;
    }
  }

  private void fillAdaptersData(StatisticsModel model) {
    fillTotalAdapter(model.getTotalStatistics());
    fillPeriodAdapter((model.getEveryDayStatistics()));
  }

  private void fillTotalAdapter(List<TotalStatisticsModel> totalList) {
    if (mTotalStatisticsAdapter == null) {
      mTotalStatisticsAdapter = new TotalStatisticsAdapter(mContext, totalList);
      totalAdapter.set(mTotalStatisticsAdapter);
    } else {
      mTotalStatisticsAdapter.setDataArrayList(totalList);
      mTotalStatisticsAdapter.notifyDataSetChanged();
    }
  }

  private void fillPeriodAdapter(List<EveryDayProfileStatisticsDTO> everyDayStatisticsList) {
    if (mPeriodStatisticsAdapter == null) {
      mPeriodStatisticsAdapter = new PeriodStatisticsAdapter(mContext, everyDayStatisticsList);
      mPeriodStatisticsAdapter.registerCallback(this);
      periodAdapter.set(mPeriodStatisticsAdapter);
    } else {
      int newListSize = everyDayStatisticsList.size();
      int oldListSize = mPeriodStatisticsAdapter.getDataArrayList().size();
      if (oldListSize > newListSize) {
        mPeriodStatisticsAdapter.notifyItemRangeRemoved(newListSize, oldListSize);
      }
      mPeriodStatisticsAdapter.setDataArrayList(everyDayStatisticsList);
      mPeriodStatisticsAdapter.notifyItemRangeChanged(EMPTY_VALUE, newListSize);
    }
  }

  private void stopRefreshing() {
    stateScreen.set(true);
    stateSwipeRefresh.set(new Boolean(false));
    if (mLoadProgressStateCallback != null) {
      mLoadProgressStateCallback.loadProgressState(GONE);
    }
  }

  private void workWithEveryDayStatistics() {
    boolean isCreated = mWorkStatisticsUtils.isCreatedEveryDayStatistics();
    Observable<Integer> everydayStatisticsId =
        isCreated ? updateEveryDayStatistics() : createEveryDayStatistics();
    Subscription subscriptionEveryDay = everydayStatisticsId
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(id -> saveIdEveryDayStatistics(isCreated, id))
        .subscribe(new Subscriber<Integer>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(Integer integer) {

          }
        });

    mSubscription.add(subscriptionEveryDay);
  }

  private Observable<Integer> createEveryDayStatistics() {
    return mService
        .createEveryDayStatistics(mDataBaseUtils.getBasic(),
            mWorkStatisticsUtils.createEveryDayStatisticsModel());
  }

  private Observable<Integer> updateEveryDayStatistics() {
    String base = mDataBaseUtils.getBasic();
    return mWorkStatisticsUtils
        .updateEveryDayStatisticsModel()
        .flatMap(statistics -> mService.updateEveryDayStatistics(base, statistics));
  }

  private void saveIdEveryDayStatistics(boolean isCreated, int id) {
    if (!isCreated) {
      mWorkStatisticsUtils.saveKeyEveryDayStatistics(id);
    }
  }

  public ArrayAdapter<CharSequence> buildPeriodAdapter() {
    ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter
        .createFromResource(mContext, R.array.time_period, R.layout.item_spinner_choose);
    arrayAdapter.setDropDownViewResource(R.layout.item_spinner_drop_down);
    return arrayAdapter;
  }

  public int getSavedPeriodForSpinner() {
    int savedValue = getSavedPeriod();
    return savedValue == EMPTY_VALUE ? savedValue : savedValue - BIAS_VALUE;
  }

  private int getSavedPeriod() {
    return mSharedPreferencesUtils.readInt(PERIOD_DIRECTION, PERIOD_TYPE_KEY);
  }

  public void savePeriod(int type) {
    mLoadProgressStateCallback.loadProgressState(
        mLoadProgressStateCallback != null && mPeriodStatisticsAdapter != null ? VISIBLE : GONE);
    mSharedPreferencesUtils.writeInt(PERIOD_DIRECTION, PERIOD_TYPE_KEY, type + BIAS_VALUE);
    getAndFormationStatisticsData();
  }

  @Override
  public void detailStatisticsRequest(String chooseDate) {
    getDetailStatisticsByPeriod(chooseDate);
  }

  private void getDetailStatisticsByPeriod(String chooseDate) {
    int typePeriod = getSavedPeriod();
    Subscription mSubscriptionDetailStatistics = mService
        .getDetailWeekStatistics(mDataBaseUtils.getBasic(), typePeriod, DateUtils.parseDateByTypePeriod(chooseDate, typePeriod))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(error -> mErrorViewUtils.showErrorToast(error))
        .doOnNext(detailsStatistics -> mPeriodStatisticsAdapter.openDetails(new ArrayList<>(detailsStatistics)))
        .subscribe(new Subscriber<List<DetailsWeekStatisticsDTO>>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(List<DetailsWeekStatisticsDTO> detailsWeekStatisticsDTOs) {

          }
        });

    mSubscription.add(mSubscriptionDetailStatistics);
  }

}
