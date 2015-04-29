package spqrlol.exproj;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilaforces1 on 4/29/15.
 */

// Extends dataStream.java
// Basically creates the URL for the search and looks to see if the items searched need ALL of the tags
// or just ANY of the tags.

public class GetFlickrJsonData extends dataStream {

    private String LOG_TAG = GetFlickrJsonData.class.getSimpleName();
    private List<Photo> Photos;
    private Uri destinationUri;

    public GetFlickrJsonData(String searchCriteria, boolean matchAll){

        super(null);
        createAndUpdateUri(searchCriteria, matchAll);
        Photos = new ArrayList<Photo>();
    }

    public void execute() {

        super.setRawURL(destinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Built URI = " + destinationUri.toString());
        downloadJsonData.execute(destinationUri.toString());
    }

    public boolean createAndUpdateUri(String searchCriteria, Boolean matchAll){

        final String FLICKER_API_BASE_URL = "https://api.flickr.com/services/feeds/photos_public.gne";
        final String TAGS_PARAM = "tags";
        final String TAGMODE_PARAM = "tagmode";
        final String FORMAT_PARAM = "format";
        final String NO_JSON_CALLBACK_PARAM = "nojsoncallback";

        destinationUri = Uri.parse(FLICKER_API_BASE_URL).buildUpon()
                .appendQueryParameter(TAGS_PARAM, searchCriteria)

                // If the boolean matchAll is true, send "ALL" and if false send "ANY"
                .appendQueryParameter(TAGMODE_PARAM, matchAll ? "ALL" : "ANY")

                // Can hard code these last two as they are constant
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(NO_JSON_CALLBACK_PARAM, "1")
                .build();

        return destinationUri != null;

    }

    // Processing parts of the JSON data
    public void processResult(){
        if(getStatus() != Status.OK){
            Log.e(LOG_TAG, "Error downloading raw file ");
            return;
        }

        final String FLICKR_ITEMS = "items";
        final String FLICKR_TITLE = "title";
        final String FLICKR_MEDIA = "media";
        final String FLICKR_PHOTO_URL = "m";
        final String FLICKR_AUTHOR = "author";
        final String FLICKR_AUTHOR_ID = "author_id";
        final String FLICKR_LINK = "link";
        final String FLICKR_TAGS = "tags";

        try{

            JSONObject jsonData = new JSONObject(getData());
            JSONArray itemsArray = jsonData.getJSONArray(FLICKR_ITEMS);
            for(int i=0; i<itemsArray.length(); i++){
                JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                String title = jsonPhoto.getString(FLICKR_TITLE);
                String author = jsonPhoto.getString(FLICKR_AUTHOR);
                String authorId = jsonPhoto.getString(FLICKR_AUTHOR_ID);
                String link = jsonPhoto.getString(FLICKR_LINK);
                String tags = jsonPhoto.getString(FLICKR_TAGS);

                JSONObject jsonMedia = jsonPhoto.getJSONObject(FLICKR_MEDIA);
                String photoUrl = jsonMedia.getString(FLICKR_PHOTO_URL);

                // Finally created the object with the JSON traits from the raw data
                Photo photoObject = new Photo(title, author, authorId, link, tags, photoUrl);

                this.Photos.add(photoObject);

            }

            for(Photo singlePhoto:Photos){
                Log.v(LOG_TAG, singlePhoto.toString());
            }

        } catch(JSONException jsone){
            jsone.printStackTrace();
            Log.e(LOG_TAG, "Error processing JSON data");
        }




    }

    public class DownloadJsonData extends RawData{

        // Once finished downloading, we will execute the standard code existing in dataStream
        // Then this will do the JSON file processing needed for the recently downloaded raw file of data
        protected void onPostExecute(String webData){
            super.onPostExecute(webData);
            processResult();
        }

        // Downloads the raw data first and saves for processing
        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }

    }

}
