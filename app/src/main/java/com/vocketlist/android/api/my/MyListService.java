package com.vocketlist.android.api.my;

import com.vocketlist.android.dto.BaseResponse;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kinamare on 2017-02-21.
 */

public interface MyListService {
	@FormUrlEncoded
	@POST("users/mylist/")
	Observable<Response<BaseResponse<MyListModel.MyList>>> write(@Field("year") int year,
																 @Field("content") String content,
																 @Field("is_done") boolean isDone);

	@FormUrlEncoded
	@PATCH("users/mylist/{id}/")
	Observable<Response<BaseResponse<MyListModel.MyList>>> modify(@Path("id") int id,
														   @Field("content") String content,
														   @Field("is_done")  boolean isDone);

	@GET("users/mylist/")
	Observable<Response<BaseResponse<MyListModel>>> get(@Query("year") int year,
														@Query("page") int page,
														@Query("page_size") int pageSize);

	@DELETE("users/mylist/{id}/")
	Observable<Response<BaseResponse<Void>>> delete(@Path("id") int id);
}
