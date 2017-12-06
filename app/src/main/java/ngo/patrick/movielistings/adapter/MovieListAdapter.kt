package ngo.patrick.movielistings.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import com.squareup.picasso.Picasso

import ngo.patrick.movielistings.R
import ngo.patrick.movielistings.api.TmdbAPI
import ngo.patrick.movielistings.model.PageListingResult.Result

/**
 * Adapter to display the results from the "discover/movie" API call into a listview
 */

class MovieListAdapter : ArrayAdapter<Result> {

    internal var viewHolder: ViewHolderMovieItem? = null

    // ViewHolder to avoid findViewById() calls by caching the component view references
    internal class ViewHolderMovieItem {
        var captionTextView: TextView? = null           //caption
        var thumbnailView: ImageView? = null            //thumbnail
        var ratingTextView: TextView? = null            //rating
        var releaseDateTextView: TextView? = null       //release date
        var progressBar: ProgressBar? = null            //progress bar to also represent the rating
    }

    constructor(context: Context, textViewResourceId: Int) : super(context, textViewResourceId) {

        MAX_RATING = getContext().resources.getInteger(R.integer.max_rating)
        MAX_PROGRESS = getContext().resources.getInteger(R.integer.max_progressbar_value)
    }

    constructor(context: Context, resource: Int, items: List<Result>) : super(context, resource, items) {}

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView

        if (v == null) {
            //inflate only once
            val vi: LayoutInflater
            vi = LayoutInflater.from(context)
            v = vi.inflate(R.layout.list_item_movie_row, null)

            //setup ViewHolder by finding appropriate components
            viewHolder = ViewHolderMovieItem()
            viewHolder?.captionTextView = v!!.findViewById(R.id.title) as TextView
            viewHolder?.thumbnailView = v.findViewById(R.id.poster) as ImageView
            viewHolder?.ratingTextView = v.findViewById(R.id.rating) as TextView
            viewHolder?.releaseDateTextView = v.findViewById(R.id.release_date) as TextView
            viewHolder?.progressBar = v.findViewById(R.id.progressBar) as ProgressBar
            v.tag = viewHolder
        } else {
            viewHolder = v.tag as ViewHolderMovieItem
        }

        //Get movie data from specified position
        val movieItem = getItem(position)

        //Display data to the appropriate components
        if (movieItem != null) {

            //title
            viewHolder?.captionTextView?.let {
                it.text = movieItem.title
            }

            //popularity
            viewHolder?.ratingTextView?.let {
                it.text = context.getString(R.string.rating) + " " + movieItem.popularity!!.toString()
            }

            //popularity progress bar
            viewHolder?.progressBar?.let {
                val percentage = movieItem.popularity / MAX_RATING * MAX_PROGRESS
                it.progress = percentage.toInt()
            }

            //release date
            viewHolder?.releaseDateTextView?.let {
                it.text = movieItem.releaseDate
            }

            //thumbnail
            viewHolder?.thumbnailView?.let {
                if (movieItem.posterPath != null) {
                    Picasso.with(context).load(TmdbAPI.BASE_URL_IMAGES_LOW + movieItem.posterPath).into(it)
                }
                else {
                    //clear image if no image
                    it.setImageResource(android.R.color.transparent)
                }
            }
        }

        return v
    }

    companion object {
        var MAX_RATING: Int = 10
        var MAX_PROGRESS: Int = 100
    }


}
