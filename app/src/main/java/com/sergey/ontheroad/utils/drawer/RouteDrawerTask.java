package com.sergey.ontheroad.utils.drawer;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sergey.ontheroad.utils.RouteParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteDrawerTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    private GoogleMap mMap;

    RouteDrawerTask(GoogleMap mMap) {
        this.mMap = mMap;
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            RouteParser parser = new RouteParser();
            routes = parser.parse(jObject);
            Log.d("RouteDrawerTask", "Executing routes" + routes.toString());
        } catch (Exception e) {
            Log.d("RouteDrawerTask", e.toString());
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        if (result != null) {
            PolylineOptions lineOptions = new PolylineOptions();
            ArrayList<LatLng> points = new ArrayList<>();

            for (int i = 0; i < result.size(); i++) {

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    points.add(new LatLng(
                            Double.parseDouble(path.get(j).get("lat")),
                            Double.parseDouble(path.get(j).get("lng"))));
                }

                lineOptions.addAll(points);
                lineOptions.width(6);
            }

            lineOptions.color(Color.RED);

            if (mMap != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "Without polyline's draw");
            }
        }
    }
}
