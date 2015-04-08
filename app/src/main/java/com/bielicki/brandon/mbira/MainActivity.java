package com.bielicki.brandon.mbira;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    private TextView projectDescription;
    private TextView projectTitle;
    private ImageView projectImageView;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawer;
    private Toolbar toolbar;
    //private AppData project;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppData project = AppData.get();
        project.isSet = true;

        toolbar = (Toolbar) findViewById(R.id.app_bar_transparent);
        //toolbar.setTitle(project.getProjectName());
        setSupportActionBar(toolbar);
        

        projectTitle = (TextView) findViewById(R.id.projectTitle);
        projectImageView = (ImageView) findViewById(R.id.projectImageView);
        projectDescription = (TextView) findViewById(R.id.projectDescription);

        projectImageView.setImageBitmap(project.getProjectImage());
        projectDescription.setText(project.getProjectDescription());
        projectTitle.setText(project.getProjectName());


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
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
        Intent startRandom = new Intent(this, SignInActivity.class);
        startActivity(startRandom);
    }
    public void learnMore(View view) {
        Intent startLearnMore = new Intent(this, LearnMoreActivity.class);
        startActivity(startLearnMore);
    }



}


