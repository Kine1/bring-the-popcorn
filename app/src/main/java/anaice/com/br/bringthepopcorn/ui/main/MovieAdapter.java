package anaice.com.br.bringthepopcorn.ui.main;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import anaice.com.br.bringthepopcorn.R;
import anaice.com.br.bringthepopcorn.data.model.MovieDBResponse;
import anaice.com.br.bringthepopcorn.data.model.Result;
import anaice.com.br.bringthepopcorn.data.network.MainService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    private List<Result> mMovies;

    private final Context mContext;

    private final ListItemClickListener mOnClickListener;

    MovieAdapter(Context context, MovieDBResponse movieDBResponse, ListItemClickListener listener) {
        mContext = context;
        mMovies = movieDBResponse == null ? new ArrayList<Result>() : movieDBResponse.getResults();
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Result movie = mMovies.get(position);
        Picasso.get().load(MainService.IMAGE_URL_185 + movie.getPosterPath()).into(holder.moviePhotoIv);
        holder.movieTitleTv.setText(movie.getTitle());
        holder.movieRatingTv.setText(String.valueOf(movie.getVoteAverage()));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void updateMovies(MovieDBResponse movieDBResponse) {
        mMovies = movieDBResponse.getResults();
        notifyDataSetChanged();
    }

    public void updateMovies(List<Result> favoriteMovies) {
        Log.d("MovieAdapter", "Filmes no adapter foi atualizado");
        mMovies = favoriteMovies;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener {
        void onListItemClick(int movieId);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_movie_title)
        TextView movieTitleTv;
        @BindView(R.id.tv_movie_rating)
        TextView movieRatingTv;
        @BindView(R.id.iv_movie_photo)
        ImageView moviePhotoIv;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(mMovies.get(clickedPosition).getId());
        }
    }
}
