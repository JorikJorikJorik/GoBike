package com.jorik.gobike.DataBase;

import android.content.Context;
import com.jorik.gobike.Model.Enum.StateApplication;
import com.jorik.gobike.Network.DTO.PreviewProfileDTO;
import com.jorik.gobike.R;
import com.jorik.gobike.Utils.ResourceUtils;
import io.realm.Realm;
import io.realm.RealmResults;

public class DataBaseUtils {

  private static final int USER_ID = 1;
  private Realm mRealm;
  private Context mContext;

  public DataBaseUtils(Context context) {
    mContext = context;
    mRealm = Realm.getDefaultInstance();
    createDefaultUser();
  }

  public UserDBModel getUser() {
    UserDBModel model = new UserDBModel();
    RealmResults<UserDBModel> realmResultsModel = allUser();
    return !realmResultsModel.isEmpty() ? realmResultsModel.get(realmResultsModel.size() - 1)
        : model;
  }

  public void createDefaultUser() {
    if (allUser().isEmpty()) {
      transaction(this::defaultUserData);
    }
  }

  public void createUser(UserDBModel model) {
    transaction(() -> createUserData(model));
  }

  public void deleteUser() {
    transaction(() -> mRealm.delete(UserDBModel.class));
  }

  public void resetUser() {
    transaction(() -> {
      mRealm.delete(UserDBModel.class);
      defaultUserData();
    });
  }

  public void close() {
    if (mRealm != null) {
      mRealm.close();
    }
  }

  public Realm getRealm() {
    return mRealm;
  }

  public void transaction(Runnable event) {
    if (event != null) {
      mRealm.beginTransaction();
      event.run();
      mRealm.commitTransaction();
    }
  }

  public String getBasic() {
    String basic = getUser().getBasicAuthorization();
    return basic != null ? basic : ResourceUtils.with(mContext).string(R.string.empty_string);
  }

  public StateApplication getStateApplication() {
    return StateApplication.fromValue(getUser().getStateApplication());
  }

  private RealmResults<UserDBModel> allUser() {
    return mRealm.where(UserDBModel.class).findAll();
  }

  private void defaultUserData() {
    UserDBModel model = mRealm.createObject(UserDBModel.class, Integer.toString(USER_ID));
    model.setStateApplication(StateApplication.LOGIN.getValue());
  }

  public UserDBModel convertPreviewProfileToUserDB(PreviewProfileDTO previewProfileDTO,
      String basic) {
    UserDBModel model = new UserDBModel();
    model.setFullName(previewProfileDTO.getFullName());
    model.setPhotoUrl(previewProfileDTO.getPhotoUrl());
    model.setBasicAuthorization(basic);
    model.setStateApplication(StateApplication.ENTER.getValue());
    return model;
  }

  private void createUserData(UserDBModel modelInput) {
    UserDBModel model = getUser();
    model.setFullName(modelInput.getFullName());
    model.setPhotoUrl(modelInput.getPhotoUrl());
    model.setBasicAuthorization(modelInput.getBasicAuthorization());
    model.setStateApplication(modelInput.getStateApplication());
  }

}
