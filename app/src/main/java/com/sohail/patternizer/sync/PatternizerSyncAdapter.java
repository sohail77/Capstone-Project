package com.sohail.patternizer.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.sohail.patternizer.data.Patternizer_provider;
import com.sohail.patternizer.Models.ColourloversService;
import com.sohail.patternizer.Models.PatternsModel;
import com.sohail.patternizer.R;

import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SOHAIL on 05/10/16.
 */
public class PatternizerSyncAdapter extends AbstractThreadedSyncAdapter {

    public final String LOG_TAG = PatternizerSyncAdapter.class.getSimpleName();
    public static final String ACTION_DATA_UPDATED = "com.sohail.patternizer.ACTION_DATA_UPDATED";


    // Interval at which to sync, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private List<PatternsModel> patternList;
    private static int resultOffset = 0;
    private static int numResults = 51;



    public PatternizerSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Log.d(LOG_TAG, "Starting sync");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.colourlovers.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ColourloversService service = retrofit.create(ColourloversService.class);

        try {
            Call<List<PatternsModel>> call = service.getTopRatedPatterns(resultOffset, numResults);
            patternList = call.execute().body();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the new information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(patternList.size());

        for (int i = 0; i < patternList.size(); i++) {
            int patternId = patternList.get(i).getId();
            String title = patternList.get(i).getTitle();
            String imageUrl = patternList.get(i).getImageUrl();
            String userName = patternList.get(i).getUserName();
            double likes = patternList.get(i).getNumVotes();
            int views = patternList.get(i).getNumViews();
            String apiUrl = patternList.get(i).getApiUrl();

            ContentValues patternValues = new ContentValues();

            patternValues.put(Patternizer_provider.PatternColumns.PATTERN_ID, patternId);
            patternValues.put(Patternizer_provider.PatternColumns.TITLE, title);
            patternValues.put(Patternizer_provider.PatternColumns.IMAGE_URL, imageUrl);
            patternValues.put(Patternizer_provider.PatternColumns.USER_NAME, userName);
            patternValues.put(Patternizer_provider.PatternColumns.LIKES, likes);
            patternValues.put(Patternizer_provider.PatternColumns.VIEWS, views);
            patternValues.put(Patternizer_provider.PatternColumns.API_URL, apiUrl);

            cVVector.add(patternValues);
        }
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            getContext().getContentResolver().delete(Patternizer_provider.Patterns.CONTENT_URI, null, null);
            getContext().getContentResolver().bulkInsert(Patternizer_provider.Patterns.CONTENT_URI, cvArray);
            updateWidgets();
        }
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));


        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        PatternizerSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    private void updateWidgets() {
        Context context = getContext();
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }



}
