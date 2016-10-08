package com.sohail.patternizer.Models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.sohail.patternizer.data.Patternizer_provider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SOHAIL on 05/10/16.
 */
public class PatternsModel implements Parcelable {
    private Integer id;
    private String title;
    private String userName;
    private Integer numViews;
    private Integer numVotes;
    private Integer numComments;
    private Double numHearts;
    private Integer rank;
    private String dateCreated;
    private List<String> colors = new ArrayList<String>();
    private String description;
    private String url;
    private String imageUrl;
    private String badgeUrl;
    private String apiUrl;
    private TemplateModel template;


    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public String getBadgeUrl() {
        return badgeUrl;
    }

    public void setBadgeUrl(String badgeUrl) {
        this.badgeUrl = badgeUrl;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getNumComments() {
        return numComments;
    }

    public void setNumComments(Integer numComments) {
        this.numComments = numComments;
    }

    public Double getNumHearts() {
        return numHearts;
    }

    public void setNumHearts(Double numHearts) {
        this.numHearts = numHearts;
    }

    public Integer getNumViews() {
        return numViews;
    }

    public void setNumViews(Integer numViews) {
        this.numViews = numViews;
    }

    public Integer getNumVotes() {
        return numVotes;
    }

    public void setNumVotes(Integer numVotes) {
        this.numVotes = numVotes;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public TemplateModel getTemplate() {
        return template;
    }

    public void setTemplate(TemplateModel template) {
        this.template = template;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public static final Creator<PatternsModel> CREATOR = new Parcelable.Creator<PatternsModel>() {
        @Override
        public PatternsModel createFromParcel(Parcel in) {
            return new PatternsModel(in);
        }

        @Override
        public PatternsModel[] newArray(int size) {
            return new PatternsModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.userName);
        dest.writeValue(this.numViews);
        dest.writeValue(this.numVotes);
        dest.writeValue(this.numComments);
        dest.writeValue(this.numHearts);
        dest.writeValue(this.rank);
        dest.writeString(this.dateCreated);
        dest.writeStringList(this.colors);
        dest.writeString(this.description);
        dest.writeString(this.url);
        dest.writeString(this.imageUrl);
        dest.writeString(this.badgeUrl);
        dest.writeString(this.apiUrl);
        dest.writeParcelable(this.template, flags);
    }

    public PatternsModel(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
        this.userName = in.readString();
        this.numViews = (Integer) in.readValue(Integer.class.getClassLoader());
        this.numVotes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.numComments = (Integer) in.readValue(Integer.class.getClassLoader());
        this.numHearts = (Double) in.readValue(Double.class.getClassLoader());
        this.rank = (Integer) in.readValue(Integer.class.getClassLoader());
        this.dateCreated = in.readString();
        this.colors = in.createStringArrayList();
        this.description = in.readString();
        this.url = in.readString();
        this.imageUrl = in.readString();
        this.badgeUrl = in.readString();
        this.apiUrl = in.readString();
        this.template = in.readParcelable(TemplateModel.class.getClassLoader());
    }

    public PatternsModel() {
    }

    public static PatternsModel fromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Patternizer_provider.PatternColumns.PATTERN_ID));
        String title = cursor.getString(cursor.getColumnIndex(Patternizer_provider.PatternColumns.TITLE));
        String imageUrl = cursor.getString(cursor.getColumnIndex(Patternizer_provider.PatternColumns.IMAGE_URL));
        String userName = cursor.getString(cursor.getColumnIndex(Patternizer_provider.PatternColumns.USER_NAME));
        int likes = cursor.getInt(cursor.getColumnIndex(Patternizer_provider.PatternColumns.LIKES));
        int views = cursor.getInt(cursor.getColumnIndex(Patternizer_provider.PatternColumns.VIEWS));
        String apiUrl = cursor.getString(cursor.getColumnIndex(Patternizer_provider.PatternColumns.API_URL));

        return new Builder()
                .id(id)
                .imageUrl(imageUrl)
                .title(title)
                .userName(userName)
                .likes(likes)
                .views(views)
                .apiUrl(apiUrl)
                .build();
    }

    public static class Builder {
        private PatternsModel mPatternModel;

        public Builder() {
            mPatternModel = new PatternsModel();
        }

        public Builder id(int id) {
            mPatternModel.setId(id);
            return this;
        }

        public Builder title(String title) {
            mPatternModel.setTitle(title);
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            mPatternModel.setImageUrl(imageUrl);
            return this;
        }

        public Builder userName(String userName) {
            mPatternModel.setUserName(userName);
            return this;
        }

        public Builder likes(int likes) {
            mPatternModel.setNumVotes(likes);
            return this;
        }

        public Builder views(int views) {
            mPatternModel.setNumViews(views);
            return this;
        }

        public Builder apiUrl(String apiUrl) {
            mPatternModel.setApiUrl(apiUrl);
            return this;
        }

        public PatternsModel build() {
            return mPatternModel;
        }
    }
}
