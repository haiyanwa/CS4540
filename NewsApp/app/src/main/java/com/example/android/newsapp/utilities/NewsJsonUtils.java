package com.example.android.newsapp.utilities;

import android.util.Log;

import com.example.android.newsapp.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by apple on 6/27/17.
 */

public final class NewsJsonUtils {

    public static ArrayList<NewsItem> parseJson(String newsJsonStr)
            throws JSONException {

        ArrayList<NewsItem> newsItems = new ArrayList<>();

        JSONObject newsJsonData = new JSONObject(newsJsonStr);
        JSONArray newsArticles = newsJsonData.getJSONArray("articles");

        for(int i=0;i<newsArticles.length();i++){

            JSONObject article = newsArticles.getJSONObject(i);
            Log.d(TAG, "article" + article);

            String author = article.getString("author");
            String title = article.getString("title");
            Log.d(TAG, "title" + title);

            String description = article.getString("description");
            String url = article.getString("url");
            Log.d(TAG, "url" + url);

            String url_image = article.getString("urlToImage");
            String timestamp = article.getString("publishedAt");

            NewsItem newsItem = new NewsItem(title, description, url, timestamp);
            newsItems.add(newsItem);
        }

        return newsItems;

    }
}