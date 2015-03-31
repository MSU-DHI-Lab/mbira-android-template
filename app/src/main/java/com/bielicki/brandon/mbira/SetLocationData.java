package com.bielicki.brandon.mbira;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;


import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SetLocationData extends AsyncTask<Void,Void,AppData> {
    AppData project;
    Constants constants;

    @Override
    protected AppData doInBackground(Void... x) {
        constants = Constants.get();
        project = AppData.get();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(constants.WEBSERVICE);
        String text = "";

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("query_type", "locations"));
            nameValuePairs.add(new BasicNameValuePair("projectID", constants.PROJECT_ID));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
            Log.v("Post Status" , "code: "+response.getStatusLine().getStatusCode());
            InputStream inputStream = response.getEntity().getContent();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(20);

            int current = 0;

            while((current = bufferedInputStream.read()) != -1) {
                byteArrayBuffer.append((byte)current);
            }
            text = new String(byteArrayBuffer.toByteArray());
        }
        catch(ClientProtocolException e) {
            Log.d("ClientProtocol","Nope"+e);
        }
        catch (IOException e) {
            Log.d("IOException","Nope"+e);
        }

        try {
            JSONObject locObj = new JSONObject(text);
            JSONArray locations = locObj.getJSONArray("item");
            for (int y = 0; y < locations.length(); y++ ) {
                Location location = new Location();
                //build location object
                location.id = locations.getJSONObject(y).getInt("id");
                location.project_id = locations.getJSONObject(y).getInt("project_id");
                location.exhibit_id = locations.getJSONObject(y).getInt("exhibit_id");
                location.name = locations.getJSONObject(y).getString("name");
                try {
                    location.pid = locations.getJSONObject(y).getInt("pid");
                } catch (JSONException e) {
                    location.pid = -1;
                }
                try {
                    location.sid = locations.getJSONObject(y).getInt("sid");
                } catch (JSONException e) {
                    location.sid = -1;
                }
                try {
                    location.description = locations.getJSONObject(y).getString("description");
                } catch (JSONException e) {
                    location.description = "";
                }
                try {
                    location.dig_deeper = locations.getJSONObject(y).getString("dig_deeper");
                } catch (JSONException e) {
                    location.dig_deeper = "";
                }
                try {
                    location.latitude = locations.getJSONObject(y).getDouble("latitude");
                } catch (JSONException e) {
                    location.latitude = Double.NaN;
                }
                try {
                    location.longitude = locations.getJSONObject(y).getDouble("longitude");
                } catch (JSONException e) {
                    location.longitude = Double.NaN;
                }
                try {
                    location.toggle_dig_deeper = locations.getJSONObject(y).getBoolean("toggle_dig_deeper");
                } catch (JSONException e) {
                    location.toggle_dig_deeper = false;
                }
                try {
                    location.toggle_media = locations.getJSONObject(y).getBoolean("toggle_media");
                } catch (JSONException e) {
                    location.toggle_media = false;
                }
                try {
                    location.toggle_comments = locations.getJSONObject(y).getBoolean("toggle_comments");
                } catch (JSONException e) {
                    location.toggle_comments = false;
                }
                try {
                    location.thumb_path = locations.getJSONObject(y).getString("thumb_path");
                    Bitmap img = ImageLoader.getInstance().loadImageSync(constants.BASE_PATH + "/images/" + location.thumb_path);
                    location.locationImage = img;
                } catch (JSONException e) {
                    //no location image default
                }
                try {
                    ArrayList<Bitmap> imageArray = new ArrayList<Bitmap>();
                    JSONArray paths = locations.getJSONObject(y).getJSONArray("media");
                    for (int j = 0; j < paths.length(); j++ ) {
                        String path = paths.getJSONObject(j).getString("path");
                        Bitmap img = ImageLoader.getInstance().loadImageSync(constants.BASE_PATH + "/images/" + path);
                        imageArray.add(img);
                    }
                    location.media = imageArray;
                } catch(JSONException e) {

                }

                project.addLocation(location);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return project;
    }

    protected void onPostExecute(AppData result) {

    }
}