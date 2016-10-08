package com.sohail.patternizer.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SOHAIL on 05/10/16.
 */
public class Author_model implements Parcelable {

    private String userName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String url;

    public Author_model() {
    }

    protected Author_model(Parcel in) {
        this.userName = in.readString();
        this.url = in.readString();

    }

    public static final Creator<Author_model> CREATOR = new Creator<Author_model>() {
        @Override
        public Author_model createFromParcel(Parcel in) {
            return new Author_model(in);
        }

        @Override
        public Author_model[] newArray(int size) {
            return new Author_model[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {

        dest.writeString(this.userName);
        dest.writeString(this.url);
    }
}
