package com.bielicki.brandon.mbira;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Random;


public class RandomActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        AppData project = AppData.get();

        Random r = new Random();
        Integer min = 0;
        Integer max = 1;
        Integer i = r.nextInt(max - min + 1) + min;

        // Location Chosen
        if (i.equals(1)) {
            max = project.getLocationArrayList().size() - 1;
            i = r.nextInt(max - min + 1) + min;
            Intent intent = new Intent(this, SingleLocation.class);
            Bundle bundle = new Bundle();
            bundle.putDouble("Latitude", project.getLocationArrayList().get(i).latitude);
            bundle.putDouble("Longitude", project.getLocationArrayList().get(i).longitude);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        //Area Chosen
        else {
            max = project.getAreaArrayList().size() - 1;
            i = r.nextInt(max - min + 1) + min;
            Intent intent = new Intent(this, SingleLocation.class);
            Bundle bundle = new Bundle();
            bundle.putDouble("Latitude", project.getAreaArrayList().get(i).coordinates.get(0).getX());
            bundle.putDouble("Longitude", project.getAreaArrayList().get(i).coordinates.get(0).getY());
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

}
