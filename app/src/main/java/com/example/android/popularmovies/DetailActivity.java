package com.example.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

//    private ShareActionProvider mShareActionProvider;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.detail, menu);
//        MenuItem item = menu.findItem(R.id.menu_item_share);
//
//        // Fetch and store ShareActionProvider
//        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        return true;
    }
//    private void setShareIntent(Intent shareIntent) {
//        if (mShareActionProvider != null) {
//            mShareActionProvider.setShareIntent(shareIntent);
//        }
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            startActivity(new Intent(this, SettingsActivity.class));
//            return true;
//        }
//
//        if (id == R.id.menu_item_share) {
//            Intent sendIntent = new Intent();
//            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//            sendIntent.setType("text/plain");
//            startActivity(Intent.createChooser(sendIntent, "Hello world"));
//            setShareIntent(sendIntent);
//            return true;
//        }



//        return super.onOptionsItemSelected(item);
//    }
    public static class PlaceholderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

        private static final int DETAIL_LOADER = 0;
        private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS,
            MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY,
            MovieContract.MovieEntry.COLUMN_MOVIE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_USERRATING,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE
    };
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            // The detail Activity called via intent.  Inspect the intent for forecast data.
//            Intent intent = getActivity().getIntent();
//            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
//                Movie movie = (Movie)intent.getSerializableExtra(Intent.EXTRA_TEXT);
//                System.out.println("Helllllooo" + movie);
//                ((TextView) rootView.findViewById(R.id.detail_titleID))
//                        .setText(movie.OriginalTitle);
//                ((TextView) rootView.findViewById(R.id.summaryID))
//                        .setText(movie.Synopsis);
//                ((TextView) rootView.findViewById(R.id.releaseDateID))
//                        .setText(movie.ReleaseDate);
//                ImageView imageView = (ImageView) rootView.findViewById(R.id.movieImageID);
//                Picasso
//                        .with(getActivity())
//                        .load(movie.PosterPath)
////                .fit() // will explain later
//                        .into((ImageView) imageView);
//                ((RatingBar) rootView.findViewById(R.id.ratingBarID))
//                        .setRating(Float.parseFloat(movie.UserRating)/2);

//            }
            return rootView;
        }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                intent.getData(),
                FORECAST_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if (!data.moveToFirst()) { return; }

        ((TextView) getView().findViewById(R.id.detail_titleID))
                        .setText(data.getString(7));
                ((TextView) getView().findViewById(R.id.summaryID))
                        .setText(data.getString(3));
                ((TextView) getView().findViewById(R.id.releaseDateID))
                        .setText(data.getString(5));
                ImageView imageView = (ImageView) getView().findViewById(R.id.movieImageID);
                Picasso
                        .with(getActivity())
                        .load(data.getString(1))
//                .fit() // will explain later
                        .into((ImageView) imageView);
                ((RatingBar) getView().findViewById(R.id.ratingBarID))
                        .setRating(Float.parseFloat(data.getString(6)) / 2);


        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
//        if (mShareActionProvider != null) {
//            mShareActionProvider.setShareIntent(createShareForecastIntent());
//        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
}
