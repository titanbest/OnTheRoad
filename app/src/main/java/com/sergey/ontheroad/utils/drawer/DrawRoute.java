package com.sergey.ontheroad.utils.drawer;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class DrawRoute extends AsyncTask<String, Void, String> {

    private GoogleMap mMap;

    public DrawRoute(GoogleMap mMap) {
        this.mMap = mMap;
    }

    @Override
    protected String doInBackground(String... url) {
        String data = "";
        try {
            data = getJsonRoutePoint(url[0]);
            Log.d("Background Task data", data);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        RouteDrawerTask routeDrawerTask = new RouteDrawerTask(mMap);
        routeDrawerTask.execute(result);
    }

    private String getJsonRoutePoint(String strUrl) throws IOException {
        String data = "";

        URL url = new URL(strUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();

        try (InputStream iStream = urlConnection.getInputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuilder sb = new StringBuilder();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("getJsonRoutePoint", data);
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            urlConnection.disconnect();
        }
        return data;
    }

}