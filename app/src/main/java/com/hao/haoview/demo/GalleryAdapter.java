package com.hao.haoview.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hao.haoview.R;

import java.util.ArrayList;

/**
 * Created by mr on 4/22/2018.
 */

public class GalleryAdapter extends RecyclerView.Adapter{

    private static String TAG = GalleryAdapter.class.getName();

    Context mContext;
    LayoutInflater mInflater;
    ArrayList<String> mUrls;

    public GalleryAdapter(Context context, ArrayList<String> urls){
        mContext = context;
        mUrls = urls;
        mInflater = LayoutInflater.from(context);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.gallery_item_img);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.gallery_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        //1. 设置占位图-填充
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.alpha_loading);

        //1. URL数组
        Glide.with(mContext)
                    .load((String) mUrls.get(position ))
                    .apply(options)
                    .into(viewHolder.mImageView);
        Log.d(TAG, "onBindViewHolder position=" + position);
    }

    @Override
    public int getItemCount() {
        return mUrls.size();
    }
}
