package com.bielicki.brandon.mbira;

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

    private Constants() {
        WEBSERVICE = "http://dev2.matrix.msu.edu/mbira-android/webService.php";
        PROJECT_ID = "31";
        BASE_PATH = "http://dev2.matrix.msu.edu/mbira-android/kora/plugins/mbira_plugin/";
    }
}
