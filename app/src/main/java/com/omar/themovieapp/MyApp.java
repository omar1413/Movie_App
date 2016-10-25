package com.omar.themovieapp;

import android.app.Application;

public class MyApp extends Application {

    public static final String POPULAR_SORT_TYPE = "popular";
    public static final String TOP_RATED_SORT_TYPE = "top_rated";
    public static final String FAVOURITE_SORT_TYPE = "favourte";

    public static final int POPULAR_STATUS = 0;
    public static final int TOP_RATED_STATUS = 1;
    public static final int FAVOURITE_STATUS = 2;
    public static int status = POPULAR_STATUS;

    public static  int moviePosStatus = 0;
}
