package com.sohail.patternizer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sohail.patternizer.data.Patternizer_provider;
import com.sohail.patternizer.Detail_Activity;
import com.sohail.patternizer.Detail_Activity_Fragment;
import com.sohail.patternizer.Models.PatternsModel;
import com.sohail.patternizer.R;
import com.sohail.patternizer.view.MyImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SOHAIL on 05/10/16.
 */
public class ImageGridAdapter extends CursorAdapter<RecyclerView.ViewHolder> {

    Context context;
    private boolean mTwoPane;
//
//    @Bind(R.id.imgTitle)
//    TextView imgTitle;




    public ImageGridAdapter(Context context, Cursor cursor, boolean twoPaneView) {
        super(cursor);
        this.context = context;
        this.mTwoPane = twoPaneView;
    }

    @Override
    public void onBindViewHolderCursor(RecyclerView.ViewHolder holder, Cursor cursor) {
        ((NormalViewHolder) holder).bindView(cursor);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_view, parent, false);
        return new NormalViewHolder(itemView);
    }


    class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.imgTitle)
        TextView imgTitle;

        @Bind(R.id.imageView)
        MyImageView imageView;

        PatternsModel mItem;


        public NormalViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(Cursor cursor) {

            mItem = PatternsModel.fromCursor(cursor);
            String imageUrl = cursor.getString(cursor.getColumnIndex(Patternizer_provider.PatternColumns.IMAGE_URL));
            Picasso.with(context).load(imageUrl).into((Target) imageView);
            String titleTxt=cursor.getString(cursor.getColumnIndex(Patternizer_provider.PatternColumns.TITLE));
            imgTitle.setText(titleTxt);

        }


        @Override
        public void onClick(View v) {
            if (mTwoPane) {

                Bundle arguments = new Bundle();
                arguments.putParcelable(Detail_Activity_Fragment.ARG_ITEM, mItem);
                Detail_Activity_Fragment fragment = new Detail_Activity_Fragment();
                fragment.setArguments(arguments);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Context context = v.getContext();
                Intent intent = new Intent(context, Detail_Activity.class);
                intent.putExtra(Detail_Activity_Fragment.ARG_ITEM, mItem);

                context.startActivity(intent);
            }

        }
    }

}

