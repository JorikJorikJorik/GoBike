package com.jorik.gobike.Utils;

import static com.jorik.gobike.Model.Enum.CashDataType.ARDUINO;
import static com.jorik.gobike.Utils.DateUtils.PARSE_DATE_CREATE;
import static com.jorik.gobike.Utils.SharedPreferencesUtils.EVERY_DAY_STATISTICS_DATE_CREATE_KEY;
import static com.jorik.gobike.Utils.SharedPreferencesUtils.EVERY_DAY_STATISTICS_DIRECTION;
import static com.jorik.gobike.Utils.SharedPreferencesUtils.EVERY_DAY_STATISTICS_ID_KEY;

import android.content.Context;
import com.jorik.gobike.Model.POJO.GraphModel;
import com.jorik.gobike.Model.POJO.StatisticsModel;
import com.jorik.gobike.Model.POJO.TotalStatisticsModel;
import com.jorik.gobike.Model.POJO.WeekStatisticsModel;
import com.jorik.gobike.Network.DTO.DetailsWeekStatisticsDTO;
import com.jorik.gobike.Network.DTO.EveryDayProfileStatisticsDTO;
import com.jorik.gobike.Network.DTO.ProfileStatisticsDTO;
import com.jorik.gobike.R;
import java.util.Date;
import java.util.List;
import java.util.Random;
import rx.Observable;

public class WorkStatisticsUtils {

  private static final int CREATE_ID_EVERY_DAY = -1;
  private static final int COMPARE_SUCCESS = 0;
  private static final int COUNT_TOTAL_PROFILE = 6;
  private static final int COUNT_TOTAL_WEEK = 4;
  private static final int INDEX_MINUTES = 600000000;

  private SharedPreferencesUtils mSharedPreferencesUtils;
  private ResourceUtils mResourceUtils;
//  private CacheUtils<EveryDayProfileStatisticsDTO> mCacheUtilsArduino;

  public WorkStatisticsUtils(Context context) {
    mResourceUtils = ResourceUtils.with(context);
  }

  public WorkStatisticsUtils(Context context, SharedPreferencesUtils sharedPreferencesUtils) {
    mSharedPreferencesUtils = sharedPreferencesUtils;
//    mCacheUtilsArduino = new CacheUtils<>(context, ARDUINO);
    mResourceUtils = ResourceUtils.with(context);
  }

  public EveryDayProfileStatisticsDTO createEveryDayStatisticsModel() {
    EveryDayProfileStatisticsDTO everyDayProfileStatisticsDTO = new EveryDayProfileStatisticsDTO();
    everyDayProfileStatisticsDTO.setEveryDayProfileStatisticsId(CREATE_ID_EVERY_DAY);
    everyDayProfileStatisticsDTO.setTimeCreate(new Date());
    return everyDayProfileStatisticsDTO;
  }

  public Observable<EveryDayProfileStatisticsDTO> updateEveryDayStatisticsModel() {
//    return mCacheUtilsArduino.asyncRead(EveryDayProfileStatisticsDTO.class);

    EveryDayProfileStatisticsDTO everyDayProfileStatisticsDTO = new EveryDayProfileStatisticsDTO();
    everyDayProfileStatisticsDTO.setEveryDayProfileStatisticsId(savedEveryDayStatisticsId());
    everyDayProfileStatisticsDTO.setCountDistance((double) generateRandomData(1, 10));
    everyDayProfileStatisticsDTO.setMiddleSpeed((double) generateRandomData(5, 15));
    everyDayProfileStatisticsDTO.setTimeInTrip((long) generateRandomData(1000, 2500));
    everyDayProfileStatisticsDTO.setCalories(generateRandomData(1000, 2500));
    return Observable.just(everyDayProfileStatisticsDTO);
  }

  private int generateRandomData(int min, int max){
    Random random = new Random();
    return random.nextInt((max - min) + 1) + min;
  }

  public void saveKeyEveryDayStatistics(int everyDayStatisticsId) {
    mSharedPreferencesUtils.writeInt(EVERY_DAY_STATISTICS_DIRECTION, EVERY_DAY_STATISTICS_ID_KEY,
        everyDayStatisticsId);
    mSharedPreferencesUtils
        .writeString(EVERY_DAY_STATISTICS_DIRECTION, EVERY_DAY_STATISTICS_DATE_CREATE_KEY,
            DateUtils.formatDate(new Date(), PARSE_DATE_CREATE));
  }

  private int savedEveryDayStatisticsId() {
    return mSharedPreferencesUtils
        .readInt(EVERY_DAY_STATISTICS_DIRECTION, EVERY_DAY_STATISTICS_ID_KEY);
  }

