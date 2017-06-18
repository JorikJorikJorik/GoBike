package com.jorik.gobike.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;

public class ResourceUtils {

  private static final int DEFAULT_VALUE = 0;

  private static ResourceUtils sResourceUtils;
  private Context mContext;

  public ResourceUtils(Context context) {
    mContext = context;
  }

  public static ResourceUtils with(Context context) {
    if (sResourceUtils == null) {
      sResourceUtils = new ResourceUtils(context);
    }
    return sResourceUtils;
  }

  public String string(int resourceId) {
    return mContext.getResources().getString(resourceId);
  }

  public float dimens(int resourceId) {
    return mContext.getResources().getDimension(resourceId);
  }

  public int color(int resourceId) {
    return ContextCompat.getColor(mContext, resourceId);
  }

  public String[] stringArray(int resourceId) {
    return mContext.getResources().getStringArray(resourceId);
  }

  public Integer[] integerArray(int resourceId) {
    TypedArray typedArray = mContext.getResources().obtainTypedArray(resourceId);

    Integer[] resourceArray = new Integer[typedArray.length()];
    for (int i = 0; i < resourceArray.length; i++) {
      resourceArray[i] = typedArray.getResourceId(i, DEFAULT_VALUE);
    }
    typedArray.recycle();
    return resourceArray;
  }


}
