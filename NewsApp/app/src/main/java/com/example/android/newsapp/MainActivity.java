package com.example.android.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.newsapp.utilities.NetworkUtils;
import com.example.android.newsapp.utilities.NewsJsonUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NewsAppAdapter.ListItemClickListener{

    private TextView mNewsAPITextVIEW;
    private ProgressBar mProgressIndicator;
    final static String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private NewsAppAdapter mNewsAppAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mNewsAPITextVIEW = (TextView) findViewById(R.id.news_api_data);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_newsapp);
        mProgressIndicator = (ProgressBar) findViewById(R.id.progress_indicator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mNewsAppAdapter = new NewsAppAdapter(this);
        mRecyclerView.setAdapter(mNewsAppAdapter);


        loadNewsData();

    }

    private void loadNewsData(){
        new sendNewsRequest().execute();

    }

    @Override
    public void onListItemClick(NewsItem newsItem) {
        /**Context context = this;
        Toast.makeText(context, "test click", Toast.LENGTH_SHORT)
                .show();*/
        openWebPage(newsItem.getUrl());
    }

    public class sendNewsRequest extends AsyncTask<String, Void, ArrayList<NewsItem>> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mProgressIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<NewsItem> doInBackground(String... params) {

            //String location = params[0];
            URL newsRequestUrl = NetworkUtils.buildURL();
            try{

                String jsonNewsSearchResult = NetworkUtils.getResponseFromHttpUrl(newsRequestUrl);
                Log.d(TAG, "result " + jsonNewsSearchResult);
                ArrayList<NewsItem> parsedSearchResult = NewsJsonUtils.parseJson(jsonNewsSearchResult);

                return parsedSearchResult;
                //return jsonNewsSearchResult;

            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(final ArrayList<NewsItem> newsItems){
            //super.onPostExecute(newsItems);
            mProgressIndicator.setVisibility(View.INVISIBLE);
            if(newsItems != null) {
                //mNewsAPITextVIEW.setText(newsSearchResult);
                /**for(NewsItem newsItem : newsItems){
                    String title = newsItem.getTitle();
                    mNewsAPITextVIEW.append(title + "\n\n\n");
                }*/
                mNewsAppAdapter.setNewsData(newsItems);
                /**NewsAppAdapter mNewsAdapter = new NewsAppAdapter(new NewsAppAdapter.ListItemClickListener(){
                    @Override
                    public void onListItemClick(int clickedItemIndex) {
                        String url = newsItems.get(clickedItemIndex).getUrl();
                        Log.d(TAG, String.format("Url %s", url));
                    }
                });
                mRecyclerView.setAdapter(mNewsAdapter);*/
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            //call search method
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}
