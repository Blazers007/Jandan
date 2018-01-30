//package com.blazers.jandan.api;
//
//
//
//import retrofit2.http.GET;
//import retrofit2.http.Query;
//
///**
// * Created by blazers on 2016/11/29.
// * 煎蛋api
// */
//
//public interface IJandan {
//
//    String BASE_URL = "http://i.jandan.net/";
//    String TYPE_WULIAO = "jandan.get_pic_comments";
//    String TYPE_MEIZHI = "jandan.get_ooxx_comments";
//
//
//    @GET("?oxwlxojflwblxbsapi=jandan.get_duan_comments")
//    Observable<Joke> getJoke(@Query("page") int page);
//
//    @GET("/")
//    Observable<Image> getImage(@Query("page") int page, @Query("oxwlxojflwblxbsapi") String type);
//
//    @GET("?oxwlxojflwblxbsapi=get_recent_posts&include=url,date,tags,author,title,content,comment_count,comments,comments_rank,custom_fields&custom_fields=thumb_c,views&dev=1")
//    Observable<News> getNews(@Query("page") int page);
//
//
//}
