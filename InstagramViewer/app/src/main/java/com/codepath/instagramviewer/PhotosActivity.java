package com.codepath.instagramviewer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "a3d57a672a1d4e7b8709044c1fa932e9";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotoAdapter aPhotos;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        fetchPopularPhotos();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    private void fetchPopularPhotos()
    {
        photos = new ArrayList<InstagramPhoto>();
        // Create Adapter and bind it to data in Arraylist
        aPhotos = new InstagramPhotoAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
        
        //Set up popular url endpoint
        final String popularUrl = "https://api.instagram.com/v1/media/popular?client_id="+CLIENT_ID;
        //Create Network client
        AsyncHttpClient client = new AsyncHttpClient();
        //Trigger network request
        client.get(popularUrl, new JsonHttpResponseHandler() {
            //define success and failure callbacks
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("INFO",response.toString());
                JSONArray photosJson = null;
                try {
                    photos.clear();
                    photosJson = response.getJSONArray("data");
                    for (int i=0; i<photosJson.length();i++) {
                        JSONObject photoJson = photosJson.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        if (photoJson.getJSONObject("user") != null) {
                            photo.username = photoJson.getJSONObject("user").getString("username");
                            photo.userImage = photoJson.getJSONObject("user").getString("profile_picture");
                        }
                        if (photoJson.getJSONObject("caption") != null) {
                            photo.caption = photoJson.getJSONObject("caption").getString("text");
                            photo.createdDate = photoJson.getJSONObject("caption").getLong("created_time");
                        }
                        if (photoJson.getJSONObject("images") != null) {
                            photo.imageUrl = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                            photo.imageHeight = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        }
                        if (photoJson.getJSONObject("likes") != null) {
                            photo.likesCount = photoJson.getJSONObject("likes").getInt("count");
                        }
                        photos.add(photo);
                    }
                    //Notify adapter to populate new changes into list view
                    aPhotos.notifyDataSetChanged();
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
        //Handle successful response
    }
}
