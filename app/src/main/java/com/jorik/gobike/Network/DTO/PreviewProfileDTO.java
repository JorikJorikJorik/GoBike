package com.jorik.gobike.Network.DTO;

import static com.jorik.gobike.Utils.DateUtils.PARSE_DATE_GSON;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract.Profile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jorik.gobike.Utils.DateUtils;
import java.util.Date;

public class PreviewProfileDTO implements Parcelable {

  public static final Creator<PreviewProfileDTO> CREATOR = new Creator<PreviewProfileDTO>() {
    @Override
    public PreviewProfileDTO createFromParcel(Parcel in) {
      return new PreviewProfileDTO(in);
    }

    @Override
    public PreviewProfileDTO[] newArray(int size) {
      return new PreviewProfileDTO[size];
    }
  };
  @SerializedName("FullName")
  @Expose
  private String fullName;
  @SerializedName("PhotoUrl")
  @Expose
  private String photoUrl;
  @SerializedName("TimeLastActive")
  @Expose
  private Date timeLastActive;
  @SerializedName("CountRequestedFriends")
  @Expose
  private Integer countRequestedFriends;
  @SerializedName("CountUnreadMessages")
  @Expose
  private Integer countUnreadMessages;
  @SerializedName("CountRequestedGroups")
  @Expose
  private Integer countRequestedGroups;
  @SerializedName("ProfileStatistics")
  @Expose
  private ProfileStatisticsDTO profileStatistics;

  protected PreviewProfileDTO(Parcel in) {
    fullName = in.readString();
    photoUrl = in.readString();
    timeLastActive = DateUtils.parseDate(in.readString(), PARSE_DATE_GSON);
    countRequestedFriends = in.readInt();
    countUnreadMessages = in.readInt();
    countRequestedGroups = in.readInt();
    profileStatistics = in.readParcelable(ProfileStatisticsDTO.class.getClassLoader());
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public Date getTimeLastActive() {
    return timeLastActive;
  }

  public void setTimeLastActive(Date timeLastActive) {
    this.timeLastActive = timeLastActive;
  }

  public Integer getCountRequestedFriends() {
    return countRequestedFriends;
  }

  public void setCountRequestedFriends(Integer countRequestedFriends) {
    this.countRequestedFriends = countRequestedFriends;
  }

  public Integer getCountUnreadMessages() {
    return countUnreadMessages;
  }

  public void setCountUnreadMessages(Integer countUnreadMessages) {
    this.countUnreadMessages = countUnreadMessages;
  }

  public Integer getCountRequestedGroups() {
    return countRequestedGroups;
  }

  public void setCountRequestedGroups(Integer countRequestedGroups) {
    this.countRequestedGroups = countRequestedGroups;
  }

  public ProfileStatisticsDTO getProfileStatistics() {
    return profileStatistics;
  }

  public void setProfileStatistics(ProfileStatisticsDTO profileStatistics) {
    this.profileStatistics = profileStatistics;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(fullName);
    dest.writeString(photoUrl);
    dest.writeString(timeLastActive.toString());
    dest.writeInt(countRequestedFriends);
    dest.writeInt(countUnreadMessages);
    dest.writeInt(countRequestedGroups);
    dest.writeParcelable(profileStatistics, flags);
  }
}

