package ngo.patrick.movielistings.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import ngo.patrick.movielistings.R;
import ngo.patrick.movielistings.api.TmdbAPI;
import ngo.patrick.movielistings.model.MovieDetailsResult.Genre;
import ngo.patrick.movielistings.model.MovieDetailsResult.MovieDetailsResult;
import ngo.patrick.movielistings.model.MovieDetailsResult.SpokenLanguage;
import retrofit2.Call;
import retrofit2.Response;

/**
 * ASyncTask to retrieve the details of a specific movie and display it on the main view
 */

public class FetchMovieDetailTask extends AsyncTask<Call, Void, MovieDetailsResult>
{
    /**
     * Retrieve single movie data by network Request
     * Retrieval done on separate thread to avoid cluttering main UI thread
     */

    private Context context;
    private View rootView;

    public FetchMovieDetailTask(Context context, View rootView)
    {
        super();

        //set context and view
        this.context = context;
        this.rootView = rootView;
    }

    //send the request
    @Override
    protected MovieDetailsResult doInBackground(Call ... params)
    {
        try
        {
            Call<MovieDetailsResult> call = params[0];
            Response<MovieDetailsResult> response = call.execute();

            return response.body();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //once movie data retrieved, display on the view
    @Override
    protected void onPostExecute(MovieDetailsResult result)
    {
        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Activity activity = (Activity) context;
        Intent intent = activity.getIntent();

        if (intent != null && intent.hasExtra(context.getResources().getString(R.string.intent_movie_id)) && result != null)
        {
            String id = intent.getStringExtra(context.getResources().getString(R.string.intent_movie_id));

            //display movie poster
            ImageView thumbnailImageView = ((ImageView) rootView.findViewById(R.id.poster));
            thumbnailImageView.setVisibility(View.GONE);
            if (result.getPosterPath() != null)
            {
                thumbnailImageView.setVisibility(View.VISIBLE);
                Picasso.with(context).load(TmdbAPI.BASE_URL_IMAGES_HIGH + result.getPosterPath()).into(thumbnailImageView);
            }

            //display movie title
            if (result.getOriginalTitle() != null)
            {
                TextView titleTextView =  ((TextView) rootView.findViewById(R.id.title));
                titleTextView.setText(result.getOriginalTitle());
            }

            //display popularity
            if (result.getPopularity() != null)
            {
                TextView ratingTextView =  ((TextView) rootView.findViewById(R.id.popularity));
                ratingTextView.setText( context.getResources().getString(R.string.rating) + " " + result.getPopularity());
            }

            //display synopsis
            TextView synopsisTextView =  ((TextView) rootView.findViewById(R.id.caption));
            synopsisTextView.setVisibility(View.GONE);
            if (result.getOverview() != null)
            {
                synopsisTextView.setVisibility(View.VISIBLE);
                synopsisTextView.setText( result.getOverview());
            }

            //display duration
            if (result.getRuntime() != null)
            {
                TextView durationTextView = ((TextView) rootView.findViewById(R.id.duration));
                durationTextView.setVisibility(View.GONE);
                if (result.getRuntime() > 0)
                {
                    durationTextView.setVisibility(View.VISIBLE);
                    durationTextView.setText(result.getRuntime().toString() + " " + context.getString(R.string.minutes));
                }
            }

            //display languages
            if (result.getSpokenLanguages() != null)
            {
                TextView languagesTextView = ((TextView) rootView.findViewById(R.id.languages));
                String allLanguages = "";
                for ( SpokenLanguage language : result.getSpokenLanguages())
                {
                    allLanguages = allLanguages + language.getName() + " ";
                }

                languagesTextView.setText(allLanguages);
            }

            //display genres
            if (result.getGenres() != null)
            {
                TextView genresTextView = ((TextView) rootView.findViewById(R.id.genres));
                String allGenres = "";
                for ( Genre genre : result.getGenres())
                {
                    allGenres = allGenres + genre.getName() + " ";
                }

                genresTextView.setText(allGenres);
            }
        }
    }

}