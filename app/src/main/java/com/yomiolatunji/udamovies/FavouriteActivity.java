package com.yomiolatunji.udamovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.yomiolatunji.udamovies.data.MoviesContract;
import com.yomiolatunji.udamovies.data.entity.Movies;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String[] MAIN_MOVIES_PROJECTION = {
            MoviesContract.MoviesEntry._ID,
            MoviesContract.MoviesEntry.COLUMN_TITLE,
            MoviesContract.MoviesEntry.COLUMN_SYNOPSIS,
            MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT,
            MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE,
            MoviesContract.MoviesEntry.COLUMN_ADULT,
            MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH,
            MoviesContract.MoviesEntry.COLUMN_POSTER_PATH,
            MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE
    };
    public static final int INDEX_MOVIES_ID = 0;
    public static final int INDEX_MOVIES_TITLE = 1;
    public static final int INDEX_MOVIES_SYNOPSIS = 2;
    public static final int INDEX_MOVIES_VOTE_COUNT = 3;
    public static final int INDEX_MOVIES_VOTE_AVERAGE = 4;
    public static final int INDEX_MOVIES_ADULT = 5;
    public static final int INDEX_MOVIES_BACKDROP_PATH = 6;
    public static final int INDEX_MOVIES_POSTER_PATH = 7;
    public static final int INDEX_MOVIES_RELEASE_DATE = 8;
    private static final int ID_MOVIES_LOADER = 21;
    private RecyclerView moviesRecyclerView;
    private ProgressBar loadingBar;
    private MoviesAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        moviesRecyclerView = (RecyclerView) findViewById(R.id.movies_grid);
        loadingBar = (ProgressBar) findViewById(R.id.loading);
        adapter = new MoviesAdapter(this);
        moviesRecyclerView.setAdapter(adapter);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        moviesRecyclerView.setLayoutManager(layoutManager);

        showLoading();
        getSupportLoaderManager().initLoader(ID_MOVIES_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_MOVIES_LOADER:
                Uri uri = MoviesContract.MoviesEntry.CONTENT_URI;

                return new CursorLoader(this,
                        uri,
                        MAIN_MOVIES_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new UnsupportedOperationException("Unknown Loader Id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<Movies> moviesList = new ArrayList<>();

        for (int i = 0; i < data.getCount(); i++) {
            data.moveToNext();
            Movies movies = new Movies();
            movies.setAdult(data.getInt(INDEX_MOVIES_ADULT) == 0);
            movies.setBackdropPath(data.getString(INDEX_MOVIES_BACKDROP_PATH));
            movies.setId(data.getInt(INDEX_MOVIES_ID));
            movies.setPosterPath(data.getString(INDEX_MOVIES_POSTER_PATH));
            movies.setReleaseDate(data.getString(INDEX_MOVIES_RELEASE_DATE));
            movies.setSynopsis(data.getString(INDEX_MOVIES_SYNOPSIS));
            movies.setTitle(data.getString(INDEX_MOVIES_TITLE));
            movies.setVoteAverage(data.getFloat(INDEX_MOVIES_VOTE_AVERAGE));
            movies.setVoteCount(data.getInt(INDEX_MOVIES_VOTE_COUNT));


            moviesList.add(movies);
        }

        adapter.addAndResort(moviesList);
        showMovieView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.clear();
    }

    private void showMovieView() {
        loadingBar.setVisibility(View.INVISIBLE);
        moviesRecyclerView.setVisibility(View.VISIBLE);
    }
   private void showLoading() {
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        loadingBar.setVisibility(View.VISIBLE);
    }

}
