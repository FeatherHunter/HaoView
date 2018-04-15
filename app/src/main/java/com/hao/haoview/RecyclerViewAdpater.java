package com.hao.haoview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mr on 3/24/2018.
 */

public class RecyclerViewAdpater<T> extends RecyclerView.Adapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<T> mDatas;
    private boolean isLoop = false; //是否是循环

    public void setLoop(boolean loop) {
        isLoop = loop;
    }

    //    /**
//     * ===============================*
//     * 1-线程池处理图片加载
//     * ===============================
//     */
//    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
//    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
//    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
//    private static final int KEEP_ALIVE = 1;
//
//    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
//        private final AtomicInteger mCount = new AtomicInteger(1);
//
//        public Thread newThread(Runnable r) {
//            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
//        }
//    };
//
//    private static final BlockingQueue<Runnable> sPoolWorkQueue =
//            new LinkedBlockingQueue<Runnable>(128);
//
//    public static final Executor THREAD_POOL_EXECUTOR
//            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
//            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

    public ArrayList<T> getDatas() {
        return mDatas;
    }

    public RecyclerViewAdpater(Context context, ArrayList<T> dataArrayList) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mDatas = dataArrayList;
        itemCount = mDatas.size();
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

        viewHolder.itemView.setBackgroundColor(Color.RED);

//        RequestOptions options = new RequestOptions()
//                .placeholder(R.drawable.loading)
//                .centerCrop()
//                .override(900,1600);
        //1. 设置占位图-填充
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.loading)
                .centerCrop();

//        position = (position + mDatas.size()) % mDatas.size();

        if(mDatas.get(position) instanceof String){ //为URL
            //1. URL数组
            Glide.with(mContext)
                    .load((String) mDatas.get(position ))
                    .apply(options)
                    .into(viewHolder.mImageView);

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
        final int clickPosition = position;
        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(viewHolder.mImageView, clickPosition);
                }
            }
        });
        viewHolder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemLongClick(viewHolder.mImageView, clickPosition);
                }
                return false;
            }
        });
    }

    private static int itemCount = 0;
    @Override
    public int getItemCount() {
        //1. 如果是无限循环就返回Int上限，如果不是无限循环就正常返回数组的大小
//        return (isLoop == false)?(itemCount):(++itemCount);
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
