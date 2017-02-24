package ngo.patrick.movielistings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import ngo.patrick.movielistings.adapter.MovieListAdapter;
import ngo.patrick.movielistings.api.TmdbAPI;
import ngo.patrick.movielistings.model.PageListingResult.PageListingResult;
import ngo.patrick.movielistings.model.PageListingResult.Result;
import ngo.patrick.movielistings.task.FetchAllMoviesTask;
import retrofit2.Call;

/**
 *  MainActivity: Displays a list of movies
 */

public class MainActivity extends AppCompatActivity
{
    private MovieListAdapter movieListAdapter;

    //API
    TmdbAPI apiService;

    //current page number of the movie listings
    private Integer pageNumber = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create a MovieListAdapter and bind it to the listview in the layout
        movieListAdapter = new MovieListAdapter(this, R.layout.list_item_movie_row);
        ListView listView = (ListView) findViewById(R.id.listview_movielist);
        listView.setAdapter(movieListAdapter);


        //Create the click listener for the listview items (clicking on a movie leads to the Detail page of that movie)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                //Fire an Intent to launch the Detail Activity, with the movie id as a parameter
                Result selectedMovie = movieListAdapter.getItem(position);

                String intentID = getString(R.string.intent_movie_id);

                Intent detailIntent = new Intent(getApplicationContext(), DetailActivity.class);
                detailIntent.putExtra(intentID, Integer.toString(selectedMovie.getId()));
                startActivity(detailIntent);
            }
        });


        //get initial data to display movie list

        //create api
        apiService = TmdbAPI.tmdb.create(TmdbAPI.class);

        //create retrofit call
        final Call<PageListingResult> call = apiService.getAllMovies(
                TmdbAPI.API_KEY,
                TmdbAPI.SORT_BY,
                TmdbAPI.PRIMARY_RELEASE_DATE,
                pageNumber);

        //get data
        new FetchAllMoviesTask(movieListAdapter).execute(call);
    }
}
