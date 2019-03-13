package anaice.com.br.bringthepopcorn.ui.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anaice.com.br.bringthepopcorn.R;
import anaice.com.br.bringthepopcorn.data.db.AppDatabase;
import anaice.com.br.bringthepopcorn.data.db.entity.FavoriteMovie;
import anaice.com.br.bringthepopcorn.data.model.MovieDBResponse;
import anaice.com.br.bringthepopcorn.data.model.Result;
import anaice.com.br.bringthepopcorn.data.network.MainService;
import anaice.com.br.bringthepopcorn.ui.moviedetail.MovieDetailActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    private RecyclerView mMoviesRv;
    private static final int POPULAR_MOVIES = 1;
    private static final int TOP_RATED_MOVIES = 2;
    private static final int FAVORITE_MOVIES = 3;


    private int mNumberOfColumns;
    private MovieAdapter mAdapter;
    private MainService mMainService;
    private TextView mEmptyListTv;
    private int selectedMovieFilter = POPULAR_MOVIES;

    public static final String MOVIE_ID = "MOVIE_ID";
    private final String TAG = MainActivity.this.getClass().getSimpleName();

    private AppDatabase mDb;
    private List<Result> mFavoriteMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViewsAndRequiredVariables();
        defineNumberOfColumnsForOrientation();
        configureRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        switch (selectedMovieFilter) {
            case TOP_RATED_MOVIES:
                loadTopRatedMovies();
                break;
            case FAVORITE_MOVIES:
                loadFavoriteMovies();
                break;
            default:
                loadPopularMovies();
                break;
        }
    }

    private void initViewsAndRequiredVariables() {
        mMoviesRv = findViewById(R.id.rv_movies);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEmptyListTv = findViewById(R.id.tv_empty_movie_list);

        mMainService = new MainService();
        mDb = AppDatabase.getInstance(getApplicationContext());
        showMovieList();
    }

    private void defineNumberOfColumnsForOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mNumberOfColumns = 2;
        } else {
            mNumberOfColumns = 4;
        }
    }

    private void configureRecyclerView() {
        mMoviesRv.setHasFixedSize(true);
        mMoviesRv.setLayoutManager(new GridLayoutManager(this, mNumberOfColumns));
        mAdapter = new MovieAdapter(MainActivity.this, null, MainActivity.this);
        mMoviesRv.setAdapter(mAdapter);

    }

    private void loadPopularMovies() {
        mMainService.getMovieService().listPopularMovies().enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieDBResponse> call, @NonNull Response<MovieDBResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Resposta obtida para filmes populares: " + response);
                    showMovieList();
                    mAdapter.updateMovies(response.body());
                } else {
                    showEmptyListWarning();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDBResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "Falha na busca de filmes populares: " + t.getMessage());
                t.printStackTrace();
                showEmptyListWarning();
            }
        });
    }

    private void loadTopRatedMovies() {
        mMainService.getMovieService().listTopRatedMovies().enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieDBResponse> call, @NonNull Response<MovieDBResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Resposta obtida para filmes bem avaliados: " + response);
                    showMovieList();
                    mAdapter.updateMovies(response.body());
                } else {
                    showEmptyListWarning();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDBResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "Falha na busca de filmes bem avaliados" + t.getMessage());
                showEmptyListWarning();
            }
        });
    }

    private void loadFavoriteMovies() {
        mDb.favoriteMovieDao().getAll().observe(MainActivity.this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(final List<FavoriteMovie> favoriteMovies) {
                Log.d(TAG, "Recebeu filmes favoritos do banco. Size = " + favoriteMovies.size());
                for (FavoriteMovie favoriteMovie: favoriteMovies) {
                    mMainService.getMovieService().getMovieAsSingleResult(favoriteMovie.getId()).enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            Log.d(TAG, "Chamada ao movie/{id} ocorreu");
                            if (response.isSuccessful()) {
                                Log.d(TAG, "Chamada ao movie/{id} foi bem sucedida");
                                showMovieList();
                                mFavoriteMovies.add(response.body());
                                mAdapter.updateMovies(mFavoriteMovies);
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            Log.d(TAG, "Falha na busca de filmes favoritos " + t.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void showEmptyListWarning() {
        mEmptyListTv.setVisibility(View.VISIBLE);
        mMoviesRv.setVisibility(View.GONE);
    }

    private void showMovieList() {
        mEmptyListTv.setVisibility(View.GONE);
        mMoviesRv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListItemClick(int movieId) {
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.putExtra(MOVIE_ID, movieId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        mFavoriteMovies.clear();

        switch (item.getItemId()) {
            case R.id.action_popular:
                selectedMovieFilter = POPULAR_MOVIES;
                loadPopularMovies();
                return true;

            case R.id.action_top_rated:
                selectedMovieFilter = TOP_RATED_MOVIES;
                loadTopRatedMovies();
                return true;

            case R.id.action_favorites:
                selectedMovieFilter = FAVORITE_MOVIES;
                loadFavoriteMovies();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
