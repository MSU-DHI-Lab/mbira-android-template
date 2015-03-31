
package com.bielicki.brandon.mbira;


/*
 * Created by Brandon on 2/25/2015.
 */

import android.graphics.Bitmap;

public class ProjectData {
    private static final ProjectData ProjectData = new ProjectData();

    private Bitmap projectImage;
    private String projectDescription;
    private String projectName;

    private ProjectData() {

    }

    public static ProjectData get() {
        return ProjectData;
    }

    public Bitmap getProjectImage() {
        return projectImage;
    }
    public void setProjectImage(Bitmap projectImage) {
        this.projectImage = projectImage;
    }
    public String getProjectDescription() {
        return projectDescription;
    }
    public void setProjectDescription(String des) {
        this.projectDescription = des;
    }
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String name) {
        this.projectName = name;
    }
}
