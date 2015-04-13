package com.bielicki.brandon.mbira;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandon on 4/6/2015.
 */
public class SetNewUser extends AsyncTask {
    Constants constants;
    private PostNewUser client;

    public SetNewUser(PostNewUser client) {
        this.client = client;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        //Log.d("user555", params[0].toString());
        //Log.d("password555",params[1].toString());

        constants = Constants.get();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(constants.WEBSERVICE);
        String text = "";
        //SignInActivity activity;



        while(!isCancelled()){
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("query_type", "new_user"));
                nameValuePairs.add(new BasicNameValuePair("username", params[0].toString()));
                nameValuePairs.add(new BasicNameValuePair("firstName", params[1].toString()));
                nameValuePairs.add(new BasicNameValuePair("lastName", params[2].toString()));
                nameValuePairs.add(new BasicNameValuePair("email", params[3].toString()));
                nameValuePairs.add(new BasicNameValuePair("passwordOne", sha256(params[4].toString())));
                nameValuePairs.add(new BasicNameValuePair("passwordTwo", sha256(params[5].toString())));
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

            Log.d("NewUserResponse", text);

            return text;
        }

        return null;

    }

    @Override
    protected void onPostExecute(Object text) {
        client.postNewUser(text);
    }

    public String sha256(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA256");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public interface PostNewUser {
        void postNewUser(Object text);
    }
}
