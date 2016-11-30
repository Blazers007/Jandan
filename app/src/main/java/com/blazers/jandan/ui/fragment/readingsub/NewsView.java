package com.blazers.jandan.ui.fragment.readingsub;

import com.blazers.jandan.model.news.PostsBean;
import com.blazers.jandan.ui.fragment.base.BaseLoadMoreRefreshView;

import java.util.List;

/**
 * Created by blazers on 2016/11/30.
 */

public interface NewsView extends BaseLoadMoreRefreshView {

    void refreshDataList(List<PostsBean> postsBeanList);

    void addDataList(List<PostsBean> postsBeanList);

}
