package com.bielicki.brandon.mbira;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.FeatureCollection;
import com.cocoahero.android.geojson.Polygon;
import com.cocoahero.android.geojson.Ring;
import com.github.clans.fab.FloatingActionButton;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.Overlay;
import com.mapbox.mapboxsdk.overlay.UserLocationOverlay;
import com.mapbox.mapboxsdk.tileprovider.tilesource.*;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.MapViewListener;
import com.mapbox.mapboxsdk.views.util.TilesLoadedListener;
import com.vividsolutions.jts.awt.PointShapeFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class PlacesActivity extends ActionBarActivity {

    // MapBox
    private MapView mv;
    private UserLocationOverlay myLocationOverlay;
    private String currentMap = null;
    private Toolbar toolbar;


    // projectData
    AppData project;
    Constants constants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        project = AppData.get();
        constants = Constants.get();

//        Toolbar Code

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Places");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

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
                findMyLocationButton.setShowAnimation(AnimationUtils.loadAnimation(PlacesActivity.this, R.anim.show_from_buttom));
                findMyLocationButton.setHideAnimation(AnimationUtils.loadAnimation(PlacesActivity.this, R.anim.hide_to_buttom));
                randomLocationButton.setShowAnimation(AnimationUtils.loadAnimation(PlacesActivity.this, R.anim.show_from_buttom));
                randomLocationButton.setHideAnimation(AnimationUtils.loadAnimation(PlacesActivity.this, R.anim.hide_to_buttom));
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
                    max = project.getLocationArrayList().size() - 1;
                    i = r.nextInt(max - min + 1) + min;
                    Intent intent = new Intent(PlacesActivity.this, SingleLocation.class);
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
                    Intent intent = new Intent(PlacesActivity.this, SingleLocation.class);
                    Bundle bundle = new Bundle();
                    bundle.putDouble("Latitude", project.getAreaArrayList().get(i).coordinates.get(0).getX());
                    bundle.putDouble("Longitude", project.getAreaArrayList().get(i).coordinates.get(0).getY());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        findMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show user location
                mv.setUserLocationEnabled(true);
                mv.setUserLocationTrackingMode(UserLocationOverlay.TrackingMode.FOLLOW_BEARING);
            }
        });

        // MapBox Code

        mv = (MapView) findViewById(R.id.mapview);
        mv.setMinZoomLevel(mv.getTileProvider().getMinimumZoomLevel());
        mv.setMaxZoomLevel(mv.getTileProvider().getMaximumZoomLevel());
        mv.setCenter(mv.getTileProvider().getCenterCoordinate());
        mv.setZoom(0);
        currentMap = getResources().getString(R.string.streetMapId);


        Marker m;

        // Adding Locations
        for(int x = 0; x < project.getLocationArrayList().size(); x++) {
            m = new Marker(mv, project.getLocationArrayList().get(x).name, "", new LatLng(project.getLocationArrayList().get(x).latitude, project.getLocationArrayList().get(x).longitude));
            m.setIcon(new Icon(this, Icon.Size.LARGE, "", "3EB9FD"));
            mv.addMarker(m);
        }

        // Adding Areas
        for(int x =0; x < project.getAreaArrayList().size(); x++){
            m = new Marker(mv, project.getAreaArrayList().get(x).name, "", new LatLng(project.getAreaArrayList().get(x).coordinates.get(0).getX(), project.getAreaArrayList().get(x).coordinates.get(0).getY()));
            m.setIcon(new Icon(this, Icon.Size.LARGE, "", "3EB9FD"));
            mv.addMarker(m);
        }

        // On Click Listeners for the mapview
        mv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // On Tile Load Listeners for the mapview
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


        // Marker events listeners for the MapView
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
                Intent i = new Intent(PlacesActivity.this, SingleLocation.class);
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
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_places, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
