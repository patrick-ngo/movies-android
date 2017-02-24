package ngo.patrick.movielistings.api;

/**
 *  Service to create query the TMDB API
 */

import ngo.patrick.movielistings.model.MovieDetailsResult.MovieDetailsResult;
import ngo.patrick.movielistings.model.PageListingResult.PageListingResult;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface TmdbAPI
{
    //base urls
    public String BASE_URL_API = "http://api.themoviedb.org/3/";
    public String BASE_URL_IMAGES_LOW = "https://image.tmdb.org/t/p/w185";      //use w185 size for listview
    public String BASE_URL_IMAGES_HIGH = "https://image.tmdb.org/t/p/w500";     //use w500 size for detailview
    public String CATHAY_CINEPLEXES = "http://www.cathaycineplexes.com.sg";     //when pressing on the purchase ticket button

    //hardcoded query parameters
    public String API_KEY = "328c283cd27bd1877d9080ccb1604c91";
    public String SORT_BY = "release_date.desc";
    public String PRIMARY_RELEASE_DATE = "2016-12-31";


    //discover movies, from the set sorting method, primary release date and page number
    @GET("discover/movie")
    Call<PageListingResult> getAllMovies(
            @Query("api_key") String apiKey,
            @Query("sort_by") String sortBy,
            @Query("primary_release_date.lte") String primaryReleaseDate,
            @Query("page") Integer page);


    //get details about a specific movie, from its id
    @GET("movie/{id}")
    Call<MovieDetailsResult> getMovieDetails(
            @Path("id") String id,
            @Query("api_key") String apiKey);


    public static final Retrofit tmdb = new Retrofit.Builder()
            .baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}