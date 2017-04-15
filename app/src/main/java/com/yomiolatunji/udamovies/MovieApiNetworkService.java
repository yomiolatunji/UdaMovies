package com.yomiolatunji.udamovies;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MovieApiNetworkService {
    private static Context mContext;
    public int resultCount = -1;
    String baseUrl = "";

    private MovieApiNetworkService() {
        baseUrl=mContext.getString(R.string.baseurl);
    }

    public static MovieApiNetworkService newInstance(Context context) {
        mContext = context;
        return new MovieApiNetworkService();
    }

    public void getMovieList(String sortBy, DataLoadingCallback<List<Movies>> callback) {

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(baseUrl);
        urlBuilder.append(sortBy);
        String apikey = mContext.getString(R.string.apikey);
        urlBuilder.append("?api_key=" + apikey);


        String url = urlBuilder.toString();
        GetMoviesAsync getMoviesAsync = new GetMoviesAsync(url, callback);
        getMoviesAsync.execute();
    }


    class GetMoviesAsync extends AsyncTask<Void, Void, Void> {
        List<Movies> moviesList = new ArrayList<>();
        private String url;
        private DataLoadingCallback<List<Movies>> callback;

        public GetMoviesAsync(String url, DataLoadingCallback<List<Movies>> callback) {
            this.url = url;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String response = NetworkUtils.newInstance(mContext).get(url);
                JSONObject jsonObject = new JSONObject(response);
                JSONArray moviesJsonArray = jsonObject.getJSONArray("results");
                if (moviesJsonArray.length() == 0) {
                    callback.onFailure("Empty list");
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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            callback.onResponse(moviesList);
        }
    }


}
