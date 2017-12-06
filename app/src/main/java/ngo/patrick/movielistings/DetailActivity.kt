package ngo.patrick.movielistings

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

import ngo.patrick.movielistings.api.TmdbAPI
import ngo.patrick.movielistings.model.MovieDetailsResult.MovieDetailsResult
import ngo.patrick.movielistings.task.FetchMovieDetailTask
import retrofit2.Call

/**
 * Detail Activity: Displays details about a specific movie selected from the MainActivity
 */

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        val bookButton = findViewById(R.id.book_button) as Button
        bookButton.setOnClickListener {
            //launch cathay website on click
            val url = TmdbAPI.CATHAY_CINEPLEXES
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }


        //Get initial data to display movie details
        refreshData()
    }

    private fun refreshData() {
        //create api
        val apiService = TmdbAPI.tmdb.create<TmdbAPI>(TmdbAPI::class.java)
        val rootView = findViewById(R.id.activity_detail)         //keep reference of root view to send to the async task
        val movieID = getString(R.string.intent_movie_id)       //get movie id from intent

        //create retrofit call
        val call = apiService.getMovieDetails(intent.getStringExtra(movieID), TmdbAPI.API_KEY)
        val movieDetailTask = FetchMovieDetailTask(this, rootView)

        //get data
        movieDetailTask.execute(call)
    }
}
