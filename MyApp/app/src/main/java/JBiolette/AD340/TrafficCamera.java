package JBiolette.AD340;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import JBiolette.AD340.databinding.ActivityTrafficCameraBinding;


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



        //Testing volley
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";


        final ListView list = findViewById(R.id.list);
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayList);

        TextView textView = new TextView(this);
        // Testing Array
        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Returns camera's details
                            JSONArray arr = response.getJSONArray("Features");
                            //Selecting a single camera

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject camera = arr.getJSONObject(i);

                                //Camera.cameraData(camera);
                                //Selecting a camera's specific details from array
                                String[] caminfo = camera.getJSONArray("Cameras").toString().split(":");

                                String camId = caminfo[1].substring(0, (caminfo[1].length() - 14));
                                String camDesc = caminfo[2].substring(0, (caminfo[2].length() - 11));
                                String camImage = caminfo[3].substring(0, (caminfo[3].length() - 7));
                                String camType = caminfo[4].substring(0, (caminfo[4].length() - 2));
                                arrayList.add(camDesc);


                                Log.i("Camera " + i + " location: ", camDesc);
                            }
                            list.setAdapter(arrayAdapter);

                        } catch (JSONException e) {
                            Log.i("debug", "line 75");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TrafficCamera.this, "Not Connected", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(objReq);


    }


}