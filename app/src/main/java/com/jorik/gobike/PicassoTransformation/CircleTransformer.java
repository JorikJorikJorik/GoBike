package com.jorik.gobike.PicassoTransformation;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.squareup.picasso.Transformation;

public class CircleTransformer implements Transformation {

  private static final String KEY = "circle";
  private boolean circle;

  public CircleTransformer(boolean circle) {
    this.circle = circle;
  }

  @Override
  public Bitmap transform(Bitmap source) {
    if (circle) {
      int size = Math.min(source.getWidth(), source.getHeight());

      int x = (source.getWidth() - size) / 2;
      int y = (source.getHeight() - size) / 2;

      Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
      if (squaredBitmap != source) {
        source.recycle();
      }

      Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

      Canvas canvas = new Canvas(bitmap);
      Paint paint = new Paint();
      BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
      paint.setShader(shader);
      paint.setAntiAlias(true);

      float r = size / 2f;
      canvas.drawCircle(r, r, r, paint);

      squaredBitmap.recycle();
      return bitmap;
    } else {
      return source;
    }
  }


  @Override
  public String key() {
    return KEY;
  }
}