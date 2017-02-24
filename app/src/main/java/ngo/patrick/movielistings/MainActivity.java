package ngo.patrick.movielistings;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
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
    private static Integer FIRST_PAGE = 1;
    private static Integer MAX_PAGE = 25;

    private MovieListAdapter movieListAdapter;

    //API
    TmdbAPI apiService;

    //pull to refresh
    private SwipeRefreshLayout swipeRefreshLayout;

    //current page number of the movie listings
    private Integer pageNumber = 1;

    public Boolean isLoadingMore = false;


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


        //setup pull to refresh action
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                resetData();
            }
        });


        //get initial data to display movie list
        resetData();


        //load more from bottom mechanism
        listView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                if (pageNumber < MainActivity.MAX_PAGE)
                {
                    //find when the scroll has reached the bottom
                    if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0)
                    {
                        if (isLoadingMore == false)
                        {
                            isLoadingMore = true;
                            loadMoreData();
                        }
                    }
                }
            }
        });
    }
    private void resetData()
    {
        //reset page number
        pageNumber = FIRST_PAGE;
        getData();
    }

    private void loadMoreData()
    {
        //increment page number
        pageNumber++;
        getData();
    }

    private void getData()
    {
        //create api
        apiService = TmdbAPI.tmdb.create(TmdbAPI.class);

        //create retrofit call
        final Call<PageListingResult> call = apiService.getAllMovies(
                TmdbAPI.API_KEY,
                TmdbAPI.SORT_BY,
                TmdbAPI.PRIMARY_RELEASE_DATE,
                pageNumber);

        //get data
        new FetchAllMoviesTask(this, movieListAdapter, swipeRefreshLayout).execute(call);
    }
}
