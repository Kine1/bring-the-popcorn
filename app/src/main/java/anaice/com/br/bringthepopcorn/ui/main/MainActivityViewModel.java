package anaice.com.br.bringthepopcorn.ui.main;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import anaice.com.br.bringthepopcorn.data.db.AppDatabase;
import anaice.com.br.bringthepopcorn.data.db.entity.FavoriteMovie;
import anaice.com.br.bringthepopcorn.data.model.MovieDBResponse;
import anaice.com.br.bringthepopcorn.data.model.Result;
import anaice.com.br.bringthepopcorn.data.network.MainService;
import anaice.com.br.bringthepopcorn.util.AppExecutors;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("WeakerAccess")
public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Result>> mPopularMovies;
    private MutableLiveData<List<Result>> mTopRatedMovies;
    private MutableLiveData<List<Result>> mFavoriteMovies;
    private List<Result> mTempFavoriteMovies;

    private final String TAG = MainActivityViewModel.this.getClass().getSimpleName();
    private AppDatabase mDb;
    private Context context;
    private MainService mMainService;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        context = application;
        mDb = AppDatabase.getInstance(context);
        mMainService = new MainService();
        this.mTempFavoriteMovies = new ArrayList<>();

        this.mPopularMovies = new MutableLiveData<>();
        this.mTopRatedMovies = new MutableLiveData<>();
        this.mFavoriteMovies = new MutableLiveData<>();
    }

    public LiveData<List<Result>> getPopularMovies() {
        loadPopularMovies();
        return mPopularMovies;
    }

    public LiveData<List<Result>> getTopRatedMovies() {
        loadTopRatedMovies();
        return mTopRatedMovies;
    }

    public LiveData<List<Result>> getFavoriteMovies() {
        loadFavoriteMovies();
        return mFavoriteMovies;
    }

    private void loadPopularMovies() {
        mMainService.getMovieService().listPopularMovies().enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieDBResponse> call, @NonNull Response<MovieDBResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Resposta obtida para filmes populares: " + response);
                    mPopularMovies.setValue(response.body().getResults());
                } else {
                    mPopularMovies.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDBResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "Falha na busca de filmes populares: " + t.getMessage());
                t.printStackTrace();
                mPopularMovies.setValue(null);
            }
        });
    }

    private void loadTopRatedMovies() {
        mMainService.getMovieService().listTopRatedMovies().enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieDBResponse> call, @NonNull Response<MovieDBResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Resposta obtida para filmes bem avaliados: " + response);
                    mTopRatedMovies.setValue(response.body().getResults());
                } else {
                    mTopRatedMovies.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDBResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "Falha na busca de filmes bem avaliados" + t.getMessage());
                mTopRatedMovies.setValue(null);
            }
        });
    }

    private void loadFavoriteMovies() {
        // Clear temporary list state to avoid repeated records
        mTempFavoriteMovies.clear();
        LiveData<List<FavoriteMovie>> liveData = mDb.favoriteMovieDao().getAll();
        liveData.observeForever(new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(List<FavoriteMovie> favoriteMoviesFromDb) {
                Log.d(TAG, "Recebeu filmes favoritos do banco. Size = " + favoriteMoviesFromDb.size());

                // Iterate over favorite movie list from the DB to make API requestes for each movie by its ID
                for (FavoriteMovie favoriteMovie : favoriteMoviesFromDb) {
                    mMainService.getMovieService()
                                .getMovieAsSingleResult(favoriteMovie.getId())
                                .enqueue(new Callback<Result>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                                        Log.d(TAG, "Chamada ao movie/{id} ocorreu");
                                        if (response.isSuccessful()) {
                                            Log.d(TAG, "Chamada ao movie/{id} foi bem sucedida");
                                            mTempFavoriteMovies.add(response.body());
                                            mFavoriteMovies.setValue(mTempFavoriteMovies);
                                        } else {
                                            mFavoriteMovies.setValue(null);
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                                        Log.d(TAG, "Falha na busca de filmes favoritos " + t.getMessage());
                                        mFavoriteMovies.setValue(null);
                                    }
                                });
                }
                liveData.removeObserver(this);
            }
        });
    }

}
