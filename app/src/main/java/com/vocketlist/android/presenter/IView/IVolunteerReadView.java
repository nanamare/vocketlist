package com.vocketlist.android.presenter.IView;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.api.vocket.VolunteerDetail;

/**
 * Created by kinamare on 2017-02-23.
 */

public interface IVolunteerReadView {
	void bindVocketDetailData(BaseResponse<VolunteerDetail> volunteerDetails);
	void showCompleteDialog();
}
