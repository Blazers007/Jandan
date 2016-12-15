package com.blazers.jandan.ui.fragment.readingsub;

import com.blazers.jandan.model.news.NewsPost;
import com.blazers.jandan.ui.fragment.base.BaseLoadMoreRefreshView;

import java.util.List;

/**
 * Created by blazers on 2016/11/30.
 */

public interface NewsView extends BaseLoadMoreRefreshView {

    // 添加该界面独有的
    void onRefreshDataList(List<NewsPost> postsBeanList);

    void onAddDataList(List<NewsPost> postsBeanList);

    void onNavigateToNewsRead(int postId);
}
