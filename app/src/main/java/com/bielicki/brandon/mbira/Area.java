package com.bielicki.brandon.mbira;

import android.graphics.Bitmap;
import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Brandon on 3/3/2015.
 */
public class Area extends MapItem {
    public int id;
    public int project_id;
    public String geojson_path;
    public String name;
    public String description;
    public String dig_deeper;
    public ArrayList<Coordinate> coordinates;
    public Float radius;
    public String shape;
    public String thumb_path;
    public Boolean toggle_dig_deeper;
    public Boolean toggle_media;
    public Boolean toggle_comments;
    public Bitmap areaImage;
    public ArrayList<Bitmap> media;

    //I dont think this is used. Will look into this
    public void addCoordinate(Coordinate c) {
        coordinates.add(c);
    }


}
