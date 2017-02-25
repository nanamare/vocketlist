package com.vocketlist.android.net.errorchecker;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.VolunteerDetail;
import com.vocketlist.android.network.service.ErrorChecker;

/**
 * Created by kinamare on 2017-02-22.
 */

public class VoketDetailErrorChecker implements ErrorChecker<BaseResponse<VolunteerDetail>> {


	@Override
	public void checkError(BaseResponse<VolunteerDetail> data) throws RuntimeException {

		if (data == null) {
			throw new VoketDetailError(data);
		}


	}
}