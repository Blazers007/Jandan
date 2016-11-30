package com.blazers.jandan.ui.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.blazers.jandan.presenter.base.BasePresenter;

import java.util.List;

/**
 * Created by blazers on 2016/11/30.
 * 仅仅支持一种布局
 */

public class BaseSingleMVVMAdapter<M, P extends BasePresenter> extends RecyclerView.Adapter<MVVMViewHolder> {

    private LayoutInflater mLayoutInflater;
    private int mLayoutResId;
    private P mPresenter;
    private List<M> mList;
    private int mPresenterField;
    private int mModelField;


    public BaseSingleMVVMAdapter(LayoutInflater layoutInflater, int layoutResId, List<M> list, P presenter, int modelField, int presenterField) {
        mLayoutInflater = layoutInflater;
        mLayoutResId = layoutResId;
        mList = list;
        mPresenter = presenter;
        mModelField = modelField;
        mPresenterField = presenterField;

    }

    @Override
    public MVVMViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("OnCreateViewHolder", "CREATE");
        return new MVVMViewHolder(DataBindingUtil.inflate(mLayoutInflater, mLayoutResId, parent, false));
    }

    @Override
    public void onBindViewHolder(MVVMViewHolder holder, int position) {
        holder.mBinding.setVariable(mPresenterField, mPresenter);
        holder.mBinding.setVariable(mModelField, mList.get(position));
        holder.mBinding.executePendingBindings();
        Log.d("OnBindViewHolder", "Position -> " + position);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

}
