package com.example.pratim.flicker;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pratim on 12-06-2016.
 */
public class GetFlickrJsonData extends GetRawData {

    private String LOG_TAG = GetFlickrJsonData.class.getSimpleName();
    private List<Photo> mPhotos;
    private Uri mDestinationUri;

    public GetFlickrJsonData(String searchCriteria, Boolean matchAll) {       //If matchAll = TRUE, assume tagmode = All else tagmode = ANY.
        super(null);        //Since we didn't get the URL yet, initialize with null
        createAndUpdateUri(searchCriteria, matchAll);
        mPhotos = new ArrayList<Photo>();
    }

    public void execute(){
        super.setmRawUrl(mDestinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Built URI = "+mDestinationUri.toString());
        downloadJsonData.execute(mDestinationUri.toString());
    }

    //To construct the URL we need to download
    public boolean createAndUpdateUri(String searchCriteria, Boolean matchAll){
        final String FLICKR_API_BASE_URL = "https://api.flickr.com/services/feeds/photos_public.gne";
        final String TAGS_PARAM = "tags";
        final String TAGMODE_PARAM = "tagmode";
        final String FORMAT_PARAM = "format";
        final String NO_JSON_CALL_BACK_PARAM = "nojsoncallback";

        //Construct the URL
        mDestinationUri = Uri.parse(FLICKR_API_BASE_URL).buildUpon()
                .appendQueryParameter(TAGS_PARAM,searchCriteria)
                .appendQueryParameter(TAGMODE_PARAM, matchAll ? "ALL" : "ANY")
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(NO_JSON_CALL_BACK_PARAM,"1")
                .build();

        return mDestinationUri != null;     //Return True if the URL is valid
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public void processResult(){
        if (getmDownloadStatus() != DownloadStatus.OK){
            Log.e(LOG_TAG, "Error downloading Raw File");
            return;
        }
        final String FLICKR_ITEMS = "items";
        final String FLICKR_TITLE = "title";
        final String FLICKR_MEDIA = "media";
        final String FLICKR_PHOTO_URL = "m";
        final String FLICKR_AUTHOR = "author";
        final String FLICKR_AUTHOR_ID = "author_id";
        final String FLICKR_TAGS = "tags";
        final String FLICKR_LINK = "link";

        //Capturing the data
        try{
            JSONObject jsonData = new JSONObject(getmData());
            JSONArray itemsArray = jsonData.getJSONArray(FLICKR_ITEMS);
            for(int i=0; i<itemsArray.length();i++){
                JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                String title = jsonPhoto.getString(FLICKR_TITLE);
                String author = jsonPhoto.getString(FLICKR_AUTHOR);
                String authorId = jsonPhoto.getString(FLICKR_AUTHOR_ID);
                //String link = jsonPhoto.getString(FLICKR_LINK);x
                String tags = jsonPhoto.getString(FLICKR_TAGS);

                JSONObject jsonMedia = jsonPhoto.getJSONObject(FLICKR_MEDIA);
                String photoURL = jsonMedia.getString(FLICKR_PHOTO_URL);
                String link = photoURL.replaceFirst("_m.","_b.");

                Photo photoObject = new Photo(title,author,authorId,link,tags,photoURL);
                this.mPhotos.add(photoObject);
            }
            for(Photo singlePhoto: mPhotos){
                Log.v(LOG_TAG,singlePhoto.toString());
            }
        }catch (JSONException jsone){
            jsone.printStackTrace();

            Log.e(LOG_TAG,"Error processing Json Data");
        }
    }

    public class DownloadJsonData extends DownloadData{
        @Override
        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            processResult();
        }

        @Override
        protected String doInBackground(String... params) {
            String[] par = {mDestinationUri.toString()};
            return super.doInBackground(par);
        }
    }
}
