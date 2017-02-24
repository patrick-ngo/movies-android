package ngo.patrick.movielistings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    // ViewHolder to avoid findViewById() calls by caching the component view references
    static class ViewHolderMovieItem
    {
        TextView idTextView;                //id
        TextView captionTextView;           //caption
        ImageView thumbnailView;            //thumbnail
        TextView ratingTextView;            //rating
        TextView releaseDateTextView;       //release date
    }

    ViewHolderMovieItem viewHolder;

    public MovieListAdapter(Context context, int textViewResourceId)
    {
        super(context, textViewResourceId);
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
            viewHolder.idTextView = (TextView) v.findViewById(R.id.id);
            viewHolder.captionTextView = (TextView) v.findViewById(R.id.title);
            viewHolder.thumbnailView = (ImageView) v.findViewById(R.id.thumbnail);
            viewHolder.ratingTextView = (TextView) v.findViewById(R.id.rating);
            viewHolder.releaseDateTextView = (TextView) v.findViewById(R.id.release_date);
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
            if (viewHolder.captionTextView != null) {
                viewHolder.captionTextView.setText(movieItem.getOriginalTitle());
            }

            //popularity
            if (viewHolder.ratingTextView != null) {
                viewHolder.ratingTextView.setText(getContext().getString(R.string.rating) + movieItem.getPopularity().toString());
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
            }

            //id (FOR DEBUG)
            if (viewHolder.idTextView != null)
            {
                viewHolder.idTextView.setText(Integer.toString(movieItem.getId()));
            }
        }

        return v;
    }


}
