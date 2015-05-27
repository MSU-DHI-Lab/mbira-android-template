package com.bielicki.brandon.mbira;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import com.nostra13.universalimageloader.core.ImageLoader;


public class MainActivity extends ActionBarActivity {
    private TextView projectDescription;
    private TextView projectTitle;
    private ImageView projectImageView;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawer;
    private Toolbar toolbar;
    private AppData project;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        project = AppData.get();
        project.isSet = true;

        toolbar = (Toolbar) findViewById(R.id.app_bar_transparent);
        //toolbar.setTitle(project.getProjectName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        projectTitle = (TextView) findViewById(R.id.projectTitle);
        projectImageView = (ImageView) findViewById(R.id.projectImageView);
        //projectImageView.setColorFilter(Color.parseColor("#253137"), PorterDuff.Mode.OVERLAY);
        projectDescription = (TextView) findViewById(R.id.projectDescription);

        //projectImageView.setImageBitmap(project.getProjectImage());
        ImageLoader.getInstance().displayImage(project.getProjectImageUrl(),projectImageView);
        projectDescription.setText(project.getProjectShortDescription());
        projectTitle.setText(project.getProjectName());


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void openDrawer(View view){
        mDrawerLayout.openDrawer(mLeftDrawer);
    }

    public void closeDrawer(View view){
        mDrawerLayout.closeDrawer(mLeftDrawer);
    }

    public void exhibits(View view) {
        Intent startExhibits = new Intent(this, ExhibitsActivity.class);
        startActivity(startExhibits);
    }
    public void explorations(View view) {
        Intent startExplorations = new Intent(this, ExplorationsActivity.class);
        startActivity(startExplorations);
    }
    public void places(View view) {
        Intent startPlaces = new Intent(this, PlacesActivity.class);
        startActivity(startPlaces);
    }
    public void random(View view) {
//        Intent startRandom = new Intent(this, RandomActivity.class);
//        startActivity(startRandom);

        Random r = new Random();
        Integer min = 0;
        Integer max = 1;
        Integer i = r.nextInt(max - min + 1) + min;

        if (project.getLocationArrayList().isEmpty() && project.getAreaArrayList().isEmpty()){
            Toast.makeText(getApplicationContext(), "No Locations are present in the database",
                    Toast.LENGTH_LONG).show();
            Log.i("Random: ","Both Area and Locations Empty");
            return;
        }

        else if(project.getLocationArrayList().isEmpty() && !project.getAreaArrayList().isEmpty()){
            i = 0;
            Log.i("Random: ","Location List is Empty");
            Log.i("LocationList: ", String.valueOf(project.getLocationArrayList().isEmpty()));
            Log.i("LocationSize: ", String.valueOf(project.getLocationArrayList().size()));
        }

        else if(project.getAreaArrayList().isEmpty() && !project.getLocationArrayList().isEmpty()){
            i = 1;
            Log.i("Random: ","Area List is Empty");
            Log.i("AreaList: ", String.valueOf(project.getAreaArrayList().isEmpty()));
            Log.i("AreaSize: ", String.valueOf(project.getAreaArrayList().size()));
        }

        else {
            Log.i("Random: ","Both lists are not Empty.");
            Log.i("i: ", String.valueOf(i));
        }

        // Location Chosen
        if (i.equals(1)) {
            max = project.getLocationArrayList().size() - 1;
            i = r.nextInt(max - min + 1) + min;

            Log.i("Location Chosen: ","Max: " + max + "  i: " + i);
            Log.i("Location Name:", project.getLocationArrayList().get(i).name);
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

            Log.i("Area Chosen: ","Max: " + max + "  i: " + i);
            Log.i("Area Name:", project.getAreaArrayList().get(i).name);
            Intent intent = new Intent(this, SingleLocation.class);
            Bundle bundle = new Bundle();
            bundle.putDouble("Latitude", project.getAreaArrayList().get(i).coordinates.get(0).getX());
            bundle.putDouble("Longitude", project.getAreaArrayList().get(i).coordinates.get(0).getY());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


    public void learnMore(View view) {
        Intent startLearnMore = new Intent(this, LearnMoreActivity.class);
        startActivity(startLearnMore);
    }

}


