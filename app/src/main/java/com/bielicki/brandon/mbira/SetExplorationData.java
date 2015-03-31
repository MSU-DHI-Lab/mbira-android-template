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
 * Created by Brandon on 3/12/2015.
 */
public class SetExplorationData extends AsyncTask<Void,Void,AppData> {
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
            nameValuePairs.add(new BasicNameValuePair("query_type", "explorations"));
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
            JSONObject explorationObj = new JSONObject(text);
            JSONArray explorations = explorationObj.getJSONArray("item");
            for (int y = 0; y < explorations.length(); y++ ) {
                Exploration exploration = new Exploration();
                //build Explorations object
                exploration.id = explorations.getJSONObject(y).getInt("id");
                try {
                    exploration.project_id = explorations.getJSONObject(y).getInt("project_id");
                } catch (JSONException e) {
                    exploration.project_id = -1;
                }
                try {
                    exploration.pid = explorations.getJSONObject(y).getInt("pid");
                } catch (JSONException e) {
                    exploration.pid = -1;
                }
                try {
                    exploration.name = explorations.getJSONObject(y).getString("name");
                } catch (JSONException e) {
                    exploration.name = "";
                }
                try {
                    exploration.description = explorations.getJSONObject(y).getString("description");
                } catch (JSONException e) {
                    exploration.description = "";
                }
                try {
                    ArrayList<String> directionsArray = new ArrayList<String>();
                    JSONArray directions = explorations.getJSONObject(y).getJSONArray("direction");
                    for (int j = 0; j < directions.length(); j++ ) {
                        String item = directions.getJSONObject(j).getString("item");
                        directionsArray.add(item);
                    }
                    exploration.direction = directionsArray;

                    for(int m = 0; m < directionsArray.size(); m++) {
                        String temp = directionsArray.get(m);
                        MapItem mapItem;
                        if(temp.indexOf('A') == -1) {
                            int itemInt = Integer.parseInt(temp);
                            mapItem = project.getLocationById(itemInt);
                        }
                        else {
                            //area
                            temp = temp.substring(1);
                            int itemInt = Integer.parseInt(temp);
                            mapItem = project.getAreaById(itemInt);
                        }
                        exploration.addMapItem(mapItem);
                    }

                } catch (JSONException e) {
                    //default directions array
                }

                try {
                    exploration.thumb_path = explorations.getJSONObject(y).getString("thumb_path");
                    Bitmap img = ImageLoader.getInstance().loadImageSync(constants.BASE_PATH + "/images/" + exploration.thumb_path);
                    exploration.explorationImage = img;
                } catch (JSONException e) {
                    //no image set default
                }
                try {
                    exploration.toggle_media = explorations.getJSONObject(y).getBoolean("toggle_media");
                } catch (JSONException e) {
                    exploration.toggle_media = false;
                }
                try {
                    exploration.toggle_comments = explorations.getJSONObject(y).getBoolean("toggle_comments");
                } catch (JSONException e) {
                    exploration.toggle_comments = false;
                }
                try {
                    ArrayList<Bitmap> imageArray = new ArrayList<Bitmap>();
                    JSONArray paths = explorations.getJSONObject(y).getJSONArray("media");
                    for (int j = 0; j < paths.length(); j++ ) {
                        String path = paths.getJSONObject(j).getString("path");
                        Bitmap img = ImageLoader.getInstance().loadImageSync(constants.BASE_PATH + "/images/" + path);
                        imageArray.add(img);
                    }
                    exploration.media = imageArray;
                } catch(JSONException e) {

                }

                project.addExploration(exploration);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return project;
    }

    protected void onPostExecute(AppData result) {

    }
}