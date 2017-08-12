package com.example.android.newsapp.sync;

import android.util.Log;

import com.example.android.newsapp.MainActivity;
import com.example.android.newsapp.NewsItem;
import com.example.android.newsapp.utilities.NetworkUtils;
import com.example.android.newsapp.utilities.NewsJsonUtils;

import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by apple on 7/28/17.
 */

public class SyncNewsTask {

    public static void syncNews(){
        URL newsRequestUrl = NetworkUtils.buildURL();
        try{

            String jsonNewsSearchResult = NetworkUtils.getResponseFromHttpUrl(newsRequestUrl);
            Log.d(TAG, "result " + jsonNewsSearchResult);
            ArrayList<NewsItem> parsedSearchResult = NewsJsonUtils.parseJson(jsonNewsSearchResult);

            //insert retrieved data into database
            MainActivity.deleteOldNews();
            MainActivity.insertNewsItemList(parsedSearchResult);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
