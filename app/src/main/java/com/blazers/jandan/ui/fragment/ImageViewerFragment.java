package com.blazers.jandan.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.orm.Picture;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import io.realm.Realm;
import io.realm.RealmResults;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Blazers on 15/8/27.
 */
public class ImageViewerFragment extends DialogFragment {

    @Bind(R.id.viewer) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_image_viewer, container , false);
        ButterKnife.bind(this, root);
        initRecyclerView();
        return root;
    }

    void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(new ViewAdapter());
    }

    class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder>{

        private LayoutInflater inflater;
        private Long comment_ID;
        private int index;
        private RealmResults<Picture> pictures;

        public ViewAdapter() {
            inflater = LayoutInflater.from(getActivity());
//            comment_ID = getArguments().getLong("comment_ID");
//            index = getArguments().getInt("index");
            Realm realm = Realm.getInstance(getActivity());
            pictures = realm.where(Picture.class).findAllSorted("comment_ID", false);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.item_viewer, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ImageRequest imageRequest = ImageRequest.fromUri(Uri.parse(pictures.get(position).getUrl()));
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest)
                    .setAutoPlayAnimations(true)
                    .build();
            holder.draweeView.setController(controller);
            // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
//            holder.mAttacher = new PhotoViewAttacher(holder.draweeView);
        }

        @Override
        public int getItemCount() {
            return pictures.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public SimpleDraweeView draweeView;
            public PhotoViewAttacher mAttacher;
            public ViewHolder(View itemView) {
                super(itemView);
                draweeView = (SimpleDraweeView) itemView.findViewById(R.id.image_viewer);
            }
        }
    }

}
