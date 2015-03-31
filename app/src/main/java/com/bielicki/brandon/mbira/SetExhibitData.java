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

/**
 * Created by Brandon on 3/3/2015.
 */
public class SetExhibitData extends AsyncTask<Void,Void,AppData> {
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
            nameValuePairs.add(new BasicNameValuePair("query_type", "exhibits"));
            nameValuePairs.add(new BasicNameValuePair("projectID", constants.PROJECT_ID));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
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
            Log.d("ClientProtocol", "Nope" + e);
        }
        catch (IOException e) {
            Log.d("IOException","Nope"+e);
        }

        try {
            JSONObject exhibitObj = new JSONObject(text);
            JSONArray exhibits = exhibitObj.getJSONArray("item");
            for (int y = 0; y < exhibits.length(); y++ ) {
                Exhibit exhibit = new Exhibit();
                //build Exhibit object
                exhibit.id = exhibits.getJSONObject(y).getInt("id");
                try {
                    exhibit.project_id = exhibits.getJSONObject(y).getInt("project_id");
                } catch (JSONException e) {
                    exhibit.project_id = -1;
                }
                try {
                    exhibit.pid = exhibits.getJSONObject(y).getInt("pid");
                } catch (JSONException e) {
                    exhibit.pid = -1;
                }
                try {
                    exhibit.name = exhibits.getJSONObject(y).getString("name");
                } catch (JSONException e) {
                    exhibit.name = "";
                }
                try {
                    exhibit.description = exhibits.getJSONObject(y).getString("description");
                } catch (JSONException e) {
                    exhibit.description = "";
                }
                try {
                    exhibit.thumb_path = exhibits.getJSONObject(y).getString("thumb_path");
                    Bitmap img = ImageLoader.getInstance().loadImageSync(constants.BASE_PATH + "/images/" + exhibit.thumb_path);
                    exhibit.exhibitImage = img;
                } catch (JSONException e) {
                    //no image set default
                }
                try {
                    JSONArray locations = exhibits.getJSONObject(y).getJSONArray("locations");
                    for (int j = 0; j < locations.length(); j++ ) {
                        int item = locations.getJSONObject(j).getInt("ID");
                        exhibit.addLocation(project.getLocationById(item));
                    }
                } catch (JSONException e) {
                    //default directions array
                }
                try {
                    JSONArray locations = exhibits.getJSONObject(y).getJSONArray("areas");
                    for (int j = 0; j < locations.length(); j++ ) {
                        int item = locations.getJSONObject(j).getInt("ID");
                        exhibit.addArea(project.getAreaById(item));
                    }
                } catch (JSONException e) {
                    //default directions array
                }

                project.addExhibit(exhibit);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return project;
    }

    protected void onPostExecute(AppData result) {

    }
}