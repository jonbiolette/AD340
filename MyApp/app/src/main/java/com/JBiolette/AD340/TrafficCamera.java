package com.JBiolette.AD340;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import com.JBiolette.AD340.databinding.ActivityTrafficCameraBinding;


public class TrafficCamera extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityTrafficCameraBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTrafficCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        connectionTest();


    }
    protected void onResume() {
        super.onResume();
        connectionTest();
    }

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
                        createList();
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



    public void createList () {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";

        final ListView list = findViewById(R.id.list);
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Returns camera's details
                            JSONArray arr = response.getJSONArray("Features");
                            Camera.createArray(arr);
                            arrayList.addAll(Camera.getAllSelectedInfo(1));
                            list.setAdapter(arrayAdapter);

                        } catch (JSONException e) {
                            Log.i("debug", "line 75");
                            e.printStackTrace();
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