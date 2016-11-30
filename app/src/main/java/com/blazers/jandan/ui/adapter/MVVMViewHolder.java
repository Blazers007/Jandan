package com.blazers.jandan.ui.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by blazers on 2016/11/30.
 */

public class MVVMViewHolder extends RecyclerView.ViewHolder {

    public ViewDataBinding mBinding;

    public MVVMViewHolder(ViewDataBinding dataBinding) {
        super(dataBinding.getRoot());
        mBinding = dataBinding;
    }
}
