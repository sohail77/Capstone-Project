package com.sohail.patternizer;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sohail.patternizer.Adapters.ImageGridAdapter;
import com.sohail.patternizer.data.Patternizer_provider;
import com.sohail.patternizer.sync.PatternizerSyncAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    public static final String[] PROJECTION = new String[]{
            Patternizer_provider.PatternColumns._ID,
            Patternizer_provider.PatternColumns.PATTERN_ID,
            Patternizer_provider.PatternColumns.TITLE,
            Patternizer_provider.PatternColumns.IMAGE_URL,
            Patternizer_provider.PatternColumns.USER_NAME,
            Patternizer_provider.PatternColumns.LIKES,
            Patternizer_provider.PatternColumns.VIEWS,
            Patternizer_provider.PatternColumns.API_URL
    };
    private static final int LOADER_PATTERNS = 20;

    @Bind(R.id.grid_recycler_view)
    RecyclerView mRecyclerView;


    private ImageGridAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View recyclerView = findViewById(R.id.grid_recycler_view);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        PatternizerSyncAdapter.syncImmediately(this);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        if (mAdView != null) {
            mAdView.loadAd(adRequest);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().initLoader(LOADER_PATTERNS,null,this);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                }
                return 1;
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);

    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,Patternizer_provider.Patterns.CONTENT_URI,PROJECTION,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(mAdapter!=null && mAdapter.getCursor()!=null){
            mAdapter.swapCursor(cursor);

        }else {
            mAdapter=new ImageGridAdapter(this,cursor,mTwoPane);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
