package com.yomiolatunji.udamovies;

import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    public static String ARG_MOVIE = "movie_key";
    private ImageView back;
    private ImageView image;
    private TextView title;
    private TextView adultContent;
    private RatingBar userRating;
    private TextView ratingCount;
    private TextView releaseDate;
    private TextView synopsis;
    private Movies movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        back = (ImageView) findViewById(android.R.id.home);
        image = (ImageView) findViewById(R.id.image);
        title = (TextView) findViewById(R.id.title);
        adultContent = (TextView) findViewById(R.id.adult_content);
        userRating = (RatingBar) findViewById(R.id.user_rating);
        ratingCount = (TextView) findViewById(R.id.rating_count);
        releaseDate = (TextView) findViewById(R.id.release_date);
        synopsis = (TextView) findViewById(R.id.synopsis);

        if (getIntent().hasExtra(ARG_MOVIE)) {
            movies = getIntent().getParcelableExtra(ARG_MOVIE);
            VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(getResources(), R.drawable.ic_local_movies, null);
            if (movies.getBackdropPath() != null)
                Picasso.with(MovieDetailActivity.this)
                        .load("http://image.tmdb.org/t/p/w500/" + movies.getBackdropPath())
                        .placeholder(vectorDrawableCompat)
                        .error(vectorDrawableCompat)
                        .fit()
                        .into(image);
            title.setText(movies.getTitle());
            adultContent.setVisibility(movies.isAdult() ? View.INVISIBLE : View.VISIBLE);
            userRating.setProgress((int) (movies.getVoteAverage() * 100));
            ratingCount.setText(String.valueOf(movies.getVoteCount()));
            releaseDate.setText(movies.getReleaseDate());
            synopsis.setText(movies.getSynopsis());
        } else {
            throw new IllegalStateException("Movies extra not attachment");
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
