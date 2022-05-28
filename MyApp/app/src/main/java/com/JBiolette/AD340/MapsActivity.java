package com.JBiolette.AD340;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.JBiolette.AD340.databinding.ActivityMapsBinding;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,OnMapReadyCallback {

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;


    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static Location userLocation;
    private static final String TAG = "Testing";
    private static final int RECORD_REQUEST_CODE = 101;
    protected LocationManager locationManager;
    private LocationListener ls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        connectionTest();
    }

    protected void onResume() {
        super.onResume();
        connectionTest();
    }

    private void setupPermissions() {
        int permission = ContextCompat.checkSelfPermission(this,
                ACCESS_COARSE_LOCATION);
        if (permission != PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    ACCESS_COARSE_LOCATION)) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this);
                builder.setMessage("Location needed to find nearby traffic camera's.")
                        .setTitle("Permission required");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                makeRequest();

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                makeRequest();
            }
        }
    }

    protected void makeRequest() {

        ActivityCompat.requestPermissions(this,
                new String[]{ACCESS_COARSE_LOCATION},
                RECORD_REQUEST_CODE);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RECORD_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] !=

                        PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user");
                    //Default location
                    userLocation = new Location("Seattle");
                    userLocation.setLatitude(47.6);
                    userLocation.setLongitude(-122.335167);
                    onLocationReady(mMap,userLocation);

                } else {
                    Log.i(TAG, "Permission has been granted by user");
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    ls = new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            onLocationReady(mMap,userLocation);
                        }
                    };
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30, 10, ls);
                }
            }
        }
        //users location

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        createList(mMap);

        if(ContextCompat.checkSelfPermission(this,ACCESS_COARSE_LOCATION) == 0){
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            onLocationReady(mMap,userLocation);
        }else {
            setupPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    public void onLocationReady(GoogleMap googleMap,Location location) {
        mMap = googleMap;
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 14));
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .position(current)
                .title("Current Location"));
    }


    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
                .show();
        return false;
    }

    //Test connection
    @SuppressWarnings("MissingPermission")
    public void connectionTest () {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        Network currentNetwork = connectivityManager.getActiveNetwork();
        NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(currentNetwork);
        LinkProperties linkProperties = connectivityManager.getLinkProperties(currentNetwork);

        connectivityManager.registerDefaultNetworkCallback
                (new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        Log.e("Network", "The default network is now: " + network);
                    }

                    @Override
                    public void onLost(Network network) {
                        Log.e("Network", "Lost Connection");
                        Snackbar lost = Snackbar.make((findViewById(R.id.cameraList)), "Connection has been lost", Snackbar.LENGTH_LONG);
                        lost.show();
                    }
                });

        //When unavailable
        NetworkInfo activeNetwrokInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetwrokInfo == null) {
            Snackbar unavailable = Snackbar.make((findViewById(R.id.cameraList)), "Network is unavailable", Snackbar.LENGTH_LONG);
            unavailable.show();
        }

    }

    //Create Traffic cams
    public void createList (GoogleMap googleMap) {
        String[][] data = new String[300][2];
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
        ArrayList<String> location = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();

        final ListView list = findViewById(R.id.list);
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int arrSize = 0;
                        try {
                            JSONArray arr = response.getJSONArray("Features");
                            arrSize = arr.length();
                            Camera.createArray(arr);
                            location.addAll(Camera.getAllSelectedInfo(1));
                            description.addAll(Camera.getAllSelectedInfo(4));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < location.size(); i++) {
                            data[i][0] = location.get(i);
                            data[i][1] = description.get(i);
                            String[] latLing = data[i][1].split(",");
                            Double lat = Double.parseDouble(latLing[0].substring(1));
                            Double longe = Double.parseDouble(latLing[1].substring(0, latLing[1].length() - 1));
                            LatLng camLocation = new LatLng(lat, longe);

                            googleMap.addMarker(new MarkerOptions()
                                    .position(camLocation)
                                    .title(data[i][0]));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                connectionTest();
            }
        });
        queue.add(objReq);
    }
}









