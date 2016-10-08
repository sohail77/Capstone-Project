package com.sohail.patternizer;

import android.annotation.TargetApi;
import android.support.v4.app.Fragment;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.sohail.patternizer.Models.PatternsModel;
import com.sohail.patternizer.view.MyImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Detail_Activity_Fragment extends Fragment{
    public static final String ARG_ITEM = "item";

    @Bind(R.id.titleTextView)
    TextView titleTextView;

    @Bind(R.id.nameTextView)
    TextView nameTextView;

    @Bind(R.id.likesTextView)
    TextView likesTextView;

    @Bind(R.id.viewsTextView)
    TextView viewsTextView;

    @Bind(R.id.shareImageView)
    ImageView shareImageView;

    private PatternsModel mItem;

    @Bind(R.id.backdrop)
    MyImageView backdropImageView;

    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

    @Bind(R.id.detail_toolbar)
    Toolbar toolbar;
    private Tracker mTracker;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Detail_Activity_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM)) {
            mItem = getArguments().getParcelable(ARG_ITEM);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail__activity_, container, false);
        ButterKnife.bind(this, rootView);
        initUI();
        //trackScreen();
        return rootView;
    }

//    private void trackScreen() {
//        if (mItem != null) {
//            BaseApplication application = (BaseApplication) getActivity().getApplication();
//            mTracker = application.getDefaultTracker();
//            mTracker.setScreenName("Title: " + mItem.getTitle());
//            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
//        }
//    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initUI() {
        if (mItem != null) {
            Picasso.with(getContext()).load(mItem.getImageUrl()).into((Target) backdropImageView);
            titleTextView.setText(mItem.getTitle());
            String userName = getString(R.string.by) + " " + mItem.getUserName();
            nameTextView.setText(userName);
            likesTextView.setText("" + mItem.getNumVotes());
            viewsTextView.setText("" + mItem.getNumViews());
        }

    }

    @OnClick(R.id.fab)
    void onFabClicked() {
        showSetWallpaperConfirmation();
    }

    @OnClick(R.id.shareImageView)
    void onShareClicked() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mItem.getApiUrl());
        startActivity(shareIntent);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share")
                .build());
    }

    void showSetWallpaperConfirmation() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setPositiveButton(getString(R.string.set_wallpaper), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setWallpaper();
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Action")
                                .setAction("Cancel Wallpaper: " + mItem.getTitle())
                                .build());
                    }
                }).setMessage(getString(R.string.set_wallpaper_msg)).create();
        alertDialog.show();

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setWallpaper() {
        Picasso.with(getContext()).load(mItem.getImageUrl()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (bitmap != null) {
                    BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
                    drawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

                    WallpaperManager wallpaperManager
                            = WallpaperManager.getInstance(getContext());
                    int widthPixels = wallpaperManager.getDesiredMinimumWidth();
                    int heightPixels = wallpaperManager.getDesiredMinimumHeight();
                    Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(mutableBitmap);
                    drawable.setBounds(0, 0, widthPixels, heightPixels);
                    drawable.draw(canvas);
                    try {
                        wallpaperManager.setBitmap(mutableBitmap);
                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Action")
                                .setAction("Set Wallpaper: " + mItem.getTitle())
                                .build());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!getResources().getBoolean(R.bool.isTablet)) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(getActivity(), new Intent(getActivity(), MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
