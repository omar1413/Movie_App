package com.omar.themovieapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class Utils {


    public static final String LOG_TAG = Utils.class.getSimpleName();

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    public static boolean hasActiveInternetConnection(Context context){
        try {
            return (new FetchInterntAvailabilty().execute(context).get());
        } catch (Exception e){
            return false;
        }

        //return false;
    }

    public static void showToast(Context context, String title){
        Toast.makeText(context, title, Toast.LENGTH_LONG).show();
    }

    static class FetchInterntAvailabilty extends AsyncTask<Context, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Context... context) {
            return hasActiveInternetConnection(context[0]);
        }

        public boolean hasActiveInternetConnection(Context context) {
            if (isNetworkAvailable(context)) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                    urlc.setRequestProperty("User-Agent", "Test");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    return (urlc.getResponseCode() == 200);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error checking internet connection", e);
                }
            } else {
                Log.d(LOG_TAG, "No network available!");
            }
            return false;
        }
    }
}
