package com.vocketlist.android.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.messaging.FirebaseMessaging;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.vocketlist.android.R;
import com.vocketlist.android.fragment.CommunityFragment;
import com.vocketlist.android.fragment.DrawerMenuFragment;
import com.vocketlist.android.fragment.VolunteerFragment;
import com.vocketlist.android.view.NavigationDrawerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leolin.shortcutbadger.ShortcutBadger;

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


		FirebaseMessaging.getInstance().subscribeToTopic("news");
		FirebaseMessaging.getInstance().subscribeToTopic("reports");
		//
		setSupportActionBar(mToolbar);

		// 헤더 CI 적용
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

		initViews();
		FirebaseCrash.log("Activity created");

		//런칭시 팝업창
		final View innerView = getLayoutInflater().inflate(R.layout.dialog_launch_custom, null);
		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		alert.setView(innerView);
		alert.setPositiveButton("닫기", (dialog, whichButton) -> {
			//todo

		});

		alert.setNegativeButton("자세히 보기",
				(dialog, whichButton) -> {
					//todo

				});
		AlertDialog dialog = alert.create();
		dialog.show();
	}

	private void initViews() {
		initDrawer();
		initBottomNavigation();

		mNavigationView.setNavigationItemSelectedListener(this);
		NavigationDrawerView headerView = (NavigationDrawerView) mNavigationView.getHeaderView(0);
		headerView.setFragmentManager(getSupportFragmentManager(), new DrawerMenuFragment());
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

		//
		goToFragment(VolunteerFragment.class);
	}

	@Override
	public void onBackPressed() {
		if (mDrawer.isDrawerOpen(GravityCompat.START)) {
			mDrawer.closeDrawer(GravityCompat.START);
		} else {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("보킷리스트를 종료 하시겠습니까?").setCancelable(false)
					.setPositiveButton("확인", (dialog, which) -> finish())
					.setNegativeButton("취소", (dialog, which) -> {
						return;
					});

			AlertDialog alertDialog = alert.create();
			alertDialog.show();
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		int id = item.getItemId();

		// TODO 전달할 값이 있으면 extras 파라미터에 담아서...
		switch (id) {
			// 알림설정
			case R.id.naviNotification:
				goToActivity(NotificationActivity.class);
				break;

			// 관심정보 모아보기
			case R.id.naviFavorite:
				goToActivity(FavoriteActivity.class);
				break;

			// 마이리스트
			case R.id.naviMyList:
				goToActivity(MyListActivity.class);
				break;

			//-----------------------------------------------//

			// 프로필관리
			case R.id.naviProfile:
				goToActivity(ProfileActivity.class);
				break;

			// 스케줄관리
			case R.id.naviSchedule:
				goToActivity(ScheduleActivity.class);
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

			// 정책
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

	private BroadcastReceiver badgeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Get the received random number
			int badgeCount = intent.getIntExtra("badgeCount", 0);
			updateBadge(badgeCount);
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(
				badgeReceiver,
				new IntentFilter("badgeCount")
		);
	}

	private void updateBadge(int count) {
		ShortcutBadger.applyCount(this, count); //for 1.1.4+
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(badgeReceiver);
	}
}