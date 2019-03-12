package anaice.com.br.bringthepopcorn.ui.moviedetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import anaice.com.br.bringthepopcorn.data.model.MovieReview;
import anaice.com.br.bringthepopcorn.ui.main.MovieAdapter;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import anaice.com.br.bringthepopcorn.R;
import anaice.com.br.bringthepopcorn.data.model.Genre;
import anaice.com.br.bringthepopcorn.data.model.Movie;
import anaice.com.br.bringthepopcorn.data.network.MainService;
import anaice.com.br.bringthepopcorn.ui.main.MainActivity;
import anaice.com.br.bringthepopcorn.util.DateUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends Activity {

    private MainService mMainService;
    private int mMovieId;
    private ImageView mMovieBigPosterIv;
    private ImageView mMovieSmallPosterIv;
    private ImageView mMovieStarRatingIv;
    private TextView mMovieRatingTv;
    private TextView mMovieTitleTv;
    private TextView mMovieReleaseDateTv;
    private TextView mMovieOverviewTv;
    private TextView mMovieGenreTv;
    private TextView mMovieLabelTitle;
    private TextView mMovieLabelGenre;
    private TextView mMovieLabelReleaseDate;
    private TextView mMovieLabelOverview;
    private TextView mEmptyText;
    private LinearLayout mMovieReviewsLayout;
    private RecyclerView mMovieReviewsRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        mMovieId = intent.getIntExtra(MainActivity.MOVIE_ID, 0);

        mMainService = new MainService();

        initViews();
        fetchMovie();
    }

    private void initViews() {

        mMovieBigPosterIv = findViewById(R.id.iv_movie_big_poster);
        mMovieSmallPosterIv = findViewById(R.id.iv_movie_poster);
        mMovieStarRatingIv = findViewById(R.id.iv_movie_star_rating);
        mMovieTitleTv = findViewById(R.id.tv_movie_title);
        mMovieRatingTv = findViewById(R.id.tv_movie_rating);
        mMovieReleaseDateTv = findViewById(R.id.tv_movie_release_date);
        mMovieOverviewTv = findViewById(R.id.tv_movie_overview);
        mMovieGenreTv = findViewById(R.id.tv_movie_genre);
        mMovieLabelTitle = findViewById(R.id.label_movie_title);
        mMovieLabelGenre = findViewById(R.id.label_movie_genre);
        mMovieLabelReleaseDate = findViewById(R.id.label_movie_release_date);
        mMovieLabelOverview = findViewById(R.id.label_movie_overview);
        mEmptyText = findViewById(R.id.tv_empty_text);
        mMovieReviewsLayout = findViewById(R.id.llayout_movie_reviews);
        mMovieReviewsRv = findViewById(R.id.rv_movie_reviews);

        mEmptyText.setVisibility(View.INVISIBLE);
    }

    private void fetchMovie() {
        mMainService.getMovieService().getMovie(mMovieId).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful()) {
                    Movie movie = response.body();
                    if (movie != null) {
                        fillScreenMovieData(movie);
                    }
                } else {
                    resetScreen();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                t.printStackTrace();
                resetScreen();
            }
        });
    }

    private void fillScreenMovieData(Movie movie) {
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

    private void fetchReviews() {
        mMainService.getMovieService().getReviews(mMovieId).enqueue(new Callback<MovieReview>() {
            @Override
            public void onResponse(@NonNull Call<MovieReview> call, @NonNull Response<MovieReview> response) {
                if (response.isSuccessful()) {
                    MovieReview reviews = response.body();
                    if (reviews != null && reviews.getUserReviews() != null && reviews.getUserReviews().size() > 0) {
                        mMovieReviewsLayout.setVisibility(View.VISIBLE);
                        mMovieReviewsRv.setHasFixedSize(true);
                        mMovieReviewsRv.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this));
                        mMovieReviewsRv.setAdapter(new ReviewAdapter(MovieDetailActivity.this, reviews.getUserReviews()));
                    } else {
                        mMovieReviewsLayout.setVisibility(View.GONE);
                    }
                } else {
                    mMovieReviewsLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieReview> call, @NonNull Throwable t) {
                t.printStackTrace();
                mMovieReviewsLayout.setVisibility(View.GONE);
            }
        });
    }

    private void fetchTrailers() {

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
