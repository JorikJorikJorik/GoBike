package com.jorik.gobike.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jorik.gobike.Utils.DateUtils;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

  private static final String BASE_URL = "http://192.168.0.103/api/";
  private static final int TIMEOUT = 60;
  private static Gson sGson = new GsonBuilder().setDateFormat(DateUtils.PARSE_DATE_GSON).create();

  public static <T> T createClientService(Class<T> serviceInterface){

    OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .build();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(sGson))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .client(client)
        .build();

    return retrofit.create(serviceInterface);
  }

}
