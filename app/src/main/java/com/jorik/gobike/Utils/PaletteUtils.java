package com.jorik.gobike.Utils;

import static android.graphics.drawable.GradientDrawable.RADIAL_GRADIENT;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.Swatch;
import com.jorik.gobike.R;
import com.jorik.gobike.Utils.Callback.PaletteGradientListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class PaletteUtils {

  private static final int GRADIENT_RADIUS = 500;
  private static final float CENTER_GRADIENT_X = 0.5f;
  private static final float CENTER_GRADIENT_Y = -0.5f;
  private static final int MIN_COUNT_COLORS_FOR_GRADIENT = 500;
  private static final int ONE_SIZE_LIST = 1;
  private static final int EMPTY_LIST = 0;
  private static final int MAX_NEED_COUNT_COLOR = 3;

  private Context mContext;
  private Subscription mSubscriptionParse;
  private Integer[] parseColor;

  public PaletteUtils(Context context) {
    mContext = context;
  }

  public void buildGradient(Bitmap accountImageBitmap, PaletteGradientListener listener) {

    if (accountImageBitmap != null && !accountImageBitmap.isRecycled()) {
      Palette
          .from(accountImageBitmap)
          .generate(palette -> {
            List<Integer> colorList = new ArrayList<>();
            int defaultValue = 0x000000;

            checkExistColorFromImage(colorList, palette.getVibrantSwatch(),
                palette.getVibrantColor(defaultValue));
            checkExistColorFromImage(colorList, palette.getLightVibrantSwatch(),
                palette.getLightVibrantColor(defaultValue));
            checkExistColorFromImage(colorList, palette.getDarkVibrantSwatch(),
                palette.getDarkVibrantColor(defaultValue));
            checkExistColorFromImage(colorList, palette.getLightMutedSwatch(),
                palette.getLightMutedColor(defaultValue));
            checkExistColorFromImage(colorList, palette.getMutedSwatch(),
                palette.getMutedColor(defaultValue));
            checkExistColorFromImage(colorList, palette.getDarkMutedSwatch(),
                palette.getDarkMutedColor(defaultValue));
            checkExistColorFromImage(colorList, palette.getDominantSwatch(),
                palette.getDominantColor(defaultValue));

            parseColorList(colorList);
            if (listener != null) {
              listener.resultGradient(createGradientBackground());
            }
          });
    }
  }

  private void checkExistColorFromImage(List<Integer> colorList, Swatch swatch, int defaultColor) {
    colorList.add(swatch != null ? swatch.getRgb() : defaultColor);
  }

  private GradientDrawable createGradientBackground() {

    GradientDrawable gradientDrawable = new GradientDrawable(Orientation.TOP_BOTTOM,
        ConverterUtils.convertObjectToBaseInt(parseColor));
    gradientDrawable.setGradientType(RADIAL_GRADIENT);
    gradientDrawable.setGradientRadius(GRADIENT_RADIUS);
    gradientDrawable.setGradientCenter(CENTER_GRADIENT_X, CENTER_GRADIENT_Y);
    return gradientDrawable;
  }

  private void parseColorList(List<Integer> colorList) {
    mSubscriptionParse = Observable
        .from(colorList)
        .filter(item -> item != 0)
        .distinct()
        .toSortedList()
        .doOnNext(Collections::reverse)
        .flatMap(this::parseBySize)
        .subscribe(new Subscriber<List<Integer>>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(List<Integer> integers) {
            parseColor = new Integer[integers.size()];
            integers.toArray(parseColor);
          }
        });

  }

  private Observable<List<Integer>> parseBySize(List<Integer> parseColor) {

    Observable<Integer> parseColorObservable = Observable.from(parseColor);

    if (parseColor.size() > MAX_NEED_COUNT_COLOR) {
      return parseColorObservable.take(MAX_NEED_COUNT_COLOR).toList();
    } else if (parseColor.size() == ONE_SIZE_LIST) {
      return parseColorObservable.repeat(MIN_COUNT_COLORS_FOR_GRADIENT - 1).toList();
    } else if (parseColor.size() == EMPTY_LIST) {
      parseColorObservable
          .startWith(ContextCompat.getColor(mContext, R.color.palette_utils_default_color));
      return parseColorObservable.repeat(MIN_COUNT_COLORS_FOR_GRADIENT - 1).toList();
    }

    return parseColorObservable.toList();
  }

  public void unsubscribe() {
    if (mSubscriptionParse != null) {
      mSubscriptionParse.unsubscribe();
    }
  }

}
