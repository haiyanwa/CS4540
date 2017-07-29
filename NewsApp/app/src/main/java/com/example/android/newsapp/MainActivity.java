package com.example.android.newsapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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

import com.example.android.newsapp.data.NewsAppContract;
import com.example.android.newsapp.data.NewsAppDBHelper;
import com.example.android.newsapp.utilities.NetworkUtils;
import com.example.android.newsapp.utilities.NewsJsonUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, NewsAppAdapter.ListItemClickListener{

    private TextView mNewsAPITextVIEW;
    private ProgressBar mProgressIndicator;
    final static String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private NewsAppAdapter mNewsAppAdapter;

    //database object reference
    private SQLiteDatabase mDb;

    //loader ID
    private static final int ATASK_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mNewsAPITextVIEW = (TextView) findViewById(R.id.news_api_data);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_newsapp);
        mProgressIndicator = (ProgressBar) findViewById(R.id.progress_indicator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        NewsAppDBHelper newsAppDBHelper = new NewsAppDBHelper(this);
        mDb = newsAppDBHelper.getWritableDatabase();

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        //Old implements for AsyncTask + database, no need anymore
        //loadNewsData();

        //Old implements for AsyncTask + database, no need anymore
        //get all news items in the database
        //Cursor cursor = getAllNewsItems();

        //create Adapter object and connect RecyclerView with this Adapter
        mNewsAppAdapter = new NewsAppAdapter(this);
        mRecyclerView.setAdapter(mNewsAppAdapter);

        //init AsyncTaskLoader
        getSupportLoaderManager().initLoader(ATASK_LOADER_ID, null, this);
    }

    private void loadNewsData(){
        new sendNewsRequest().execute();

    }

    //Implementing AsyncTaskLoader to replace AsyncTask

    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(ATASK_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor
            Cursor mTaskData = null;

            //From Udacity Excercise example
            //onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // use loadInBackground() to load of data in the background
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data
                try{
                    //get cursor by querying database
                    return getAllNewsItems();

                }catch (Exception e){
                    Log.d(TAG, "Failed to load data in the backgroud");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNewsAppAdapter.swapCursor(null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mNewsAppAdapter.swapCursor(data);
    }

    @Override
    public void onListItemClick(NewsItem newsItem) {
        /**Context context = this;
        Toast.makeText(context, "test click", Toast.LENGTH_SHORT)
                .show();*/
        openWebPage(newsItem.getUrl());
    }

    //Old implement before using AsyncTaskLoader--------------------------------------------
    //using AsyncTask to retrieve data from remote api and convert json into List<NewsItem>
    //save into database
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

                //insert retrieved data into database
                insertNewsItemList(parsedSearchResult);

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
                //Old implementation
                //mNewsAppAdapter.setNewsData(newsItems);
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
    //Old implement before using AsyncTaskLoader--------------------------------------------

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

    //insert a list of NewsItem data into database
    private void insertNewsItemList(List<NewsItem> newsItems){
        for(NewsItem newsItem : newsItems){
            addNewsData(newsItem);
        }
    }


    //method to insert NewsItem data to database
    private long addNewsData(NewsItem newsItem){
        ContentValues cv = new ContentValues();

        cv.put(NewsAppContract.NewsAppEntry.COLUMN_TITLE,newsItem.getTitle());
        cv.put(NewsAppContract.NewsAppEntry.COLUMN_URL,newsItem.getUrl());
        cv.put(NewsAppContract.NewsAppEntry.COLUMN_AUTHOR,newsItem.getAuthor());
        cv.put(NewsAppContract.NewsAppEntry.COLUMN_URL_IMAGE,newsItem.getUrl_image());
        cv.put(NewsAppContract.NewsAppEntry.COLUMN_DESCRIPTION,newsItem.getDescription());
        cv.put(NewsAppContract.NewsAppEntry.COLUMN_TIMESTAMP,newsItem.getTimestamp());

        // TODO (8) call insert to run an insert query on TABLE_NAME with the ContentValues created
        return mDb.insert(NewsAppContract.NewsAppEntry.TABLE_NAME, null, cv);

    }

    //get all news items in the database
    private Cursor getAllNewsItems(){
        return mDb.query(
                NewsAppContract.NewsAppEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                NewsAppContract.NewsAppEntry.COLUMN_TIMESTAMP
        );
    }


}
