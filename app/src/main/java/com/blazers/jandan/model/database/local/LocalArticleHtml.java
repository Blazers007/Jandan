package com.blazers.jandan.model.database.local;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Blazers on 2015/10/21.
 * <p>
 * 此处存放需要缓存在本地的  该数据是原服务器返回数据类型中不存在的
 * <p>
 * 故单独存放 便于管理 同样的也便于 将 @see com.blazers.jandan.models.db.sync 中的与API Server保持高度一致
 */
public class LocalArticleHtml extends RealmObject {

    @PrimaryKey
    private long id;
    private String html;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
