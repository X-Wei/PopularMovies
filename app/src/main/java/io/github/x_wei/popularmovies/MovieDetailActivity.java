package io.github.x_wei.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static io.github.x_wei.popularmovies.Movie.MovieFromJson;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        // find views
        TextView titleTV = (TextView) findViewById(R.id.tv_title);
        TextView factsTV = (TextView) findViewById(R.id.tv_detail_facts);
        TextView overviewTV = (TextView) findViewById(R.id.tv_overview);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rb_rating);
        ImageView posterIV = (ImageView) findViewById(R.id.iv_poster_image);


        // unpack json and construct a movie object
        Intent commingIntent = getIntent();
        if(commingIntent.hasExtra(Intent.EXTRA_TEXT)){
            String rawjson = commingIntent.getStringExtra(Intent.EXTRA_TEXT);
            Movie movie = MovieFromJson(rawjson);
            // populate the views with movie info
            titleTV.setText(movie.title);
            ratingBar.setMax(10);
            ratingBar.setNumStars(10);
            ratingBar.setStepSize((float) 1.0);
            ratingBar.setRating(movie.rating);
            ratingBar.invalidate();
            Log.d("rating", movie.rating+"");
            factsTV.setText(movie.getFacts());
            overviewTV.setText(movie.overview);
            Toast.makeText(this, movie.overview, Toast.LENGTH_LONG);
            Picasso.with(this)
                    .load(movie.getPosterImgURL())
                    .into(posterIV);
        }
    }
}
