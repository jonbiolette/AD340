package JBiolette.AD340;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import JBiolette.AD340.databinding.ActivityMovieDetailsBinding;

public class MovieDetails extends AppCompatActivity {

    private ActivityMovieDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String[] dets = intent.getStringArrayExtra("details");

        String title = dets[0];
        String year = dets[1];
        String director = dets[2];
        String image = dets[3];
        String descript = dets[4];

        //Header
        TextView header = findViewById(R.id.movidsDetailsTitle);
        header.setText(title + " ," + year);

        //Image
        ImageView moviePic = (ImageView) findViewById(R.id.movieImage);
        Picasso.get().load(image).into(moviePic);

        //Description
        TextView body = findViewById(R.id.movieDetailsText);
        body.setText(descript);
    }
}