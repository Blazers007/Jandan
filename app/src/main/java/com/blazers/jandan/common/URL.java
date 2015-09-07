package com.blazers.jandan.common;

/**
 * Created by Blazers on 2015/8/25.
 */
public class URL {

    /* JANDAN http://jandan.net */
    public static final String JANDAN_HOME = "http://jandan.net";
    public static final String JANDAN_OOXX = "http://jandan.net/ooxx";

    public static final String JANDAN_OOXX_API = "http://i.jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments";
    public static final String JANDAN_NEWS_API_PREFIX = "http://i.jandan.net/?oxwlxojflwblxbsapi=get_recent_posts&include=url,date,tags,author,title,comment_count,custom_fields&page=";
    public static final String JANDAN_NEWS_API_END = "&custom_fields=thumb_c,views&dev=1";
    public static final String JANDAN_NEWS_CONTENT = "http://i.jandan.net/?oxwlxojflwblxbsapi=get_post&id=";
    public static final String JANDAN_NEWS_CONTENT_END = "&include=content";
    public static final String JANDAN_JOKE_API = "http://i.jandan.net/?oxwlxojflwblxbsapi=jandan.get_duan_comments";

    public static String GernarateNewsContentUrl(long id) {
        return JANDAN_NEWS_CONTENT + id + JANDAN_NEWS_CONTENT_END;
    }


    /* Other restful api */
}
