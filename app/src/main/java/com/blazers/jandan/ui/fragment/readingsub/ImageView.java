package com.blazers.jandan.ui.fragment.readingsub;

import com.blazers.jandan.model.image.ImagePage;
import com.blazers.jandan.model.image.SingleImage;
import com.blazers.jandan.ui.fragment.base.BaseLoadMoreRefreshView;

import java.util.List;

/**
 * Created by blazers on 2016/12/2.
 */

public interface ImageView extends BaseLoadMoreRefreshView {

    // 添加该界面独有的
    void refreshDataList(List<SingleImage> postsBeanList);

    void addDataList(List<SingleImage> postsBeanList);
}
