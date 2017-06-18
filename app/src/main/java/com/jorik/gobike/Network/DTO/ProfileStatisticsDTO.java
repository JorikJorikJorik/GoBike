package com.jorik.gobike.Network.DTO;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jorik.gobike.Utils.ConverterUtils;
import java.util.Date;
import java.util.List;

public class ProfileStatisticsDTO implements Parcelable {

  @SerializedName("CountDistanceTotal")
  @Expose
  private Double countDistanceTotal;

  @SerializedName("MiddleSpeedTotal")
  @Expose
  private Double middleSpeedTotal;

  @SerializedName("TimeInTripTotal")
  @Expose
  private Integer timeInTripTotal;

  @SerializedName("CaloriesTotal")
  @Expose
  private Integer caloriesTotal;

  @SerializedName("CountDangerousSituation")
  @Expose
  private Integer countDangerousSituation;

  @SerializedName("CountAttemptedTheft")
  @Expose
  private Integer countAttemptedTheft;

  @SerializedName("EveryDayProfileStatistics")
  @Expose
  private List<EveryDayProfileStatisticsDTO> everyDayProfileStatistics;

  protected ProfileStatisticsDTO(Parcel in) {
    countDistanceTotal = in.readDouble();
    middleSpeedTotal = in.readDouble();
    timeInTripTotal = in.readInt();
    caloriesTotal = in.readInt();
    countDangerousSituation = in.readInt();
    countAttemptedTheft = in.readInt();
    everyDayProfileStatistics = in.createTypedArrayList(EveryDayProfileStatisticsDTO.CREATOR);
  }

  public static final Creator<ProfileStatisticsDTO> CREATOR = new Creator<ProfileStatisticsDTO>() {
    @Override
    public ProfileStatisticsDTO createFromParcel(Parcel in) {
      return new ProfileStatisticsDTO(in);
    }

    @Override
    public ProfileStatisticsDTO[] newArray(int size) {
      return new ProfileStatisticsDTO[size];
    }
  };

  public Double getCountDistanceTotal() {
    return ConverterUtils.decimal(countDistanceTotal);
  }

  public void setCountDistanceTotal(Double countDistanceTotal) {
    this.countDistanceTotal = countDistanceTotal;
  }

  public Double getMiddleSpeedTotal() {
    return ConverterUtils.decimal(middleSpeedTotal);
  }

  public void setMiddleSpeedTotal(Double middleSpeedTotal) {
    this.middleSpeedTotal = middleSpeedTotal;
  }

  public Integer getTimeInTripTotal() {
    return timeInTripTotal;
  }

  public void setTimeInTripTotal(Integer timeInTripTotal) {
    this.timeInTripTotal = timeInTripTotal;
  }

  public Integer getCaloriesTotal() {
    return caloriesTotal;
  }

  public void setCaloriesTotal(Integer caloriesTotal) {
    this.caloriesTotal = caloriesTotal;
  }

  public Integer getCountDangerousSituation() {
    return countDangerousSituation;
  }

  public void setCountDangerousSituation(Integer countDangerousSituation) {
    this.countDangerousSituation = countDangerousSituation;
  }

  public Integer getCountAttemptedTheft() {
    return countAttemptedTheft;
  }

  public void setCountAttemptedTheft(Integer countAttemptedTheft) {
    this.countAttemptedTheft = countAttemptedTheft;
  }

  public List<EveryDayProfileStatisticsDTO> getEveryDayProfileStatistics() {
    return everyDayProfileStatistics;
  }

  public void setEveryDayProfileStatistics(
      List<EveryDayProfileStatisticsDTO> everyDayProfileStatistics) {
    this.everyDayProfileStatistics = everyDayProfileStatistics;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeDouble(countDistanceTotal);
    dest.writeDouble(middleSpeedTotal);
    dest.writeInt(timeInTripTotal);
    dest.writeInt(caloriesTotal);
    dest.writeInt(countDangerousSituation);
    dest.writeInt(countAttemptedTheft);
    dest.writeTypedList(everyDayProfileStatistics);
  }
}
