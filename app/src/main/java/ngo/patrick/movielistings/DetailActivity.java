package ngo.patrick.movielistings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ngo.patrick.movielistings.api.TmdbAPI;
import ngo.patrick.movielistings.model.MovieDetailsResult.MovieDetailsResult;
import ngo.patrick.movielistings.task.FetchMovieDetailTask;
import retrofit2.Call;

public class DetailActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        //Get initial data to display movie details

        //create api
        TmdbAPI apiService = TmdbAPI.tmdb.create(TmdbAPI.class);
        View rootView = findViewById(R.id.activity_detail);         //keep reference of root view to send to the async task
        String movieID = getString(R.string.intent_movie_id);       //get movie id from intent

        //create retrofit call
        final Call<MovieDetailsResult> call = apiService.getMovieDetails(getIntent().getStringExtra(movieID), TmdbAPI.API_KEY);
        FetchMovieDetailTask movieDetailTask = new FetchMovieDetailTask(this, rootView);

        //get data
        movieDetailTask.execute(call);
    }
}
