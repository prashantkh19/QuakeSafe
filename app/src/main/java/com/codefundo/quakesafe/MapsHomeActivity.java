package com.codefundo.quakesafe;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Pair;
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
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;

public class MapsHomeActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

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

//        Polygon polygon = mMap.addPolygon(new PolygonOptions()
//                .add(new LatLng(28.36267, 75.58565), new LatLng(28.36276, 75.58629), new LatLng(28.36185, 75.58644), new LatLng(28.36176, 75.58576))
//                .strokeColor(0x7FFF0000)
//                );
//        polygon.setFillColor(0x7FFF0000);
//        polygon.setStrokeWidth(50f);
//        mMap.moveCamera(new CameraUpdate());

        LatLng l1 = new LatLng(38.817495	,-75.68303);
        LatLng l2 = new LatLng(38.817401	,-75.68298);
        LatLng l3 = new LatLng(38.817443,	-75.682849	);
        LatLng l4 = new LatLng(38.817443,	-75.682849);

        double r = 5/111139;
        LatLng l1_ = getNewLatLng(l1,l3,r).first;
        LatLng l2_ = getNewLatLng(l2,l4,r).first;
        LatLng l3_ = getNewLatLng(l1,l3,r).second;
        LatLng l4_ = getNewLatLng(l2,l4,r).second;


//        Polygon polygon1 = mMap.addPolygon(new PolygonOptions()
//                .add(new LatLng(38.819893,-75.68537), new LatLng(38.819893,-75.685429), new LatLng(38.819814,	-75.685429	), new LatLng(38.819814,-75.68537))
//                .strokeColor(0x7FFF0000)
//        );
//        polygon1.setFillColor(0x7FFF0000);
//        polygon1.setStrokeWidth(50f);
//
        Polygon polygon1c = mMap.addPolygon(new PolygonOptions()
                .add(l1_,l2_,l3_,l4_)
                .strokeColor(0x7FFF0000)
        );
        polygon1c.setFillColor(0x7F0000FF);



        Polygon polygon2 = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(38.817495	,-75.68303	), new LatLng(38.817401	,-75.68298	), new LatLng(38.817443,	-75.682849), new LatLng(38.817443,	-75.682849))
                .strokeColor(0x7FFF0000)
        );
        polygon2.setFillColor(0x7FFF0000);
        polygon2.setStrokeWidth(50f);

//        Polygon polygon2c = mMap.addPolygon(new PolygonOptions()
//                .add(new LatLng(38.817495+0.00017995,-75.68303-0.00017995	), new LatLng(38.817401+0.00017995	,-75.68298+0.00017995	), new LatLng(38.817443-0.00017995,	-75.682849+0.00017995), new LatLng(38.817443-0.00017995,	-75.682849-0.00017995))
//                .strokeColor(0x7FFF0000)
//        );
//        polygon2c.setFillColor(0x7F0000FF);
//
//
//
//        Polygon polygon3 = mMap.addPolygon(new PolygonOptions()
//                .add(new LatLng(38.762018	,-75.696625	), new LatLng(38.762147	,-75.69658	), new LatLng(38.762172	,-75.6967), new LatLng(	38.762044,	-75.696745))
//                .strokeColor(0x7FFF0000)
//        );
//        polygon3.setFillColor(0x7FFF0000);
//        polygon3.setStrokeWidth(50f);
//
//        Polygon polygon3c = mMap.addPolygon(new PolygonOptions()
//                .add(new LatLng(38.762018+0.00017995	,-75.696625-0.00017995	), new LatLng(38.762147+0.00017995	,-75.69658+0.00017995	), new LatLng(38.762172-0.00017995	,-75.6967+0.00017995), new LatLng(	38.762044-0.00017995,	-75.696745-0.00017995))
//                .strokeColor(0x7FFF0000)
//        );
//        polygon3c.setFillColor(0x7F0000FF);
//
//
//
//        Polygon polygon4 = mMap.addPolygon(new PolygonOptions()
//                .add(new LatLng(38.764937,	-75.69347), new LatLng(38.764981	,-75.693595	), new LatLng(38.764883	,-75.693651), new LatLng(	38.764839,	-75.693526))
//                .strokeColor(0x7FFF0000)
//        );
//        polygon4.setFillColor(0x7FFF0000);
//        polygon4.setStrokeWidth(50f);
//
//        Polygon polygon4c = mMap.addPolygon(new PolygonOptions()
//                .add(new LatLng(38.764937+0.00017995,	-75.69347-0.00017995), new LatLng(38.764981+0.00017995	,-75.693595+0.00017995	), new LatLng(38.764883-0.00017995	,-75.693651+0.00017995), new LatLng(	38.764839-0.00017995,	-75.693526-0.00017995))
//                .strokeColor(0x7FFF0000)
//        );
//        polygon4c.setFillColor(0x7F0000FF);
//
//
//
//        Polygon polygon5 = mMap.addPolygon(new PolygonOptions()
//                .add(new LatLng(38.766027	,-75.688199), new LatLng(38.766016	,-75.688219), new LatLng(38.765953	,-75.688162), new LatLng(	38.766051,	-75.687986))
//                .strokeColor(0x7FFF0000)
//        );
//        polygon5.setFillColor(0x7FFF0000);
//        polygon5.setStrokeWidth(50f);
//
//        Polygon polygon5c = mMap.addPolygon(new PolygonOptions()
//                .add(new LatLng(38.766027+0.00017995	,-75.688199-0.00017995), new LatLng(38.766016+0.00017995	,-75.688219+0.00017995), new LatLng(38.765953-0.00017995	,-75.688162+0.00017995), new LatLng(	38.766051-0.00017995,	-75.687986-0.00017995))
//                .strokeColor(0x7FFF0000)
//        );
//        polygon5c.setFillColor(0x7F0000FF);


        ArrayList<LatLng> al = new ArrayList<LatLng>();
        al.add(new LatLng(28.36267, 75.58565));
        al.add(new LatLng(28.36276, 75.58629));
        al.add(new LatLng(28.36185, 75.58644));
        al.add(new LatLng(28.36176, 75.58576));

