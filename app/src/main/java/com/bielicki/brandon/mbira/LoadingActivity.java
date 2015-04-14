package com.bielicki.brandon.mbira;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.IOException;


public class LoadingActivity extends Activity {
    AppData project;

    @Override
    protected void onStop() {
        super.onStop();
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        project = AppData.get();

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        project.username = sharedPref.getString("USERNAME",null);
        project.password = sharedPref.getString("PASSWORD",null);

        if(project.username != null){
            Log.d("USERNAME", project.username);
            Log.d("PASSWORD", project.password);
        }

        Constants constants = Constants.get();
        constants.PROJECT_ID = this.getString(R.string.project_id);
        constants.BASE_PATH = this.getString(R.string.base_path);
        constants.WEBSERVICE = this.getString(R.string.webservice);

        try {
            project.httpCacheDir = new File(getExternalCacheDir(), "http");
            project.httpCacheSize = 10 * 1024 * 1024;
            HttpResponseCache.install(project.httpCacheDir, project.httpCacheSize);
        } catch (IOException e) {
            Log.d("HTTPCACHE_FAILED", "HTTP response cache installation failed:" + e);
        }







        if(project.isSet == false) {
            File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
            // Create global configuration and initialize ImageLoader with this config
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                    .defaultDisplayImageOptions(defaultOptions)
                    .diskCache(new UnlimitedDiscCache(cacheDir))
                    .build();
            ImageLoader.getInstance().init(config);

            new SetProjectData().execute(this);
            new SetLocationData().execute();
            new SetAreaData().execute();
            new SetExhibitData().execute();
            new SetExplorationData().execute();

        }
        else {
            doneLoading();
        }

    }

    public void doneLoading() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

}
