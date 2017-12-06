package ngo.patrick.movielistings

import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast

import ngo.patrick.movielistings.adapter.MovieListAdapter
import ngo.patrick.movielistings.api.TmdbAPI
import ngo.patrick.movielistings.model.PageListingResult.PageListingResult
import ngo.patrick.movielistings.model.PageListingResult.Result
import ngo.patrick.movielistings.task.FetchAllMoviesTask
import retrofit2.Call

/**
 * MainActivity: Displays a list of movies
 */

open class MainActivity : AppCompatActivity() {

    //API
    internal var apiService: TmdbAPI? = null

    var movieListAdapter: MovieListAdapter? = null          //adapter to display the movies to view
    var swipeRefreshLayout: SwipeRefreshLayout? = null       //pull to refresh
    var isLoadingMore: Boolean = false               //flag to know when listview is bottom loading

    private var listView: ListView? = null                          //main listview
    private var progressBarLoadMore: ProgressBar? = null            //bottom loading progress bar
    private var pageNumber: Int = 1                     //current page number of the movie listings


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FIRST_PAGE = resources.getInteger(R.integer.first_page)
        MAX_PAGE = resources.getInteger(R.integer.max_pages)

        //create a MovieListAdapter and bind it to the listview in the layout
        movieListAdapter = MovieListAdapter(this, R.layout.list_item_movie_row)
        listView = findViewById(R.id.listview_movielist) as ListView
        listView!!.adapter = movieListAdapter


        //Create the click listener for the listview items (clicking on a movie leads to the Detail page of that movie)
        listView!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, l ->
            //Fire an Intent to launch the Detail Activity, with the movie id as a parameter
            val selectedMovie = movieListAdapter?.getItem(position)

            val intentID = getString(R.string.intent_movie_id)

            val detailIntent = Intent(applicationContext, DetailActivity::class.java)
            detailIntent.putExtra(intentID, Integer.toString(selectedMovie!!.id!!))
            startActivity(detailIntent)
        }


        //setup pull to refresh action
        swipeRefreshLayout = findViewById(R.id.swiperefresh) as SwipeRefreshLayout
        swipeRefreshLayout?.setOnRefreshListener { resetData() }


        //get initial data to display movie list
        resetData()


        //load more from bottom mechanism
        listView!!.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}

            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (pageNumber < MainActivity.MAX_PAGE) {
                    //find when the scroll has reached the bottom
                    if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                        if (isLoadingMore == false) {
                            loadMoreData()
                        }
                    }
                }
            }
        })

    }

    private fun resetData() {
        //reset page number
        pageNumber = FIRST_PAGE
        getData()
    }

    private fun loadMoreData() {
        //set flag, increment page number and show loading
        isLoadingMore = true
        pageNumber++
        showBottomLoading(true)
        getData()
    }

    private fun getData() {
        //create api
        apiService = TmdbAPI.tmdb.create(TmdbAPI::class.java)

        //create retrofit call
        val call = apiService!!.getAllMovies(
                TmdbAPI.API_KEY,
                TmdbAPI.SORT_BY,
                TmdbAPI.PRIMARY_RELEASE_DATE,
                pageNumber)

        //get data
        FetchAllMoviesTask(this).execute(call)
    }

    fun showBottomLoading(show: Boolean?) {
        if (progressBarLoadMore == null) {
            progressBarLoadMore = ProgressBar(this)
        }

        //add or remove the progressBar from the footer to create a bottom loading effect
        if (show!!) {
            listView!!.addFooterView(progressBarLoadMore)
        } else {
            listView!!.removeFooterView(progressBarLoadMore)
        }
    }

    companion object {
        private var FIRST_PAGE: Int = 0
        private var MAX_PAGE: Int = 0
    }
}
