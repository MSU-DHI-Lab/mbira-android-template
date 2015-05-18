package com.bielicki.brandon.mbira;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class SingleExhibitActivity extends ActionBarActivity {

    private Toolbar toolbar;
    AppData project = AppData.get();
    int exhibitId;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_exhibit);

        Intent intent = getIntent();
        pos = intent.getIntExtra("pos",0);
        exhibitId = intent.getIntExtra("exhibitId", 0);
        Exhibit exhibit = project.getExhibitArrayList().get(pos);

        toolbar = (Toolbar) findViewById(R.id.app_bar_transparent);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        TextView exhibit_description = (TextView) findViewById(R.id.exhibitDescription);
        exhibit_description.setMovementMethod(new ScrollingMovementMethod());
        exhibit_description.setText(exhibit.description);

        TextView exhibit_title = (TextView) findViewById(R.id.exhibitTitle);
        exhibit_title.setText(exhibit.name);

        ImageView exhibitImage = (ImageView) findViewById(R.id.exhibitImageView);
        exhibitImage.setImageBitmap(exhibit.exhibitImage);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_exhibit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void exhibitMap(View view) {
        Intent startExhibitMap = new Intent(this, ExhibitMapActivity.class);
        startExhibitMap.putExtra("exhibitId", exhibitId);
        startExhibitMap.putExtra("pos", pos);
        startActivity(startExhibitMap);
    }
}
