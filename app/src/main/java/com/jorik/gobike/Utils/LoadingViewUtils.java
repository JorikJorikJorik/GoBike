package com.jorik.gobike.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;
import com.jorik.gobike.R;

public class LoadingViewUtils {

  private Context mContext;
  private ProgressDialog mProgressDialog;

  public LoadingViewUtils(Context context) {
    mContext = context;
  }

  public void loading() {
    if (mProgressDialog == null) {
      mProgressDialog = new ProgressDialog(mContext);
      mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      mProgressDialog
          .setMessage(ResourceUtils.with(mContext).string(R.string.loading_utils_message));
      mProgressDialog.setIndeterminate(true);
      mProgressDialog.setCanceledOnTouchOutside(false);
    }
    mProgressDialog.show();
  }

  public void cancel() {
    if (mProgressDialog != null) {
      mProgressDialog.cancel();
    }
  }

  public ProgressBar createLoadingProgress() {
    ProgressBar mProgressLoad = new ProgressBar(mContext, null, android.R.attr.progressBarStyleSmall);
    mProgressLoad.setIndeterminate(true);
    mProgressLoad.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.load_view_utils_progress_color), Mode.MULTIPLY);
    return mProgressLoad;
  }

}
