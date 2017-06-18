package com.jorik.gobike.View.Activity;

import static com.jorik.gobike.Utils.AuthorizationUtils.PREVIEW_PROFILE_NAME_EXTRA;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.jorik.gobike.Model.POJO.ProfileModel;
import com.jorik.gobike.R;
import com.jorik.gobike.View.Logic.Callback.UpdateHeaderCallback;
import com.jorik.gobike.View.Logic.MainActivityLogic;
import com.jorik.gobike.View.View.HeaderView;

public class MainActivity extends AppCompatActivity implements UpdateHeaderCallback {

  private NavigationView mNavigationView;
  private DrawerLayout mDrawerLayout;
  private Toolbar mToolbar;
  private MainActivityLogic mLogic;
  private HeaderView mHeaderView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mNavigationView = (NavigationView) findViewById(R.id.navigationViewMain);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutMain);
    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(mToolbar);

    mLogic = new MainActivityLogic(this);
    mLogic.registerCallback(this);
    mLogic.setPreviewProfileDTO(getIntent().getParcelableExtra(PREVIEW_PROFILE_NAME_EXTRA));
    mHeaderView = new HeaderView(this, mLogic);

    loadView();
  }

  private void loadView() {
    mLogic.checkStateApplication();
    loadNavigation();
    loadDrawer();
  }

  private void loadNavigation() {
    mNavigationView.setCheckedItem(R.id.navigation_item_statistics);
    mNavigationView.addHeaderView(mHeaderView.createView(this));
    mNavigationView.setNavigationItemSelectedListener(item -> {
      mLogic.chooseFragmentAndTitleById(item.getItemId());
      mDrawerLayout.closeDrawers();
      return true;
    });
  }

  private void loadDrawer() {
    ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
        mToolbar, R.string.main_activity_open_drawer, R.string.main_activity_close_drawer) {
      @Override
      public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
      }

      @Override
      public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
      }
    };

    mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
    mActionBarDrawerToggle.syncState();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mLogic.onResume();
  }

  @Override
  protected void onDestroy() {
    mHeaderView.onDestroy();
    mLogic.onDestroy();
    super.onDestroy();
  }

  @Override
  public void onBackPressed() {
    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
      mDrawerLayout.closeDrawers();
      return;
    }
    super.onBackPressed();
  }

  @Override
  public void updateHeader(ProfileModel model) {
    mHeaderView.updateData(model);
  }
}
