package com.jorik.gobike.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

public class ImageUtils {

  private static final int X_ZERO = 0;
  private static final int Y_ZERO = 0;
  private static final int LEFT_ZERO = 0;
  private static final int TOP_ZERO = 0;

  private Context mContext;

  public ImageUtils(Context context) {
    mContext = context;
  }

  public Bitmap changeColorByResource(int resource, int color){
    Bitmap bitmapFromDrawable = getBitmapFromDrawable(resource);
    return changeColorImageBitmap(bitmapFromDrawable, color);
  }

  public Bitmap getBitmapFromDrawable(int resource) {
    return BitmapFactory.decodeResource(mContext.getResources(), resource);
  }

  public Bitmap changeColorImageBitmap(Bitmap imageBitmap, int color) {
    Bitmap resultBitmap = Bitmap
        .createBitmap(imageBitmap, X_ZERO, Y_ZERO, imageBitmap.getWidth() - 1,
            imageBitmap.getHeight() - 1);
    Paint paint = new Paint();
    ColorFilter filter = new LightingColorFilter(color, 1);
    paint.setColorFilter(filter);

    Canvas canvas = new Canvas(resultBitmap);
    canvas.drawBitmap(resultBitmap, LEFT_ZERO, TOP_ZERO, paint);

    return resultBitmap;
  }

}
