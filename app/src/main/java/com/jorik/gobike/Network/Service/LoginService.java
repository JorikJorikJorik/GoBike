package com.jorik.gobike.Network.Service;

import com.jorik.gobike.Network.DTO.PreviewProfileDTO;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

public interface LoginService {

  @GET("Login")
  Observable<PreviewProfileDTO> login(@Header("Authorization") String authorization);
}
