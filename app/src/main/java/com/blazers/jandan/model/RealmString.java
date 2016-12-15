package com.blazers.jandan.model;

import io.realm.RealmObject;

/**
 * Created by blazers on 2016/12/7.
 */

public class RealmString extends RealmObject {
    private String val;

    public RealmString() {

    }

    public RealmString(String val) {
        this.val = val;
    }

    public String getValue() {
        return val;
    }

    public void setValue(String value) {
        this.val = value;
    }
}
