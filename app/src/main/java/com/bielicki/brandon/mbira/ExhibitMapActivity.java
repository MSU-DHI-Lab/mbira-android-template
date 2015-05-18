package com.bielicki.brandon.mbira;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;

import com.github.clans.fab.FloatingActionButton;
import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.UserLocationOverlay;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.MapViewListener;
import com.mapbox.mapboxsdk.views.util.TilesLoadedListener;

import java.util.ArrayList;
import java.util.Random;


public class ExhibitMapActivity extends ActionBarActivity {

    AppData project = AppData.get();
    private Toolbar toolbar;

    // MapBox
    private MapView mv;
    private UserLocationOverlay myLocationOverlay;
    private String currentMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibit_map);


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("");


        Intent intent = getIntent();
        int exhibitId = intent.getIntExtra("exhibitId", 0);
        int pos = intent.getIntExtra("pos",0);
        final Exhibit exhibit = project.getExhibitArrayList().get(pos);

        mv = (MapView) findViewById(R.id.mapview);
        mv.setMinZoomLevel(mv.getTileProvider().getMinimumZoomLevel());
        mv.setMaxZoomLevel(mv.getTileProvider().getMaximumZoomLevel());
        mv.setCenter(mv.getTileProvider().getCenterCoordinate());
        mv.setZoom(0);
        currentMap = getResources().getString(R.string.streetMapId);

        // Show user location
        mv.setUserLocationEnabled(true);
        mv.setUserLocationTrackingMode(UserLocationOverlay.TrackingMode.FOLLOW_BEARING);

        Marker m;

        // Loading all the location markers in the exhibit chosen
        for(int x = 0; x < exhibit.getLocationArrayList().size(); x++) {
            m = new Marker(mv, exhibit.getLocationArrayList().get(x).name, "", new LatLng(exhibit.getLocationArrayList().get(x).latitude, exhibit.getLocationArrayList().get(x).longitude));
            m.setIcon(new Icon(this, Icon.Size.LARGE, "", "3EB9FD"));
            mv.addMarker(m);
        }

        // Loading all the area markers in the exhibit chosen
        for(int x = 0; x < exhibit.getAreaArrayList().size(); x++) {
            m = new Marker(mv, exhibit.getAreaArrayList().get(x).name, "", new LatLng(exhibit.getAreaArrayList().get(x).coordinates.get(0).getX(), exhibit.getAreaArrayList().get(x).coordinates.get(0).getY()));
            m.setIcon(new Icon(this, Icon.Size.LARGE, "", "3EB9FD"));
            mv.addMarker(m);
        }

        // Floating Action Button

        final FloatingActionButton randomLocationButton = (FloatingActionButton) findViewById(R.id.randomLocationButton);
        final FloatingActionButton findMyLocationButton = (FloatingActionButton) findViewById(R.id.findMyLocationButton);

        findMyLocationButton.hide(false);
        randomLocationButton.hide(false);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findMyLocationButton.show(true);
                randomLocationButton.show(true);
                findMyLocationButton.setShowAnimation(AnimationUtils.loadAnimation(ExhibitMapActivity.this, R.anim.show_from_buttom));
                findMyLocationButton.setHideAnimation(AnimationUtils.loadAnimation(ExhibitMapActivity.this, R.anim.hide_to_buttom));
                randomLocationButton.setShowAnimation(AnimationUtils.loadAnimation(ExhibitMapActivity.this, R.anim.show_from_buttom));
                randomLocationButton.setHideAnimation(AnimationUtils.loadAnimation(ExhibitMapActivity.this, R.anim.hide_to_buttom));
            }
        }, 300);

        randomLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                Integer min = 0;
                Integer max = 1;
                Integer i = r.nextInt(max - min + 1) + min;

                // Location Chosen
                if (i.equals(1)) {
                    max = exhibit.getLocationArrayList().size() - 1;
                    i = r.nextInt(max - min + 1) + min;
                    Intent intent = new Intent(ExhibitMapActivity.this, SingleLocation.class);
                    Bundle bundle = new Bundle();
                    bundle.putDouble("Latitude", exhibit.getLocationArrayList().get(i).latitude);
                    bundle.putDouble("Longitude", exhibit.getLocationArrayList().get(i).longitude);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                //Area Chosen
                else {
                    max = exhibit.getAreaArrayList().size() - 1;
                    i = r.nextInt(max - min + 1) + min;
                    Intent intent = new Intent(ExhibitMapActivity.this, SingleLocation.class);
                    Bundle bundle = new Bundle();
                    bundle.putDouble("Latitude", exhibit.getAreaArrayList().get(i).coordinates.get(0).getX());
                    bundle.putDouble("Longitude", exhibit.getAreaArrayList().get(i).coordinates.get(0).getY());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        findMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        mv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        mv.setOnTilesLoadedListener(new TilesLoadedListener() {
            @Override
            public boolean onTilesLoaded() {
                return false;
            }

            @Override
            public boolean onTilesLoadStarted() {
                // TODO Auto-generated method stub
                return false;
            }
        });

        MapViewListener mapViewListener = new MapViewListener() {
            @Override
            public void onShowMarker(MapView mapView, Marker marker) {
            }

            @Override
            public void onHideMarker(MapView mapView, Marker marker) {
            }

            @Override
            public void onTapMarker(MapView mapView, Marker marker) {
            }

            @Override
            public void onLongPressMarker(MapView mapView, Marker marker) {
                Intent i = new Intent(ExhibitMapActivity.this, SingleLocation.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("Latitude", marker.getPoint().getLatitude());
                bundle.putDouble("Longitude", marker.getPoint().getLongitude());
                i.putExtras(bundle);
                startActivity(i);
            }

            @Override
            public void onTapMap(MapView mapView, ILatLng iLatLng) {

            }

            @Override
            public void onLongPressMap(MapView mapView, ILatLng iLatLng) {

            }
        };

        mv.setMapViewListener(mapViewListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exhibit_map, menu);
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
}
