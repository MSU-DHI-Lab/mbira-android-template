package com.bielicki.brandon.mbira;

import android.graphics.Bitmap;
import android.graphics.PointF;
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
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandon on 3/3/2015.
 */
public class SetAreaData extends AsyncTask<Void,Void,AppData> {
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
            nameValuePairs.add(new BasicNameValuePair("query_type", "areas"));
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
            Log.d("ClientProtocol","Nope"+e);
        }
        catch (IOException e) {
            Log.d("IOException","Nope"+e);
        }

        try {
            JSONObject areaObj = new JSONObject(text);
            JSONArray areas = areaObj.getJSONArray("item");
            for (int y = 0; y < areas.length(); y++ ) {
                Area area = new Area();
                //build location object
                area.id = areas.getJSONObject(y).getInt("id");
                try {
                    area.project_id = areas.getJSONObject(y).getInt("project_id");
                } catch (JSONException e) {
                    area.project_id = -1;
                }
                try {
                    area.exhibit_id = areas.getJSONObject(y).getInt("exhibit_id");
                } catch (JSONException e) {
                    area.exhibit_id = -1;
                }
                try {
                    area.name = areas.getJSONObject(y).getString("name");
                } catch (JSONException e) {
                    area.name = "";
                }
                try {
                    area.description = areas.getJSONObject(y).getString("description");
                } catch (JSONException e) {
                    area.description = "";
                }
                try {
                    area.dig_deeper = areas.getJSONObject(y).getString("dig_deeper");
                } catch (JSONException e) {
                    area.dig_deeper = "";
                }

                try {
                    ArrayList<Coordinate> coordinatesArray = new ArrayList<Coordinate>();
                    //coordinates
                    JSONArray coords = areas.getJSONObject(y).getJSONArray("coordinates");
                    for (int j = 0; j < coords.length(); j++ ) {
                        Coordinate coord = new Coordinate();
                        coord.x = coords.getJSONObject(j).getDouble("x");
                        coord.y = coords.getJSONObject(j).getDouble("y");
                        coordinatesArray.add(coord);
                    }
                    area.coordinates = coordinatesArray;

                } catch (JSONException e) {
                    //default coordinates
                }

                try {
                    area.radius = (float) areas.getJSONObject(y).getDouble("radius");
                } catch (JSONException e) {
                    area.radius = Float.NaN;
                }
                try {
                    area.shape = areas.getJSONObject(y).getString("shape");
                } catch (JSONException e) {
                    area.shape = "";
                }
                try {
                    area.toggle_dig_deeper = areas.getJSONObject(y).getBoolean("toggle_dig_deeper");
                } catch (JSONException e) {
                    area.toggle_dig_deeper = false;
                }
                try {
                    area.toggle_media = areas.getJSONObject(y).getBoolean("toggle_media");
                } catch (JSONException e) {
                    area.toggle_media = false;
                }
                try {
                    area.toggle_comments = areas.getJSONObject(y).getBoolean("toggle_comments");
                } catch (JSONException e) {
                    area.toggle_comments = false;
                }
                try {
                    area.thumb_path = areas.getJSONObject(y).getString("thumb_path");
                    Bitmap img = ImageLoader.getInstance().loadImageSync(constants.BASE_PATH + "/images/" + area.thumb_path);
                    area.areaImage = img;
                } catch (JSONException e) {
                    //no image set default
                }

                try {
                    ArrayList<Bitmap> imageArray = new ArrayList<Bitmap>();
                    JSONArray paths = areas.getJSONObject(y).getJSONArray("media");
                    for (int j = 0; j < paths.length(); j++ ) {
                        String path = paths.getJSONObject(j).getString("path");
                        Bitmap img = ImageLoader.getInstance().loadImageSync(constants.BASE_PATH + "/images/" + path);
                        imageArray.add(img);
                    }
                    area.media = imageArray;
                } catch(JSONException e) {

                }

                project.addArea(area);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return project;
    }

    protected void onPostExecute(AppData result) {

    }
}
