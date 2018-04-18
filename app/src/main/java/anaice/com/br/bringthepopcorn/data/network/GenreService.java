package anaice.com.br.bringthepopcorn.data.network;

import anaice.com.br.bringthepopcorn.data.model.MovieDBResponse;
import retrofit2.Call;
import retrofit2.http.GET;

@SuppressWarnings("unused")
public interface GenreService {
    @GET("movie/top_rated?api_key=" + MainService.MOVIE_DB_API_KEY + "&language=pt-BR")
    Call<MovieDBResponse> getMovieGenre();
}
