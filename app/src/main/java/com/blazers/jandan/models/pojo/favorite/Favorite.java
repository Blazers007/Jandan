package com.blazers.jandan.models.pojo.favorite;

import android.content.Context;
import com.blazers.jandan.common.URL;
import com.blazers.jandan.models.db.local.*;
import com.blazers.jandan.models.db.local.serializers.ImageSerializer;
import com.blazers.jandan.models.db.local.serializers.JokeSerializer;
import com.blazers.jandan.models.db.local.serializers.NewsSerializer;
import com.blazers.jandan.network.BlazersAPI;
import com.blazers.jandan.util.DBHelper;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.Sort;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import java.util.List;

/**
 * Created by Blazers on 2015/11/17.
 */
public class Favorite {

    @Expose public List<LocalFavNews> newses;
    @Expose public List<LocalFavImages> images;
    @Expose public List<LocalFavJokes> jokes;

    private static BlazersAPI INSTANCE;
    private static Gson gson;

    public Favorite(List<LocalFavNews> newses, List<LocalFavImages> images, List<LocalFavJokes> jokes) {
        this.newses = newses;
        this.images = images;
        this.jokes = jokes;
    }
    /* Public API */
    public static BlazersAPI getRetrofitServiceInstance() throws ClassNotFoundException{
        if (INSTANCE == null) {
            gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapter(Class.forName("io.realm.LocalFavNewsRealmProxy"), new NewsSerializer())
                .registerTypeAdapter(Class.forName("io.realm.LocalFavImagesRealmProxy"), new ImageSerializer())
                .registerTypeAdapter(Class.forName("io.realm.LocalFavJokesRealmProxy"), new JokeSerializer())
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
            return INSTANCE = new Retrofit.Builder()
                .baseUrl(URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(BlazersAPI.class);
        }
        return INSTANCE;
    }


    /**
     * 异步线程获取Favorite的String
     * */
    public static Observable<String> getLocalFavorite(Context context) {
        return Observable.create(subscriber -> {
            Realm realm = DBHelper.generateTempRealm(context);
            subscriber.onNext(gson.toJson(getLocalFavorite(realm)));
            subscriber.onCompleted();
        });
    }


    public static Favorite getLocalFavorite(Realm realm) {
        List<LocalFavNews> newses = realm.where(LocalFavNews.class).findAllSorted("favTime", Sort.DESCENDING);
        List<LocalFavImages> images = realm.where(LocalFavImages.class).findAllSorted("favTime", Sort.DESCENDING);
        List<LocalFavJokes> jokes = realm.where(LocalFavJokes.class).findAllSorted("favTime", Sort.DESCENDING);
        return new Favorite(newses, images, jokes);
    }
}
