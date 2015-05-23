package com.bielicki.brandon.mbira;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.PathOverlay;
import com.mapbox.mapboxsdk.overlay.UserLocationOverlay;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.MapViewListener;
import com.mapbox.mapboxsdk.views.util.TilesLoadedListener;

import java.util.Random;


public class ExplorationMapActivity extends ActionBarActivity {

    AppData project = AppData.get();
    private WebView mWebView;

    // MapBox
    private MapView mv;
    private UserLocationOverlay myLocationOverlay;
    private String currentMap = null;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exploration_map);

        Intent intent = getIntent();
        int explorationId = intent.getIntExtra("explorationId", 0);
        int pos = intent.getIntExtra("pos",0);
        final Exploration exploration = project.getExplorationArrayList().get(pos);

        mv = (MapView) findViewById(R.id.mapview);
        mv.setMinZoomLevel(mv.getTileProvider().getMinimumZoomLevel());
        mv.setMaxZoomLevel(mv.getTileProvider().getMaximumZoomLevel());
        mv.setCenter(mv.getTileProvider().getCenterCoordinate());
        mv.setZoom(0);
        currentMap = getResources().getString(R.string.streetMapId);

        TextView explorationTitle = (TextView) findViewById(R.id.explorationTitle);
        explorationTitle.setText(exploration.name);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


//        for(int x = 0; x < exploration.getLocationArrayList().size(); x++) {
//            m = new Marker(mv, exploration.getLocationArrayList().get(x).name, "", new LatLng(exploration.getLocationArrayList().get(x).latitude, exploration.getLocationArrayList().get(x).longitude));
//            m.setIcon(new Icon(this, Icon.Size.LARGE, "", "3EB9FD"));
//            mv.addMarker(m);
//        }


        Marker m;
        for(int x = 0; x < exploration.getMapItemArrayList().size(); x++) {

            // Checking if the MapItem is of instance type Area
            if (exploration.getMapItemArrayList().get(x) instanceof Area)
            {

//                Area location = (Area) exploration.getMapItemArrayList().get(x);

                m = new Marker(mv, ((Area)exploration.getMapItemArrayList().get(x)).name, "", new LatLng(((Area)exploration.getMapItemArrayList().get(x)).coordinates.get(0).getX(), ((Area)exploration.getMapItemArrayList().get(x)).coordinates.get(0).getY()));
                Log.i( ((Area) exploration.getMapItemArrayList().get(x)).name, "Latitude: " + Double.toString(((Area) exploration.getMapItemArrayList().get(x)).coordinates.get(0).getX()) + "  Longitude: " + Double.toString(((Area) exploration.getMapItemArrayList().get(x)).coordinates.get(0).getY()));
                m.setIcon(new Icon(this, Icon.Size.LARGE, Integer.toString(x+1), "455A64"));
                mv.addMarker(m);
            }

            // Checking if the MapItem is of instance type Location
            else if (exploration.getMapItemArrayList().get(x) instanceof Location)
            {
//                Location location = (Location) exploration.getMapItemArrayList().get(x);

                m = new Marker(mv, ((Location) exploration.getMapItemArrayList().get(x)).name, "", new LatLng(((Location) exploration.getMapItemArrayList().get(x)).latitude, ((Location) exploration.getMapItemArrayList().get(x)).longitude));
                Log.i( ((Location) exploration.getMapItemArrayList().get(x)).name, "Latitude: " + Double.toString(((Location) exploration.getMapItemArrayList().get(x)).latitude) + "  Longitude: " + Double.toString(((Location) exploration.getMapItemArrayList().get(x)).longitude));
                m.setIcon(new Icon(this, Icon.Size.LARGE, Integer.toString(x+1), "455A64"));
                mv.addMarker(m);

            }

        }



        PathOverlay line;
        // Adding Paths between the Markers.

        for(int x = 0; x < exploration.getMapItemArrayList().size() - 1; x++) {
            line = new PathOverlay(Color.parseColor("#455A64"), 7);


            // Checking if the MapItem is of instance type Area
            if (exploration.getMapItemArrayList().get(x) instanceof Area)
            {
                line.addPoint(new LatLng( ((Area) exploration.getMapItemArrayList().get(x)).coordinates.get(0).getX(), ((Area) exploration.getMapItemArrayList().get(x)).coordinates.get(0).getY()));

                // Checking if the MapItem is of instance type Area
                if (exploration.getMapItemArrayList().get(x+1) instanceof Area)
                {
                    line.addPoint(new LatLng( ((Area) exploration.getMapItemArrayList().get(x+1)).coordinates.get(0).getX(), ((Area) exploration.getMapItemArrayList().get(x+1)).coordinates.get(0).getY()));
                }

                // Checking if the MapItem is of instance type Location
                else if (exploration.getMapItemArrayList().get(x+1) instanceof Location)
                {
                    line.addPoint(new LatLng(((Location) exploration.getMapItemArrayList().get(x+1)).latitude, ((Location) exploration.getMapItemArrayList().get(x+1)).longitude));
                }
            }

            // Checking if the MapItem is of instance type Location
            else if (exploration.getMapItemArrayList().get(x) instanceof Location)
            {
                line.addPoint(new LatLng(((Location) exploration.getMapItemArrayList().get(x)).latitude, ((Location) exploration.getMapItemArrayList().get(x)).longitude));

                // Checking if the MapItem is of instance type Area
                if (exploration.getMapItemArrayList().get(x+1) instanceof Area)
                {
                    line.addPoint(new LatLng( ((Area) exploration.getMapItemArrayList().get(x+1)).coordinates.get(0).getX(), ((Area) exploration.getMapItemArrayList().get(x+1)).coordinates.get(0).getY()));
                }

                // Checking if the MapItem is of instance type Location
                else if (exploration.getMapItemArrayList().get(x+1) instanceof Location)
                {
                    line.addPoint(new LatLng(((Location) exploration.getMapItemArrayList().get(x+1)).latitude, ((Location) exploration.getMapItemArrayList().get(x+1)).longitude));
                }
            }
            mv.getOverlays().add(line);
        }

        // Floating Action Button

        final FloatingActionButton findMyLocationButton = (FloatingActionButton) findViewById(R.id.findMyLocationButton);

        findMyLocationButton.hide(false);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findMyLocationButton.show(true);
                findMyLocationButton.setShowAnimation(AnimationUtils.loadAnimation(ExplorationMapActivity.this, R.anim.show_from_buttom));
                findMyLocationButton.setHideAnimation(AnimationUtils.loadAnimation(ExplorationMapActivity.this, R.anim.hide_to_buttom));
            }
        }, 300);

