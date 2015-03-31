package com.bielicki.brandon.mbira;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
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


public class PlacesActivity extends Activity {

    // MapBox
    private MapView mv;
    private UserLocationOverlay myLocationOverlay;
    private String currentMap = null;

    // projectData
    AppData project;
    Constants constants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        project = AppData.get();
        constants = Constants.get();

        // MapBox Code

        mv = (MapView) findViewById(R.id.mapview);
        mv.setMinZoomLevel(mv.getTileProvider().getMinimumZoomLevel());
        mv.setMaxZoomLevel(mv.getTileProvider().getMaximumZoomLevel());
        mv.setCenter(mv.getTileProvider().getCenterCoordinate());
        mv.setZoom(0);
        currentMap = getResources().getString(R.string.streetMapId);

        // Show user location
        mv.setUserLocationEnabled(true);
        mv.setUserLocationTrackingMode(UserLocationOverlay.TrackingMode.FOLLOW);

        mv.loadFromGeoJSONURL("http://dev2.matrix.msu.edu/kora/plugins/mbira_plugin/json/map_area_data.geojson");

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
        return super.onOptionsItemSelected(item);
    }

}
