package com.blazers.jandan.model.command;

import java.io.Serializable;

/**
 * Created by blazers on 2016/11/22.
 *
 * 用于传输离线下载命令
 *
 */

public class OfflineCommand implements Serializable {
    public boolean news;
    public boolean jokes;
    public boolean boring;
    public boolean girls;
    public int page;
}
