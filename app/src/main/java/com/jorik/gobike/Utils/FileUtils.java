package com.jorik.gobike.Utils;

import static com.jorik.gobike.Model.Enum.CashDataType.RESPONSE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import com.jorik.gobike.Model.Enum.CashDataType;
import com.jorik.gobike.R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {

  private static final String RESPONSE_FILE = "cacheFirstType.txt";
  private static final String ARDUINO_FILE = "cacheSecondType.txt";
  private static final String PROFILE_PHOTO_NAME = "profile.png";
  private static final int QUALITY_PHOTO = 99;

  private Context mContext;
  private CashDataType mCashDataType;
  private ResourceUtils mResourceUtils;

  public FileUtils(Context context) {
    mContext = context;
  }

  public FileUtils(Context context, CashDataType cashDataType) {
    mContext = context;
    mCashDataType = cashDataType;
    mResourceUtils = ResourceUtils.with(mContext);
  }

  public String readFromFile() {
    String result = mResourceUtils.string(R.string.empty_string);
    if (!isEmptyPathCacheFile()) {
      try {
        StringBuilder stringBuilder = new StringBuilder();
        FileReader fileReader = new FileReader(accessToFilePath());
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;

        while ((line = bufferedReader.readLine()) != null) {
          stringBuilder.append(line);
        }

        fileReader.close();
        bufferedReader.close();

        result = stringBuilder.toString();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public void writeToFile(String data) {
    if (!isEmptyPathCacheFile()) {
      try {
        FileOutputStream outputStream = new FileOutputStream(accessToFilePath());
        outputStream.write(data.getBytes());
        outputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void deleteCacheFile(){
    deleteFile(RESPONSE_FILE);
    deleteFile(PROFILE_PHOTO_NAME);
  }

  public Bitmap readBitmapFromPhotoFile() {
    Bitmap photoBitmap = null;
    File cacheDir = mContext.getExternalCacheDir();
    if (cacheDir != null) {
      File photoFile = new File(cacheDir, PROFILE_PHOTO_NAME);
      if (photoFile.exists()) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.ARGB_8888;
        photoBitmap = BitmapFactory.decodeFile(photoFile.getPath(), options);
      }
    }
    return photoBitmap;
  }

  public String savePhotoFile(Bitmap photoBitmap) {
    File photoFile = null;
    try {
      File cacheDir = mContext.getExternalCacheDir();
      if (cacheDir != null) {
        photoFile = new File(cacheDir.getPath(), PROFILE_PHOTO_NAME);
        FileOutputStream fileOutputStream = new FileOutputStream(photoFile);
        photoBitmap.compress(CompressFormat.PNG, QUALITY_PHOTO, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return photoFile != null ? photoFile.getPath()
        : mResourceUtils.string(R.string.file_utils_no_photo_file);
  }



  public void deleteFile(String nameFile) {
    File cacheDir = mContext.getExternalCacheDir();
    if (cacheDir != null) {
      File photoFile = new File(cacheDir.getPath(), nameFile);
      if (photoFile.exists()) {
        photoFile.delete();
      }
    }
  }

  private boolean isEmptyPathCacheFile() {
    return accessToFilePath().isEmpty();
  }

  private String accessToFilePath() {
    File cashFile = null;
    File cacheDir = mContext.getExternalCacheDir();
    if (cacheDir != null) {
      cashFile = new File(cacheDir.getPath(),
          mCashDataType == RESPONSE ? RESPONSE_FILE : ARDUINO_FILE);
      if (!cashFile.exists()) {
        try {
          cashFile.createNewFile();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return cashFile != null ? cashFile.getPath() : mResourceUtils.string(R.string.empty_string);
  }
}
