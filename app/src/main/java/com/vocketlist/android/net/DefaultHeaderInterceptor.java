package com.vocketlist.android.net;

import android.text.TextUtils;

import com.vocketlist.android.util.SharePrefUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SeungTaek.Lim on 2017. 2. 25..
 */
public class DefaultHeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String token = SharePrefUtil.getSharedPreference("token");

        Request.Builder requestBuilder = original.newBuilder();

        if (TextUtils.isEmpty(token) == false) {
                requestBuilder.header("authorization", "JWT " + token);
        }

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}