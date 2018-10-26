package com.codefundo.quakesafe;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MapsHomeActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final String TAG = "MapsHomeActivity";

    Button bt;
    CardView cv_text;
    private GoogleMap mMap;
    ImageView hamburger;
    DrawerLayout mDrawerLayout;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    TextView tv_is_safe, tv_index;
    ArrayList<ItemLocation> latlngArray = new ArrayList<>();

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_home);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        hamburger = (ImageView) findViewById(R.id.hamburger);
        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //initialising button
        bt = (Button) findViewById(R.id.bt);
        cv_text = (CardView) findViewById(R.id.cv_text);
        tv_is_safe = (TextView) findViewById(R.id.tv_is_safe);
        tv_index = (TextView) findViewById(R.id.tv_index);
        cv_text.setVisibility(View.GONE);
        bt.setVisibility(View.VISIBLE);


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrLocationMarker != null) {
                    getData(mCurrLocationMarker.getPosition());
                } else {
                    Toast.makeText(MapsHomeActivity.this, "Try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.setPadding(2,0,0,0);
        mMap.getUiSettings().setCompassEnabled(false);



        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14f));

                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                mCurrLocationMarker.showInfoWindow();
                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

//                //stop location updates
//                if (mGoogleApiClient != null) {
//                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//                }

                //updating safe parameter
                double min_distance = 10000;
                boolean flag = true;
                int k = Math.min(latlngArray.size(), 50);
                for (int i = 0; i < k; i++) {
                    ArrayList<LatLng> boundary = new ArrayList<>();
                    boundary.add(latlngArray.get(i).latLngA);
                    boundary.add(latlngArray.get(i).latLngB);
                    boundary.add(latlngArray.get(i).latLngC);
                    boundary.add(latlngArray.get(i).latLngD);
                    min_distance = Math.min(min_distance, distance(latLng.latitude, latLng.longitude, latlngArray.get(i).getCenterLatLng().latitude, latlngArray.get(i).getCenterLatLng().longitude));
                    if (PolyUtil.containsLocation(latLng, boundary, false)) {
                        //safe
                        flag = false;
                    }
                }

                if (flag) {
                    //safe
                    tv_is_safe.setText("SAFE");
                    tv_is_safe.setTextColor(ContextCompat.getColor(MapsHomeActivity.this, R.color.safe));
                    tv_index.setVisibility(View.VISIBLE);
                    tv_index.setText("Safety Index : " + String.format("%.2f", min_distance));
                } else {
                    //unsafe
                    tv_is_safe.setText("UNSAFE");
                    tv_is_safe.setTextColor(ContextCompat.getColor(MapsHomeActivity.this, R.color.unsafe));
                    tv_index.setVisibility(View.GONE);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f));
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(Location location) {

        Log.d("llllllllllllllllllllll", "changed " + location.getLatitude() + " " + location.getLongitude());
        mLastLocation = location;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14f));


        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        //updating safe parameter
