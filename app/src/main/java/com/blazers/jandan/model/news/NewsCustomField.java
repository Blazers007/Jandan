package com.blazers.jandan.model.news;

import com.blazers.jandan.model.RealmString;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by blazers on 2016/12/7.
 */

public class NewsCustomField extends RealmObject {
    public RealmList<RealmString> thumb_c;
}
