package com.jorik.gobike.Utils;

import android.content.Context;
import android.content.Intent;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jorik.gobike.DataBase.DataBaseUtils;
import com.jorik.gobike.DataBase.UserDBModel;
import com.jorik.gobike.Network.DTO.PreviewProfileDTO;
import com.jorik.gobike.Network.RestClient;
import com.jorik.gobike.Network.Service.FirebaseService;
import com.jorik.gobike.View.Activity.MainActivity;
import rx.Observable;

public class AuthorizationUtils {

  public static final String PREVIEW_PROFILE_NAME_EXTRA = "preview_profile_name_extra";

  private Context mContext;
  private DataBaseUtils mDataBaseUtils;
  private PreviewProfileDTO profileDTO;

  public AuthorizationUtils(Context context, DataBaseUtils dataBaseUtils) {
    mContext = context;
    mDataBaseUtils = dataBaseUtils;
  }

  public Observable<String> prepareData(PreviewProfileDTO mPreviewProfileDTO, String login,
      String password) {

    CryptoUtils cryptoUtils = new CryptoUtils(mContext);

    profileDTO = mPreviewProfileDTO;
    UserDBModel dbModel = mDataBaseUtils
        .convertPreviewProfileToUserDB(profileDTO, cryptoUtils.generateBasic(login, password));
    mDataBaseUtils.createUser(dbModel);

    return Observable.just(mDataBaseUtils.getBasic());
  }

  public Observable<Void> addToken(String baseAuth) {
    FirebaseService service = RestClient.createClientService(FirebaseService.class);
    return service.addToken(baseAuth, FirebaseInstanceId.getInstance().getToken());
  }

  public void moveToEnter() {
    Intent moveToEnterIntent = new Intent(mContext, MainActivity.class);
    moveToEnterIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    moveToEnterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    moveToEnterIntent.putExtra(PREVIEW_PROFILE_NAME_EXTRA, profileDTO);
    mContext.startActivity(moveToEnterIntent);
  }
}
