package com.blazers.jandan.ui.fragment.readingsub;

import com.blazers.jandan.model.news.NewsPage;
import com.blazers.jandan.ui.fragment.base.BaseLoadMoreRefreshView;

import java.util.List;

/**
 * Created by blazers on 2016/11/30.
 */

public interface NewsView extends BaseLoadMoreRefreshView {

    void refreshDataList(List<NewsPage.Posts> postsBeanList);

    void addDataList(List<NewsPage.Posts> postsBeanList);

}
