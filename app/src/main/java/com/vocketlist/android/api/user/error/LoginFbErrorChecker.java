package com.vocketlist.android.api.user.error;

import com.vocketlist.android.network.service.ErrorChecker;

import okhttp3.ResponseBody;

/**
 * Created by kinamare on 2017-02-21.
 */

public class LoginFbErrorChecker  implements ErrorChecker<ResponseBody> {
	@Override
	public void checkError(ResponseBody data) throws RuntimeException {
		if (data == null) {
			throw new LoginFbException();
		}

	}
}
