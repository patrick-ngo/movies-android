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

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item_movie_row, null);
        }

        //Get movie data from specified position

        {
            //title
            }

            //popularity
            }

            //release date
            }

            //thumbnail
            {
                {
                }
            }

            //id (FOR DEBUG)
            {
            }
        }

        return v;
    }


}
