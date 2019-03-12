package anaice.com.br.bringthepopcorn.ui.moviedetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anaice.com.br.bringthepopcorn.R;
import anaice.com.br.bringthepopcorn.data.model.MovieReview;
import anaice.com.br.bringthepopcorn.data.model.UserReview;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<UserReview> mReviews;
    private Context mContext;

    public ReviewAdapter(Context context, List<UserReview> reviews) {
        mContext = context;
        mReviews = reviews == null ? new ArrayList<UserReview>() : reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_review, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        UserReview review = mReviews.get(position);

        holder.movieReviewUsername.setText(review.getAuthor());
        holder.movieReviewContent.setText(review.getContent());

    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void updateReviews(List<UserReview> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView movieReviewUsername;
        private final TextView movieReviewContent;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            movieReviewUsername = itemView.findViewById(R.id.tv_movie_review_username);
            movieReviewContent = itemView.findViewById(R.id.tv_movie_review_content);
        }

    }

}
