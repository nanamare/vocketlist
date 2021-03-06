package com.vocketlist.android.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vocketlist.android.R;
import com.vocketlist.android.api.user.UserServiceManager;
import com.vocketlist.android.api.vocket.Participate;
import com.vocketlist.android.api.vocket.VocketServiceManager;
import com.vocketlist.android.api.vocket.Volunteer;
import com.vocketlist.android.api.vocket.VolunteerDetail;
import com.vocketlist.android.dialog.VolunteerApplyDialog;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.error.ExceptionHelper;
import com.vocketlist.android.network.service.EmptySubscriber;
import com.vocketlist.android.roboguice.log.Ln;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * 봉사활동 : 보기
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class VolunteerReadActivity extends DepthBaseActivity {
	private static final int REQUEST_WRITE_COMMUNITY = 1000;
	public static final String EXTRA_KEY_VOCKET_ID = "vocketId";

	@BindView(R.id.toolbar) protected Toolbar toolbar;
	@BindView(R.id.volunteer_read_title) protected TextView mVocketTitleText;
	@BindView(R.id.start_date_tv) protected TextView mStartDateText;
	@BindView(R.id.end_date_tv) protected TextView mEndDateText;
	@BindView(R.id.start_time_tv) protected TextView mStartTimeText;
	@BindView(R.id.end_time_tv) protected TextView mEndTimeText;
	@BindView(R.id.recruit_start_date_tv) protected TextView mRecruitStartDateText;
	@BindView(R.id.recruit_end_date_tv) protected TextView mRecruitEndDateText;
	@BindView(R.id.active_day_tv) protected TextView mActiveDayText;
	@BindView(R.id.place_tv) protected TextView mPlaceText;
	@BindView(R.id.num_by_day_tv) protected TextView mNumByDayText;
	@BindView(R.id.host_name_tv) protected TextView mHostNameText;
	@BindView(R.id.category_tv) protected TextView mCategoryText;
	@BindView(R.id.apply_btn) protected TextView mApplyBtn;
	@BindView(R.id.apply_cancel_btn) protected TextView mApplyCancelBtn;
	@BindView(R.id.write_diary_btn) protected TextView mWriteDiaryBtn;
	@BindView(R.id.isActiveDayTv) protected TextView mIsActiveDayText;
	@BindView(R.id.volunteer_read_detail_content) protected TextView mContentText;
	@BindView(R.id.volunteer_iv) protected ImageView mVolunteerImageView;
	@BindView(R.id.volunteer_read_internal_noti_text) protected TextView mInternalNotiTextView;

	private int vocketIndex;

	private BaseResponse<VolunteerDetail> mVolunteerDetail;

	private boolean isInternalApply;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_volunteer_read);
		ButterKnife.bind(this);

		Intent intent = getIntent();
		if (intent != null) {
			String vocketIdx = intent.getStringExtra("vocketId");
			vocketIndex = Integer.valueOf(vocketIdx);
			System.out.print(vocketIndex);
		}

		setSupportActionBar(toolbar);
		requestVolunteerDetail(vocketIndex);
	}

	private void requestVolunteerDetail(int vocketIndex) {
		VocketServiceManager.getVocketDetail(vocketIndex)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Response<BaseResponse<VolunteerDetail>>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Ln.e(e, "onError : " + e.toString());

						if (ExceptionHelper.isNetworkError(e)) {
							Toast.makeText(VolunteerReadActivity.this, R.string.error_message_network, Toast.LENGTH_SHORT).show();
							finish();
							return;
						}

						String errorMessage = ExceptionHelper.getFirstErrorMessage(e);
						Toast.makeText(VolunteerReadActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
						finish();
						return;
					}

					@Override
					public void onNext(Response<BaseResponse<VolunteerDetail>> baseResposeResponse) {
						mVolunteerDetail = baseResposeResponse.body();
						bindVocketDetailData(mVolunteerDetail);
					}
				});
	}

	public void bindVocketDetailData(BaseResponse<VolunteerDetail> volunteerDetails) {
		mVolunteerDetail = volunteerDetails;
		mVocketTitleText.setText(volunteerDetails.mResult.mTitle);
		mStartDateText.setText(volunteerDetails.mResult.mStartDate);
		mEndDateText.setText(volunteerDetails.mResult.mEndDate);
		mActiveDayText.setText(volunteerDetails.mResult.mActiveDay);
		mPlaceText.setText(volunteerDetails.mResult.mPlace);
		mNumByDayText.setText(String.valueOf(volunteerDetails.mResult.mNumByDay));
		mHostNameText.setText(volunteerDetails.mResult.mHostName);
		mCategoryText.setText(getString(volunteerDetails.mResult.mFirstCategory.getTabResId()));
		mStartTimeText.setText(volunteerDetails.mResult.mStartTime);
		mEndTimeText.setText(volunteerDetails.mResult.mEndTime);
		mRecruitStartDateText.setText(String.valueOf(volunteerDetails.mResult.mRecruitStartDate));
		mRecruitEndDateText.setText(String.valueOf(volunteerDetails.mResult.mRecruitEndDate));
		mContentText.setText(volunteerDetails.mResult.mContent);

		if (volunteerDetails.mResult.mIsActive) {
			mIsActiveDayText.setVisibility(View.VISIBLE);
		} else {
			mIsActiveDayText.setVisibility(View.GONE);
		}
		Glide.with(this).load(getString(R.string.vocket_base_url) + volunteerDetails.mResult.mImageUrl).into(mVolunteerImageView);
		//dialog 타이틀
		isInternalApply = volunteerDetails.mResult.mIsDirect;
		mInternalNotiTextView.setVisibility(isInternalApply ? View.GONE : View.VISIBLE);


		if (volunteerDetails.mResult.mIsParticipate) {
			mApplyBtn.setVisibility(View.GONE);
			mApplyCancelBtn.setVisibility(View.VISIBLE);
		} else {
			mApplyBtn.setVisibility(View.VISIBLE);
			mApplyCancelBtn.setVisibility(View.GONE);
		}
	}

	@OnClick(R.id.apply_btn)
	void apply_onClick() {
		if (UserServiceManager.isLogin() == false) {
			Toast.makeText(this, R.string.error_message_login, Toast.LENGTH_SHORT).show();
			return;
		}

		final VolunteerApplyDialog dialog = new VolunteerApplyDialog(this, isInternalApply, mVolunteerDetail.mResult);
		dialog.setListener(new VolunteerApplyDialog.VolunteerApplyListener() {
			@Override
			public void onVolunteerApply() {
				if (isInternalApply == false
						&& TextUtils.isEmpty(mVolunteerDetail.mResult.mInternalLinkUrl) == false) {
					moveToWebSite(mVolunteerDetail.mResult.mInternalLinkUrl);
				}

				mApplyBtn.setVisibility(View.GONE);
				mApplyCancelBtn.setVisibility(View.VISIBLE);
			}

			@Override
			public void onCancel() {

			}
		});
		dialog.show();
	}

	@OnClick(R.id.apply_cancel_btn)
	void apply_cancel_onClick() {
		showProgressDialog(false);
		VocketServiceManager.applyVolunteer(mVolunteerDetail.mResult.mId,
											false,
											null,
											null,
											null)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnTerminate(new Action0() {
					@Override
					public void call() {
						hideProgressDialog(true);
					}
				})
				.subscribe(new EmptySubscriber<Response<BaseResponse<Participate>>>() {
					@Override
					public void onError(Throwable e) {
						if (ExceptionHelper.isNetworkError(e)) {
							Toast.makeText(VolunteerReadActivity.this, R.string.error_message_network, Toast.LENGTH_SHORT).show();
							return;
						}
					}

					@Override
					public void onNext(Response<BaseResponse<Participate>> baseResponseResponse) {
						Toast.makeText(VolunteerReadActivity.this, R.string.volunteer_read_apply_cancel_message, Toast.LENGTH_SHORT).show();

						mApplyBtn.setVisibility(View.VISIBLE);
						mApplyCancelBtn.setVisibility(View.GONE);
					}
				});
	}

	private void moveToWebSite(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
	}

	@OnClick(R.id.write_diary_btn)
	void onClickWriteDiary() {
		Intent intent = new Intent(this, PostCUActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.putExtra(PostCUActivity.EXTRA_KEY_VOLUNTEER_DATA, getVolunteerData(mVolunteerDetail.mResult));
		startActivityForResult(intent, REQUEST_WRITE_COMMUNITY);
	}

	private Volunteer.Data getVolunteerData(VolunteerDetail detail) {
		Volunteer.Data data = new Volunteer.Data();

		data.mTitle = detail.mTitle;
		data.mId = detail.mId;
		data.mOrganizationId = detail.mOrganzationId;
		data.mFirstOffice = detail.mFirstRegisterOffice;
		data.mSecondOffice = detail.mSecondRegisterOffice;
		data.mImageUrl = detail.mImageUrl;
		data.mIsActive = detail.mIsActive;
		data.mStartDate = detail.mStartDate;

		return data;
	}
}
