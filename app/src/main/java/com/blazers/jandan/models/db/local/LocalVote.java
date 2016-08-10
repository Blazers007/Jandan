package com.blazers.jandan.models.db.local;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Blazers on 2015/11/3.
 */
public class LocalVote extends RealmObject {
    @PrimaryKey
    private long id;
    private int vote; // -1 0 1

    /* Public APIs */
    public static LocalVote getLocalVoteById(Realm realm, long id) {
        return realm.where(LocalVote.class).equalTo("id", id).findFirst();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
