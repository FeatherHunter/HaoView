package com.hao.haoview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mr on 3/24/2018.
 */

public class RecyclerViewAdpater<T> extends RecyclerView.Adapter {

    LayoutInflater mLayoutInflater;
    ArrayList<T> mDatas;

    public RecyclerViewAdpater(Context context, ArrayList<T> dataArrayList) {
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        if(mDatas.get(position) instanceof String){ //为URL
        }else if(mDatas.get(position) instanceof Drawable){
            viewHolder.mImageView.setImageDrawable((Drawable) mDatas.get(position));
        }
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
