package com.blazers.jandan.models;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Blazers on 2015/9/9.
 */
public class HuabanPin extends RealmObject {
    @PrimaryKey
    private long pin_id;
    private long file_id;
    private long user_id;
    private long board_id;
    private int like_count;
    private int repin_count;
    private int comment_count;

    /* Names */
    private String board;

    /* Image Keys */
    private String user_key;
    private String file_key;

    /* LocalStorage */
    private String localUrl;

    public long getPin_id() {
        return pin_id;
    }

    public void setPin_id(long pin_id) {
        this.pin_id = pin_id;
    }

    public long getFile_id() {
        return file_id;
    }

    public void setFile_id(long file_id) {
        this.file_id = file_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getBoard_id() {
        return board_id;
    }

    public void setBoard_id(long board_id) {
        this.board_id = board_id;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getRepin_count() {
        return repin_count;
    }

    public void setRepin_count(int repin_count) {
        this.repin_count = repin_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getFile_key() {
        return file_key;
    }

    public void setFile_key(String file_key) {
        this.file_key = file_key;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    /* APIS */
    public static RealmResults<HuabanPin> findAllSortDesc(Realm realm) {
        return realm.where(HuabanPin.class).findAllSorted("pin_id", false);
    }

    public static RealmResults<HuabanPin> loadMoreLessThan(Realm realm, long last) {
        return realm.where(HuabanPin.class).lessThan("pin_id", last).findAll();
    }
}
