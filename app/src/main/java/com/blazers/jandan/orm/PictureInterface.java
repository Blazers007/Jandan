package com.blazers.jandan.orm;

import com.blazers.jandan.common.URL;
import io.realm.RealmObject;

/**
 * Created by Blazers on 2015/9/9.
 */
public interface PictureInterface {
    RealmObject getOrmObject();

    String getLocalUrl();

    String getUrl();

    String getUrlByQulity(URL.HUABAN_QULITY qulity);
}
