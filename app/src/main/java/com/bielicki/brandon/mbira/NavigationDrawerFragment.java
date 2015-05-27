package com.bielicki.brandon.mbira;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private AppData project;


    public NavigationDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }


    public void setUp(DrawerLayout drawerLayout, Toolbar toolbar) {
        project = AppData.get();
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        ImageView drawerImage = (ImageView) getView().findViewById(R.id.drawerImage);
        ImageLoader.getInstance().displayImage(project.getProjectImageUrl(), drawerImage);

        TextView projectTitle = (TextView) getView().findViewById(R.id.projectTitle);
        projectTitle.setText(project.getProjectName());

        ImageButton exhibitNav = (ImageButton) getView().findViewById(R.id.exhibitImage);
        ImageButton explorationNav = (ImageButton) getView().findViewById(R.id.explorationImage);
        ImageButton placesNav = (ImageButton) getView().findViewById(R.id.placesImage);
        ImageButton randomNav = (ImageButton) getView().findViewById(R.id.randomImage);
        ImageButton learnNav = (ImageButton) getView().findViewById(R.id.learnImage);
        ImageButton signInNav = (ImageButton) getView().findViewById(R.id.signInImage);

        exhibitNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startExhibits = new Intent(getActivity(), ExhibitsActivity.class);
                startActivity(startExhibits);
            }
        });

        explorationNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startExplorations = new Intent(getActivity(), ExplorationsActivity.class);
                startActivity(startExplorations);
            }
        });

        placesNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startPlaces = new Intent(getActivity(), PlacesActivity.class);
                startActivity(startPlaces);
            }
        });


        randomNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random r = new Random();
                Integer min = 0;
                Integer max = 1;
                Integer i = r.nextInt(max - min + 1) + min;

                if (project.getLocationArrayList().isEmpty() && project.getAreaArrayList().isEmpty()){
                    Toast.makeText(getActivity(), "No Locations are present in the database",
                            Toast.LENGTH_LONG).show();
                    //Log.i("Random: ", "Both Area and Locations Empty");
                    return;
                }

                else if(project.getLocationArrayList().isEmpty() && !project.getAreaArrayList().isEmpty()){
                    i = 0;
//                    Log.i("Random: ","Location List is Empty");
//                    Log.i("LocationList: ", String.valueOf(project.getLocationArrayList().isEmpty()));
//                    Log.i("LocationSize: ", String.valueOf(project.getLocationArrayList().size()));

                }

                else if(project.getAreaArrayList().isEmpty() && !project.getLocationArrayList().isEmpty()){
                    i = 1;
//                    Log.i("Random: ","Area List is Empty");
//                    Log.i("AreaList: ", String.valueOf(project.getAreaArrayList().isEmpty()));

                }


                // Location Chosen
                if (i.equals(1)) {
                    max = project.getLocationArrayList().size() - 1;
                    i = r.nextInt(max - min + 1) + min;

//                    Log.i("Location Chosen: ","Max: " + max + "  i: " + i);
                    Intent intent = new Intent(getActivity(), SingleLocation.class);
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

//                    Log.i("Area Chosen: ","Max: " + max + "  i: " + i);
                    Intent intent = new Intent(getActivity(), SingleLocation.class);
                    Bundle bundle = new Bundle();
                    bundle.putDouble("Latitude", project.getAreaArrayList().get(i).coordinates.get(0).getX());
                    bundle.putDouble("Longitude", project.getAreaArrayList().get(i).coordinates.get(0).getY());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        learnNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startLearnMore = new Intent(getActivity(), LearnMoreActivity.class);
                startActivity(startLearnMore);
            }
        });

        signInNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startSignIn = new Intent(getActivity(), SignInActivity.class);
                startActivity(startSignIn);
            }
        });
    }
}
