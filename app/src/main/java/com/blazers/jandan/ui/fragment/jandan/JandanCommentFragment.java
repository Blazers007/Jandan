package com.blazers.jandan.ui.fragment.jandan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.models.jandan.comment.Comments;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.ui.adapters.JandanCommentAdapter;
import com.blazers.jandan.util.RecyclerViewHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Blazers on 2015/10/13.
 */
public class JandanCommentFragment extends Fragment {


    @Bind(R.id.comment_recycler_view) RecyclerView commentRecyclerView;


    public static JandanCommentFragment NewInstance(long commentId) {
        JandanCommentFragment fragment = new JandanCommentFragment();
        Bundle data = new Bundle();
        data.putLong("commentId", commentId);
        fragment.setArguments(data);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() == null || getArguments().getLong("commentId", -1) == -1){
            // 关闭
            return null;
        }
        View root = inflater.inflate(R.layout.fragment_comment, container, false);
        ButterKnife.bind(this, root);
        loadCommentById(getArguments().getLong("commentId", -1));
        return root;
    }

    void loadCommentById(long id) {
        Parser.getInstance().getCommentById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::parseAndShowComments,
                throwable -> Log.e("[Comments]", throwable.toString())
            );
    }

    void parseAndShowComments(Comments comments) {
        commentRecyclerView.setLayoutManager(RecyclerViewHelper.getVerticalLinearLayoutManager(getActivity()));
        JandanCommentAdapter adapter = new JandanCommentAdapter(LayoutInflater.from(getActivity()), comments);
        commentRecyclerView.setAdapter(adapter);
    }
}
