package anaice.com.br.bringthepopcorn.data.network;

import retrofit2.Retrofit;

@SuppressWarnings("unused")
public class MainService {

    public static final String MOVIE_DB_API_KEY = "";

    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    public static final String IMAGE_URL_185 = BASE_IMAGE_URL + "w185";

    public static final String IMAGE_URL_342 = BASE_IMAGE_URL + "w342";

    public static final String IMAGE_URL_500 = BASE_IMAGE_URL + "w500";

    private final Retrofit retrofit;

    public MainService() {
        retrofit = RetrofitClient.getClient();
    }

    public GenreService getGenreService() {
        return retrofit.create(GenreService.class);
    }

    public MovieService getMovieService() {
        return retrofit.create(MovieService.class);
    }
}
