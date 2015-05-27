
package com.bielicki.brandon.mbira;


/*
 * Created by Brandon on 2/25/2015.
 */

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.LruCache;

import java.io.File;
import java.util.ArrayList;

public class AppData {
    public boolean isSet = false;
    private static final AppData AppData = new AppData();
    public String username;
    public String password;
    public File httpCacheDir;
    public long httpCacheSize;
    private AppData() {

    }

    public static AppData get() {
        return AppData;
    }

    //PROJECT RELATED DATA
    private String projectImageUrl;
//    private String projectDescription;
    private String projectShortDescription;
    private String projectLongDescripion;
    private String projectName;

    public String getProjectImageUrl() {
        return projectImageUrl;
    }

    public void setProjectImageUrl(String projectImageUrl) {
        this.projectImageUrl = projectImageUrl;
    }

//    public String getProjectDescription() {
//        return projectDescription;
//    }
//
//    public void setProjectDescription(String des) {
//        this.projectDescription = des;
//    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String name) {
        this.projectName = name;
    }

    //LOCATION RELATED DATA
    private ArrayList<Location> locationArrayList = new ArrayList<Location>();
    public ArrayList<Location> getLocationArrayList() {
        return locationArrayList;
    }
    public void addLocation(Location L) {
        locationArrayList.add(L);
    }
    public Location getLocationById(int id) {
        for(int i = 0; i <locationArrayList.size(); i++) {
            Location temp = locationArrayList.get(i);
            if(temp.id == id) {
                return temp;
            }
        }
        return null;
    }

    //AREA RELATED DATA
    private ArrayList<Area> areaArrayList = new ArrayList<Area>();
    public ArrayList<Area> getAreaArrayList() {
        return areaArrayList;
    }
    public void addArea(Area a) {
        areaArrayList.add(a);
    }
    public Area getAreaById(int id) {
        for(int i = 0; i <areaArrayList.size(); i++) {
            Area temp = areaArrayList.get(i);
            if(temp.id == id) {
                return temp;
            }
        }
        return null;
    }

    //EXHIBIT RELATED DATA
    private ArrayList<Exhibit> exhibitArrayList = new ArrayList<Exhibit>();
    public ArrayList<Exhibit> getExhibitArrayList() {
        return exhibitArrayList;
    }
    public void addExhibit(Exhibit e) {
        exhibitArrayList.add(e);
    }

    //EXPLORATION RELATED DATA
    private ArrayList<Exploration> explorationArrayList = new ArrayList<Exploration>();
    public ArrayList<Exploration> getExplorationArrayList() {
        return explorationArrayList;
    }
    public void addExploration(Exploration e) {
        explorationArrayList.add(e);
    }

    public String getProjectShortDescription() {
        return projectShortDescription;
    }

    public void setProjectShortDescription(String projectShortDescription) {
        this.projectShortDescription = projectShortDescription;
    }

    public String getProjectLongDescripion() {
        return projectLongDescripion;
    }

    public void setProjectLongDescripion(String projectLongDescripion) {
        this.projectLongDescripion = projectLongDescripion;
    }
}


