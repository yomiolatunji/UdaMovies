package com.yomiolatunji.udamovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView moviesRecyclerView;
    private ProgressBar loadingBar;
    private ImageView noConnectionView;
    private MoviesAdapter adapter;
    private GridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        moviesRecyclerView = (RecyclerView) findViewById(R.id.movies_grid);
        loadingBar = (ProgressBar) findViewById(R.id.loading);
        noConnectionView = (ImageView) findViewById(R.id.no_connection);

        adapter = new MoviesAdapter(MainActivity.this);
        MovieApiNetworkService movieApiNetworkService = MovieApiNetworkService.newInstance(MainActivity.this);
        if (NetworkUtils.isNetworkAvailable(MainActivity.this)) {
            getData(movieApiNetworkService,getString(R.string.sort_popular));
        } else {
            moviesRecyclerView.setVisibility(View.GONE);
            loadingBar.setVisibility(View.GONE);
            noConnectionView.setVisibility(View.VISIBLE);
        }
        moviesRecyclerView.setAdapter(adapter);
        layoutManager = new GridLayoutManager(this, 2);
        moviesRecyclerView.setLayoutManager(layoutManager);
        moviesRecyclerView.setHasFixedSize(true);
    }

    private void getData(MovieApiNetworkService movieApiNetworkService,String sortBy) {
        loadingBar.setVisibility(View.VISIBLE);
        moviesRecyclerView.setVisibility(View.GONE);
        noConnectionView.setVisibility(View.GONE);
        movieApiNetworkService.getMovieList(sortBy, new DataLoadingCallback<List<Movies>>() {
            @Override
            public void onResponse(List<Movies> data) {
                if (data != null && data.size() > 0) {
                    adapter.addAndResort(data);
                    moviesRecyclerView.setVisibility(View.VISIBLE);
                }
                loadingBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
