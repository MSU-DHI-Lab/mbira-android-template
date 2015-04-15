package com.bielicki.brandon.mbira;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


public class SingleLocation extends ActionBarActivity {
    private Toolbar toolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private static int id = 0;
    private static Boolean isLocation;

    // Project Data
    static AppData project;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_location);

        // TEMP PROJECT
        project = AppData.get();


        Bundle bundle = getIntent().getExtras();
        Double latitude = bundle.getDouble("Latitude");
        Double longitude = bundle.getDouble("Longitude");


        for(int x = 0; x < project.getLocationArrayList().size(); x++) {
            if ( (project.getLocationArrayList().get(x).latitude == latitude) && ((project.getLocationArrayList().get(x).longitude == longitude))){
                isLocation = true;
                id = x;
                break;
            }

            else if ( (project.getAreaArrayList().get(x).coordinates.get(0).getX() == latitude) && (project.getAreaArrayList().get(x).coordinates.get(0).getY() == longitude)) {
                isLocation = false;
                id = x;
                break;
            }

        }

        // Toobar Creation
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ImageView locationImage = (ImageView) findViewById(R.id.singleLocationImageView);
        TextView titleTextView = (TextView) findViewById(R.id.singleLocationTitleTextView);

        if (isLocation) {
            locationImage.setImageBitmap(project.getLocationArrayList().get(id).locationImage);
            titleTextView.setText(project.getLocationArrayList().get(id).name);
        }

        else {
            locationImage.setImageBitmap(project.getAreaArrayList().get(id).areaImage);
            titleTextView.setText(project.getAreaArrayList().get(id).name);
        }



        mPager = (ViewPager) findViewById(R.id.locationPager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout) findViewById(R.id.locationsTab);
        mTabs.setViewPager(mPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_single_location, menu);
        return super.onCreateOptionsMenu(menu);
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

        else if (id == android.R.id.home) {
            //NavUtils.navigateUpFromSameTask(this);
            this.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        String[] locationTabsTitle;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            locationTabsTitle = getResources().getStringArray(R.array.location_tabs_title);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 2){
                return MediaFragment.newInstance(5, isLocation, id);
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
                    if (isLocation) {
                        textView.setText(project.getLocationArrayList().get(id).description);
                    }
                    else{
                        textView.setText(project.getAreaArrayList().get(id).description);
                    }
                }
                else if (bundle.getInt("Position") == 1) {
                    textView = (TextView) layout.findViewById(R.id.position);
                    if (isLocation) {
                        textView.setText(project.getLocationArrayList().get(id).dig_deeper);
                    }
                    else {
                        textView.setText(project.getAreaArrayList().get(id).dig_deeper);
                    }
                }

                else{
                    textView.setText("Comments will be placed here.");
                }
            }
            return layout;
        }
    }
}
