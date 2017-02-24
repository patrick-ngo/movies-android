package ngo.patrick.movielistings;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ngo.patrick.movielistings.api.TmdbAPI;
import ngo.patrick.movielistings.model.MovieDetailsResult.MovieDetailsResult;
import ngo.patrick.movielistings.model.PageListingResult.PageListingResult;
import ngo.patrick.movielistings.task.FetchAllMoviesTask;
import ngo.patrick.movielistings.task.FetchMovieDetailTask;
import retrofit2.Call;

public class DetailActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Button bookButton = (Button) findViewById(R.id.book_button);
        bookButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //launch cathay website on click
                String url = TmdbAPI.CATHAY_CINEPLEXES;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        //Get initial data to display movie details
        refreshData();
    }

    private void refreshData()
    {
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
