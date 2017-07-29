package com.example.android.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.newsapp.data.NewsAppContract;

import java.util.ArrayList;

/**
 * Created by apple on 6/27/17.
 */

public class NewsAppAdapter extends RecyclerView.Adapter<NewsAppAdapter.NewsAppAdapterViewHolder>{

    private ArrayList<NewsItem> newsItems;
    private Cursor mCursor;
    private NewsItem newsItem;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener{

        //void onListItemClick(int clickedItemIndex);
        void onListItemClick(NewsItem newsItem);
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

        if(!mCursor.moveToPosition(position)){
            return;
        }

        /** old implement without database
        String title = newsItems.get(position).getTitle();
        String description = newsItems.get(position).getDescription();
        String date = newsItems.get(position).getTimestamp();*/

        //get the data we want from database cursor
        String title = mCursor.getString(mCursor.getColumnIndex(NewsAppContract.NewsAppEntry.COLUMN_TITLE));
        String description = mCursor.getString(mCursor.getColumnIndex(NewsAppContract.NewsAppEntry.COLUMN_DESCRIPTION));
        String date = mCursor.getString(mCursor.getColumnIndex(NewsAppContract.NewsAppEntry.COLUMN_TIMESTAMP));
        String url = mCursor.getString(mCursor.getColumnIndex(NewsAppContract.NewsAppEntry.COLUMN_URL));

        //construct a NewsItem object to pass to onClick method
        newsItem = new NewsItem(title, description,url,date);

        //bind data to views
        holder.newsTitleTextView.setText(title);
        holder.newsDescriptionView.setText(description);
        holder.newsDateView.setText(date);
    }

    /**
     * Old implement before using database
    @Override
    public int getItemCount() {
        if (newsItems==null) {
            return 0;
        } else {
            return newsItems.size();
        }
    }*/

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    void setNewsData(ArrayList<NewsItem> newsItems){

        /** Old implement before using database
         * mNewsTitleData = new String[newsItems.size()];
        mNewsDescriptionData = new String[newsItems.size()];
        mNewsDate = new String[newsItems.size()];

        for(int i=0;i<mNewsTitleData.length;i++){
            mNewsTitleData[i] = newsItems.get(i).getTitle();
            mNewsDescriptionData[i] = newsItems.get(i).getDescription();
            mNewsDate[i] =  newsItems.get(i).getTimestamp();
        }*/
        this.newsItems = newsItems;
        notifyDataSetChanged();
    }

    public void swapCursor(Cursor cursor) {
        // check if this cursor is the same as the previous cursor
        //if same then return
        if (mCursor == cursor) {
            return;
        }
        //check if this is a valid cursor, then update the cursor
        if (cursor != null) {
            this.mCursor = cursor;
            this.notifyDataSetChanged();
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

            //Old implementation before using database
            //mOnClickListener.onListItemClick(newsItems.get(onClickPosition));

            mOnClickListener.onListItemClick(newsItem);
        }
    }

}
