package com.bielicki.brandon.mbira;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SingleExplorationActivity extends ActionBarActivity {

    private Toolbar toolbar;
    AppData project = AppData.get();
    private NonScrollListView stopsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_exploration);

        Intent intent = getIntent();
        int pos = intent.getIntExtra("pos",0);
        int explorationId = intent.getIntExtra("explorationId", 0);
        Exploration exploration = project.getExplorationArrayList().get(pos);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(exploration.name);

        TextView exploration_description = (TextView) findViewById(R.id.explorationDescription);
        exploration_description.setMovementMethod(new ScrollingMovementMethod());
        exploration_description.setText(exploration.description);

        TextView explorationTitle = (TextView) findViewById(R.id.explorationTitle);
        explorationTitle.setText(exploration.name);

        ImageView explorationImage = (ImageView) findViewById(R.id.explorationImageView);
        explorationImage.setImageBitmap(exploration.explorationImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_exploration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
