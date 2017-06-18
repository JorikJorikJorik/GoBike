package com.jorik.gobike.View.View;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jorik.gobike.Model.POJO.ProfileModel;
import com.jorik.gobike.PicassoTransformation.CircleTransformer;
import com.jorik.gobike.R;
import com.jorik.gobike.Utils.FileUtils;
import com.jorik.gobike.Utils.PaletteUtils;
import com.jorik.gobike.Utils.ResourceUtils;
import com.jorik.gobike.View.Logic.Callback.SignOutCallback;
import com.squareup.picasso.Picasso;
import java.io.File;

public class HeaderView extends BaseView {

  private Context mContext;
  private FileUtils mFileUtils;
  private PaletteUtils mPaletteUtils;
  private SignOutCallback mSignOutCallback;

  private TextView mTextViewFullName;
  private ImageView mImageViewPhotoProfile;
  private ConstraintLayout mConstraintLayoutHeader;

  public HeaderView(Context context, SignOutCallback mCallback) {
    mContext = context;
    mSignOutCallback = mCallback;
    mFileUtils = new FileUtils(mContext);
    mPaletteUtils = new PaletteUtils(mContext);
  }

  @Override
  public View createView(Context context) {
    View view = super.createView(context);

    mTextViewFullName = (TextView) view.findViewById(R.id.textViewFullName);
    mImageViewPhotoProfile = (ImageView) view.findViewById(R.id.imageViewProfile);
    mConstraintLayoutHeader = (ConstraintLayout) view.findViewById(R.id.constrainLayoutHeader);

    ((TextView) view.findViewById(R.id.textViewLastActiveTime))
        .setText(ResourceUtils.with(context).string(R.string.header_online));
    view.findViewById(R.id.imageViewExit).setOnClickListener(v -> {
      if (mSignOutCallback != null) {
        mSignOutCallback.signOut();
      }
    });

    return view;
  }

  public void updateData(ProfileModel profileModel) {

    mTextViewFullName.setText(profileModel.getFullName());
    Picasso.with(mContext).invalidate(new File(profileModel.getPhotoUrl()));
    Picasso.with(mContext)
        .load(new File(profileModel.getPhotoUrl()))
        .error(R.mipmap.ic_dafault_icon)
        .placeholder(R.mipmap.ic_dafault_icon)
        .transform(new CircleTransformer(true))
        .into(mImageViewPhotoProfile);

    mPaletteUtils.buildGradient(mFileUtils.readBitmapFromPhotoFile(),
        result -> mConstraintLayoutHeader.setBackground(result));
  }

  public void onDestroy() {
    mPaletteUtils.unsubscribe();
  }

  @Override
  public int layoutId() {
    return R.layout.header;
  }
}
