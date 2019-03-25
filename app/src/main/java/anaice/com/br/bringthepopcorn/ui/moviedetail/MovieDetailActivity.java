package anaice.com.br.bringthepopcorn.ui.moviedetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import anaice.com.br.bringthepopcorn.R;
import anaice.com.br.bringthepopcorn.data.db.entity.FavoriteMovie;
import anaice.com.br.bringthepopcorn.data.model.Genre;
import anaice.com.br.bringthepopcorn.data.model.Movie;
import anaice.com.br.bringthepopcorn.data.model.MovieReview;
import anaice.com.br.bringthepopcorn.data.model.MovieTrailer;
import anaice.com.br.bringthepopcorn.data.model.MovieTrailers;
import anaice.com.br.bringthepopcorn.data.network.MainService;
import anaice.com.br.bringthepopcorn.ui.main.MainActivity;
import anaice.com.br.bringthepopcorn.util.AppExecutors;
import anaice.com.br.bringthepopcorn.util.DateUtils;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerClickedListener {

    @BindView(R.id.iv_movie_big_poster)
    ImageView mMovieBigPosterIv;
    @BindView(R.id.iv_movie_poster)
    ImageView mMovieSmallPosterIv;
    @BindView(R.id.iv_movie_star_rating)
    ImageView mMovieStarRatingIv;
    @BindView(R.id.iv_movie_bookmark)
    ImageView mMovieBookmarkIv;
    @BindView(R.id.tv_movie_rating)
    TextView mMovieRatingTv;
    @BindView(R.id.tv_movie_title)
    TextView mMovieTitleTv;
    @BindView(R.id.tv_movie_release_date)
    TextView mMovieReleaseDateTv;
    @BindView(R.id.tv_movie_overview)
    TextView mMovieOverviewTv;
    @BindView(R.id.tv_movie_genre)
    TextView mMovieGenreTv;
    @BindView(R.id.label_movie_title)
    TextView mMovieLabelTitle;
    @BindView(R.id.label_movie_genre)
    TextView mMovieLabelGenre;
    @BindView(R.id.label_movie_release_date)
    TextView mMovieLabelReleaseDate;
    @BindView(R.id.label_movie_overview)
    TextView mMovieLabelOverview;
    @BindView(R.id.tv_empty_text)
    TextView mEmptyText;
    @BindView(R.id.llayout_movie_reviews)
    LinearLayout mMovieReviewsLayout;
    @BindView(R.id.llayout_movie_trailers)
    LinearLayout mMovieTrailersLayout;
    @BindView(R.id.rv_movie_reviews)
    RecyclerView mMovieReviewsRv;
    @BindView(R.id.rv_movie_trailers)
    RecyclerView mMovieTrailersRv;
    @BindView(R.id.scroll_movie_data)
    ScrollView mScrollMovieData;
    @BindView(R.id.progress_fetch_movie)
    ProgressBar mProgressFetchMovie;

    private Movie mMovie;
    private int mMovieId;
    private FavoriteMovie favoriteMovie;
    private MovieDetailViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.MOVIE_ID)) {
            mMovieId = intent.getIntExtra(MainActivity.MOVIE_ID, 0);
        }

        setVariablesInitialState();
        setViewsListeners();
        fetchMovie();
    }

    private void setVariablesInitialState() {
        mEmptyText.setVisibility(View.INVISIBLE);

        mViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);

        mViewModel.getMovieFromDb(mMovieId).observe(this, movie -> {
            this.favoriteMovie = movie;
            if (favoriteMovie == null) {
                mMovieBookmarkIv.setImageDrawable(getDrawable(R.drawable.ic_bookmark_border_yellow_24dp));
            } else {
                mMovieBookmarkIv.setImageDrawable(getDrawable(R.drawable.ic_bookmark_yellow_24dp));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setViewsListeners() {
        mMovieBookmarkIv.setOnClickListener(view -> {
            if (favoriteMovie == null) {
                saveFavoriteMovie();
            } else {
                deleteFavoriteMovie();
            }
        });
    }

    private void fetchMovie() {
        showLoadingFetchMovie();
        mViewModel.getMovie(mMovieId).observe(this, movie -> {
            mMovie = movie;
            hideLoadingFetchMovie();
            if (movie != null) {
                populateScreenWithMovieData(movie);
                setupToolbar(movie.getTitle());
            } else {
                resetScreen();
            }
        });
    }

    private void setupToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void populateScreenWithMovieData(Movie movie) {
        Picasso.get().load(MainService.IMAGE_URL_500 + movie.getBackdrop_path()).into(mMovieBigPosterIv);
        Picasso.get().load(MainService.IMAGE_URL_185 + movie.getPoster_path()).into(mMovieSmallPosterIv);
        mMovieTitleTv.setText(movie.getTitle());
        mMovieRatingTv.setText(String.valueOf(movie.getVote_average()));
        mMovieReleaseDateTv.setText(DateUtils.getBrazilianDateFormat(movie.getRelease_date()));

        StringBuilder sb = new StringBuilder();

        for (Genre genre : movie.getGenres()) {
            sb.append(genre.getName()).append(", ");
        }

        String movieGenre = sb.substring(0, sb.lastIndexOf(","));

        mMovieGenreTv.setText(movieGenre);
        mMovieOverviewTv.setText(movie.getOverview());

        fetchReviews();
        fetchTrailers();
    }

    private void showLoadingFetchMovie() {
        mProgressFetchMovie.setVisibility(View.VISIBLE);
        mScrollMovieData.setVisibility(View.GONE);
    }

    private void hideLoadingFetchMovie() {
        mProgressFetchMovie.setVisibility(View.GONE);
        mScrollMovieData.setVisibility(View.VISIBLE);
    }

    private void fetchReviews() {
        mViewModel.getMovieReviews(mMovieId).observe(this, movieReview -> {
            if (movieReview != null && movieReview.getUserReviews() != null &&
                movieReview.getUserReviews().size() > 0) {
                mMovieReviewsLayout.setVisibility(View.VISIBLE);
                mMovieReviewsRv.setHasFixedSize(true);
                mMovieReviewsRv.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this));
                mMovieReviewsRv.setAdapter(new ReviewAdapter(MovieDetailActivity.this,
                                                             movieReview.getUserReviews()));
            } else {
                mMovieReviewsLayout.setVisibility(View.GONE);
            }
        });
    }

    private void fetchTrailers() {
        mViewModel.getMovieTrailers(mMovieId).observe(this, movieTrailers -> {
            if (movieTrailers != null && movieTrailers.getTrailers() != null &&
                movieTrailers.getTrailers().size() > 0) {
                mMovieTrailersLayout.setVisibility(View.VISIBLE);
                mMovieTrailersRv.setHasFixedSize(true);
                mMovieTrailersRv.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this));
                mMovieTrailersRv.setAdapter(new TrailerAdapter(MovieDetailActivity.this,
                                                               movieTrailers.getTrailers(),
                                                               MovieDetailActivity.this));
            } else {
                mMovieTrailersLayout.setVisibility(View.GONE);
            }
        });
    }

    private void openMovieTrailer(String videoPath) {
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW);
        youtubeIntent.setData(Uri.parse("https://www.youtube.com/watch?v=" + videoPath));
        if (youtubeIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(youtubeIntent);
        }
    }

    @Override
    public void onTrailerClicked(MovieTrailer trailer) {
        openMovieTrailer(trailer.getKey());
    }

    private void saveFavoriteMovie() {
        if (mMovie != null) {
            mViewModel.saveFavoriteMovie(new FavoriteMovie(mMovie.getId(), mMovie.getTitle()));
            mMovieBookmarkIv.setImageDrawable(getDrawable(R.drawable.ic_bookmark_yellow_24dp));
        }
    }

    private void deleteFavoriteMovie() {
        if (mMovie != null) {
            mViewModel.deleteFavoriteMovie(new FavoriteMovie(mMovie.getId(), mMovie.getTitle()));
            mMovieBookmarkIv.setImageDrawable(getDrawable(R.drawable.ic_bookmark_border_yellow_24dp));
        }
    }

    private void resetScreen() {
        mEmptyText.setVisibility(View.VISIBLE);
        mMovieTitleTv.setText("");
        mMovieGenreTv.setText("");
        mMovieReleaseDateTv.setText("");
        mMovieRatingTv.setText("");
        mMovieOverviewTv.setText("");

        mMovieLabelTitle.setText("");
        mMovieLabelOverview.setText("");
        mMovieLabelReleaseDate.setText("");
        mMovieLabelGenre.setText("");

        //Image Views
        mMovieSmallPosterIv.setImageResource(android.R.color.transparent);
        mMovieBigPosterIv.setImageResource(android.R.color.transparent);
        mMovieStarRatingIv.setImageResource(android.R.color.transparent);
    }

}
