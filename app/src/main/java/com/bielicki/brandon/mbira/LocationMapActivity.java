package com.bielicki.brandon.mbira;

import android.content.Intent;
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
import com.mapbox.mapboxsdk.overlay.UserLocationOverlay;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.MapViewListener;
import com.mapbox.mapboxsdk.views.util.TilesLoadedListener;


public class LocationMapActivity extends ActionBarActivity {

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
        setContentView(R.layout.activity_location_map);

        Intent intent = getIntent();
        boolean isLocation = intent.getBooleanExtra("isLocation", false);
        int pos = intent.getIntExtra("pos",0);

        mv = (MapView) findViewById(R.id.mapview);
        mv.setMinZoomLevel(mv.getTileProvider().getMinimumZoomLevel());
        mv.setMaxZoomLevel(mv.getTileProvider().getMaximumZoomLevel());
        currentMap = getResources().getString(R.string.streetMapId);

        TextView explorationTitle = (TextView) findViewById(R.id.locationTitle);
        Marker m;

        if (isLocation){
            final Location loc = project.getLocationArrayList().get(pos);
            explorationTitle.setText(loc.name);

            m = new Marker(mv, project.getLocationArrayList().get(pos).name, "", new LatLng( project.getLocationArrayList().get(pos).latitude, project.getLocationArrayList().get(pos).longitude ));
            m.setIcon(new Icon(this, Icon.Size.LARGE, "", "455A64"));
            mv.addMarker(m);

            LatLng centerCoord = new LatLng(project.getLocationArrayList().get(pos).latitude, project.getLocationArrayList().get(pos).longitude);
            mv.setCenter(centerCoord);
            mv.setZoom(13);
        }
        else {
            final Area area = project.getAreaArrayList().get(pos);
            explorationTitle.setText(area.name);

            m = new Marker(mv, project.getAreaArrayList().get(pos).name, "", new LatLng( project.getAreaArrayList().get(pos).coordinates.get(0).getX(), project.getAreaArrayList().get(pos).coordinates.get(0).getY() ));
            m.setIcon(new Icon(this, Icon.Size.LARGE, "", "455A64"));
            mv.addMarker(m);

            LatLng centerCoord = new LatLng(project.getAreaArrayList().get(pos).coordinates.get(0).getX(), project.getAreaArrayList().get(pos).coordinates.get(0).getY());
            mv.setCenter(centerCoord);
            mv.setZoom(13);
        }


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        // Floating Action Button

        final FloatingActionButton findMyLocationButton = (FloatingActionButton) findViewById(R.id.findMyLocationButton);

        findMyLocationButton.hide(false);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findMyLocationButton.show(true);
                findMyLocationButton.setShowAnimation(AnimationUtils.loadAnimation(LocationMapActivity.this, R.anim.show_from_buttom));
                findMyLocationButton.setHideAnimation(AnimationUtils.loadAnimation(LocationMapActivity.this, R.anim.hide_to_buttom));
            }
        }, 300);


        findMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show user location
                mv.setUserLocationEnabled(false);
                mv.setUserLocationEnabled(true);
                mv.setUserLocationTrackingMode(UserLocationOverlay.TrackingMode.FOLLOW_BEARING);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_map, menu);
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
