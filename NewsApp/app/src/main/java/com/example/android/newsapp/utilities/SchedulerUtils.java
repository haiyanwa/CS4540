package com.example.android.newsapp.utilities;

import android.content.Context;
import android.util.Log;

import com.example.android.newsapp.sync.NewsAppUpdateJobDispatcher;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by Haiyan on 7/28/17.
 * Schedule jobs defined in NewsAppUpdateJobDispatcher to update news every minute
 */

public class SchedulerUtils {

    private static final int REMINDER_INTERVAL_SECONDS = 60;
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;
    private static boolean initialized;
    private static final String UPDATE_JOB_TAG = "update_news_tag";
    private static final String TAG = UPDATE_JOB_TAG;

    synchronized public static void scheduler(Context context){

        //if the job has been initialized, then return
        if(initialized == true) return;

        //generate new GooglePlayerDriver object
        Driver driver = new GooglePlayDriver(context);
        //create a FireBaseJobDispatcher
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        //build the job
        Job UpdateNewsJob = dispatcher.newJobBuilder()
                //the service we need for the update
                .setService(NewsAppUpdateJobDispatcher.class)
                .setTag(UPDATE_JOB_TAG)
                //lifetime as forever
                .setLifetime(Lifetime.FOREVER)
                //we want it to recur
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))

                .setReplaceCurrent(true)
                .build();

        //schedule the job
        dispatcher.schedule(UpdateNewsJob);
        Log.d(TAG, "scheduler scheduled");

        //set the initialized as true since the job is scheduled
        initialized = true;

    }

}
