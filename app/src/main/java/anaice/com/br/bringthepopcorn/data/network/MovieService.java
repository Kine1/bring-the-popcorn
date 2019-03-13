package anaice.com.br.bringthepopcorn.data.network;

import anaice.com.br.bringthepopcorn.data.model.Movie;
import anaice.com.br.bringthepopcorn.data.model.MovieDBResponse;
import anaice.com.br.bringthepopcorn.data.model.MovieReview;
import anaice.com.br.bringthepopcorn.data.model.MovieTrailer;
import anaice.com.br.bringthepopcorn.data.model.MovieTrailers;
import anaice.com.br.bringthepopcorn.data.model.Result;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieService {

    @GET("movie/popular?api_key=" + MainService.MOVIE_DB_API_KEY + "&language=pt-BR")
    Call<MovieDBResponse> listPopularMovies();

    @GET("movie/top_rated?api_key=" + MainService.MOVIE_DB_API_KEY + "&language=pt-BR")
    Call<MovieDBResponse> listTopRatedMovies();

    @GET("movie/{id}?api_key=" + MainService.MOVIE_DB_API_KEY + "&language=pt-BR")
    Call<Movie> getMovie(@Path("id") int movieId);

    @GET("movie/{id}?api_key=" + MainService.MOVIE_DB_API_KEY + "&language=pt-BR")
    Call<Result> getMovieAsSingleResult(@Path("id") int movieId);

    @GET("movie/{id}/videos?api_key=" + MainService.MOVIE_DB_API_KEY)
    Call<MovieTrailers> getTrailers(@Path("id") int movieId);

    @GET("movie/{id}/reviews?api_key=" + MainService.MOVIE_DB_API_KEY)
    Call<MovieReview> getReviews(@Path("id") int movieId);
}
