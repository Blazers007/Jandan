package com.blazers.jandan.ui.fragment.favoritesub;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.models.db.local.LocalFavImages;
import com.blazers.jandan.ui.fragment.base.BaseFragment;
import com.blazers.jandan.ui.fragment.base.BaseSwipeRefreshFragment;
import com.facebook.drawee.view.SimpleDraweeView;
import io.realm.Realm;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Blazers on 2015/11/12.
 */
public class FavoriteImageFragment extends BaseSwipeRefreshFragment {

    private ArrayList<LocalFavImages> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_common_fav_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trySetupSwipeRefreshLayout();
        init();
    }

    /**
     * 长按取消收藏 OR 长按进入编辑模式 ？
     * */

    @Override
    public void refresh() {

    }

    void init() {
        list = new ArrayList<>();
        List<LocalFavImages> addons = realm.where(LocalFavImages.class).findAllSorted("favTime", false);
        if (null != addons)
            list.addAll(addons);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(new FavMeizhiAdapter());
    }

    /**
     * Adapter
     * */
    class FavMeizhiAdapter extends RecyclerView.Adapter<FavMeizhiAdapter.MeizhiHolder> {

        @Override
        public MeizhiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MeizhiHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_favorite_image, parent, false));
        }

        @Override
        public void onBindViewHolder(MeizhiHolder holder, int position) {
            holder.simpleDraweeView.setImageURI(Uri.parse(list.get(position).getUrl()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MeizhiHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.content) SimpleDraweeView simpleDraweeView;

            public MeizhiHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnLongClickListener(v->{
                    PopupMenu popup = new PopupMenu(getActivity(), getActivity().getWindow().getDecorView(), Gravity.CENTER);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                        .inflate(R.menu.menu_popup, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(item->true);
                    popup.show();
                    return true;
                });
            }
        }
    }
}
