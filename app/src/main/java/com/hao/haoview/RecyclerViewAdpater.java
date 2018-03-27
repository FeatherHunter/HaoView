package com.hao.haoview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

/**
 * Created by mr on 3/24/2018.
 */

public class RecyclerViewAdpater<T> extends RecyclerView.Adapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<T> mDatas;

    public ArrayList<T> getDatas() {
        return mDatas;
    }

    public RecyclerViewAdpater(Context context, ArrayList<T> dataArrayList) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mDatas = dataArrayList;
    }

    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        //1. 设置占位图
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.loading)
                .centerCrop()
                .override(900,1600);

        if(mDatas.get(position) instanceof String){ //为URL

        }else if(mDatas.get(position) instanceof Drawable){
            //2. 加载占位图并显示需要加载的图片
            Glide.with(mContext)
                    .load((Drawable) mDatas.get(position))
                    .apply(options)
                    .into(viewHolder.mImageView);
        }else if(mDatas.get(position) instanceof Integer){
            Glide.with(mContext)
                    .load((Integer)mDatas.get(position))
                    .apply(options)
                    .into(viewHolder.mImageView);
        }
        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(viewHolder.mImageView, position);
            }
        });
        viewHolder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemClickListener.onItemLongClick(viewHolder.mImageView, position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

//    //提供了增删的接口
//    public void addData(int position, String newMsg) {
//        mDatas.add(position, newMsg);
//        notifyItemInserted(position);
//    }
//
//    public void removeData(int position) {
//        mDatas.remove(position);
//        notifyItemRemoved(position);
//    }
}
