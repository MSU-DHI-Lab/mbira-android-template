package com.bielicki.brandon.mbira;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ExplorationsActivity extends ActionBarActivity {

    private Toolbar toolbar;
    AppData project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorations);
        project = AppData.get();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(project.getProjectName());

        GridView gridView = (GridView)findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_explorations, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class ImageAdapter extends BaseAdapter {
        private List<Item> items = new ArrayList<Item>();
        private LayoutInflater inflater;

        public ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);

            for(int x = 0; x < project.getExplorationArrayList().size(); x++) {
                String title = project.getExplorationArrayList().get(x).name;
                Bitmap img = project.getExplorationArrayList().get(x).explorationImage;
                int id = project.getExplorationArrayList().get(x).id;
                items.add(new Item(x,id,title,img));
            }

        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return items.get(position).id;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup)
        {
            View v = view;
            ImageButton picture;
            TextView name;

            if(v == null)
            {
                v = inflater.inflate(R.layout.gridview_item, viewGroup, false);
                v.setTag(R.id.picture, v.findViewById(R.id.picture));
                v.setTag(R.id.text, v.findViewById(R.id.text));
            }

            picture = (ImageButton)v.getTag(R.id.picture);
            name = (TextView)v.getTag(R.id.text);

            Item item = (Item)getItem(position);

            picture.setImageBitmap(item.drawable);
            name.setText(item.name);

            picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Item temp = (Item) getItem(position);
                    Intent startSingleExploration = new Intent(view.getContext(), SingleExplorationActivity.class);
                    startSingleExploration.putExtra("pos", temp.pos);
                    startSingleExploration.putExtra("explorationId", temp.id);
                    startActivity(startSingleExploration);
                }
            });

            return v;
        }

        private class Item {
            final int pos;
            final int id;
            final String name;
            final Bitmap drawable;

            Item(int pos, int id, String name, Bitmap drawable) {
                this.pos = pos;
                this.id = id;
                this.name = name;
                this.drawable = drawable;
            }
        }
    }


}
