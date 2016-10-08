package com.sohail.patternizer.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by SOHAIL on 05/10/16.
 */

/**
 * The service which allows the sync adapter framework to access the authenticator.
 */
public class PatternizerAuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private PatternizerAuthenticator mAuthenticator;


    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new PatternizerAuthenticator(this);
    }

    @Nullable

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
