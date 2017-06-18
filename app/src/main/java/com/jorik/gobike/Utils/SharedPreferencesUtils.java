package com.jorik.gobike.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.jorik.gobike.R;

public class SharedPreferencesUtils {

  public static final String PERIOD_TYPE_KEY = "period_type_key";
  public static final String PERIOD_DIRECTION = "period_direction";

  public static final String EVERY_DAY_STATISTICS_ID_KEY = "every_day_statistics_id_key";
  public static final String EVERY_DAY_STATISTICS_DATE_CREATE_KEY = "every_day_statistics_date_create_key";
  public static final String EVERY_DAY_STATISTICS_DIRECTION = "every_day_statistics_direction";


  private static final int EMPTY_INT_VALUE = 0;

  private Context mContext;

  public SharedPreferencesUtils(Context context) {
    mContext = context;
  }

  public void writeString(String direction, String key, String value) {
    Editor editor = initSharedPreferences(direction).edit();
    editor.putString(key, value);
    editor.apply();
  }

  public String readString(String direction, String key) {
    return initSharedPreferences(direction)
        .getString(key, ResourceUtils.with(mContext).string(R.string.empty_string));
  }

  public void writeInt(String direction, String key, int value) {
    Editor editor = initSharedPreferences(direction).edit();
    editor.putInt(key, value);
    editor.apply();
  }

  public int readInt(String direction, String key) {
    return initSharedPreferences(direction).getInt(key, EMPTY_INT_VALUE);
  }

  private SharedPreferences initSharedPreferences(String direction) {
    return mContext.getSharedPreferences(direction, Context.MODE_PRIVATE);
  }


}
