package io.github.x_wei.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

// main activity implements the item onclick handler interface defined in Adapter
public class MainActivity extends AppCompatActivity implements MovieGridAdapter.MovieAdapterOnClickHandler {
    private RecyclerView mMoviesGridRV;
    private ProgressBar mLoadingIndicator;

    private RecyclerView.LayoutManager mLayoutManager;
    private MovieGridAdapter mAdapter;

    private Toast mToast;
    private String API_KEY;
    private static final String TAG = MainActivity.class.getSimpleName();


    // helper functions
    private void toast(String msg) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        mToast.show();
    }

    private void fetchMovies(boolean byPopular) {
        String url;
        if (byPopular)
            url = String.format("http://api.themoviedb.org/3/movie/popular?api_key=%s", API_KEY);
        else
            url = String.format("http://api.themoviedb.org/3/movie/top_rated?api_key=%s", API_KEY);
        new FetchMoviesTask().execute(url); // start a async task to fetch
    }


    @Override // entry point
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set layouts, find views by id
        setContentView(R.layout.activity_main);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mMoviesGridRV = (RecyclerView) findViewById(R.id.rv_moives_grid);
        mMoviesGridRV.setHasFixedSize(true);
        API_KEY = Utilities.decode( getString(R.string.API_KEY) );
//        Log.d("apikey", API_KEY);

        // set layout manager
        mLayoutManager = new GridLayoutManager(this, 3); // grid with 3 columns
        mMoviesGridRV.setLayoutManager(mLayoutManager);

        // set adapter
        mAdapter = new MovieGridAdapter(this, this); // arg1=context, arg2=item onclick handler
        mMoviesGridRV.setAdapter(mAdapter);

        // start an async task to fetch data
        String url = String.format("http://api.themoviedb.org/3/movie/popular?api_key=%s", API_KEY);
        new FetchMoviesTask().execute(url);

    }

    @Override // inflate and show menu items
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_grid, menu);
        return true;
    }


    @Override // handle menu actions
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();
        switch (itemid) {
            case R.id.action_bypopular:
                fetchMovies(true);
                return true;
            case R.id.action_byrate:
                fetchMovies(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //////// handle item onClick events -- implemente onClick defined in custom inner interface in Adapter
    @Override // when clicked: go to the detail activity
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movie.rawjson);// we pass json raw text
        // if want to pass directly an Object, need to implement Parcel...
        startActivity(intentToStartDetailActivity);

    }

    //////////////// inner class that wraps the fetch movies Async task in background thread
    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mMoviesGridRV.setVisibility(View.INVISIBLE);
            Log.d(TAG, "start fetching movies");
            toast("start fetching movies");
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            String url = params[0];
            Movie[] popularMovies = Utilities.getMovies(url);
            return popularMovies;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mMoviesGridRV.setVisibility(View.VISIBLE);
            Log.d(TAG, "finished getting movies: " + movies.length);
            mAdapter.setMovies(movies); // feed retrieved data to mAdapter
            toast("finished fetching movies " + movies.length);
        }
    }
}
