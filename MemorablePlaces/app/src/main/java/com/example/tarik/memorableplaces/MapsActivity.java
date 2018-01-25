package com.example.tarik.memorableplaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;


    public void centerMapOnLocation(Location location,String title){
        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.clear();
        if(title !="Your location"){
            mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,5));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        mMap.setOnMapLongClickListener(this);
        Intent intent = getIntent();
        Integer placeNumber = intent.getIntExtra("placeNumber",0);
        if(placeNumber==0){
            // zoom in on user's location
            locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    centerMapOnLocation(location,"Your location");
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            if(Build.VERSION.SDK_INT<23){
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }else{
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                    Location lastKnowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    centerMapOnLocation(lastKnowLocation,"Your Location");
                }else{
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                }
            }
        }else{
            LatLng location = MainActivity.locations.get(placeNumber);
            String placeName = MainActivity.placesArr.get(placeNumber);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(location).title(placeName));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,5));
        }
    }

    @Override
    public void onMapLongClick(LatLng point) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String address = "";
        try {
            List<Address> listAddresses = geocoder.getFromLocation(point.latitude,point.longitude,1);
            if(listAddresses!=null && listAddresses.size()>0){
                if(listAddresses.get(0).getThoroughfare() !=null){
                    if(listAddresses.get(0).getSubThoroughfare()!=null){
                        address += listAddresses.get(0).getSubThoroughfare() + "";
                    }
                    address += listAddresses.get(0).getThoroughfare();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(address == ""){
            SimpleDateFormat sdf = new SimpleDateFormat("mm:HH yyyy-MM- dd");
            address = sdf.format(new Date());
        }
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(point).title(address));

        MainActivity.placesArr.add(address);
        MainActivity.locations.add(point);
        MainActivity.arrayAdapter.notifyDataSetChanged();
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.tarik.memorableplaces", Context.MODE_PRIVATE);
        try {
            ArrayList<String> latitudes = new ArrayList<>();
            ArrayList<String> longitudes = new ArrayList<>();

            for(LatLng coordinates : MainActivity.locations){
                latitudes.add(Double.toString(coordinates.latitude));
                latitudes.add(Double.toString(coordinates.longitude));
            }
            sharedPreferences.edit().putString("locations",ObjectSerializer.serialize(MainActivity.locations)).apply();
            sharedPreferences.edit().putString("latitudes",ObjectSerializer.serialize(latitudes)).apply();
            sharedPreferences.edit().putString("longitudes",ObjectSerializer.serialize(longitudes)).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);*/
        Toast.makeText(this,"Location Saved",Toast.LENGTH_LONG).show();
    }
}
