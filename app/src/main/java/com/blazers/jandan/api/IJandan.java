package com.blazers.jandan.api;

import com.blazers.jandan.model.ImagePage;
import com.blazers.jandan.model.JokePage;
import com.blazers.jandan.model.news.NewsPage;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by blazers on 2016/11/29.
 * 煎蛋api
 */

public interface IJandan {

    String BASE_URL = "http://jandan.net/";

    @GET("?oxwlxojflwblxbsapi=get_recent_posts&include=url,date,tags,author,title,comment_count,custom_fields&custom_fields=thumb_c,views&dev=1")
    Observable<NewsPage> getNews(@Query("page") int page);

    @GET("?oxwlxojflwblxbsapi=jandan.get_pic_comments")
    Observable<ImagePage> getWuliao(@Query("page") int page);

    @GET("http://i.jandan.net/?oxwlxojflwblxbsapi=jandan.get_duan_comments")
    Observable<JokePage> getJoke(@Query("page") int page);

    @GET("?oxwlxojflwblxbsapi=jandan.get_ooxx_comments")
    Observable<ImagePage> getMeizhi(@Query("page") int page);


}
