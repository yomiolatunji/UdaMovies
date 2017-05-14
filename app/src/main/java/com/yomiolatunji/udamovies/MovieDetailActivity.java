package com.yomiolatunji.udamovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yomiolatunji.udamovies.data.MoviesContract.MoviesEntry;
import com.yomiolatunji.udamovies.data.entity.Movies;
import com.yomiolatunji.udamovies.data.entity.Review;
import com.yomiolatunji.udamovies.data.entity.Trailer;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_FAVOURITE_LOADER = 321;
    private static final String MOVIE_ID = "movieId";
    public static String ARG_MOVIE = "movie_key";
    private ImageView image;
    private TextView adultContent;
    private RatingBar userRating;
    private TextView ratingCount;
    private TextView releaseDate;
    private TextView synopsis;
    private Movies movies;
    private Button btnMarkFav;
    private RecyclerView reviewList;
    private RecyclerView trailerList;
    private boolean isfavorite = false;
    View.OnClickListener favClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isfavorite) {
                getContentResolver().delete(MoviesEntry.buildMoviesUriWithId(movies.getId()), null, null);
            } else {
                ContentValues cv = new ContentValues();
                cv.put(MoviesEntry._ID, movies.getId());
                cv.put(MoviesEntry.COLUMN_ADULT, movies.isAdult() ? 0 : 1);
                cv.put(MoviesEntry.COLUMN_BACKDROP_PATH, movies.getBackdropPath());
                cv.put(MoviesEntry.COLUMN_FAVORITE, 0);
                cv.put(MoviesEntry.COLUMN_POSTER_PATH, movies.getPosterPath());
                cv.put(MoviesEntry.COLUMN_RELEASE_DATE, movies.getReleaseDate());
                cv.put(MoviesEntry.COLUMN_SYNOPSIS, movies.getSynopsis());
                cv.put(MoviesEntry.COLUMN_TITLE, movies.getTitle());
                cv.put(MoviesEntry.COLUMN_VOTE_AVERAGE, movies.getVoteAverage());
                cv.put(MoviesEntry.COLUMN_VOTE_COUNT, movies.getVoteCount());

                getContentResolver().insert(MoviesEntry.buildMoviesUriWithId(movies.getId()), cv);
            }
        }
    };
    private ReviewAdapter reviewAdapter;
    private LinearLayoutManager reviewLayoutManager;
    private LinearLayoutManager trailerLayoutManager;
    private TrailerAdapter trailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        image = (ImageView) findViewById(R.id.image);
        adultContent = (TextView) findViewById(R.id.adult_content);
        userRating = (RatingBar) findViewById(R.id.user_rating);
        ratingCount = (TextView) findViewById(R.id.rating_count);
        releaseDate = (TextView) findViewById(R.id.release_date);
        synopsis = (TextView) findViewById(R.id.synopsis);
        btnMarkFav = (Button) findViewById(R.id.mark_favorite);

        reviewList = (RecyclerView) findViewById(R.id.review_list);
        trailerList = (RecyclerView) findViewById(R.id.trailer_list);

        if (getIntent().hasExtra(ARG_MOVIE)) {
            movies = getIntent().getParcelableExtra(ARG_MOVIE);
            VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(getResources(), R.drawable.ic_local_movies, null);
            if (movies.getPosterPath() != null)
                Picasso.with(MovieDetailActivity.this)
                        .load("http://image.tmdb.org/t/p/w185/" + movies.getPosterPath())
                        .placeholder(vectorDrawableCompat)
                        .error(vectorDrawableCompat)
                        .fit()
                        .into(image);
            getSupportActionBar().setTitle(movies.getTitle());
            adultContent.setVisibility(movies.isAdult() ? View.VISIBLE : View.GONE);
            userRating.setProgress((int) (movies.getVoteAverage() * 100));
            ratingCount.setText(String.valueOf(movies.getVoteCount()));
            releaseDate.setText(movies.getReleaseDate());
            synopsis.setText(movies.getSynopsis());

            Bundle bundle = new Bundle();
            bundle.putLong(MOVIE_ID, movies.getId());
            getSupportLoaderManager().initLoader(ID_FAVOURITE_LOADER, bundle, this);
        } else {
            throw new IllegalStateException("Movies extra not attachment");
        }
        btnMarkFav.setOnClickListener(favClickListener);

        reviewAdapter = new ReviewAdapter(MovieDetailActivity.this);
        final MovieApiNetworkService movieApiNetworkService = MovieApiNetworkService.newInstance(MovieDetailActivity.this);
        movieApiNetworkService.getReviewList(movies.getId(), new DataLoadingCallback<List<Review>>() {
            @Override
            public void onResponse(List<Review> data) {
                if (data != null && data.size() > 0) {
                    reviewAdapter.addAndResort(data);
                    reviewList.setVisibility(View.VISIBLE);
                }
                //loadingBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MovieDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        reviewList.setAdapter(reviewAdapter);
        reviewLayoutManager = new LinearLayoutManager(this);
        reviewList.setLayoutManager(reviewLayoutManager);
        trailerAdapter = new TrailerAdapter(MovieDetailActivity.this);
        movieApiNetworkService.getTrailerList(movies.getId(), new DataLoadingCallback<List<Trailer>>() {
            @Override
            public void onResponse(List<Trailer> data) {
                if (data != null && data.size() > 0) {
                    trailerAdapter.addAndResort(data);
                    trailerList.setVisibility(View.VISIBLE);
                }
                //loadingBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MovieDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        trailerLayoutManager=new LinearLayoutManager(this);
        trailerList.setAdapter(trailerAdapter);
        trailerList.setLayoutManager(trailerLayoutManager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        long movieId = args.getLong(MOVIE_ID);
        switch (id) {
            case ID_FAVOURITE_LOADER:
                Uri moviesUri = MoviesEntry.CONTENT_URI;
                return new CursorLoader(this, moviesUri, null, MoviesEntry._ID + "=?", new String[]{String.valueOf(movieId)}, null);
            default:
                throw new UnsupportedOperationException("Unknown loader Id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() != 0) {
            isfavorite = true;
            btnMarkFav.setText(R.string.remove_favourite);
        } else {
            isfavorite = false;
            btnMarkFav.setText(getString(R.string.mark_as_favorite));
            btnMarkFav.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
