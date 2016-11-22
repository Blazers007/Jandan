package com.blazers.jandan.model.timeline;

import com.blazers.jandan.model.database.local.LocalFavImages;
import com.blazers.jandan.model.database.local.LocalFavJokes;
import com.blazers.jandan.model.database.local.LocalFavNews;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 2015/11/16.
 */
public class Timeline {
    public String date;
    public List<LocalFavImages> favImagesList;
    public List<LocalFavNews> favNewsList;
    public List<LocalFavJokes> favJokesList;

    public Timeline(String date) {
        this.date = date;
    }

    public void addFavImage(LocalFavImages image) {
        if (null == favImagesList)
            favImagesList = new ArrayList<>();
        favImagesList.add(image);
    }

    public void addFavNews(LocalFavNews news) {
        if (null == favNewsList)
            favNewsList = new ArrayList<>();
        favNewsList.add(news);
    }

    public void addFavJoke(LocalFavJokes joke) {
        if (null == favJokesList)
            favJokesList = new ArrayList<>();
        favJokesList.add(joke);
    }
}
