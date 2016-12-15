package com.blazers.jandan.ui.fragment.readingsub;
import com.blazers.jandan.model.image.SingleImage;
import com.blazers.jandan.ui.fragment.base.BaseLoadMoreRefreshView;

import java.util.List;

/**
 * Created by blazers on 2016/12/2.
 */

public interface ImageView extends BaseLoadMoreRefreshView {

    // 添加该界面独有的
    void onRefreshDataList(List<SingleImage> postsBeanList);

    void onAddDataList(List<SingleImage> postsBeanList);

    void onInspectImage(SingleImage singleImage);
}
