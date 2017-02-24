package ngo.patrick.movielistings.task;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import java.io.IOException;

import ngo.patrick.movielistings.adapter.MovieListAdapter;
import ngo.patrick.movielistings.model.PageListingResult.PageListingResult;
import ngo.patrick.movielistings.model.PageListingResult.Result;
import retrofit2.Call;
import retrofit2.Response;

/**
 * ASyncTask to retrieve the movie list and display it in the listview via the adapter
 */

public class FetchAllMoviesTask extends AsyncTask<Call, Void, PageListingResult>
{
    /**
     * Retrieve all movie data by network Request
     * Retrieval done on separate thread to avoid cluttering main UI thread
     */
    private MovieListAdapter movieListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FetchAllMoviesTask(MovieListAdapter adapter, SwipeRefreshLayout swipeRefreshLayout)
    {
        super();

        //set adapter
        this.movieListAdapter = adapter;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }


    //send the request
    @Override
    protected PageListingResult doInBackground(Call... params)
    {
        try
        {
            Call<PageListingResult> call = params[0];
            Response<PageListingResult> response = call.execute();

            return response.body();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //once movie data retrieved, add to list adapter for display on the view
    @Override
    protected void onPostExecute(PageListingResult results)
    {
        if (results != null)
        {
            if (movieListAdapter != null)
            {
                //clear the list
                movieListAdapter.clear();

                //add new list
                for (Result singleMovie : results.getResults())
                {
                    movieListAdapter.add(singleMovie);
                    Log.v("WTF", singleMovie.getTitle() );
                }

                //stop refresh animation
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }


}
