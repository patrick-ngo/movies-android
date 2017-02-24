package ngo.patrick.movielistings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import ngo.patrick.movielistings.adapter.MovieListAdapter;
import ngo.patrick.movielistings.api.TmdbAPI;
import ngo.patrick.movielistings.model.PageListingResult.PageListingResult;
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
