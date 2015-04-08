package com.bielicki.brandon.mbira;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.IOException;


public class LoadingActivity extends Activity {
    AppData project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        project = AppData.get();


        if(project.isSet == false) {
            // Create global configuration and initialize ImageLoader with this config
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                    .defaultDisplayImageOptions(defaultOptions)
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
