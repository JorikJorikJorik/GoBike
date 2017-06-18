package com.jorik.gobike.Network.Service;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface FirebaseService {

  @POST("Firebase/AddToken")
  Observable<Void> addToken(@Header("Authorization") String authorization, @Query("token") String token);

  @POST("Firebase/RefreshToken")
  Observable<Void> refreshToken(@Header("Authorization") String authorization, @Query("newToken") String newToken);

}