//        double min_distance = 10000;
//        boolean flag = true;
//        int k = Math.min(latlngArray.size(), 20);
//        for (int i = 0; i < k; i++) {
//            ArrayList<LatLng> boundary = new ArrayList<>();
//            boundary.add(latlngArray.get(i).latLngA);
//            boundary.add(latlngArray.get(i).latLngB);
//            boundary.add(latlngArray.get(i).latLngC);
//            boundary.add(latlngArray.get(i).latLngD);
//            min_distance = Math.min(min_distance, distance(latLng.latitude, latLng.longitude, latlngArray.get(i).getCenterLatLng().latitude, latlngArray.get(i).getCenterLatLng().longitude));
//            if (PolyUtil.containsLocation(latLng, boundary, false)) {
//                //safe
//                flag = false;
//            }
//        }
//        if (flag) {
//            //safe
//            tv_is_safe.setText("SAFE!");
//            tv_is_safe.setTextColor(ContextCompat.getColor(this, R.color.safe));
//            tv_index.setVisibility(View.VISIBLE);
//            tv_index.setText(min_distance + "");
//        } else {
//            //unsafe
//            tv_is_safe.setText("UNSAFE!");
//            tv_is_safe.setTextColor(ContextCompat.getColor(this, R.color.unsafe));
//            tv_index.setVisibility(View.GONE);
//        }

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist * 1000);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    ProgressDialog progressDialog;
    private void getData(final LatLng latLng) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Calculating Safe Locations..");
        progressDialog.setCancelable(false);
        class GetData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                parseJSON(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    URL url = new URL("https://earthquakerapp.azurewebsites.net/get_safe_location/");
                    String urlParams = "latitude=" + latLng.latitude +
                            "&longitude=" + latLng.longitude +
                            "&distance=" + "0.3";

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    StringBuilder sb = new StringBuilder();

                    OutputStream os = con.getOutputStream();
                    os.write(urlParams.getBytes());
                    os.flush();
                    os.close();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    String s = sb.toString().trim();
                    return s;

                } catch (IOException e) {
                    // Toast.makeText(getActivity(), "Please Check your Connection", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return "error";
                }
            }

        }
        GetData gd = new GetData();
        gd.execute();
    }

    private void parseJSON(String json) {
        boolean error = true;
        String message = "";
        Log.i("aaaaaaaaa", json);
        try {
            JSONObject root = new JSONObject(json);
            error = root.getBoolean("error");
            if (!error) {
                JSONArray jsonArray = root.getJSONArray("response");
//                Toast.makeText(MapsHomeActivity.this, jsonArray.length() + "abcd", Toast.LENGTH_SHORT).show();
                if (jsonArray.length() > 0)
                    latlngArray.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    Log.i("aaaaaaaaa", i + "");
                    JSONObject o = jsonArray.getJSONObject(i);
                    ItemLocation itemLocation = new ItemLocation(
                            new LatLng(Double.parseDouble(o.getString("lat1")), Double.parseDouble(o.getString("lon1"))),
                            new LatLng(Double.parseDouble(o.getString("lat2")), Double.parseDouble(o.getString("lon2"))),
                            new LatLng(Double.parseDouble(o.getString("lat3")), Double.parseDouble(o.getString("lon3"))),
                            new LatLng(Double.parseDouble(o.getString("lat4")), Double.parseDouble(o.getString("lon4"))),
                            Double.parseDouble(o.getString("height")),
                            Double.parseDouble(o.getString("radius"))
                    );
                    latlngArray.add(itemLocation);
                }
//                Toast.makeText(MapsHomeActivity.this, latlngArray.size() + "abcd", Toast.LENGTH_SHORT).show();
                showOnMap();
            } else {
                Toast.makeText(MapsHomeActivity.this, "Connection error!", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public Pair<LatLng, LatLng> getNewLatLng(LatLng l1, LatLng l3, double r) {
        //diag1
        double slope = (l1.latitude - l3.latitude) / (l1.longitude - l3.longitude);

//        Log.d("slope",slope+"");
        double angle = Math.atan(slope);
//        Log.d("angle",angle+"");

        //+r for 1
        LatLng newlatlng = new LatLng(
                r * Math.sin(angle) + l1.latitude, r * Math.cos(angle) + l1.longitude
        );
        //-r for 1
        LatLng newlatlng2 = new LatLng(
                -1 * r * Math.sin(angle) + l1.latitude, -1 * r * Math.cos(angle) + l1.longitude
        );

        LatLng l_final = null;
        //choose
        if (distance(newlatlng.latitude, newlatlng.longitude, l3.latitude, l3.longitude) > distance(newlatlng2.latitude, newlatlng2.longitude, l3.latitude, l3.longitude)) {
            l_final = newlatlng;
        } else {
            l_final = newlatlng2;
        }

//        Log.d("lat",l_final.latitude+"");
//        Log.d("lon",l_final.longitude+"");

        //+r for 2
        LatLng newlatlng_ = new LatLng(
                r * Math.sin(angle) + l3.latitude, r * Math.cos(angle) + l3.longitude
        );
        //-r for 2
        LatLng newlatlng2_ = new LatLng(
                -1 * r * Math.sin(angle) + l3.latitude, -1 * r * Math.cos(angle) + l3.longitude
        );

        LatLng l_final_ = null;
        //choose
        if (distance(newlatlng_.latitude, newlatlng_.longitude, l1.latitude, l1.longitude) > distance(newlatlng2_.latitude, newlatlng2_.longitude, l1.latitude, l1.longitude)) {
            l_final_ = newlatlng_;
        } else {
            l_final_ = newlatlng2_;
        }

        return new Pair<>(l_final, l_final_);
    }

    //showing building polygon on maps using the server response
    private void showOnMap() {
        bt.setVisibility(View.GONE);
        cv_text.setVisibility(View.VISIBLE);
        Collections.sort(latlngArray, new Comparator<ItemLocation>() {
            @Override
            public int compare(ItemLocation o1, ItemLocation o2) {
                if (distance(mCurrLocationMarker.getPosition().latitude, mCurrLocationMarker.getPosition().longitude, o1.getCenterLatLng().latitude, o1.getCenterLatLng().longitude) > distance(mCurrLocationMarker.getPosition().latitude, mCurrLocationMarker.getPosition().longitude, o2.getCenterLatLng().latitude, o2.getCenterLatLng().longitude)) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        for (int i = 0; i < latlngArray.size(); i++) {
            LatLng l1 = latlngArray.get(i).latLngA;
            LatLng l2 = latlngArray.get(i).latLngB;
            LatLng l3 = latlngArray.get(i).latLngC;
            LatLng l4 = latlngArray.get(i).latLngD;

            double r = latlngArray.get(i).radius / 111139;

            //updated latlngs
            LatLng l1_ = getNewLatLng(l1, l3, r).first;
            LatLng l2_ = getNewLatLng(l2, l4, r).first;
            LatLng l3_ = getNewLatLng(l1, l3, r).second;
            LatLng l4_ = getNewLatLng(l2, l4, r).second;

//            Log.i("l1",l1.latitude+" "+l1.longitude);
//            Log.i("l1",l2.latitude+" "+l2.longitude);
//            Log.i("l1",l3.latitude+" "+l3.longitude);
//            Log.i("l1",l4.latitude+" "+l4.longitude);
//
//            Log.i("l1",l1_.latitude+" "+l1_.longitude);
//            Log.i("l1",l2_.latitude+" "+l2_.longitude);
//            Log.i("l1",l3_.latitude+" "+l3_.longitude);
//            Log.i("l1",l4_.latitude+" "+l4_.longitude);
//
//            Log.i("r",r+"");

            latlngArray.get(i).setLatLngA(l1_);
            latlngArray.get(i).setLatLngB(l2_);
            latlngArray.get(i).setLatLngC(l3_);
            latlngArray.get(i).setLatLngD(l4_);

            Polygon polygon1 = mMap.addPolygon(new PolygonOptions()
                            .add(l1_, l2_, l3_, l4_)
                    .strokeColor(0x9fd32f2f)
            );
            polygon1.setFillColor(0x9fd32f2f);

            Polygon polygon2 = mMap.addPolygon(new PolygonOptions()
                            .add(l1, l2, l3, l4)
                    .strokeColor(0x9f9a0007)
            );
            polygon2.setFillColor(0x9f9a0007);
        }

        Circle circle = mMap.addCircle(new CircleOptions()
                .center(mCurrLocationMarker.getPosition())
                .radius(500)
                .strokeColor(Color.GREEN)
                .strokeWidth(10)
        );
        circle.setFillColor(Color.argb(80, 0, 255, 0));

        //updating safe parameter
        double min_distance = 10000;
        boolean flag = true;
        int k = Math.min(latlngArray.size(), 20);
        for (int i = 0; i < k; i++) {
            ArrayList<LatLng> boundary = new ArrayList<>();
            boundary.add(latlngArray.get(i).latLngA);
            boundary.add(latlngArray.get(i).latLngB);
            boundary.add(latlngArray.get(i).latLngC);
            boundary.add(latlngArray.get(i).latLngD);
            min_distance = Math.min(min_distance, distance(mCurrLocationMarker.getPosition().latitude, mCurrLocationMarker.getPosition().longitude, latlngArray.get(i).getCenterLatLng().latitude, latlngArray.get(i).getCenterLatLng().longitude));
            if (PolyUtil.containsLocation(mCurrLocationMarker.getPosition(), boundary, false)) {
                //safe
                flag = false;
            }
        }
        if (flag) {
            //safe
            tv_is_safe.setText("SAFE");
            tv_is_safe.setTextColor(ContextCompat.getColor(this, R.color.safe));
            tv_index.setVisibility(View.VISIBLE);
            tv_index.setText("Safety Index : " + String.format("%.2f", min_distance));
        } else {
            //unsafe
            tv_is_safe.setText("UNSAFE");
            tv_is_safe.setTextColor(ContextCompat.getColor(this, R.color.unsafe));
            tv_index.setVisibility(View.GONE);
        }
        progressDialog.dismiss();
        LatLng location = findDestination(mCurrLocationMarker.getPosition());
        mMap.addMarker(new MarkerOptions().position(location).title("Safest spot near you")).showInfoWindow();
        mCurrLocationMarker.hideInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17f));
        //show navigation here
        startNavigation(mCurrLocationMarker.getPosition(), new LatLng(location.latitude, location.longitude));
    }


    public LatLng findDestination(LatLng current) {
        double x, y, r;
        LatLng destination = current, check;
        double max_dist = 0, dist;
        for (int i = 1; i <= 100; i++) {
            r = (double) i / 111139;
            int N = (int) Math.min(2 * Math.PI * i, 100);
            Log.d("ppppppppppp", "N " + N);
            for (int n = 1; n <= N; n++) {
                check = new LatLng(current.latitude + r * Math.sin(2 * Math.PI * n / (double) N), current.longitude + r * Math.cos(2 * Math.PI * n / (double) N));

                dist = getMaxIndex(check);
                if (dist > max_dist) {
                    max_dist = dist;
                    destination = check;
                }
            }
        }
        return destination;
    }

    private double getMaxIndex(LatLng location) {
        double min_distance = 151511515;
        int k = Math.min(latlngArray.size(), 50);
        boolean is = true;
        for (int i = 0; i < k; i++) {
            ArrayList<LatLng> boundary = new ArrayList<>();
            boundary.add(latlngArray.get(i).latLngA);
            boundary.add(latlngArray.get(i).latLngB);
            boundary.add(latlngArray.get(i).latLngC);
            boundary.add(latlngArray.get(i).latLngD);
            min_distance = Math.min(min_distance, distance(location.latitude, location.longitude, latlngArray.get(i).getCenterLatLng().latitude, latlngArray.get(i).getCenterLatLng().longitude));
            if (PolyUtil.containsLocation(location, boundary, false)) {
                //safe
                is = false;
            }
        }
        if (is)
            return min_distance;
        else
            return -1;
    }

    private void startNavigation(LatLng start, LatLng end) {

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(start, end);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            Log.d("tttttttt", jsonData[0]);
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = new ArrayList<>();

            try {
                jObject = new JSONObject(jsonData[0]);
                Toast.makeText(MapsHomeActivity.this, jObject.getString("error_message"), Toast.LENGTH_SHORT).show();
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("tttttttt", routes.toString());
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            Log.d("tttttttt", result.toString());
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap point = path.get(j);

                    double lat = Double.parseDouble((String) point.get("lat"));
                    double lng = Double.parseDouble((String) point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null)
                mMap.addPolyline(lineOptions);
//            else
//                Toast.makeText(MapsHomeActivity.this,"not available!",Toast.LENGTH_SHORT).show();
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=walking";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&key=AIzaSyDigv2JcnMYg1Y1W9kFA5_gYJTUpuyMgnY";

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

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
