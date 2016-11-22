package com.blazers.jandan.util;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by blazers on 2016/11/18.
 * 用于替换Html中Img标签的Src路径
 */

public class HtmlImgReplaceUtil {

    private Document mDocument;
    private Elements mImageElements;

    public HtmlImgReplaceUtil(String html) {
        mDocument = Jsoup.parse(html);
        mImageElements = mDocument.getElementsByTag("img");
    }

    public static String getNoParameterUrl(String urlString) {
        return urlString.split("\\?")[0];
    }

    /**
     * 获取全部带有Src属性的节点 或者所有Img标签节点?
     *
     * @return 图片url列表
     */
    public List<String> getHtmlSrcList() {
        return Stream.of(mImageElements)
//                .filter(img -> !TextUtils.isEmpty(img.attr("src")))
                .filter(img -> !img.attr("src").equals(""))
                .map(img -> img.attr("src"))
                .collect(Collectors.toList());
    }

    /**
     * 替换全部Src路径
     *
     * @param localAddressList 本地src路径
     */
    public void replaceSrcListWithLocalAddress(List<String> localAddressList) {
        Stream.of(mImageElements)
                .filter(img -> !img.attr("src").equals(""))
                .indexed()
                .forEach(pair -> pair.getSecond().attr("src", "file://" + localAddressList.get(pair.getFirst())));
    }

    /**
     * 返回修改过后的String
     *
     * @return html string
     */
    public String getHtmlString() {
        return mDocument.toString();
    }
}
