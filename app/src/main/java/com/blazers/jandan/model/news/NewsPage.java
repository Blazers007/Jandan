package com.blazers.jandan.model.news;

import android.databinding.BindingAdapter;
import android.net.Uri;

import com.blazers.jandan.model.extension.Time;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by blazers on 2016/11/30.
 */

public class NewsPage {

    public String status;
    public int count;
    public int count_total;
    public int pages;
    public List<Posts> posts;

    public static class Posts extends Time {
        /**
         * id : 84188
         * url : http://i.jandan.net/2016/12/01/fake-news-lincoln.html
         * title : 林肯当年差点就被假新闻毁了
         * content : <p><img src="http://tankr.net/s/small/JV85.jpg" alt="林肯当年差点就被假新闻毁了" /></p>
         <p>林肯不仅是个奴隶制反对者，还是个优生学提倡者，他认为黑人与白人联姻可生出一个超级新种族。</p>
         <p>其实，这只是1864年报纸上的假新闻，但这还是差点毁了林肯。</p>
         <p>内战时，林肯和共和党人受到了意外的攻击。1864年2月，林肯为了改选的竞选事宜焦头烂额。对手是民主党的Samuel Sullivan Cox，支持奴隶制的议员，他在国会演讲中，称共和党支持通过黑人与白人联姻生出新种族的观点。</p>
         <p>各地民主党的报纸发表了他的演讲内容，共和党“恶心的理论”像病毒一样扩散开来。Cox引用了一份72页的匿名书册，书名是《种族联姻：美国白人与黑人混血理论》，这本书涉及颂赞种族联姻，鼓励共和党人将此作为官方理念的内容。</p>
         <p><img src="http://tankr.net/s/small/M8UW.jpg" alt="林肯当年差点就被假新闻毁了" /><br />
         <em>Cox引用的书册</em></p>
         <p>3月，《纽约论坛报》的共和党编辑Horace Greeley写文称，婚姻是夫妻自己的事情。此后民主党报社立刻又发了一堆关于共和党赞成种族联姻的文章。民主党报纸<a target=_blank rel="external" href="http://hoaxes.org/archive/permalink/the_miscegenation_hoax">《纽约世界报》写道</a>，“共和党最大的报纸是毫不知耻的‘种族联姻’支持者，虽然它将此称为社会和政治的最大问题。”</p>
         <p>整个1864年，这类声音不绝于耳。虽然如此，林肯还是在竞选中以55%的选票打败了民主党。</p>
         <p>这成了史上最为成功的假新闻事件。选举结束后，Cox在国会上引用的小册子的作者终于水落石出，这是两名民主党的新闻工作人员，他们下决心煽动工人阶级的白人共和党员的焦虑感。书中的言论来自包括Lucretia Mott在内的老实巴交的反奴隶制知识分子。其他民主党人也写了许多小册子，在工薪阶层的白人中传播。虽然William Lloyd Garrison等废奴主义者知道是怎么回事，却无力阻止。</p>
         <p>史学家Heather Cox Richardson称，这并不是一次独立事件。1860年，废奴主义狂热分子John Brown在哈泼斯渡口搞了次失败的袭击，舆论借此故意煽动人们对废奴主义者和奴隶的恐惧，推动了内战的爆发。70年代，尤利西斯·辛普森·格兰特总统又遭到了贪腐毁谤，耽误了重建工作。当时，由于科技进步，新闻飞速传播，许多报纸都在煽风点火。公众对政府产生了信任危机。由于缺乏共识和权威说法，人们都是凭直觉判断真假。而美国四分五裂的情况又让公众不得不受偏见和恐惧支配。</p>
         <p>有趣的是，这场假新闻运动的影响至今还在。1864年以前，“amalgamation”(混合)被用来形容任何东西的混合，不只是种族。而“miscegenation”(种族联姻)将拉丁语的“种族”和“混合”结合在一起，听上去非常“科学”。从此，美语词汇又多了一个单词，也多了一个用来恐慌的新概念。</p>
         <p><em>[<a target=_blank href="http://i.jandan.net/2016/12/01/fake-news-lincoln.html">蛋奶</a> via <a target=_blank rel="external" href="http://qz.com/842816/fake-news-almost-destroyed-abraham-lincoln/">qz</a>]</em></p>

         * date : 2016-12-01 11:00:39
         * tags : [{"id":843,"slug":"%e5%8f%b2%e6%b5%b7%e9%92%a9%e6%b2%89","title":"史海钩沉","description":"","post_count":350}]
         * author : {"id":623,"slug":"miriam","name":"蛋奶","first_name":"","last_name":"","nickname":"蛋奶","url":"","description":""}
         * comments : [{"id":3337206,"name":"风大算吊毛","url":"","date":"2016-12-01 11:04:43","content":"<p>手上沾满了南方绅士的鲜血<\/p>\n","parent":0,"vote_positive":1,"vote_negative":0,"index":1},{"id":3337211,"name":"奔波儿灞","url":"","date":"2016-12-01 11:09:14","content":"<p>广州50W黑人快造出来第57个民族了&#8230;<\/p>\n","parent":0,"vote_positive":0,"vote_negative":0,"index":2},{"id":3337213,"name":"奔波儿灞","url":"","date":"2016-12-01 11:09:38","content":"<p>我的评论呢?<\/p>\n","parent":0,"vote_positive":0,"vote_negative":0,"index":3},{"id":3337215,"name":"普利尼大王","url":"","date":"2016-12-01 11:12:54","content":"<p>奥巴马种族。<\/p>\n","parent":0,"vote_positive":0,"vote_negative":0,"index":4}]
         * comments_rank : [{"id":3337206,"name":"风大算吊毛","url":"","date":"2016-12-01 11:04:43","content":"<p>手上沾满了南方绅士的鲜血<\/p>\n","parent":0,"vote_positive":1,"vote_negative":0,"index":1},{"id":3337215,"name":"普利尼大王","url":"","date":"2016-12-01 11:12:54","content":"<p>奥巴马种族。<\/p>\n","parent":0,"vote_positive":0,"vote_negative":0,"index":2},{"id":3337213,"name":"奔波儿灞","url":"","date":"2016-12-01 11:09:38","content":"<p>我的评论呢?<\/p>\n","parent":0,"vote_positive":0,"vote_negative":0,"index":3},{"id":3337211,"name":"奔波儿灞","url":"","date":"2016-12-01 11:09:14","content":"<p>广州50W黑人快造出来第57个民族了&#8230;<\/p>\n","parent":0,"vote_positive":0,"vote_negative":0,"index":4}]
         * comment_count : 4
         * custom_fields : {"thumb_c":["http://tankr.net/s/custom/JV85.jpg"]}
         */

