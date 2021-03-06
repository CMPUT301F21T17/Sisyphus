/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sisyphus.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleMaps extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = GoogleMaps.class.getSimpleName();
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;
    private Marker marker;
    private LatLng markerPos = new LatLng(0, 0);
    private String pickedPlace = null;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private Geocoder geocoder;

    /**
     * A function called to create a map a retrieve data from it
     * @param savedInstanceState
     *  saved instances' state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        Context context = this.getApplicationContext();

        geocoder = new Geocoder(context);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        final Button confirm = findViewById(R.id.confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onConfirm();
            }
        });

    }

    /**
     * Request location permission, so that we can get the location of the
     *  device. The result of the permission request is handled by a callback,
     *  onRequestPermissionsResult.
     */
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * function to handle the request and verification of permissions for location
     * @param requestCode
     *  Code of permissions requested
     * @param permissions
     *  list of strings of permissions requested
     * @param grantResults
     *  results of permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        // If request is cancelled, the result arrays are empty.
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        }
        updateLocationUI();
    }

    /**
     * Update the location UI to reflect location permissions
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Get the best and most recent location of the device, which may be null in rare
     *  cases when a location is not available.
     */
    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                markerPos = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                marker.setPosition(markerPos);
                                getAddress(marker);
                                marker.setSnippet(pickedPlace);
                                marker.showInfoWindow();
                            }

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Function to retrieve location data of marker
     * @param marker
     *  marker to retrieve location data from
     */
    private void getAddress(Marker marker){
        if (marker != null) {
            LatLng userLocation = marker.getPosition();

            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocation(userLocation.latitude, userLocation.longitude, 5);
                if (addresses.size() > 0) {
                    Log.d("OOPS", "Location: " + addresses.get(0).getAddressLine(0));
                    pickedPlace = addresses.get(0).getAddressLine(0);

                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * function called when confirm button is clicked to return result of map
     */
    private void onConfirm() {
        Intent intent = new Intent();
        Bundle extras = new Bundle();
        if (pickedPlace != null) {
            extras.putString("LOCATION", pickedPlace);
        }

        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * function called when returning to a previous map view
     * @param outState
     *  current state of map
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * function called to setup a GoogleMap
     * @param googleMap
     *  GoogleMap to setup
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("OOPS", String.format("%f", markerPos.latitude));
        Log.d("OOPS", String.format("%f", markerPos.longitude));
        marker = mMap.addMarker(new MarkerOptions().position(markerPos).title("Location"));
        assert marker != null;
        marker.setDraggable(true);

        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();

        GoogleMap.OnMarkerDragListener dragListener = new GoogleMap.OnMarkerDragListener() {
            /**
             * A listener called when marker is moved
             * @param marker
             *  marker being moved
             */
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {
                Log.d("OOPS", "Dragging");
            }

            /**
             * A listener called when a drag has finished to return the address
             * @param marker
             *  marker to get address from
             */
            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                Log.d("OOPS", "Drag end");
                getAddress(marker);
                marker.setSnippet(pickedPlace);
                marker.showInfoWindow();
            }

            /**
             * A listener to hide location disply while being moved
             * @param marker
             *  marker being dragged
             */
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {
                Log.d("OOPS", "Dragging start");
                marker.hideInfoWindow();
            }
        };

        mMap.setOnMarkerDragListener(dragListener);
    }
}
