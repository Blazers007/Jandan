package com.blazers.jandan.model.news;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by blazers on 2016/11/30.
 */
public class PostsBean extends RealmObject {
    /**
     * id : 84177
     * type : post
     * slug : two-colours-photons
     * url : http://jandan.net/2016/11/30/two-colours-photons.html
     * status : publish
     * title : 一个光子，两种颜色
     * title_plain : 一个光子，两种颜色
     * content : <p><img src="http://tankr.net/s/medium/W9HU.jpg" alt="一个光子，两种颜色" /><br />
     美国和德国的物理学家们成功将单个光子置于两种不同颜色的叠加态中。这种光子有助于连接使用不同色光的量子信息网络。</p>
     <p>叠加态在量子力学中是一个重要概念，允许一个物理系统同时处于两种甚至更多种状态，直到对系统的测量迫使其跌落到某一特定状态。例如，光子可以处于水平偏振和垂直偏振的叠加态，直至通过一个偏光计为止。</p>
     <p>信息可被编码进量子态并被量子计算机处理。量子计算机利用叠加态和其他量子力学特征处理信息，速度大大超越传统计算机。</p>
     <p>通常在物理学家眼中，光子处于某一确定的能态，有着特定的颜色。然而量子力学允许光子处于两个或多个能态，或者说颜色。在最近研究中，康奈尔大学、洪堡大学和剑桥大学的<a target=_blank rel="external" href="http://journals.aps.org/prl/abstract/10.1103/PhysRevLett.117.223601">Stéphane Clemmen</a>等人创造了处于两种不同颜色叠加态的双色光子。</p>
     <p>该团队使用了一种名为“布拉格四波混频”的技术。实验运行于一条100米长的光纤中，其中传输着两束激光。当一个“红色”光子射入光纤时，它会与激光发生相互作用，因而被置于初始“红色”状态和次级的“蓝色”状态的叠加态中。</p>
     <p><img src="http://tankr.net/s/medium/Q296.jpg" alt="一个光子，两种颜色" /><br />
     <em>单色光子转变为双色叠加态的过程示意图</em></p>
     <p>该装置可以调整，以使从一端发射的光子在被测量时变为红色或蓝色的概率相同。</p>
     <p>Clemmen及其同事也能够调整量子叠加态中的红与蓝的相对相位，从而创造出可控的光子，使之被测量时完全为蓝色或红色，或红与蓝的某一特定组合。这也印证了他们的光子处于连续叠加态中。</p>
     <p>该技术将来可用于连接使用不同色光的量子设备。例如，两个量子内存可以通过放进一个双色光子而被置于量子纠缠状态中。这种纠缠态的内存将广泛适用于量子计算和量子通讯应用。此外潜在应用还包括对活组织比如眼睛的光谱分析。</p>
     <p><em>[<a href="http://jandan.net/2016/11/30/two-colours-photons.html">卤鸡爪子</a> via <a target=_blank rel="external" href="http://physicsworld.com/cws/article/news/2016/nov/29/photons-created-in-a-superposition-of-two-colours">Physics World</a>]</em></p>

     * excerpt : 美国和德国的物理学家们成功将单个光子置于两种不同颜色的叠加态中。
     * date : 2016-11-30 15:18:56
     * modified : 2016-11-30 15:18:56
     * categories : [{"id":1,"slug":"news","title":"NEWS","description":"新鲜事","parent":0,"post_count":52261}]
     * tags : [{"id":449,"slug":"%e8%b5%b0%e8%bf%9b%e7%a7%91%e5%ad%a6","title":"走进科学","description":"","post_count":3808}]
     * author : {"id":605,"slug":"paw","name":"卤鸡爪子","first_name":"","last_name":"","nickname":"卤鸡爪子","url":"","description":""}
     * comments : [{"id":3336568,"name":"海狸","url":"","date":"2016-11-30 15:36:21","content":"<p>今天天气不错<\/p>\n","parent":0,"vote_positive":0,"vote_negative":0,"index":1}]
     * comments_rank : [{"id":3336568,"name":"海狸","url":"","date":"2016-11-30 15:36:21","content":"<p>今天天气不错<\/p>\n","parent":0,"vote_positive":0,"vote_negative":0,"index":1}]
     * attachments : []
     * comment_count : 1
     * comment_status : open
     */

    public int id;
    public String type;
    public String slug;
    public String url;
    public String status;
    public String title;
    public String title_plain;
    public String content;
    public String excerpt;
    public String date;
    public String modified;
    public AuthorBean author;
    public int comment_count;
    public String comment_status;
    public RealmList<CategoriesBean> categories;
    public RealmList<TagsBean> tags;
    public RealmList<CommentsBean> comments;
    public RealmList<CommentsRankBean> comments_rank;
}
