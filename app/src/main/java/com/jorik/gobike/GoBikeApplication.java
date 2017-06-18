package com.jorik.gobike;

import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class GoBikeApplication extends Application {

  private static final int SCHEMA_VERSION = 0;

  private static GoBikeApplication goBikeApplication = null;

  public static GoBikeApplication getInstance(){
      if(goBikeApplication ==  null) goBikeApplication = new GoBikeApplication();
      return goBikeApplication;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Realm.init(this);
    RealmConfiguration configuration = new RealmConfiguration.Builder()
        .name(Realm.DEFAULT_REALM_NAME)
        .schemaVersion(SCHEMA_VERSION)
        .deleteRealmIfMigrationNeeded()
        .build();

    Realm.setDefaultConfiguration(configuration);
  }


}
