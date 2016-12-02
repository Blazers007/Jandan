package com.blazers.jandan.ui.fragment.readingsub;

import com.blazers.jandan.model.joke.JokePage;
import com.blazers.jandan.ui.fragment.base.BaseLoadMoreRefreshView;

import java.util.List;

/**
 * Created by blazers on 2016/12/2.
 */

public interface JokeView extends BaseLoadMoreRefreshView {

    // 添加该界面独有的
    void refreshDataList(List<JokePage.Comments> postsBeanList);

    void addDataList(List<JokePage.Comments> postsBeanList);

}
