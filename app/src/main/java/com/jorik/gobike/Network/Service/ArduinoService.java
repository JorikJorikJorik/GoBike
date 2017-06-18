package com.jorik.gobike.Network.Service;

import android.app.ListActivity;
import com.jorik.gobike.Network.DTO.ArduinoDTO;
import com.jorik.gobike.Network.DTO.ArduinoListDTO;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ArduinoService {

  @GET("Arduino")
  Observable<ArduinoListDTO> getArduinoList(@Header("Authorization") String authorization);

  @POST("Arduino")
  Observable<Void> createArduino(@Header("Authorization") String authorization, @Body ArduinoDTO arduinoDTO);

  @DELETE("Arduino")
  Observable<Void> deleteArduino(@Header("Authorization") String authorization, @Query("arduinoId") int arduinoId);

}
