package com.bielicki.brandon.mbira;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Brandon on 3/10/2015.
 */
public class Exhibit {
    public int id;
    public int project_id;
    public int pid;
    public String name;
    public String description;
    public String thumb_path;
    public Bitmap exhibitImage;

    //LOCATION RELATED DATA
    private ArrayList<Location> locationArrayList = new ArrayList<Location>();
    public ArrayList<Location> getLocationArrayList() {
        return locationArrayList;
    }
    public void addLocation(Location L) {
        locationArrayList.add(L);
    }

    //AREA RELATED DATA
    private ArrayList<Area> areaArrayList = new ArrayList<Area>();
    public ArrayList<Area> getAreaArrayList() {
        return areaArrayList;
    }
    public void addArea(Area a) {
        areaArrayList.add(a);
    }
}
