package com.bielicki.brandon.mbira;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;


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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SetProjectData extends AsyncTask<LoadingActivity,Void,AppData> {
    AppData project;
    LoadingActivity loading;
    Constants constants;


    @Override
    protected AppData doInBackground(LoadingActivity... x) {
        constants = Constants.get();
        loading = x[0];
        project = AppData.get();
        String text = "";



        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(constants.WEBSERVICE);

            conn = (HttpURLConnection)url.openConnection();
            //conn.addRequestProperty("Cache-Control", "only-if-cached");
            conn.setUseCaches(true);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            String urlParameters = "query_type=project&projectID=" + constants.PROJECT_ID;

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("query_type", "project"));
            nameValuePairs.add(new BasicNameValuePair("projectID", constants.PROJECT_ID));

            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(urlParameters);
            out.flush();
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            text = response.toString();
            Log.d("HTTPRESPONSE",response.toString());
        } catch(MalformedURLException e) {

        } catch(IOException e) {

        }

        /*HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(constants.WEBSERVICE);


        try { //Try to make connection and return string value
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("query_type", "project"));
            nameValuePairs.add(new BasicNameValuePair("projectID", constants.PROJECT_ID));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
            Log.v("Post Status", "code: " + response.getStatusLine().getStatusCode());
            InputStream inputStream = response.getEntity().getContent();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(20);

            int current = 0;

            while ((current = bufferedInputStream.read()) != -1) {
                byteArrayBuffer.append((byte) current);
            }
            text = new String(byteArrayBuffer.toByteArray());
        } catch(ClientProtocolException e) {
            Log.d("ClientProtocol","Nope"+e);
        } catch (IOException e) {
            Log.d("IOException","Nope"+e);
        }*/

        String fileName = "";
        try {
            JSONObject projectInfo = new JSONObject(text);
            try {
                project.setProjectDescription(projectInfo.getString("description"));
            } catch (JSONException e) {
                project.setProjectDescription("");
            }
            try {
                project.setProjectName(projectInfo.getString("name"));
            } catch (JSONException e) {
                project.setProjectName("");
            }
            try {
                fileName = projectInfo.getString("image_path");
                //Bitmap img = ImageLoader.getInstance().loadImageSync(constants.BASE_PATH + "/images/" + fileName);
                project.setProjectImageUrl(constants.BASE_PATH + "/images/" + fileName);
            } catch (JSONException e) {
                //no project image due to JSON failure
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return project;
    }

    protected void onPostExecute(AppData result) {
        loading.doneLoading();
    }
}