// Outside
//        Log.i("PolyUtil Check", PolyUtil.containsLocation(new LatLng(28.362309, 75.585494),al,false)+" a ");
//        Toast.makeText(this, PolyUtil.containsLocation(new LatLng(28.362309, 75.585494),al,false)+" a ", Toast.LENGTH_SHORT).show();

// Inside
//        Log.i("PolyUtil Check", PolyUtil.containsLocation(new LatLng(28.362274, 75.586038),al,false)+" a ");
//        Toast.makeText(this, PolyUtil.containsLocation(new LatLng(28.362274, 75.586038),al,false)+" a ", Toast.LENGTH_SHORT).show();

//Road
        Log.i("PolyUtil Check", PolyUtil.containsLocation(new LatLng(28.362300, 75.585630),al,false)+" a ");
        Toast.makeText(this, PolyUtil.containsLocation(new LatLng(28.362300, 75.585630),al,false)+" a ", Toast.LENGTH_SHORT).show();

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public Pair<LatLng,LatLng> getNewLatLng(LatLng l1, LatLng l3, double r){
        //diag1
        double slope = (l1.latitude-l3.longitude)/(l1.longitude-l3.longitude);

        double angle = Math.atan(slope);
        //+r for 1
        LatLng newlatlng = new LatLng(
                r*Math.sin(angle)+l1.latitude,r*Math.cos(angle)+l1.longitude
        );
        //-r for 1
        LatLng newlatlng2 = new LatLng(
                -1*r*Math.sin(angle)+l1.latitude,-1*r*Math.cos(angle)+l1.longitude
        );

        LatLng l_final=null;
        //choose
        if(distance(newlatlng.latitude,newlatlng.longitude,l3.latitude,l3.longitude)>distance(newlatlng2.latitude,newlatlng2.longitude,l3.latitude,l3.longitude)){
            l_final = newlatlng;
        }else{
            l_final = newlatlng2;
        }

        //+r for 2
        LatLng newlatlng_ = new LatLng(
                r*Math.sin(angle)+l3.latitude,r*Math.cos(angle)+l3.longitude
        );
        //-r for 2
        LatLng newlatlng2_ = new LatLng(
                -1*r*Math.sin(angle)+l3.latitude,-1*r*Math.cos(angle)+l3.longitude
        );

        LatLng l_final_=null;
        //choose
        if(distance(newlatlng_.latitude,newlatlng_.longitude,l1.latitude,l1.longitude)>distance(newlatlng2_.latitude,newlatlng2_.longitude,l1.latitude,l1.longitude)){
            l_final_ = newlatlng_;
        }else{
            l_final_ = newlatlng2_;
        }

        return new Pair<>(l_final,l_final_);
    }
    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
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
        mLastLocation = location;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),14f));


        Circle circle = mMap.addCircle(new CircleOptions()
        .center(new LatLng(location.getLatitude(),location.getLongitude()))
        .radius(500)
        .strokeColor(Color.GREEN)
        .strokeWidth(10)
        );
        circle.setFillColor(Color.argb(50,0,255,0));

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
        Toast.makeText(this, mCurrLocationMarker.getPosition()+"abcd", Toast.LENGTH_SHORT).show();
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

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
        return (dist*1000);
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
    public boolean checkLocationPermission(){
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

}