package com.vocketlist.android.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kinamare on 2017-02-25.
 */

public class BaseResponse<T extends Serializable> implements Serializable {
	@SerializedName("result") public T mResult;
}
