package com.blazers.jandan.network;

import com.blazers.jandan.models.pojo.favorite.Favorite;
import retrofit.http.*;
import rx.Observable;
/**
 * Created by Blazers on 2015/11/17.
 */
public interface BlazersAPI {

    /**
     * 获取指定ID下的Favorite
     * */
    @GET("api/users/{userId}/favorite")
    Observable<Favorite> getUserFavorite(@Path("userId") String userId);

    //PUT用于一次更新所有值

    /* 只负责添加数据 */
    @POST("api/users/{userId}/favorite")
    @FormUrlEncoded()
    Observable<String> postUserFavorite(@Path("userId") String userId, @Field("favorite") String favorite);
}
