package com.example.pratim.flicker;

import java.io.Serializable;

/**
 * Created by pratim on 12-06-2016.
 */
public class Photo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String mTitle;
    private String mAuthor;
    private String mAuthorID;
    private String mLink;
    private String mTags;
    private String mImage;

    public Photo(String mTitle, String mAuthor, String mAuthorID, String mLink, String mTags, String mImage) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mAuthorID = mAuthorID;
        this.mLink = mLink;
        this.mTags = mTags;
        this.mImage = mImage;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getAuthorID() {
        return mAuthorID;
    }

    public String getLink() {
        return mLink;
    }

    public String getTags() {
        return mTags;
    }

    public String getImage() {
        return mImage;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mTitle='" + mTitle + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mAuthorID='" + mAuthorID + '\'' +
                ", mLink='" + mLink + '\'' +
                ", mTags='" + mTags + '\'' +
                ", mImage='" + mImage + '\'' +
                '}';
    }
}
