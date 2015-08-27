package com.blazers.jandan.orm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Blazers on 15/8/25.
 *
 * JSON 转数据库持久化
 *
 */
public class Meizi extends RealmObject {


    @PrimaryKey
    private long comment_ID;
    private long comment_post_ID;
    private String comment_author;
    private String comment_date;
    private String vote_positive;
    private String vote_negative;
    private String text_content;
    private int picture_size;

    /* Getter & Setter */

    public long getComment_ID() {
        return comment_ID;
    }

    public void setComment_ID(long comment_ID) {
        this.comment_ID = comment_ID;
    }

    public long getComment_post_ID() {
        return comment_post_ID;
    }

    public void setComment_post_ID(long comment_post_ID) {
        this.comment_post_ID = comment_post_ID;
    }

    public String getComment_author() {
        return comment_author;
    }

    public void setComment_author(String comment_author) {
        this.comment_author = comment_author;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public String getVote_positive() {
        return vote_positive;
    }

    public void setVote_positive(String vote_positive) {
        this.vote_positive = vote_positive;
    }

    public String getVote_negative() {
        return vote_negative;
    }

    public void setVote_negative(String vote_negative) {
        this.vote_negative = vote_negative;
    }

    public String getText_content() {
        return text_content;
    }

    public void setText_content(String text_content) {
        this.text_content = text_content;
    }

    public int getPicture_size() {
        return picture_size;
    }

    public void setPicture_size(int picture_size) {
        this.picture_size = picture_size;
    }
}
