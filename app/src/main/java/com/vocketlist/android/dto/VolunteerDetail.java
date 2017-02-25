package com.vocketlist.android.dto;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by kinamare on 2017-02-22.
 */
//봉사 상세페이지
public class VolunteerDetail implements Serializable {
	@SerializedName("title") public String mTitle;
	@SerializedName("start_date") public String mStartDate;
	@SerializedName("end_date") public String mEndDate;
	@SerializedName("recruit_start_date") public String mRecruitStartDate;
	@SerializedName("recruit_end_date") public String mRecruitEndDate;
	@SerializedName("start_time") public int mStartTime;
	@SerializedName("end_time") public int mEndTime;
	@SerializedName("activeDay") public  String mActiveDay;
	@SerializedName("hostName") public String mHostName;
	@SerializedName("place") public String mPlace;
	@SerializedName("content") public String mContent;
	@SerializedName("is_active") public boolean mIsActive;
	@SerializedName("first_category") public String mFirstCategory;
	@SerializedName("second_category") public String mSecondCategory;
	@SerializedName("first_register_office") public String mFirstRegisterOffice;
	@SerializedName("second_register_office") public String mSecondRegisterOffice;
	@SerializedName("recruit_num_by_day") public int mNumByDay;
	@SerializedName("image") public String mImageUrl;
	@SerializedName("organization_id") public int mOrganzationId;
	@SerializedName("url") public String mInternalLinkUrl;
}