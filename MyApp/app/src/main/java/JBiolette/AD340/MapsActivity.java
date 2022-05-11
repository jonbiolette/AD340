package JBiolette.AD340;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import JBiolette.AD340.databinding.ActivityMapsBinding;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        //Default location
        userLocation = new Location("Seattle");
        userLocation.setLatitude(47.6062);
        userLocation.setLongitude(-122.335167);
        setupPermissions();
        mapFragment.getMapAsync(this);


    }

    private void setupPermissions() {
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
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
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                RECORD_REQUEST_CODE);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RECORD_REQUEST_CODE: {
                if (grantResults.length == 0

                        || grantResults[0] !=

                        PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user");

                } else {
                    Log.i(TAG, "Permission has been granted by user");
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
                    userLocation = fusedLocationProviderClient.getLastLocation().getResult();
                }
            }
        }
    }

        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady (GoogleMap googleMap){
            //setupPermissions();

            //users location
            mMap = googleMap;
            googleMap.setMapType(1);

            LatLng current = new LatLng(userLocation.getLatitude(),userLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 14));
            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .position(current)
                    .title("Current Location"));

            // Camera Locations

            String[][] data = new String[300][2];
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
            ArrayList<String> location = new ArrayList<>();
            ArrayList<String> description = new ArrayList<>();

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
                    Toast.makeText(MapsActivity.this, "Not Connected", Toast.LENGTH_LONG).show();
                }
            });
            queue.add(objReq);
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
}




