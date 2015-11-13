package com.blazers.jandan.ui.fragment.favoritesub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.ui.fragment.base.BaseSwipeRefreshFragment;
import com.blazers.jandan.util.RecyclerViewHelper;

/**
 * Created by Blazers on 2015/11/13.
 */
public class FavoriteTimelineFragment extends BaseSwipeRefreshFragment {

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
     * Timeline Adapter
     * */
    class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineHolder> {

        @Override
        public TimelineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TimelineHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_timeline, parent, false));
        }

        @Override
        public void onBindViewHolder(TimelineHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }

        class TimelineHolder extends RecyclerView.ViewHolder {

            public TimelineHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
