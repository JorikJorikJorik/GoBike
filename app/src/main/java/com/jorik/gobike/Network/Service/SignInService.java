package com.jorik.gobike.Network.Service;

import com.jorik.gobike.Network.DTO.PreviewProfileDTO;
import com.jorik.gobike.Network.DTO.SignInDTO;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface SignInService {

  @POST("SignUp")
  Observable<PreviewProfileDTO> signIn(@Body SignInDTO signInDTO);
}