  public boolean isCreatedEveryDayStatistics() {
    String date = mSharedPreferencesUtils
        .readString(EVERY_DAY_STATISTICS_DIRECTION, EVERY_DAY_STATISTICS_DATE_CREATE_KEY);
    if (date != null) {
      new Date();
      return DateUtils.parseDate(date, PARSE_DATE_CREATE).compareTo(DateUtils
          .parseDate(DateUtils.formatDate(new Date(), PARSE_DATE_CREATE), PARSE_DATE_CREATE))
          == COMPARE_SUCCESS;
    }
    return false;
  }

  public Observable<List<TotalStatisticsModel>> constructTotalStatisticsModel(int titleArray,
      int iconArray, int indexArray, String[] totalValue) {
    return Observable
        .zip(Observable.from(mResourceUtils.stringArray(titleArray)),
            Observable.from(mResourceUtils.integerArray(iconArray)),
            Observable.from(totalValue),
            Observable.from(mResourceUtils.stringArray(indexArray)),
            TotalStatisticsModel::new)
        .toList();
  }

  public Observable<StatisticsModel> constructStatisticsModel(
      ProfileStatisticsDTO profileStatisticsDTO) {
    return constructTotalStatisticsModel(R.array.title_total_statistics_profile,
        R.array.icon_total_statistics_profile, R.array.index_total_statistics_profile,
        generateTotalValueFromProfileStatistics(profileStatisticsDTO))
        .zipWith(Observable.just(profileStatisticsDTO.getEveryDayProfileStatistics()),
            StatisticsModel::new);
  }

  public Observable<WeekStatisticsModel> constructWeekStatisticsModel(
      DetailsWeekStatisticsDTO detailsWeekStatisticsDTO) {
    return constructTotalStatisticsModel(R.array.title_total_statistics_week,
        R.array.icon_total_statistics_week, R.array.index_total_statistics_week,
        generateTotalValueFromWeekStatistics(detailsWeekStatisticsDTO))
        .zipWith(createWeekTypeStatistics(detailsWeekStatisticsDTO), WeekStatisticsModel::new);
  }

  private Observable<List<GraphModel>> createWeekTypeStatistics(DetailsWeekStatisticsDTO dto) {
    return Observable
        .concat(generateGraphModelFromTimestamp(dto.getDistance(), 0),
            generateGraphModelFromTimestamp(dto.getSpeed(), 1),
            generateGraphModelFromTimestamp(dto.getTimeInTrip(), 2),
            generateGraphModelFromTimestamp(dto.getCalories(), 3))
        .toList();
  }

  private <T> Observable<GraphModel> generateGraphModelFromTimestamp(List<T> inputDataList,
      int count) {
    return Observable.zip(
        Observable.just(mResourceUtils.stringArray(R.array.name_graph)[count]),
        Observable.just(mResourceUtils.stringArray(R.array.index_graph)[count]),
        Observable.from(mResourceUtils.stringArray(R.array.name_week)).toList(),
        convertDataToString(inputDataList), GraphModel::new);
  }

  private <T> Observable<List<Integer>> convertDataToString(List<T> inputDataList) {
    return Observable
        .from(inputDataList)
        .map(this::convertData)
        .toList();
  }

  private <T> Integer convertData(T inputData) {
    if (inputData instanceof Long) {
      return (int) ((Long) inputData / INDEX_MINUTES);
    }
    return (Integer) inputData;
  }

  private String[] generateTotalValueFromProfileStatistics(
      ProfileStatisticsDTO profileStatisticsDTO) {

    String[] totalValue = new String[COUNT_TOTAL_PROFILE];
    totalValue[0] = Double.toString(profileStatisticsDTO.getCountDistanceTotal());
    totalValue[1] = Double.toString(profileStatisticsDTO.getMiddleSpeedTotal());
    totalValue[2] = DateUtils.parseInterval(profileStatisticsDTO.getTimeInTripTotal());
    totalValue[3] = Integer.toString(profileStatisticsDTO.getCaloriesTotal());
    totalValue[4] = Integer.toString(profileStatisticsDTO.getCountDangerousSituation());
    totalValue[5] = Integer.toString(profileStatisticsDTO.getCountAttemptedTheft());

    return totalValue;
  }

  private String[] generateTotalValueFromWeekStatistics(
      DetailsWeekStatisticsDTO detailsWeekStatisticsDTO) {

    String[] totalValue = new String[COUNT_TOTAL_WEEK];
    totalValue[0] = detailsWeekStatisticsDTO.getLongest();
    totalValue[1] = detailsWeekStatisticsDTO.getShortest();
    totalValue[2] = detailsWeekStatisticsDTO.getMiddleEffective();
    totalValue[3] = detailsWeekStatisticsDTO.getCountEffectiveDay();

    return totalValue;
  }

}
