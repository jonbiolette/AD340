package JBiolette.AD340;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button Movie,State,County,Zip;
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