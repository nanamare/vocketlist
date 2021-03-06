package com.vocketlist.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.messaging.FirebaseMessaging;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.vocketlist.android.R;
import com.vocketlist.android.api.user.UserInfoModel;
import com.vocketlist.android.api.user.UserServiceManager;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.fragment.CommunityFragment;
import com.vocketlist.android.fragment.DrawerMenuFragment;
import com.vocketlist.android.fragment.VolunteerFragment;
import com.vocketlist.android.manager.ToastManager;
import com.vocketlist.android.preference.NoticePreference;
import com.vocketlist.android.util.RxEventManager;
import com.vocketlist.android.view.NavigationDrawerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 메인
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        OnTabSelectListener {

    @BindView(R.id.toolbar) protected Toolbar mToolbar;
    @BindView(R.id.drawer_layout) protected DrawerLayout mDrawer;
    @BindView(R.id.navigationView) protected NavigationView mNavigationView;


    @BindView(R.id.bottomBar) BottomBar bottomBar;

    @BindView(R.id.llVolunteerTab) LinearLayout llVolunteerTab;
    @BindView(R.id.llCommunityTab) LinearLayout llCommunityTab;

		private AppCompatTextView myListTv;
		private AppCompatTextView scheduleTv;


    // Event
    private View.OnClickListener onToolbarNavigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mDrawer.isDrawerVisible(GravityCompat.START)) {
                mDrawer.closeDrawer(GravityCompat.START);
            } else {
                mDrawer.openDrawer(GravityCompat.START);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FirebaseCrash.log("Activity created");
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseMessaging.getInstance().subscribeToTopic("reports");

        setRootActivity(true);

        initViews();

        //런칭시 팝업창
        if (NoticePreference.getInstance().showGuidePopup()) {
            dialogIntroduce();
        }

        clearDrawerFragment();
    }

	private void clearDrawerFragment() {
		RxEventManager.getInstance().getObjectObservable()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Object>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Object o) {
						if(o instanceof String){
							if(o.equals("DrawerMenuFragment")){
								myListTv.setText("");
								scheduleTv.setText("");
							}
						}
					}
				});
	}


	@Override
	protected void onResume() {
		super.onResume();

		getMenuUserInfo();

		clearDrawerFragment();

	}

    @Override
    protected void onStart() {
        super.onStart();

        getMenuUserInfo();
    }

    /**
     * 가이드 다이얼로그
     */
    private void dialogIntroduce() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_launch_custom, null, false);

        new MaterialDialog.Builder(this)
                .customView(v, false)
                .positiveText(R.string.close).positiveColorRes(R.color.btn_positive_guide_popup)
                .negativeText(R.string.detail_view).negativeColorRes(R.color.btn_negative_guide_popup)
                .onAny((dialog, which) -> {
                    NoticePreference.getInstance().setGuidePopup(false);

                    if (which == DialogAction.NEGATIVE) {
                        goToActivity(IntroduceActivity.class);
                        return;
                    }
                })
                .show();
    }

    private void initViews() {
        initToolbar();
        initDrawer();
        initBottomNavigation();
        mNavigationView.setNavigationItemSelectedListener(this);
        NavigationDrawerView headerView = (NavigationDrawerView) mNavigationView.getHeaderView(0);
        headerView.setFragmentManager(getSupportFragmentManager(), new DrawerMenuFragment());

        getMenuUserInfo();
    }

    private void initMenuCnt(BaseResponse<UserInfoModel> userInfo) {

        Menu myMenu = mNavigationView.getMenu();
        View myView = MenuItemCompat.getActionView(myMenu.findItem(R.id.naviMyList));
        myListTv = (AppCompatTextView) myView.findViewById(R.id.tvLabel);
        myListTv.setText(getString(R.string.count, userInfo.mResult.mMyListInfo.mTotal));

        Menu scheduleMenu = mNavigationView.getMenu();
        View scheduleView = MenuItemCompat.getActionView(scheduleMenu.findItem(R.id.naviSchedule));
	      scheduleTv = (AppCompatTextView) scheduleView.findViewById(R.id.tvLabel);
        scheduleTv.setText(getString(R.string.count, userInfo.mResult.mScheduleInfo.mTotalSchedule));

    }

    private void getMenuUserInfo() {
        UserServiceManager.getUserInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<BaseResponse<UserInfoModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Response<BaseResponse<UserInfoModel>> baseResponseResponse) {
                        initMenuCnt(baseResponseResponse.body());
                        RxEventManager.getInstance().sendData(baseResponseResponse.body().mResult.mMyListInfo);
                    }
                });
    }

    private void initToolbar() {
        // 헤더 CI 적용
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(
                getLayoutInflater().inflate(R.layout.appbar_title, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                )
        );
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_action_perm_identity);
        toggle.setToolbarNavigationClickListener(onToolbarNavigationClickListener);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initBottomNavigation() {
        bottomBar.setOnTabSelectListener(this, true);

        goToFragment(VolunteerFragment.class);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        // TODO 전달할 값이 있으면 extras 파라미터에 담아서...
        switch (id) {
//			// 관심정보 모아보기
//			case R.id.naviFavorite:
//				goToActivity(FavoriteActivity.class);
//				break;

            // 마이리스트
            case R.id.naviMyList:
                if(checkedLogin()) goToActivity(MyListActivity.class);
                break;

            // 스케줄관리
            case R.id.naviSchedule:
                if(checkedLogin()) goToActivity(ScheduleActivity.class);
                break;

            //-----------------------------------------------//

            // 알림설정
            case R.id.naviNotification:
                goToActivity(NotificationActivity.class);
                break;

            // 프로필관리
            case R.id.naviProfile:
                if(checkedLogin()) goToActivity(ProfileActivity.class);
                break;

            // 공지사항
            case R.id.naviNotice:
                goToActivity(NoticeActivity.class);
                break;

            // 서비스소개
            case R.id.naviIntroduce:
                goToActivity(IntroduceActivity.class);
                break;

            //-----------------------------------------------//

            // 문의/도움말
            case R.id.naviHelp:
                goToActivity(HelpActivity.class);
                break;

            // 이용약관
            case R.id.naviTerms:
                goToActivity(TermsActivity.class);
                break;

            // 라이센스
            case R.id.naviLicense:
                goToActivity(LicenseActivity.class);
                break;
        }

//        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        // TODO 전달할 값이 있으면 extras 파라미터에 담아서...
        switch (tabId) {
            // 봉사활동
            case R.id.action_volunteer:
                llVolunteerTab.setVisibility(View.VISIBLE);
                llCommunityTab.setVisibility(View.INVISIBLE);
                goToFragment(VolunteerFragment.class);
                break;
            // 커뮤니티
            case R.id.action_community:
                llVolunteerTab.setVisibility(View.INVISIBLE);
                llCommunityTab.setVisibility(View.VISIBLE);
                goToFragment(CommunityFragment.class);
                break;
        }
    }

    /**
     * 액티비티 호출
     *
     * @param cls
     */
    private void goToActivity(Class<?> cls) {
        goToActivity(cls, null);
    }

    /**
     * 액티비티 호출
     *
     * @param cls
     * @param extras
     */
    private void goToActivity(Class<?> cls, @Nullable Bundle extras) {
        Intent intent = new Intent(this, cls);
        if (extras != null) intent.putExtras(extras);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * 플래그먼트
     *
     * @param cls
     */
    private void goToFragment(Class<?> cls) {
        goToFragment(cls, null);
    }

    /**
     * 플래그먼트
     *
     * @param cls
     * @param args
     */
    private void goToFragment(Class<?> cls, @Nullable Bundle args) {
        try {
            Fragment fragment = (Fragment) cls.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 로그인 체크 > 비로그인 시 토스트
     *
     * @return
     */
    private boolean checkedLogin() {
        boolean isLogin = UserServiceManager.isLogin();
        if(!isLogin) ToastManager.show(R.string.login);
        return isLogin;
    }
}
