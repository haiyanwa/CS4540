package com.example.android.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by apple on 6/27/17.
 */

public class NewsAppAdapter extends RecyclerView.Adapter<NewsAppAdapter.NewsAppAdapterViewHolder>{
    private String[] mNewsTitleData;
    private String[] mNewsDescriptionData;
    private String[] mNewsDate;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener{

        void onListItemClick(int clickedItemIndex);
    }

    public NewsAppAdapter(ListItemClickListener listener){
        mOnClickListener = listener;

    }

    @Override
    public NewsAppAdapter.NewsAppAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdFromListItem = R.layout.news_items;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdFromListItem, viewGroup, shouldAttachToParentImmediately);
        return new NewsAppAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAppAdapter.NewsAppAdapterViewHolder holder, int position) {
        holder.newsTitleTextView.setText(mNewsTitleData[position]);
        holder.newsDescriptionView.setText(mNewsDescriptionData[position]);
        holder.newsDateView.setText(mNewsDate[position]);
    }

    @Override
    public int getItemCount() {
        if (mNewsTitleData==null) {
            return 0;
        } else {
            return mNewsTitleData.length;
        }
    }

    public class NewsAppAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView newsTitleTextView;
        TextView newsDescriptionView;
        TextView newsDateView;

        public NewsAppAdapterViewHolder(View view){
            super(view);
            newsTitleTextView = (TextView) view.findViewById(R.id.newsitem_title);
            newsDescriptionView = (TextView) view.findViewById(R.id.newsitem_description);
            newsDateView = (TextView) view.findViewById(R.id.newsitem_date);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int onClickPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(onClickPosition);
        }
    }

    void setNewsData(ArrayList<NewsItem> newsItems){

        mNewsTitleData = new String[newsItems.size()];
        mNewsDescriptionData = new String[newsItems.size()];
        mNewsDate = new String[newsItems.size()];

        for(int i=0;i<mNewsTitleData.length;i++){
            mNewsTitleData[i] = newsItems.get(i).getTitle();
            mNewsDescriptionData[i] = newsItems.get(i).getDescription();
            mNewsDate[i] =  newsItems.get(i).getTimestamp();
        }
        notifyDataSetChanged();
    }

}
