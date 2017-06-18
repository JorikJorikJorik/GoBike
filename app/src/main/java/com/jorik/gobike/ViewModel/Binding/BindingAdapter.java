package com.jorik.gobike.ViewModel.Binding;

import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.jorik.gobike.R;
import com.jorik.gobike.Utils.ImageUtils;
import com.jorik.gobike.Utils.ResourceUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BindingAdapter {

  private static final int EMPTY_DATA_RESOURCE = 0;
  private static boolean pressed;

  @android.databinding.BindingAdapter(value = {"adapter", "divider",
      "drawable_divider"}, requireAll = false)
  public static <T extends Adapter> void recyclerViewAdapter(RecyclerView view, T adapter,
      boolean divider, Drawable drawableDivider) {
    view.setLayoutManager(new LinearLayoutManager(view.getContext()));
    if (divider) {
      DividerItemDecoration itemDecoration = new DividerItemDecoration(view.getContext(),
          DividerItemDecoration.VERTICAL);
//      if (drawableDivider != null) {
//        itemDecoration.setDrawable(drawableDivider);
//      }
      view.addItemDecoration(itemDecoration);
    }
    view.setAdapter(adapter);
  }

  @android.databinding.BindingAdapter("error")
  public static void textInputLayoutError(TextInputLayout textInputLayout, String error) {
    textInputLayout.setErrorEnabled(error != null && !error.isEmpty());
    textInputLayout.setError(error);
  }

  @android.databinding.BindingAdapter("resource")
  public static void setResourceImage(ImageView imageView, int resource) {
    imageView.setImageResource(resource);
  }

  @android.databinding.BindingAdapter(value = {"visibility_fab"})
  public static void setVisabilityGone(FloatingActionButton button, boolean state) {
    if (state) {
      button.show();
    } else {
      button.hide();
    }
  }

  @android.databinding.BindingAdapter(value = {"visibility_gone"})
  public static void setVisabilityGone(View view, boolean state) {
    view.setVisibility(state ? View.VISIBLE : View.GONE);
  }

  @android.databinding.BindingAdapter(value = {"state_visibility", "second_visible_value"})
  public static void setVisibleByState(View view, boolean state, int secondValue) {
    view.setVisibility(state ? View.VISIBLE : secondValue);
  }

  @android.databinding.BindingAdapter("enable")
  public static void setEnable(View view, boolean enable) {
    view.setEnabled(enable);
  }

  @android.databinding.BindingAdapter(value = {"pressed", "resource_pressed"}, requireAll = false)
  public static void setPressedView(View view, boolean isPressed, int resource) {

    int colorPressed = ResourceUtils.with(view.getContext())
        .color(isPressed ? R.color.error_pressed : R.color.error_unpressed);

    if (view instanceof TextView) {
      ((TextView) view).setTextColor(colorPressed);
    } else if (view instanceof ImageView) {
      if (resource > EMPTY_DATA_RESOURCE) {
        ImageView imageView = (ImageView) view;
        ImageUtils imageUtils = new ImageUtils(view.getContext());
        Bitmap imageBitmap = imageUtils.changeColorByResource(resource, colorPressed);
        imageView.setImageBitmap(imageBitmap);
      }
    }
  }

//  @android.databinding.BindingAdapter(value = {"resource", "color_change"})
//  public static void changeColorIcon(ImageView imageView, int resourceImage, int colorChange){
//    int colorPressed = ResourceUtils.with(imageView.getContext())
//        .color(R.color.error_unpressed);
//    ImageUtils imageUtils = new ImageUtils(imageView.getContext());
//    Bitmap imageBitmap = imageUtils.changeColorByResource(resourceImage, colorPressed);
//    imageView.setImageBitmap(imageBitmap);
//  }

  @android.databinding.BindingAdapter(value = {"pressed_listener", "action_pressed",
      "pressed_listener_on_change"}, requireAll = false)
  public static void setPressedListener(View view, boolean pressedEnable, Runnable actionPressed,
      InverseBindingListener listener) {
    view.setOnTouchListener((v, event) -> {
      int eventAction = event.getAction();
      pressed = eventAction == MotionEvent.ACTION_DOWN || !(eventAction == MotionEvent.ACTION_UP);
      listener.onChange();

      if (!pressed && actionPressed != null) {
        actionPressed.run();
      }
      return true;
    });
  }

  @android.databinding.BindingAdapter("style_progress")
  public static void styleProgress(ProgressBar progressBar, boolean isStyle) {
    if (isStyle) {
      progressBar.setIndeterminate(true);
      progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat
              .getColor(progressBar.getContext(), R.color.style_progress_indeterminate_drawable_color),
          Mode.MULTIPLY);
    }
  }

  @InverseBindingAdapter(attribute = "pressed_listener", event = "pressed_listener_on_change")
  public static boolean getPressedListenerResult(View view) {
    return pressed;
  }

  @android.databinding.BindingAdapter(value = {"action_swipe", "state"})
  public static void setDataForSwipeRefresh(SwipeRefreshLayout swipeRefresh, Runnable action,
      boolean stateRefreshing) {
    swipeRefresh.setColorSchemeColors(
        ResourceUtils.with(swipeRefresh.getContext())
            .color(R.color.swipe_refresh_layout_scheme_colors));
    swipeRefresh.setOnRefreshListener(() -> {
      if (swipeRefresh.isRefreshing() && action != null) {
        action.run();
      }
    });
    swipeRefresh.setRefreshing(stateRefreshing);
  }

  @android.databinding.BindingAdapter("onClick")
  public static void setOnClick(View view, Runnable event) {
    view.setOnClickListener(v -> {
      if (event != null) {
        event.run();
      }
    });
  }

  @android.databinding.BindingAdapter("listener")
  public static void setValidation(TextInputEditText editText, Runnable event) {
    editText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable editable) {
        if (editable != null && event != null) {
          event.run();
        }
      }
    });
  }

  @android.databinding.BindingAdapter(value = {"name_column", "input_data", "animated_time",
      "index"})
  public static void createGraph(HorizontalBarChart graph, List<String> nameColumn,
      List<Integer> inputData, int animatedMillis, String index) {

    Collections.reverse(inputData);
    ArrayList<BarEntry> entries = new ArrayList<>();
    for (int i = 0; i < inputData.size(); i++) {
      entries.add(new BarEntry(inputData.get(i), i));
    }

    BarDataSet dataset = new BarDataSet(entries, index);
    BarData data = new BarData(nameColumn, dataset);

    graph.setPinchZoom(false);
    graph.setTouchEnabled(false);
    graph.setDoubleTapToZoomEnabled(false);
    graph.animateY(animatedMillis);
    graph.setData(data);


  }
}
