package com.yomiolatunji.udamovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.yomiolatunji.udamovies.data.entity.Movies;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView moviesRecyclerView;
    private ProgressBar loadingBar;
    private ImageView noConnectionView;
    private MoviesAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private FloatingActionMenu menuView;
    private FloatingActionButton menuItemPopular;
    private FloatingActionButton menuItemRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        moviesRecyclerView = (RecyclerView) findViewById(R.id.movies_grid);
        loadingBar = (ProgressBar) findViewById(R.id.loading);
        noConnectionView = (ImageView) findViewById(R.id.no_connection);
        menuView = (FloatingActionMenu) findViewById(R.id.menu);
        menuItemPopular = (FloatingActionButton) findViewById(R.id.menu_item_popular);
        menuItemRating = (FloatingActionButton) findViewById(R.id.menu_item_rating);

        adapter = new MoviesAdapter(MainActivity.this);
        final MovieApiNetworkService movieApiNetworkService = MovieApiNetworkService.newInstance(MainActivity.this);
        if (NetworkUtils.isNetworkAvailable(MainActivity.this)) {
            getData(movieApiNetworkService, getString(R.string.sort_popular));
        } else {
            moviesRecyclerView.setVisibility(View.GONE);
            menuView.setVisibility(View.GONE);
            loadingBar.setVisibility(View.GONE);
            noConnectionView.setVisibility(View.VISIBLE);
        }
        moviesRecyclerView.setAdapter(adapter);
        layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        moviesRecyclerView.setLayoutManager(layoutManager);
        //moviesRecyclerView.setHasFixedSize(true);
        menuItemPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(movieApiNetworkService, getString(R.string.sort_popular));
            }
        });
        menuItemRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(movieApiNetworkService, getString(R.string.sort_top_rated));
            }
        });
    }

    private void getData(MovieApiNetworkService movieApiNetworkService, String sortBy) {
        loadingBar.setVisibility(View.VISIBLE);
        menuView.setVisibility(View.GONE);
        moviesRecyclerView.setVisibility(View.GONE);
        noConnectionView.setVisibility(View.GONE);
        adapter.clear();
        movieApiNetworkService.getMovieList(sortBy, new DataLoadingCallback<List<Movies>>() {
            @Override
            public void onResponse(List<Movies> data) {
                if (data != null && data.size() > 0) {
                    adapter.addAndResort(data);
                    moviesRecyclerView.setVisibility(View.VISIBLE);
                    menuView.setVisibility(View.VISIBLE);
                }
                loadingBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