        public int id;
        public String url;
        public String title;
        public String content;
        public String date;
        public Author author;
        public int comment_count;
        public CustomFields custom_fields;
        public List<Tags> tags;
        public List<Comments> comments;
        public List<CommentsRank> comments_rank;

        public static class Author implements Serializable {
            /**
             * id : 623
             * slug : miriam
             * name : 蛋奶
             * first_name :
             * last_name :
             * nickname : 蛋奶
             * url :
             * description :
             */

            public int id;
            public String slug;
            public String name;
            public String first_name;
            public String last_name;
            public String nickname;
            public String url;
            public String description;
        }

        public static class CustomFields implements Serializable {
            public List<String> thumb_c;
        }

        public static class Tags implements Serializable {
            /**
             * id : 843
             * slug : %e5%8f%b2%e6%b5%b7%e9%92%a9%e6%b2%89
             * title : 史海钩沉
             * description :
             * post_count : 350
             */

            public int id;
            public String slug;
            public String title;
            public String description;
            public int post_count;
        }

        public static class Comments implements Serializable {
            /**
             * id : 3337206
             * name : 风大算吊毛
             * url :
             * date : 2016-12-01 11:04:43
             * content : <p>手上沾满了南方绅士的鲜血</p>

             * parent : 0
             * vote_positive : 1
             * vote_negative : 0
             * index : 1
             */

            public int id;
            public String name;
            public String url;
            public String date;
            public String content;
            public int parent;
            public int vote_positive;
            public int vote_negative;
            public int index;
        }

        public static class CommentsRank implements Serializable {
            /**
             * id : 3337206
             * name : 风大算吊毛
             * url :
             * date : 2016-12-01 11:04:43
             * content : <p>手上沾满了南方绅士的鲜血</p>

             * parent : 0
             * vote_positive : 1
             * vote_negative : 0
             * index : 1
             */

            public int id;
            public String name;
            public String url;
            public String date;
            public String content;
            public int parent;
            public int vote_positive;
            public int vote_negative;
            public int index;
        }


        @BindingAdapter({"post"})
        public static void showImage(SimpleDraweeView simpleDraweeView, Posts posts) {
            simpleDraweeView.setImageURI(Uri.parse(posts.custom_fields.thumb_c.get(0)));
        }

    }
}
