package io.github.x_wei.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.MovieGridViewHolder> {
    private static final String TAG = MovieGridAdapter.class.getSimpleName();
    private Movie[] mMovies;
    private Context mContext;
    private final MovieAdapterOnClickHandler mClickHandler; // inner interface defined below

    // constructor
    public MovieGridAdapter(Context context, MovieAdapterOnClickHandler handler) {
        this.mContext = context;
        this.mClickHandler = handler;
        this.mMovies = null; // this data is to be fed by Async tasks
    }

    // set the data content
    public void setMovies(Movie[] movies) {
        this.mMovies = movies; // data comes from async tasks
        notifyDataSetChanged(); // re-render the view with new data
    }

    ////// overriding functions related to inner ViewHolder class
    @Override
    public MovieGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(R.layout.movie_grid_item, parent, shouldAttachToParentImmediately);
        MovieGridViewHolder viewHolder = new MovieGridViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieGridViewHolder holder, int position) {
        Log.d(TAG, String.format("position %d is binded!", position));
        holder.bind(mMovies[position]);
    }

    @Override
    public int getItemCount() {
        if (this.mMovies == null) return 0;
        else return mMovies.length;
    }


    ///////////// inner interface ClickHandler for movie items
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie); // in the parameter, pass the DATA that is click
    }


    ////////////// inner class ViewHolder, it also implements OnClickListener (NOT the above inner interface!)
    class MovieGridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;

        public MovieGridViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_moive_item);
            itemView.setOnClickListener(this); // set this(ViewHolder) as the imageview's onclick listener
        }

        void bind(Movie movie) { // helper function: put data to the display
            Picasso.with(mContext)
                    .load(movie.getPosterImgURL())
                    .into(mImageView);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Log.d("click", pos + "th item is clicked !");
            Movie movie = mMovies[pos];
            mClickHandler.onClick(movie); // let handler to digest this click
        }
    }

}
