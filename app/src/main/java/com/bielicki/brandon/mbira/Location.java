package com.bielicki.brandon.mbira;

import android.graphics.Bitmap;
import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Brandon on 3/3/2015.
 */
public class Location extends MapItem {
    public int id;
    public int project_id;
    public int exhibit_id;
    public int pid;
    public int sid;
    public String name;
    public String description;
    public String dig_deeper;
    public double latitude;
    public double longitude;
    public String thumb_path;
    public Boolean toggle_dig_deeper;
    public Boolean toggle_media;
    public Boolean toggle_comments;
    public Bitmap locationImage;
    public ArrayList<Bitmap> media;
}
