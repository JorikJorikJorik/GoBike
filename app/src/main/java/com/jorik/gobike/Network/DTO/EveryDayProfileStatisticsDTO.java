package com.jorik.gobike.Network.DTO;

import static com.jorik.gobike.Utils.ConverterUtils.COUNT_TICK;
import static com.jorik.gobike.Utils.DateUtils.PARSE_DATE_GSON;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jorik.gobike.Utils.DateUtils;
import java.util.Date;

public class EveryDayProfileStatisticsDTO implements Parcelable {

  @SerializedName("EveryDayProfileStatisticsId")
  @Expose
  private Integer everyDayProfileStatisticsId;

  @SerializedName("CountDistance")
  @Expose
  private Double countDistance;

  @SerializedName("MiddleSpeed")
  @Expose
  private Double middleSpeed;

  @SerializedName("TimeInTrip")
  @Expose
  private Long timeInTrip;

  @SerializedName("Calories")
  @Expose
  private  Integer calories;

  @SerializedName("TimeCreate")
  @Expose
  private Date timeCreate;

  @SerializedName("NameDate")
  @Expose
  private String nameDate;

  public EveryDayProfileStatisticsDTO() {
  }

  protected EveryDayProfileStatisticsDTO(Parcel in) {
    everyDayProfileStatisticsId = in.readInt();
    countDistance = in.readDouble();
    middleSpeed = in.readDouble();
    timeInTrip = in.readLong();
    calories = in.readInt();
    timeCreate = DateUtils.parseDate(in.readString(), PARSE_DATE_GSON);
    nameDate = in.readString();
  }

  public static final Creator<EveryDayProfileStatisticsDTO> CREATOR = new Creator<EveryDayProfileStatisticsDTO>() {
    @Override
    public EveryDayProfileStatisticsDTO createFromParcel(Parcel in) {
      return new EveryDayProfileStatisticsDTO(in);
    }

    @Override
    public EveryDayProfileStatisticsDTO[] newArray(int size) {
      return new EveryDayProfileStatisticsDTO[size];
    }
  };

  public Integer getEveryDayProfileStatisticsId() {
    return everyDayProfileStatisticsId;
  }

  public void setEveryDayProfileStatisticsId(Integer everyDayProfileStatisticsId) {
    this.everyDayProfileStatisticsId = everyDayProfileStatisticsId;
  }

  public Double getCountDistance() {
    return countDistance;
  }

  public void setCountDistance(Double countDistance) {
    this.countDistance = countDistance;
  }

  public Double getMiddleSpeed() {
    return middleSpeed;
  }

  public void setMiddleSpeed(Double middleSpeed) {
    this.middleSpeed = middleSpeed;
  }

  public Long getTimeInTrip() {
    return timeInTrip;
  }

  public void setTimeInTrip(Long timeInTrip) {
    this.timeInTrip = timeInTrip * COUNT_TICK;
  }

  public Integer getCalories() {
    return calories;
  }

  public void setCalories(Integer calories) {
    this.calories = calories;
  }

  public Date getTimeCreate() {
    return timeCreate;
  }

  public void setTimeCreate(Date timeCreate) {
    this.timeCreate = timeCreate;
  }

  public String getNameDate() {
    return nameDate;
  }

  public void setNameDate(String nameDate) {
    this.nameDate = nameDate;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(everyDayProfileStatisticsId);
    dest.writeDouble(countDistance);
    dest.writeDouble(middleSpeed);
    dest.writeLong(timeInTrip);
    dest.writeInt(calories);
    dest.writeString(timeCreate.toString());
    dest.writeString(nameDate);
  }
}
