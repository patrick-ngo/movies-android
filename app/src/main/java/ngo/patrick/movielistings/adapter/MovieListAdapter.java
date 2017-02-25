package ngo.patrick.movielistings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ngo.patrick.movielistings.R;
import ngo.patrick.movielistings.api.TmdbAPI;
import ngo.patrick.movielistings.model.PageListingResult.Result;

/**
 *  Adapter to display the results from the "discover/movie" API call into a listview
 */

public class MovieListAdapter extends ArrayAdapter<Result>
{
    public static Integer MAX_RATING;
    public static Integer MAX_PROGRESS;

    // ViewHolder to avoid findViewById() calls by caching the component view references
    static class ViewHolderMovieItem
    {
        TextView idTextView;                //id
        TextView captionTextView;           //caption
        ImageView thumbnailView;            //thumbnail
        TextView ratingTextView;            //rating
        TextView releaseDateTextView;       //release date
        ProgressBar progressBar;            //progress bar to also represent the rating
    }

    ViewHolderMovieItem viewHolder;

    public MovieListAdapter(Context context, int textViewResourceId)
    {
        super(context, textViewResourceId);

        this.MAX_RATING = getContext().getResources().getInteger(R.integer.max_rating);
        this.MAX_PROGRESS = getContext().getResources().getInteger(R.integer.max_progressbar_value);
    }

    public MovieListAdapter(Context context, int resource, List<Result> items)
    {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;

        if (v == null)
        {
            //inflate only once
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item_movie_row, null);

            //setup ViewHolder by finding appropriate components
            viewHolder = new ViewHolderMovieItem();
            viewHolder.captionTextView = (TextView) v.findViewById(R.id.title);
            viewHolder.thumbnailView = (ImageView) v.findViewById(R.id.poster);
            viewHolder.ratingTextView = (TextView) v.findViewById(R.id.rating);
            viewHolder.releaseDateTextView = (TextView) v.findViewById(R.id.release_date);
            viewHolder.progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            v.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolderMovieItem) v.getTag();
        }

        //Get movie data from specified position
        Result movieItem = getItem(position);

        //Display data to the appropriate components
        if (movieItem != null)
        {
            //title
            if (viewHolder.captionTextView != null)
            {
                viewHolder.captionTextView.setText(movieItem.getTitle());
            }

            //popularity
            if (viewHolder.ratingTextView != null)
            {
                viewHolder.ratingTextView.setText(getContext().getString(R.string.rating) + " " + movieItem.getPopularity().toString());
            }

            //popularity progress bar
            if (viewHolder.progressBar != null)
            {
                Double percentage = ((movieItem.getPopularity() / MAX_RATING)* MAX_PROGRESS);
                viewHolder.progressBar.setProgress(percentage.intValue());
            }

            //release date
            if (viewHolder.releaseDateTextView != null) {
                viewHolder.releaseDateTextView.setText(movieItem.getReleaseDate());
            }

            //thumbnail
            if (viewHolder.thumbnailView != null)
            {
                if (movieItem.getPosterPath() != null)
                {
                    Picasso.with(getContext()).load(TmdbAPI.BASE_URL_IMAGES_LOW + movieItem.getPosterPath()).into(viewHolder.thumbnailView);
                }
                //clear image if no image
                else
                {
                    viewHolder.thumbnailView.setImageResource(android.R.color.transparent);
                }
            }
        }

        return v;
    }


}
