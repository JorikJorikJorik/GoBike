package com.jorik.gobike.View.Logic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.jorik.gobike.DataBase.DataBaseUtils;
import com.jorik.gobike.DataBase.UserDBModel;
import com.jorik.gobike.Model.Enum.StateApplication;
import com.jorik.gobike.Model.POJO.ProfileModel;
import com.jorik.gobike.Network.DTO.PreviewProfileDTO;
import com.jorik.gobike.Network.RestClient;
import com.jorik.gobike.Network.Service.ProfileService;
import com.jorik.gobike.R;
import com.jorik.gobike.Utils.FileUtils;
import com.jorik.gobike.Utils.ResourceUtils;
import com.jorik.gobike.View.Activity.LogInActivity;
import com.jorik.gobike.View.Activity.MainActivity;
import com.jorik.gobike.View.Fragment.ArduinoFragment;
import com.jorik.gobike.View.Fragment.StatisticsFragment;
import com.jorik.gobike.View.Logic.Callback.SignOutCallback;
import com.jorik.gobike.View.Logic.Callback.UpdateHeaderCallback;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivityLogic implements SignOutCallback {

  private Context mContext;
  private FileUtils mFileUtils;
  private DataBaseUtils mDataBaseUtils;
  private ResourceUtils mResourceUtils;
  private CompositeSubscription mSubscription;
  private PreviewProfileDTO mPreviewProfileDTO;
  private UpdateHeaderCallback mUpdateHeaderCallback;

  private boolean checkPhoto;

  public MainActivityLogic(Context context) {
    mContext = context;
    mFileUtils = new FileUtils(mContext);
    mDataBaseUtils = new DataBaseUtils(mContext);
    mResourceUtils = ResourceUtils.with(mContext);
    mSubscription = new CompositeSubscription();
  }

  public void registerCallback(UpdateHeaderCallback callback) {
    mUpdateHeaderCallback = callback;
  }

  public void onResume() {
    if (mDataBaseUtils.getStateApplication().equals(StateApplication.ENTER)) {
      actionByStateEnter();
    }
  }

  public void checkStateApplication() {
    if (mDataBaseUtils.getStateApplication().equals(StateApplication.LOGIN)) {
      logOut();
    } else {
      chooseFragmentAndTitleById(R.id.navigation_item_statistics);
    }
  }

  private void actionByStateEnter() {
    Observable<PreviewProfileDTO> photoUrl =
        mPreviewProfileDTO != null ? inputData() : getDataProfile();

    Subscription mSubscriptionProfile = photoUrl
        .map(PreviewProfileDTO::getPhotoUrl)
        .observeOn(Schedulers.io())
        .map(this::savePhoto)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(this::savePhotoPathToDataBase)
        .doOnCompleted(this::uploadFromDataBase)
        .subscribe(new Subscriber<String>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(String photoUrl) {

          }
        });

    mSubscription.add(mSubscriptionProfile);
  }

  private Observable<PreviewProfileDTO> inputData() {
    return Observable.just(mPreviewProfileDTO);
  }

  private Observable<PreviewProfileDTO> getDataProfile() {
    ProfileService service = RestClient.createClientService(ProfileService.class);
    return service
        .profileData(mDataBaseUtils.getBasic())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(error -> uploadFromDataBase())
        .doOnNext(this::checkSameDataAndWriteToDataBase);
  }

  private void checkSameDataAndWriteToDataBase(PreviewProfileDTO previewProfile) {
    UserDBModel userDBModel = mDataBaseUtils.getUser();

    String fullName = previewProfile.getFullName();
    boolean isSame = previewProfile.getPhotoUrl().equals(userDBModel.getPhotoUrl());
    checkPhoto = isSame ? new File(userDBModel.getPhotoPath()).exists() && isSame : isSame;

    if (!fullName.equals(userDBModel.getFullName())) {
      mDataBaseUtils.transaction(() -> userDBModel.setFullName(fullName));
    }
    if (!isSame) {
      mDataBaseUtils.transaction(() -> userDBModel.setPhotoUrl(previewProfile.getPhotoUrl()));
    }
  }

  private String savePhoto(String photoUrl) {
    String pathPath = mResourceUtils.string(R.string.empty_string);
    if (!checkPhoto) {
      pathPath = mResourceUtils.string(R.string.file_utils_no_photo_file);
      try {
        Bitmap bitmapPhotoProfile = Picasso.with(mContext).load(photoUrl).get();
        pathPath = mFileUtils.savePhotoFile(bitmapPhotoProfile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return pathPath;
  }

  private void savePhotoPathToDataBase(String photoPath) {
    if (!photoPath.isEmpty()) {
      mDataBaseUtils.transaction(() -> mDataBaseUtils.getUser().setPhotoPath(photoPath));
    }
  }

  private void uploadFromDataBase() {
    mUpdateHeaderCallback.updateHeader(convertToProfileModel(mDataBaseUtils.getUser()));
  }

  private ProfileModel convertToProfileModel(UserDBModel dbModel) {
    ProfileModel model = new ProfileModel();
    model.setFullName(dbModel.getFullName());
    model.setPhotoUrl(dbModel.getPhotoPath());
    return model;
  }

  public void chooseFragmentAndTitleById(int menuId) {
    switch (menuId) {
      case R.id.navigation_item_statistics:
        chooseFragmentAndTitle(StatisticsFragment.newInstance(mPreviewProfileDTO),
            R.string.navigation_item_statistics);
        break;
//      case R.id.navigation_item_friends:
//        chooseFragmentAndTitle(ArduinoFragment.newInstance(), R.string.navigation_item_friends);
//        break;
//      case R.id.navigation_item_communications:
//        chooseFragmentAndTitle(StatisticsFragment.newInstance(),
//            R.string.navigation_item_communications);
//        break;
//      case R.id.navigation_item_search:
//        chooseFragmentAndTitle(ArduinoFragment.newInstance(), R.string.navigation_item_search);
//        break;
      case R.id.navigation_item_arduino:
        chooseFragmentAndTitle(ArduinoFragment.newInstance(), R.string.navigation_item_arduino);
        break;
    }
  }

  private void chooseFragmentAndTitle(Fragment fragment, int resource) {
    FragmentManager manager = ((MainActivity) mContext).getSupportFragmentManager();
    manager
        .beginTransaction()
        .replace(R.id.frameLayoutMain, fragment)
        .commit();

    ((MainActivity) mContext).getSupportActionBar()
        .setTitle(mResourceUtils.string(resource));
  }

  private void logOut() {
    mDataBaseUtils.deleteUser();
    mFileUtils.deleteCacheFile();
    Intent moveToLogin = new Intent(mContext, LogInActivity.class);
    mContext.startActivity(moveToLogin);
    ((MainActivity) mContext).finish();
  }

  public void setPreviewProfileDTO(PreviewProfileDTO previewProfileDTO) {
    mPreviewProfileDTO = previewProfileDTO;
  }

  public void onDestroy() {
    if (mSubscription != null) {
      mSubscription.unsubscribe();
    }
    if (mDataBaseUtils != null) {
      mDataBaseUtils.close();
    }
  }

  @Override
  public void signOut() {
    logOut();
  }

}
