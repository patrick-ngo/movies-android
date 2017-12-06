package ngo.patrick.movielistings.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import java.io.IOException;

import ngo.patrick.movielistings.MainActivity;
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
    private Context context;

    public FetchAllMoviesTask(Context context)
    {
        super();

        //set context
        this.context = context;
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
            MainActivity activity = (MainActivity) context;

            //not adding more, means pulled to refresh
            if (!activity.isLoadingMore())
            {
                activity.getMovieListAdapter().clear();
            }

            //add new list
            for (Result singleMovie : results.getResults())
            {
                activity.getMovieListAdapter().add(singleMovie);
            }

            //stop bottom loading
            if (activity.isLoadingMore())
            {
                activity.setLoadingMore(false);
                activity.showBottomLoading(false);
            }
            else
            {
                //stop pull to refresh animation
                activity.getSwipeRefreshLayout().setRefreshing(false);
            }
        }
    }


}
