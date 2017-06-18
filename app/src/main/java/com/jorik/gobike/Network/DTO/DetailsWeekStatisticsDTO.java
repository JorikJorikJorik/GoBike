package com.jorik.gobike.Network.DTO;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class DetailsWeekStatisticsDTO implements Parcelable {

  @SerializedName("PeriodName")
  @Expose
  private String periodName;

  @SerializedName("Longest")
  @Expose
  private String longest;

  @SerializedName("Shortest")
  @Expose
  private String shortest;

  @SerializedName("MiddleEffective")
  @Expose
  private String middleEffective;

  @SerializedName("CountEffectiveDay")
  @Expose
  private String countEffectiveDay;

  @SerializedName("Distance")
  @Expose
  private List<Integer> distance;

  @SerializedName("Speed")
  @Expose
  private List<Integer> speed;

  @SerializedName("TimeInTrip")
  @Expose
  private List<Long> timeInTrip;

  @SerializedName("Calories")
  @Expose
  private List<Integer> calories;

  public DetailsWeekStatisticsDTO() {
  }

  protected DetailsWeekStatisticsDTO(Parcel in) {
    periodName = in.readString();
    longest = in.readString();
    shortest = in.readString();
    middleEffective = in.readString();
    countEffectiveDay = in.readString();
    distance = new ArrayList<>();
    in.readList(distance, List.class.getClassLoader());
    speed = new ArrayList<>();
    in.readList(speed, List.class.getClassLoader());
    timeInTrip = new ArrayList<>();
    in.readList(timeInTrip, List.class.getClassLoader());
    calories = new ArrayList<>();
    in.readList(calories, List.class.getClassLoader());

  }

  public static final Creator<DetailsWeekStatisticsDTO> CREATOR = new Creator<DetailsWeekStatisticsDTO>() {
    @Override
    public DetailsWeekStatisticsDTO createFromParcel(Parcel in) {
      return new DetailsWeekStatisticsDTO(in);
    }

    @Override
    public DetailsWeekStatisticsDTO[] newArray(int size) {
      return new DetailsWeekStatisticsDTO[size];
    }
  };

  public String getPeriodName() {
    return periodName;
  }

  public void setPeriodName(String periodName) {
    this.periodName = periodName;
  }

  public String getLongest() {
    return longest;
  }

  public void setLongest(String longest) {
    this.longest = longest;
  }

  public String getShortest() {
    return shortest;
  }

  public void setShortest(String shortest) {
    this.shortest = shortest;
  }

  public String getMiddleEffective() {
    return middleEffective;
  }

  public void setMiddleEffective(String middleEffective) {
    this.middleEffective = middleEffective;
  }

  public String getCountEffectiveDay() {
    return countEffectiveDay;
  }

  public void setCountEffectiveDay(String countEffectiveDay) {
    this.countEffectiveDay = countEffectiveDay;
  }

  public List<Integer> getDistance() {
    return distance;
  }

  public void setDistance(List<Integer> distance) {
    this.distance = distance;
  }

  public List<Integer> getSpeed() {
    return speed;
  }

  public void setSpeed(List<Integer> speed) {
    this.speed = speed;
  }

  public List<Long> getTimeInTrip() {
    return timeInTrip;
  }

  public void setTimeInTrip(List<Long> timeInTrip) {
    this.timeInTrip = timeInTrip;
  }

  public List<Integer> getCalories() {
    return calories;
  }

  public void setCalories(List<Integer> calories) {
    this.calories = calories;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(periodName);
    dest.writeString(longest);
    dest.writeString(shortest);
    dest.writeString(middleEffective);
    dest.writeString(countEffectiveDay);
    dest.writeList(distance);
    dest.writeList(speed);
    dest.writeList(timeInTrip);
    dest.writeList(calories);
  }
}
