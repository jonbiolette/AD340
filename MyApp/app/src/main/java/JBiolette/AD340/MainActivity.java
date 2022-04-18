package JBiolette.AD340;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button City,State,County,Zip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MyActivity", "Test log");

        City = (Button) findViewById(R.id.city);
        City.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(MainActivity.this, "Seattle", Toast.LENGTH_SHORT).show();
            }
        });
        State = (Button) findViewById(R.id.state);
        State.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(MainActivity.this, "Washington", Toast.LENGTH_SHORT).show();
            }
        });
        County = (Button) findViewById(R.id.county);
        County.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(MainActivity.this, "King County", Toast.LENGTH_SHORT).show();
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