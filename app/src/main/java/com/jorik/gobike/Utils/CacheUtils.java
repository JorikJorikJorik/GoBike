package com.jorik.gobike.Utils;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jorik.gobike.Model.Enum.CashDataType;
import com.jorik.gobike.Network.DTO.ArduinoDTO;
import com.jorik.gobike.Network.DTO.CashDTO;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CacheUtils<T> {

  private Context mContext;
  private CryptoUtils mCryptoUtils;
  private FileUtils mFileUtils;
  private CashDataType mCashDataType;
  private Map<String, String> currentCacheMap;
  private Gson gson;

  public CacheUtils(Context context, CashDataType type) {
    mContext = context;
    mCryptoUtils = new CryptoUtils(mContext);
    mFileUtils = new FileUtils(mContext, type);
    mCashDataType = type;
    currentCacheMap = new HashMap<>();
    gson = new Gson();
  }

  public T read(Class<T> classData) {
    String key = classData.toString();
    currentCacheMap = getCurrentCacheMap();
    String dataJson = currentCacheMap.get(key);
    return gson.fromJson(dataJson, classData);
  }

  public void write(T data) {
    String key = data.getClass().toString();
    String dataJson = gson.toJson(data);

    currentCacheMap = getCurrentCacheMap();
    currentCacheMap.put(key, dataJson);
    saveCurrentMap();
  }

  public Observable<T> asyncRead(Class<T> classData) {
    return Observable
        .just(read(classData))
        .subscribeOn(Schedulers.io());
  }

  public Observable<T> asyncWrite(T data) {
    return Observable
        .just(data)
        .observeOn(Schedulers.io())
        .doOnNext(this::write)
        .observeOn(AndroidSchedulers.mainThread());

  }

  private void saveCurrentMap() {
    String mapDataJson = gson.toJson(currentCacheMap);
    //mCryptoUtils.generateKey();
//     String encryptMapDataJson = mCryptoUtils.getData(mapDataJson, Cipher.ENCRYPT_MODE);
    mFileUtils.writeToFile(mapDataJson);
  }

  private Map<String, String> getCurrentCacheMap() {
    Map<String, String> currentMap = new HashMap<>();

    String encryptMapDataJson = mFileUtils.readFromFile();
    if (!encryptMapDataJson.isEmpty()) {
//      String mapDataJson = mCryptoUtils.getData(encryptMapDataJson, Cipher.DECRYPT_MODE);
      currentMap = gson.fromJson(encryptMapDataJson, new TypeToken<Map<String, String>>() {
      }.getType());
    }
    return currentMap;
  }

}
