package com.blazers.jandan.common;

/**
 * Created by Blazers on 2015/8/25.
 */
public class URL {

    /* JANDAN http://jandan.net */
    public static final String JANDAN_PIC_API = "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_pic_comments&page=";
    public static final String JANDAN_OOXX_API = "http://i.jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=";

    public static final String JANDAN_NEWS_API_PREFIX = "http://i.jandan.net/?oxwlxojflwblxbsapi=get_recent_posts&include=url,date,tags,author,title,comment_count,custom_fields&page=";
    public static final String JANDAN_NEWS_API_END = "&custom_fields=thumb_c,views&dev=1";

    public static final String JANDAN_NEWS_CONTENT = "http://i.jandan.net/?oxwlxojflwblxbsapi=get_post&id=";
    public static final String JANDAN_NEWS_CONTENT_END = "&include=content";

    public static final String JANDAN_JOKE_API = "http://i.jandan.net/?oxwlxojflwblxbsapi=jandan.get_duan_comments&page=";
    /* Comment */
    public static final String JANDAN_COMMENT_COUNT = "http://jandan.duoshuo.com/api/threads/counts.json?threads="; //comment-2957362,
    public static final String JANDAN_COMMENT_API = "http://jandan.duoshuo.com/api/threads/listPosts.json?thread_key="; //comment-2957362
    /* OO XX */
    public static final String JANDAN_VOTE_API = "http://i.jandan.net/index.php?acv_ajax=true&option=";  // 0 -> xx  1-> oo


    /* 获取某一页新闻 */
    public static String getJandanNewsAtPage(long page) {
        return JANDAN_NEWS_API_PREFIX + page + JANDAN_NEWS_API_END;
    }
    public static String getJandanNewsContentById(long id) {
        return JANDAN_NEWS_CONTENT + id + JANDAN_NEWS_CONTENT_END;
    }

    /* 获取某一页图片 */
    public static String getJandanAPIByPageAndType(long page, String type) {
        if (type.equals("wuliao"))
            return JANDAN_PIC_API + page;
        else
            return JANDAN_OOXX_API + page;
    }

    /* 获取某一页段子 */
    public static String getJandanJokeAtPage(long page) {
        return JANDAN_JOKE_API + page;
    }

    /**/
}
