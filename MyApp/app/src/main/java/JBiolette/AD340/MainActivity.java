package JBiolette.AD340;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button Movie,Cam,County,Zip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Movie = (Button) findViewById(R.id.movie);
        Movie.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Toast.makeText(MainActivity.this, "Seattle", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Movies.class);
                startActivity(intent);
            }
        });

        Cam = (Button) findViewById(R.id.cam);
        Cam.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Toast.makeText(MainActivity.this, "Seattle", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, TrafficCamera.class);
                startActivity(intent);
            }
        });
        County = (Button) findViewById(R.id.location);
        County.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        Zip = (Button) findViewById(R.id.zip);
        Zip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(MainActivity.this, "98109", Toast.LENGTH_SHORT).show();
            }
        });


        }
}