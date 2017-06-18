package com.jorik.gobike.Network.Service;

import com.jorik.gobike.Network.DTO.DetailsWeekStatisticsDTO;
import com.jorik.gobike.Network.DTO.EveryDayProfileStatisticsDTO;
import com.jorik.gobike.Network.DTO.PreviewProfileDTO;
import com.jorik.gobike.Network.DTO.ProfileStatisticsDTO;
import java.util.Date;
import java.util.List;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface ProfileService {

  @GET("Profile/ProfileData")
  Observable<PreviewProfileDTO> profileData(@Header("Authorization") String authorization);

  @POST("Profile/EveryDayStatistics")
  Observable<Integer> createEveryDayStatistics(@Header("Authorization") String authorization, @Body EveryDayProfileStatisticsDTO everyDayProfileStatisticsDTO);

  @PUT("Profile/EveryDayStatistics")
  Observable<Integer> updateEveryDayStatistics(@Header("Authorization") String authorization, @Body EveryDayProfileStatisticsDTO everyDayProfileStatisticsDTO);

  @GET("Profile/Statistics")
  Observable<ProfileStatisticsDTO> getProfileStatistics(@Header("Authorization") String authorization, @Query("typePeriod") int typePeriod);

  @GET("Profile/DetailsStatistics")
  Observable<List<DetailsWeekStatisticsDTO>> getDetailWeekStatistics(@Header("Authorization") String authorization, @Query("typePeriod") int typePeriod, @Query("dateStart") String dateStart);

}
