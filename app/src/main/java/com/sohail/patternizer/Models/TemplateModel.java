package com.sohail.patternizer.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SOHAIL on 05/10/16.
 */
public class TemplateModel implements Parcelable {

    private String title;
    private String url;
    private Author_model author;

    public Author_model getAuthor() {
        return author;
    }

    public void setAuthor(Author_model author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TemplateModel() {
    }

    protected TemplateModel(Parcel in) {
        this.title = in.readString();
        this.url = in.readString();
        this.author = in.readParcelable(Author_model.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeParcelable(this.author, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TemplateModel> CREATOR = new Parcelable.Creator<TemplateModel>() {
        @Override
        public TemplateModel createFromParcel(Parcel in) {
            return new TemplateModel(in);
        }

        @Override
        public TemplateModel[] newArray(int size) {
            return new TemplateModel[size];
        }
    };
}
