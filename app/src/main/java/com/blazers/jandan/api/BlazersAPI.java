package com.blazers.jandan.api;

import com.blazers.jandan.model.favorite.Favorite;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Blazers on 2015/11/17.
 */
public interface BlazersAPI {

    /**
     * 获取指定ID下的Favorite
     */
    @GET("api/users/{userId}/favorite")
    Observable<Favorite> getUserFavorite(@Path("userId") String userId);

    //PUT用于一次更新所有值

    /* 只负责添加数据 */
    @POST("api/users/{userId}/favorite")
    @FormUrlEncoded()
    Observable<String> postUserFavorite(@Path("userId") String userId, @Field("favorite") String favorite);
}
