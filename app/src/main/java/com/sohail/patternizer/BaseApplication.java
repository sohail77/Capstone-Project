package com.sohail.patternizer;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by SOHAIL on 05/10/16.
 */
public class BaseApplication extends Application {

    private Tracker mTracker;

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            //mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
