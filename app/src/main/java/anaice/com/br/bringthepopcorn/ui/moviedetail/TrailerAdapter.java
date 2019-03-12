package anaice.com.br.bringthepopcorn.ui.moviedetail;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import anaice.com.br.bringthepopcorn.R;
import anaice.com.br.bringthepopcorn.data.model.MovieTrailer;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<MovieTrailer> mTrailers;
    private Context mContext;
    private TrailerClickedListener mOnListItemClick;

    public TrailerAdapter(Context context, List<MovieTrailer> trailers, TrailerClickedListener listener) {
        mContext = context;
        mTrailers = trailers;
        mOnListItemClick = listener;
    }

    @NonNull
    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_trailer, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.ViewHolder holder, int position) {

        MovieTrailer trailer = mTrailers.get(position);

        SpannableString content = new SpannableString(trailer.getName());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        holder.movieTrailerName.setText(content);

    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public interface TrailerClickedListener {
        void onTrailerClicked(MovieTrailer trailer);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView movieTrailerName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            movieTrailerName = itemView.findViewById(R.id.tv_movie_trailer_name);

            movieTrailerName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnListItemClick.onTrailerClicked(mTrailers.get(clickedPosition));
        }

    }

}
