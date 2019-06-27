package com.codepath.debuggingchallenges.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.debuggingchallenges.R;
import com.codepath.debuggingchallenges.adapters.MoviesAdapter;
import com.codepath.debuggingchallenges.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MoviesActivity extends AppCompatActivity {

    //constants
    public static final String API_BASE_URL = "https://api.themoviedb.org/3";
    public static final String API_KEY_PARAM = "api_key";
    public final static String TAG = "MainActivity";

    RecyclerView rvMovies;
    MoviesAdapter adapter;
    ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        // Create the adapter to convert the array to views
        adapter = new MoviesAdapter(movies);

        // Attach the adapter to a ListView
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        fetchMovies();
    }


    private void fetchMovies() {
        String url = API_BASE_URL + "/movie/now_playing";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray moviesJson = response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(moviesJson));
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, String.format("Loaded %s movies", movies.size()));
                } catch (JSONException e) {
                    Log.e(TAG, "Error getting movies from JSON array");
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "Error fetching movies");
            }
        });
    }
}
