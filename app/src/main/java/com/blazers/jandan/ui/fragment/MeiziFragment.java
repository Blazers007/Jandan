package com.blazers.jandan.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;

/**
 * Created by Blazers on 2015/8/25.
 */
public class MeiziFragment extends Fragment {

    @Bind(R.id.swipte_container) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_list) RecyclerView meiziList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meizi, container, false);
        ButterKnife.bind(this, root);
        return root;
    }



    /* Meizi Adapter */
    class MeiziAdapter extends RecyclerView.Adapter<MeiziAdapter.MeiziHolder>{

        @Override
        public MeiziHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return null;
        }

        @Override
        public void onBindViewHolder(MeiziHolder meiziHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class MeiziHolder extends RecyclerView.ViewHolder {

            public MeiziHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
