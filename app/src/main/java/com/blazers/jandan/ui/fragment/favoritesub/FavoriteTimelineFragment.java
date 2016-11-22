package com.blazers.jandan.ui.fragment.favoritesub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.model.database.local.LocalFavImages;
import com.blazers.jandan.model.database.local.LocalFavJokes;
import com.blazers.jandan.model.database.local.LocalFavNews;
import com.blazers.jandan.model.timeline.Timeline;
import com.blazers.jandan.ui.fragment.base.BaseSwipeRefreshFragment;
import com.blazers.jandan.util.RecyclerViewHelper;
import com.blazers.jandan.util.TimeHelper;
import com.blazers.jandan.widgets.TimeLineView;
import io.realm.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Blazers on 2015/11/13.
 */
public class FavoriteTimelineFragment extends BaseSwipeRefreshFragment {

    private List<Timeline> list;

    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_common_fav_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        trySetupSwipeRefreshLayout();
        // 开启线程加载 避免延迟太高
        generateTimelineObjects();
        initRecyclerView();
    }

    void initRecyclerView() {
        recyclerView.setLayoutManager(RecyclerViewHelper.getVerticalLinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new TimelineAdapter());
    }

    @Override
    public void refresh() {

    }

    /**
     * 重要方法 查询三个表 并 逐一遍历 筛选 （后期动态加载）
     * */
    void generateTimelineObjects() {
        list = new ArrayList<>();
        Map<String, Timeline> timelineHashMap = new HashMap<>();
        // 遍历Image
        List<LocalFavImages> images = realm.where(LocalFavImages.class).findAllSorted("favTime", Sort.DESCENDING);
        for (LocalFavImages image : images) {
            String time = TimeHelper.getDate(image.getFavTime());
            if (timelineHashMap.containsKey(time)) {
                timelineHashMap.get(time).addFavImage(image);
            }else{
                Timeline timeline = new Timeline(time);
                timeline.addFavImage(image);
                timelineHashMap.put(time, timeline);
                list.add(timeline);
            }
        }
        // 遍历News
        List<LocalFavNews> newses = realm.where(LocalFavNews.class).findAllSorted("favTime", Sort.DESCENDING);
        for (LocalFavNews news : newses) {
            String time = TimeHelper.getDate(news.getFavTime());
            if (timelineHashMap.containsKey(time)) {
                timelineHashMap.get(time).addFavNews(news);
            }else{
                Timeline timeline = new Timeline(time);
                timeline.addFavNews(news);
                timelineHashMap.put(time, timeline);
                list.add(timeline);
            }
        }
        // 遍历Jokes
        List<LocalFavJokes> jokes = realm.where(LocalFavJokes.class).findAllSorted("favTime", Sort.DESCENDING);
        for (LocalFavJokes joke : jokes) {
            String time = TimeHelper.getDate(joke.getFavTime());
            if (timelineHashMap.containsKey(time)) {
                timelineHashMap.get(time).addFavJoke(joke);
            }else{
                Timeline timeline = new Timeline(time);
                timeline.addFavJoke(joke);
                timelineHashMap.put(time, timeline);
                list.add(timeline);
            }
        }
    }


    /**
     * Timeline Adapter
     * */
    class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineHolder> {

        @Override
        public TimelineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TimelineHolder(new TimeLineView(getActivity()));
        }

        @Override
        public void onBindViewHolder(TimelineHolder holder, int position) {
            holder.timeLineView.setupViewsByTimeline(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class TimelineHolder extends RecyclerView.ViewHolder {

            public TimeLineView timeLineView;

            public TimelineHolder(View itemView) {
                super(itemView);
                timeLineView = (TimeLineView) itemView;
            }
        }
    }
}
