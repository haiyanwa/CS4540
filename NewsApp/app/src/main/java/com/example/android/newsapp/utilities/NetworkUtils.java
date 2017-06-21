package com.example.android.newsapp.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by apple on 6/20/17.
 */

public final class NetworkUtils {
    private static final String News_Base_URL = "https://newsapi.org/v1/articles";

    final static String TAG = "LOGGING_NETWORK";

    final static String QUERY_PARAM = "source";
    final static String SORT_PARAM = "sortBy";
    final static String KEY_PARAM = "apiKey";

    private static final String source = "the-next-web";
    private static final String term = "latest";
    private static final String apiKey = "112233445566778899";

    public static URL buildURL(){
        Uri buildUri = Uri.parse(News_Base_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM,source)
                .appendQueryParameter(SORT_PARAM,term)
                .appendQueryParameter(KEY_PARAM,apiKey)
                .build();

        URL url = null;
        try{
            url = new URL(buildUri.toString());

        }catch(IOException e){
            e.printStackTrace();
            Log.d(TAG, "url=" + url);
        }
        return url;

    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
