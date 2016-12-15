package com.blazers.jandan.model.news;

import android.databinding.BindingAdapter;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by blazers on 2016/12/7.
 */
public class NewsPost extends RealmObject {
    /**
     * id : 84360
     * url : http://i.jandan.net/2016/12/07/newton-expensive-principia.html
     * title : 初版初印：牛顿的《自然哲学的数学原理》即将拍卖
     * content : <p><img src="http://tankr.net/s/small/1KBN.jpg" alt="初版初印：牛顿的《自然哲学的数学原理》即将拍卖" /></p>
     * <p>炼金术师牛顿的《自然哲学的数学原理》是全世界最有影响力的书籍之一。它的初版初印本月将在纽约开拍，标价100万美元。</p>
     * <p>2003年，一本据说曾属于詹姆士二世的《原理》进入了拍卖市场，这本用摩洛哥山羊皮装帧的善本拍出了250万美元的天价，标价为60万美元。如今，纽约克里斯蒂拍卖行拿到了这本书的初版初印，标价在100万到150万美元之间，业内估计此书将轻松成为有史以来最贵的善本之一。</p>
     * <p>那么，它为何那么值钱呢？《原理》是现代科学领域最重要也是最具影响力的图书。它阐释了牛顿的万有引力理论，同时也是利用严谨的数学系统研究物理的开山之作。它成为了科学方法论和科学思维的标准。从1687年该书出版，到1916年爱因斯坦相对论的发表，在长达近230年的时间里，它一直都是物理研究的基石。</p>
     * <p> “牛顿在当时做出的研究彻底改变了物理科学。这是物理的基本法，”皇家学会图书馆馆长Keith Moore说。</p>
     * <p>它也是全球保有量最少的书籍版本之一。《原理》初版初印仅有约400册，其中约20%是“欧陆版”，为欧洲大陆的买家打造，它们跟英国本土销售的有细微区别。这次拍卖的书就是其中之一。</p>
     * <p>Moore认为如此昂贵的标价得益于社会文化对科学越来越推崇，以及那些暴富的Geek们。</p>
     * <p> “买这些珍贵善本的人可能都是那些搞互联网发财的人。如果你的钱来源自一个超级酷的算法，你可能想感谢一下牛顿的物理学。如果你手头有好几百万现金，为何不买一本初版初印的《原理》呢？你买不了吃亏，更买不了上当。”</p>
     * <p> “它不仅见证了科学的历史和发展；它更是有史以来出版过的最伟大的书籍之一，”Moore补充说。</p>
     * <p>英国皇家学会保有两本《原理》，包括它的“最珍贵的收藏”——牛顿的原始手稿。</p>
     * <p><em>[<a target=_blank href="http://i.jandan.net/2016/12/07/newton-expensive-principia.html">许叔</a> via <a target=_blank rel="external" href="http://www.zmescience.com/science/news-science/newton-expensive-principia/">Quartz</a>]</em>zmescience</p>
     * <p>
     * date : 2016-12-07 16:00:57
     * tags : [{"id":698,"slug":"%e8%87%b4%e5%af%8c%e4%bf%a1%e6%81%af","title":"致富信息","description":"","post_count":963}]
     * author : {"id":587,"slug":"cedric","name":"许叔","first_name":"Cedric","last_name":"Hsu","nickname":"许叔","url":"http://weibo.com/cedrichsu","description":""}
     * comments : [{"id":3341879,"name":"茶苯海明","url":"","date":"2016-12-07 16:09:20","content":"<p>第一版〈理论力学〉，比圣经更伟大的著作<\/p>\n","parent":0,"vote_positive":0,"vote_negative":0,"index":1}]
     * comments_rank : [{"id":3341879,"name":"茶苯海明","url":"","date":"2016-12-07 16:09:20","content":"<p>第一版〈理论力学〉，比圣经更伟大的著作<\/p>\n","parent":0,"vote_positive":0,"vote_negative":0,"index":1}]
     * comment_count : 1
     * custom_fields : {"thumb_c":["http://tankr.net/s/custom/1KBN.jpg"]}
     */
    public static final String ID = "id";

    @PrimaryKey
    public int id;
    public int page;
    public boolean favorite;
    public Date favorite_time;
    public String url;
    public String title;
    public String content;
    public String date;
    public NewsAuthor author;
    public int comment_count;
    public NewsCustomField custom_fields;
    public RealmList<NewsTag> tags;
    public RealmList<NewsComment> comments;
    public RealmList<NewsCommentsRank> comments_rank;


    public String tag() {
        if (tags != null && tags.size() != 0) {
            return tags.get(0).title;
        }
        return "";
    }

    @BindingAdapter({"post"})
    public static void showImage(SimpleDraweeView simpleDraweeView, NewsPost posts) {
        simpleDraweeView.setImageURI(Uri.parse(posts.custom_fields.thumb_c.get(0).getValue()));
    }
}