//        randomLocationButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Random r = new Random();
//                Integer min = 0;
//                Integer max = exploration.getMapItemArrayList().size() - 1;
//                Integer i = r.nextInt(max - min + 1) + min;
//
//                if (!max.equals(-1)) {
//                    Intent intent = new Intent(ExplorationMapActivity.this, SingleLocation.class);
//                    Bundle bundle = new Bundle();
//
//                    // Checking if the MapItem is of instance type Area
//                    if (exploration.getMapItemArrayList().get(i) instanceof Area) {
//                        bundle.putDouble("Latitude", ((Area) exploration.getMapItemArrayList().get(i)).coordinates.get(0).getX());
//                        bundle.putDouble("Longitude", ((Area) exploration.getMapItemArrayList().get(i)).coordinates.get(0).getY());
//                    }
//
//                    // Checking if the MapItem is of instance type Location
//                    else if (exploration.getMapItemArrayList().get(i) instanceof Location) {
//                        bundle.putDouble("Latitude", ((Location) exploration.getMapItemArrayList().get(i)).latitude);
//                        bundle.putDouble("Longitude", ((Location) exploration.getMapItemArrayList().get(i)).longitude);
//                    }
//
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//
//            }
//        });

        findMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show user location
                mv.setUserLocationEnabled(true);
                mv.setUserLocationTrackingMode(UserLocationOverlay.TrackingMode.FOLLOW_BEARING);
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
                Intent i = new Intent(ExplorationMapActivity.this, SingleLocation.class);
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
        getMenuInflater().inflate(R.menu.menu_exploration_map, menu);
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
