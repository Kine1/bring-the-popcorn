package anaice.com.br.bringthepopcorn.ui.moviedetail;

import android.app.Application;
import android.util.Log;

import anaice.com.br.bringthepopcorn.data.db.AppDatabase;
import anaice.com.br.bringthepopcorn.data.db.entity.FavoriteMovie;
import anaice.com.br.bringthepopcorn.data.model.Movie;
import anaice.com.br.bringthepopcorn.data.model.MovieReview;
import anaice.com.br.bringthepopcorn.data.model.MovieTrailers;
import anaice.com.br.bringthepopcorn.data.network.MainService;
import anaice.com.br.bringthepopcorn.util.AppExecutors;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("WeakerAccess")
public class MovieDetailViewModel extends AndroidViewModel {

    private MainService mMainService;
    private AppDatabase mDb;
    private MutableLiveData<Movie> mMovie;
    private MutableLiveData<MovieReview> mMovieReviews;
    private MutableLiveData<MovieTrailers> mMovieTrailers;


    public MovieDetailViewModel(@NonNull Application application) {
        super(application);

        mMainService = new MainService();
        mDb = AppDatabase.getInstance(application);
        mMovie = new MutableLiveData<>();
        mMovieReviews = new MutableLiveData<>();
        mMovieTrailers = new MutableLiveData<>();
    }

    public LiveData<Movie> getMovie(int movieId) {
        loadMovie(movieId);
        return mMovie;
    }

    public LiveData<MovieReview> getMovieReviews(int movieId) {
        loadReviews(movieId);
        return mMovieReviews;
    }

    public LiveData<MovieTrailers> getMovieTrailers(int movieId) {
        loadTrailers(movieId);
        return mMovieTrailers;
    }

    public LiveData<FavoriteMovie> getMovieFromDb(int movieId) {
        return mDb.favoriteMovieDao().getFavoriteMovieById(movieId);
    }

    public void saveFavoriteMovie(FavoriteMovie movie) {
        Log.d("MovieDetail", "Salvando favorito...");
        AppExecutors.getInstance().diskIO().execute(() -> {
            mDb.favoriteMovieDao().insert(movie);
            Log.d("MovieDetail", "Insert ocorreu...");
        });
    }

    public void deleteFavoriteMovie(FavoriteMovie movie) {
        AppExecutors.getInstance().diskIO().execute(() -> mDb.favoriteMovieDao().delete(movie));
    }

    private void loadMovie(int movieId) {
        mMainService.getMovieService().getMovie(movieId).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful()) {
                    mMovie.setValue(response.body());
                } else {
                    mMovie.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                t.printStackTrace();
                mMovie.setValue(null);
            }
        });
    }

    private void loadReviews(int movieId) {
        mMainService.getMovieService().getReviews(movieId).enqueue(new Callback<MovieReview>() {
            @Override
            public void onResponse(@NonNull Call<MovieReview> call, @NonNull Response<MovieReview> response) {
                if (response.isSuccessful()) {
                    mMovieReviews.setValue(response.body());
                } else {
                    mMovieReviews.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieReview> call, @NonNull Throwable t) {
                t.printStackTrace();
                mMovieReviews.setValue(null);
            }
        });
    }

    private void loadTrailers(int movieId) {
        mMainService.getMovieService().getTrailers(movieId).enqueue(new Callback<MovieTrailers>() {
            @Override
            public void onResponse(@NonNull Call<MovieTrailers> call, @NonNull Response<MovieTrailers> response) {
                if (response.isSuccessful()) {
                    mMovieTrailers.setValue(response.body());
                } else {
                    mMovieTrailers.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieTrailers> call, @NonNull Throwable t) {
                mMovieTrailers.setValue(null);
            }
        });
    }

}
