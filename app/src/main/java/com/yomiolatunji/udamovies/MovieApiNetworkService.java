package com.yomiolatunji.udamovies;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.yomiolatunji.udamovies.data.entity.Movies;
import com.yomiolatunji.udamovies.data.entity.Review;
import com.yomiolatunji.udamovies.data.entity.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MovieApiNetworkService implements LoaderManager.LoaderCallbacks<List<Movies>> {
    private static final int ID_MOVIES_LOADER = 425;
    private static final String KEY_URL = "url_key";
    private static final int ID_REVIEW_LOADER = 688;
    private static final int ID_TRAILER_LOADER = 3545;
    private static AppCompatActivity mContext;
    public int resultCount = -1;
    String baseUrl = "";
    private DataLoadingCallback<List<Movies>> moviesCallback;
    private DataLoadingCallback<List<Review>> reviewCallback;
    LoaderManager.LoaderCallbacks<List<Review>> reviewLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<Review>>() {
        @Override
        public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
            final String url = args.getString(KEY_URL);
            return new AsyncTaskLoader<List<Review>>(mContext) {
                @Override
                protected void onStartLoading() {
                    forceLoad();
                }

                @Override
                public List<Review> loadInBackground() {
                    List<Review> reviewList = new ArrayList<>();
                    try {
                        String response = NetworkUtils.newInstance(mContext).get(url);
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray reviewJsonArray = jsonObject.getJSONArray("results");
                        if (reviewJsonArray.length() == 0) {
                            reviewCallback.onFailure("Empty list");
                            return null;
                        }
                        for (int i = 0; i < reviewJsonArray.length(); i++) {
                            JSONObject reviewObject = reviewJsonArray.getJSONObject(i);
                            Review review = new Review();
                            if (reviewObject.has("id"))
                                review.setId(reviewObject.getString("id"));
                            if (reviewObject.has("author"))
                                review.setAuthor(reviewObject.getString("author"));
                            if (reviewObject.has("content"))
                                review.setContent(reviewObject.getString("content"));
                            if (reviewObject.has("url"))
                                review.setUrl(reviewObject.getString("url"));

                            reviewList.add(review);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return reviewList;

                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
            reviewCallback.onResponse(data);
        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {

        }
    };
    private DataLoadingCallback<List<Trailer>> trailerCallback;
    LoaderManager.LoaderCallbacks<List<Trailer>> trailerLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<Trailer>>() {
        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
            final String url = args.getString(KEY_URL);
            return new AsyncTaskLoader<List<Trailer>>(mContext) {
                @Override
                protected void onStartLoading() {
                    forceLoad();
                }

                @Override
                public List<Trailer> loadInBackground() {
                    List<Trailer> trailerList = new ArrayList<>();
                    try {
                        String response = NetworkUtils.newInstance(mContext).get(url);
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray trailerJsonArray = jsonObject.getJSONArray("results");
                        if (trailerJsonArray.length() == 0) {
                            trailerCallback.onFailure("Empty list");
                            return null;
                        }
                        for (int i = 0; i < trailerJsonArray.length(); i++) {
                            JSONObject trailerObject = trailerJsonArray.getJSONObject(i);
                            Trailer trailer = new Trailer();
                            if (trailerObject.has("id"))
                                trailer.setId(trailerObject.getString("id"));
                            if (trailerObject.has("key"))
                                trailer.setKey(trailerObject.getString("key"));
                            if (trailerObject.has("name"))
                                trailer.setName(trailerObject.getString("name"));
                            if (trailerObject.has("site"))
                                trailer.setSite(trailerObject.getString("site"));

                            trailerList.add(trailer);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return trailerList;

                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
            trailerCallback.onResponse(data);
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {

        }
    };

    private MovieApiNetworkService() {
        baseUrl = mContext.getString(R.string.baseurl);
    }

    public static MovieApiNetworkService newInstance(AppCompatActivity context) {
        mContext = context;
        return new MovieApiNetworkService();
    }

    public void getMovieList(String sortBy, DataLoadingCallback<List<Movies>> callback) {
        moviesCallback = callback;

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(baseUrl);
        urlBuilder.append(sortBy);
        String apikey = mContext.getString(R.string.apikey);
        urlBuilder.append("?api_key=" + apikey);


        String url = urlBuilder.toString();

        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);

        mContext.getSupportLoaderManager().initLoader(ID_MOVIES_LOADER, bundle, this);
    }

    public void getReviewList(int movieId, DataLoadingCallback<List<Review>> callback) {
        reviewCallback = callback;

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(baseUrl);
        urlBuilder.append(movieId);
        urlBuilder.append("/reviews");
        String apikey = mContext.getString(R.string.apikey);
        urlBuilder.append("?api_key=" + apikey);


        String url = urlBuilder.toString();

        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);

        mContext.getSupportLoaderManager().initLoader(ID_REVIEW_LOADER, bundle, reviewLoaderCallbacks);
    }

    public void getTrailerList(int movieId, DataLoadingCallback<List<Trailer>> callback) {
        trailerCallback = callback;

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(baseUrl);
        urlBuilder.append(movieId);
        urlBuilder.append("/videos");
        String apikey = mContext.getString(R.string.apikey);
        urlBuilder.append("?api_key=" + apikey);


        String url = urlBuilder.toString();

        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);

        mContext.getSupportLoaderManager().initLoader(ID_TRAILER_LOADER, bundle, trailerLoaderCallbacks);
    }

    @Override
    public Loader<List<Movies>> onCreateLoader(int id, Bundle args) {
        final String url = args.getString(KEY_URL);

        return new AsyncTaskLoader<List<Movies>>(mContext) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public List<Movies> loadInBackground() {
                List<Movies> moviesList = new ArrayList<>();
                try {
                    String response = NetworkUtils.newInstance(mContext).get(url);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray moviesJsonArray = jsonObject.getJSONArray("results");
                    if (moviesJsonArray.length() == 0) {
                        moviesCallback.onFailure("Empty list");
                        return null;
                    }
                    for (int i = 0; i < moviesJsonArray.length(); i++) {
                        JSONObject movieObject = moviesJsonArray.getJSONObject(i);
                        Movies movies = new Movies();
                        if (movieObject.has("poster_path"))
                            movies.setPosterPath(movieObject.getString("poster_path"));
                        if (movieObject.has("id"))
                            movies.setId(movieObject.getInt("id"));
                        if (movieObject.has("adult"))
                            movies.setAdult(movieObject.getBoolean("adult"));
                        if (movieObject.has("overview"))
                            movies.setSynopsis(movieObject.getString("overview"));
                        if (movieObject.has("release_date"))
                            movies.setReleaseDate(movieObject.getString("release_date"));
                        if (movieObject.has("original_title"))
                            movies.setTitle(movieObject.getString("original_title"));
                        if (movieObject.has("backdrop_path"))
                            movies.setBackdropPath(movieObject.getString("backdrop_path"));
                        if (movieObject.has("vote_count"))
                            movies.setVoteCount(movieObject.getInt("vote_count"));
                        if (movieObject.has("vote_average"))
                            movies.setVoteAverage((float) movieObject.getDouble("vote_average"));

                        moviesList.add(movies);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return moviesList;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movies>> loader, List<Movies> data) {

        moviesCallback.onResponse(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Movies>> loader) {

    }
}
