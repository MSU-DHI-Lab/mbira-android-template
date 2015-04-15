package com.bielicki.brandon.mbira;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
    public static Exploration exploration;
    int pos, explorationId;
    private static int id = 0;


    private ViewPager mPager;
    private SlidingTabLayout mTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_exploration);

        Intent intent = getIntent();
        pos = intent.getIntExtra("pos",0);
        explorationId = intent.getIntExtra("explorationId", 0);
        exploration = project.getExplorationArrayList().get(pos);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(exploration.name);

        ImageView locationImage = (ImageView) findViewById(R.id.singleExplorationImageView);
        TextView titleTextView = (TextView) findViewById(R.id.singleExplorationTitleTextView);

        locationImage.setImageBitmap(exploration.explorationImage);
        titleTextView.setText(exploration.name);


//        ImageView explorationImage = (ImageView) findViewById(R.id.explorationImageView);
//        explorationImage.setImageBitmap(exploration.explorationImage);

        mPager = (ViewPager) findViewById(R.id.locationPager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout) findViewById(R.id.locationsTab);
        mTabs.setViewPager(mPager);
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

    public void explorationMap(View view) {
        Intent startExplorationMap = new Intent(this, ExplorationMapActivity.class);
        startExplorationMap.putExtra("explorationId", explorationId);
        startExplorationMap.putExtra("pos", pos);
        startActivity(startExplorationMap);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        String[] locationTabsTitle;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            locationTabsTitle = getResources().getStringArray(R.array.exploration_tabs_title);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 2){
                return MediaExplorationFragment.newInstance(5, pos);
            }
            else {
                return MyFragment.getInstance(position);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return locationTabsTitle[position];
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public static class MyFragment extends Fragment {
        private TextView textView;

        public static MyFragment getInstance(int position) {
            MyFragment myFragment = new MyFragment();
            Bundle args = new Bundle();
            args.putInt("Position", position);
            myFragment.setArguments(args);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.slidetab_text, container, false);
            textView = (TextView) layout.findViewById(R.id.position);


            Bundle bundle = getArguments();
            textView.setMovementMethod(new ScrollingMovementMethod());

            if (bundle != null){
                if (bundle.getInt("Position") == 0) {
                    textView = (TextView) layout.findViewById(R.id.position);
                    textView.setText(exploration.description);
                }
                else if (bundle.getInt("Position") == 1) {
                    textView = (TextView) layout.findViewById(R.id.position);
                    String stopsText = "";

                    //Looping throught the exploration List
                    for(int x = 0; x < exploration.getMapItemArrayList().size(); x++) {

                        // Checking if the MapItem is of instance type Area
                        if (exploration.getMapItemArrayList().get(x) instanceof Area)
                        {
                            stopsText += Integer.toString(x+1) + ". " + ((Area)exploration.getMapItemArrayList().get(x)).name + "\n";
                        }

                        // Checking if the MapItem is of instance type Location
                        else if (exploration.getMapItemArrayList().get(x) instanceof Location)
                        {
                            stopsText += Integer.toString(x+1) + ". " + ((Location)exploration.getMapItemArrayList().get(x)).name + "\n";
                        }

                    }

                    textView.setText(stopsText);
                }

                else{
                    textView.setText("Comments will be placed here.");
                }
            }
            return layout;
        }
    }
}
