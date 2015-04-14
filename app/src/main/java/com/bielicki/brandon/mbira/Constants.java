package com.bielicki.brandon.mbira;

import android.content.Context;

/**
 * Created by Brandon on 2/25/2015.
 */
public class Constants {
    private static final Constants constants = new Constants();
    public String PROJECT_ID;
    public String BASE_PATH;
    public String WEBSERVICE;

    public static Constants get() {
        return constants;
    }

    public Constants() {

    }
}
