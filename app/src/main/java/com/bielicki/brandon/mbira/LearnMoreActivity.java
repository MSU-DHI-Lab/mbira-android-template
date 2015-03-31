package com.bielicki.brandon.mbira;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class LearnMoreActivity extends ActionBarActivity {

    private Toolbar toolbar;
    ImageView projectImage;
    AppData project = AppData.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_more);

        toolbar = (Toolbar) findViewById(R.id.app_bar_transparent);
        toolbar.setTitle("Learn more about the Project");
        setSupportActionBar(toolbar);

        TextView project_description = (TextView) findViewById(R.id.exhibitDescription);
        project_description.setText(project.getProjectDescription());

        TextView project_title = (TextView) findViewById(R.id.exhibitTitle);
        project_title.setText(project.getProjectName());

        project_description.setMovementMethod(new ScrollingMovementMethod());
        projectImage = (ImageView) findViewById(R.id.explorationImageView);
        projectImage.setImageBitmap(project.getProjectImage());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_learn_more, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}
