package ngo.patrick.movielistings.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ngo.patrick.movielistings.R;
import ngo.patrick.movielistings.model.PageListingResult.Result;

/**
 *  Adapter to display the results from the "discover/movie" API call into a listview
 */

public class MovieListAdapter extends ArrayAdapter<Result>
{
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

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item_movie_row, null);
        }

        //Get movie data from specified position
        Result p = getItem(position);

        //Find appropriate layout components and display respective data
        if (p != null)
        {
            TextView idTextView = (TextView) v.findViewById(R.id.id);
            TextView captionTextView = (TextView) v.findViewById(R.id.title);
            ImageView thumbnailView = (ImageView) v.findViewById(R.id.thumbnail);
            TextView createdOnTextView = (TextView) v.findViewById(R.id.rating);
            TextView updatedOnTextView = (TextView) v.findViewById(R.id.release_date);


            //title
            if (captionTextView != null) {
                captionTextView.setText(p.getOriginalTitle());
            }

            //popularity
            if (createdOnTextView != null) {
                createdOnTextView.setText(getContext().getString(R.string.rating) + p.getPopularity().toString());
            }

            //release date
            if (updatedOnTextView != null) {
                updatedOnTextView.setText(p.getReleaseDate());
            }

            //thumbnail
            if (thumbnailView != null)
            {
                if (p.getPosterPath() != null)
                {
                    String baseUrl = "https://image.tmdb.org/t/p/w185";
                    Picasso.with(getContext()).load(baseUrl + p.getPosterPath()).into(thumbnailView);
                }
            }

            //id (FOR DEBUG)
            if (idTextView != null)
            {
                idTextView.setText(Integer.toString(p.getId()));

            }
        }

        return v;
    }


}
