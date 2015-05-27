
package com.bielicki.brandon.mbira;


/*
 * Created by Brandon on 2/25/2015.
 */

import android.graphics.Bitmap;

public class ProjectData {
    private static final ProjectData ProjectData = new ProjectData();

    private Bitmap projectImage;
    private String projectShortDescription;
    private String projectLongDescription;
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

    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String name) {
        this.projectName = name;
    }

    public String getProjectShortDescription() {
        return projectShortDescription;
    }
    public void setProjectShortDescription(String projectShortDescription) {
        this.projectShortDescription = projectShortDescription;
    }

    public String getProjectLongDescription() {
        return projectLongDescription;
    }
    public void setProjectLongDescription(String projectLongDescription) {
        this.projectLongDescription = projectLongDescription;
    }
}
