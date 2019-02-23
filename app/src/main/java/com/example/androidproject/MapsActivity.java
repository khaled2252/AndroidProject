package com.example.androidproject;

import android.graphics.Color;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Random rnd = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.setFinishOnTouchOutside(false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        LatLng[] starts = new LatLng[PastTripsActivity.tripList.size()];
        LatLng[] ends = new LatLng[PastTripsActivity.tripList.size()];
        double slat = 0;
        double slon = 0;
        double elat = 0;
        double elon = 0;

        for (int i = 0; i < PastTripsActivity.tripList.size(); i++) {
            try {
                slat = geocoder.getFromLocationName(PastTripsActivity.tripList.get(i).getStartPoint(), 1).get(0).getLatitude();
                slon = geocoder.getFromLocationName(PastTripsActivity.tripList.get(i).getStartPoint(), 1).get(0).getLongitude();
                elat = geocoder.getFromLocationName(PastTripsActivity.tripList.get(i).getEndPoint(), 1).get(0).getLatitude();
                elon = geocoder.getFromLocationName(PastTripsActivity.tripList.get(i).getEndPoint(), 1).get(0).getLongitude();
            } catch (IOException e) {
                e.printStackTrace();
            }

            starts[i] = new LatLng(slat, slon);
            ends[i] = new LatLng(elat, elon);

        }


        LatLng alex = new LatLng(31.24, 29.96);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(alex, 11f));

        for (int i = 0; i < PastTripsActivity.tripList.size(); i++) {
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            /*mMap.addMarker(new MarkerOptions().position(starts[i]).title("Start"));
            mMap.addMarker(new MarkerOptions().position(ends[i]).title("End"));
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .add(starts[i], ends[i])
                    .startCap(new RoundCap())
                    .endCap(new RoundCap())
                    .width(10)
                    .color(color));*/
            String url_route = getUrl(starts[i],ends[i]);
            DrawRoute drawRoute = new DrawRoute(googleMap);
            drawRoute.execute(url_route);


        }
    }
    public String getUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        Log.i("kkkk",parameters);
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

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

        /**
         * A method to download json data from url
         */
        private String getJsonRoutePoint(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();
                Log.d("getJsonRoutePoint", data.toString());
                br.close();

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

    }

    public class RouteDrawerTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        private PolylineOptions lineOptions;
        private GoogleMap mMap;
        private int routeColor;

        public RouteDrawerTask(GoogleMap mMap) {
            this.mMap = mMap;
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("RouteDrawerTask", jsonData[0]);
                DataRouteParser parser = new DataRouteParser();
                Log.d("RouteDrawerTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("RouteDrawerTask", "Executing routes");
                Log.d("RouteDrawerTask", routes.toString());

            } catch (Exception e) {
                Log.d("RouteDrawerTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            if (result != null)
                drawPolyLine(result);
        }

        private void drawPolyLine(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            lineOptions = null;

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position =  new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(6);
                if (routeColor == 0)
                    lineOptions.color(0xFF0A8F08);
                else
                    lineOptions.color(routeColor);
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null && mMap != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines draw");
            }
        }

    }
    public class DataRouteParser {
        public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

            List<List<HashMap<String, String>>> routes = new ArrayList<>();
            JSONArray jRoutes;
            JSONArray jLegs;
            JSONArray jSteps;

            try {

                jRoutes = jObject.getJSONArray("routes");

                /** Traversing all routes */
                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<>();

                    /** Traversing all legs */
                    for (int j = 0; j < jLegs.length(); j++) {
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                        /** Traversing all steps */
                        for (int k = 0; k < jSteps.length(); k++) {
                            String polyline = "";
                            polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);

                            /** Traversing all points */
                            for (int l = 0; l < list.size(); l++) {
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("lat", Double.toString((list.get(l)).latitude));
                                hm.put("lng", Double.toString((list.get(l)).longitude));
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        /**
         * Method to decode polyline points
         * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
         */
        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }
    }

}

