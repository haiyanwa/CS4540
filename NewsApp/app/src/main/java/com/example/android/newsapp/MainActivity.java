package com.example.android.newsapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.newsapp.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mNewsAPITextVIEW;
    private ProgressBar mProgressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNewsAPITextVIEW = (TextView) findViewById(R.id.news_api_data);
        mProgressIndicator = (ProgressBar) findViewById(R.id.progress_indicator);
        loadNewsData();

    }

    private void loadNewsData(){
        new sendNewsRequest().execute();

    }

    public class sendNewsRequest extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mProgressIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            //String location = params[0];
            URL newsRequestUrl = NetworkUtils.buildURL();

            try{

                String jsonNewsSearchResult = NetworkUtils.getResponseFromHttpUrl(newsRequestUrl);

                return jsonNewsSearchResult;

            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String jsonNewsSearchResult){
            mProgressIndicator.setVisibility(View.INVISIBLE);
            if(jsonNewsSearchResult != null) {
                mNewsAPITextVIEW.setText(jsonNewsSearchResult);

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
}
