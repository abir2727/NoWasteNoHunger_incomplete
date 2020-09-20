package com.example.nowastenohunger.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.location.LocationManagerCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nowastenohunger.Fragment.MakeDonationsFragment;
import com.example.nowastenohunger.Fragment.SearchDonationsFragment;
import com.example.nowastenohunger.R;

//import com.firebase.geofire.GeoFire;
//import com.firebase.geofire.GeoLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener, MapboxMap.OnMapClickListener, View.OnClickListener {

    String currentUserID;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    private Point originPosition;
    private Point destinationPosition;
    private Marker destinationMarker;
    private Button startButton;
    double x,y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_maps);
        mapView=(MapView) findViewById((R.id.mapView));
        startButton = findViewById(R.id.button);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        Toolbar optionsToolBar = findViewById(R.id.optionsToolBar);
        setSupportActionBar(optionsToolBar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        optionsToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        isLocationEnabled();

        startButton.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        currentUserID = user.getUid();

        databaseReference =  FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
    }

   /* private void makeGeocodeSearch(double lat,double lon) {
        try {
            // Build a Mapbox geocoding request
            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(getString(R.string.access_token))
                    .query(Point.fromLngLat(lon,lat))
                    .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
                    .mode(GeocodingCriteria.MODE_PLACES)
                    .build();
            client.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(Call<GeocodingResponse> call,
                                       Response<GeocodingResponse> response) {
                    if (response.body() != null) {
                        //List<CarmenFeature> results = response.body().features();
                        //if (results.size() > 0) {

                            // Get the first Feature from the successful geocoding response
                          //  CarmenFeature feature = results.get(0);

                            // Get the address string from the CarmenFeature
                            //String carmenFeatureAddress = feature.text();
                            String placeName = response.body().features().get(0).placeName();
                            //String address = feature.placeName().replaceFirst(feature.text().concat(", "),"");
                            Toast.makeText(MapsActivity.this, placeName,Toast.LENGTH_SHORT).show();


                        }
                    else {
                            Toast.makeText(MapsActivity.this, "no results in geocoding request",Toast.LENGTH_SHORT).show();
                        }
                    //}
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                    //Log.e(TAG,"Geocoding Failure: " + throwable.getMessage());
                }
            });
        } catch (ServicesException servicesException) {
            //Logd.e(TAG,"Error geocoding: " + servicesException.toString());
            servicesException.printStackTrace();
        }
    }*/

    private LocationManager locationManager ;
    protected void isLocationEnabled(){
       // Intent intent = new Intent(this,MapsActivity.class);
       // String le = this.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            //return false;
            Toast.makeText(this, "Turn your device location on", Toast.LENGTH_SHORT).show();
            //finish();
        } else {
            //return true;
        }
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        map.addOnMapClickListener(this);
        enableLocation();
    }

    private void enableLocation(){
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            initializeLocationEngine();
            initializeLocationLayer();
        }
        else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            originLocation=location;
            //setCameraPosition(location);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        //Toast
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            enableLocation();
        }
        else{
            Toast.makeText(this, "Please enable location permission", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine(){
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if(lastLocation!=null){
            originLocation = lastLocation;
            //setCameraPosition(lastLocation);
        }
        else{
            locationEngine.addLocationEngineListener(this);
        }
    }

    @SuppressLint("WrongConstant")
    private void initializeLocationLayer(){
        locationLayerPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
    }

    private void setCameraPosition(Location location){

       // y =  location.getLongitude();
        //x =  location.getLatitude();
        //Toast.makeText(this, "Lat "+x+" Long "+y, Toast.LENGTH_SHORT).show();
        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(x,y), 1.0));

    }

    @SuppressWarnings("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        if(locationEngine!=null){
            locationEngine.requestLocationUpdates();
        }
        if(locationLayerPlugin!=null){
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(locationEngine!=null){
            locationEngine.removeLocationUpdates();
        }
        if(locationLayerPlugin!=null){
            locationLayerPlugin.onStop();
        }
        mapView.onStop();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationEngine!=null){
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {

        if(destinationMarker!=null){
            map.removeMarker(destinationMarker);
        }
        destinationMarker=map.addMarker(new MarkerOptions().position(point));
        y =  point.getLongitude();
        x =  point.getLatitude();
        //Toast.makeText(this, "Lat "+x+" Long "+y, Toast.LENGTH_SHORT).show();
        destinationPosition = Point.fromLngLat(point.getLongitude(),point.getLatitude());
        //Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
        originPosition=Point.fromLngLat(originLocation.getLongitude(), originLocation.getLatitude());

        startButton.setEnabled(true);
        //startButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        startButton.setBackground(getDrawable(R.drawable.customized_buttons));
        //startButton.setBackgroundColor(Color.parseColor("F60458"));

        /*DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Drivers");
        GeoFire geoFire = new GeoFire(ref);
        geoFire.setLocation(i+"", new GeoLocation(x,y));
        i++;*/
    }

    @Override
    public void onClick(View v) {
        //System.out.println(currentUserID);
        double latitude = x;
        double longitude = y;

        Map<String, Object> updates = new HashMap<>();

        databaseReference = databaseReference.child(currentUserID);
        updates.put("postLocationLat",latitude);
        updates.put("postLocationlong",longitude);

        databaseReference.updateChildren(updates);

        //Toast.makeText(this, "Location confirmed", Toast.LENGTH_SHORT).show();
        finish();
    }
}