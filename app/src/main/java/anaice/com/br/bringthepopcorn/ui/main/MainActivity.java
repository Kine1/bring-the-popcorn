package anaice.com.br.bringthepopcorn.ui.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import anaice.com.br.bringthepopcorn.R;
import anaice.com.br.bringthepopcorn.data.model.Result;
import anaice.com.br.bringthepopcorn.ui.moviedetail.MovieDetailActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    @BindView(R.id.tv_empty_movie_list)
    TextView mEmptyListTv;
    @BindView(R.id.rv_movies)
    RecyclerView mMoviesRv;
    private static final int POPULAR_MOVIES = 1;
    private static final int TOP_RATED_MOVIES = 2;
    private static final int FAVORITE_MOVIES = 3;

    private int mNumberOfColumns;
    private MovieAdapter mAdapter;
    private int mSelectedMovieFilter = POPULAR_MOVIES;
    private MainActivityViewModel mViewModel;

    public static final String MOVIE_ID = "MOVIE_ID";
    public static final String SELECTED_FILTER = "SELECTED_FILTER";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Get selected movie filter
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_FILTER)) {
            this.mSelectedMovieFilter = savedInstanceState.getInt(SELECTED_FILTER);
        }

        initViewsAndRequiredVariables();
        defineNumberOfColumnsForOrientation();
        configureRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        switch (mSelectedMovieFilter) {
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SELECTED_FILTER, mSelectedMovieFilter);
    }

    private void initViewsAndRequiredVariables() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
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
        mViewModel.getPopularMovies().observe(this, this::handleMoviesVisibility);
    }

    private void loadTopRatedMovies() {
        mViewModel.getTopRatedMovies().observe(this, this::handleMoviesVisibility);
    }

    private void loadFavoriteMovies() {
        mViewModel.getFavoriteMovies().observe(this, this::handleMoviesVisibility);
    }

    private void handleMoviesVisibility(List<Result> results) {
        if (results != null) {
            mAdapter.updateMovies(results);
            showMovieList();
        } else {
            showEmptyListWarning();
        }
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
        switch (item.getItemId()) {
            case R.id.action_popular:
                mSelectedMovieFilter = POPULAR_MOVIES;
                loadPopularMovies();
                return true;

            case R.id.action_top_rated:
                mSelectedMovieFilter = TOP_RATED_MOVIES;
                loadTopRatedMovies();
                return true;

            case R.id.action_favorites:
                mSelectedMovieFilter = FAVORITE_MOVIES;
                loadFavoriteMovies();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
