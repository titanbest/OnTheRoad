package com.sergey.ontheroad.utils.drawer;

import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sergey.ontheroad.R;

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
            DataRouteParser parser = new DataRouteParser();
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
        if (result != null) drawPolyLine(result);
    }

    private void drawPolyLine(List<List<HashMap<String, String>>> result) {
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

        lineOptions.color(ContextCompat.getColor(DrawRouteMaps.getContext(), R.color.colorRoute));

        if (mMap != null) {
            mMap.addPolyline(lineOptions);
        } else {
            Log.d("onPostExecute", "Without polyline's draw");
        }
    }

}
