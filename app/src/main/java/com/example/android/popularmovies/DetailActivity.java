package com.example.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
        private final Context mContext;
        private String movieId = null;
        ArrayAdapter<String> mForecastAdapter;
        String videoId = null;



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
            mContext= getActivity();
        }

    private void updateReviews(String m) {
        GetReviewsAndTrailer r = new GetReviewsAndTrailer(getActivity());
        r.execute(m);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void watchYoutubeVideo(){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + videoId));
            startActivity(intent);
        }
    }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            List<String> reviews = new ArrayList<>();

//            mForecastAdapter =new ArrayAdapter<String>(getActivity(), // The current context (this activity)
//                                                R.layout.fragment_detail, // The name of the layout ID.
//                                                R.id.reviewsTextView, // The ID of the textview to populate.
//                    reviews);

//             ((TextView) rootView.findViewById(R.id.summaryID));
//            ListView listView = (ListView) rootView.findViewById(R.id.listview_reviews);
//            listView.setAdapter(mForecastAdapter);

            final ToggleButton quantityTextView = (ToggleButton) rootView.findViewById(
                    R.id.toggleButton);
//            ContentValues values = new ContentValues();
//            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAV,"True");
//            int insertedUri = mContext.getContentResolver().update(MovieContract.MovieEntry.buildMovieUri(),values,MovieContract.MovieEntry.COLUMN_MOVIE_ID + "?",new String[] {String.valueOf(movieId)});

            quantityTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    System.out
                            .println("On click of the Toggle Button is called !!");
                    if (quantityTextView.isChecked()) {
                        System.out.println("Checked");
                        ContentValues values = new ContentValues();
                        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAV, "True");
                        UpdateDbTask weatherTask = new UpdateDbTask(getActivity());
                        weatherTask.execute();
                    } else {
                        System.out.println("Not Checked ");
                    }
                }
            });

        final ImageButton video = (ImageButton) rootView.findViewById(
                R.id.imageButton);

        video.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(videoId.equals("empty")) {
                    Toast.makeText(getActivity(), "No Trailer Present",
                            Toast.LENGTH_LONG).show();
                }
                else
                    watchYoutubeVideo();

            }
        });



//
//                Object forecast = mImage.getItem(position);
//
//                System.out.println("Beforeeeee" + forecast.Id);
//                Intent intent = new Intent(getActivity(), DetailActivity.class)
//                        .putExtra(Intent.EXTRA_TEXT, forecast);
//                startActivity(intent);

            return rootView;
        }



    public class GetReviewsAndTrailer extends AsyncTask<String, Void, List<List<String>>> {

        private final String LOG_TAG = MainActivityFragment.FetchWeatherTask.class.getSimpleName();

        private final Context mContext;

        public GetReviewsAndTrailer(Context context) {
            mContext = context;
        }

        @Override
        protected List<List<String>> doInBackground(String... params) {
            // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
            List<List<String>> Movie = new ArrayList<List<String>>();
            ArrayList<Movie> m = new ArrayList<Movie>();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            List<String> id=null;
            List<String> reviews=null;

            String forecastJsonStr = null;
            if (params.length == 0) {
                return null;
            }

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String videoUrl =
                        "http://api.themoviedb.org/3/movie/"+params[0]+"/videos";
                final String reviewUrl =
                        "http://api.themoviedb.org/3/movie/"+params[0]+"/reviews";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(videoUrl).buildUpon()
                        .appendQueryParameter(APPID_PARAM, "55a251bf633dd3c8e35ec5efa8722860")
                        .build();

                Uri builtUri1 = Uri.parse(reviewUrl).buildUpon()
                        .appendQueryParameter(APPID_PARAM, "55a251bf633dd3c8e35ec5efa8722860")
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v("Forecast Json String", builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();



                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();

                id = getValuesListFromJsonForGivenField(forecastJsonStr, "key");
                System.out.println("Hellow world " + id);

                url =  new URL(builtUri1.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                inputStream = urlConnection.getInputStream();
                buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                line = null;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();

                reviews = getValuesListFromJsonForGivenField(forecastJsonStr, "content");
                System.out.println("Hellow world " + id);

                Log.v("Forecast Json String", forecastJsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                forecastJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            Movie.add(id);
            Movie.add(reviews);
            return Movie;
        }

        @Override
        protected void onPostExecute(List<List<String>> result) {
            if (result != null) {
                System.out.println(result);
                mForecastAdapter.clear();
//                int i=0;
                if (result.get(1).size()==0)
                {
                    mForecastAdapter.add("No reviews");
                }
                else {
                    for (String dayForecastStr : result.get(1)) {
                        mForecastAdapter.add(dayForecastStr);
                    }
                }
                if (result.get(0).size()==0)
                {
                    videoId = "empty";
                }
                else {
                    videoId = result.get(0).get(0);
                }
                // New data is back from the server.  Hooray!
            }
        }
    }
    private List<String> getValuesListFromJsonForGivenField(
            String pubContentValue, String attributeName)
            throws JsonParseException, IOException {
        JsonFactory factory = new MappingJsonFactory();
        JsonParser jp = factory.createJsonParser(pubContentValue);
        org.codehaus.jackson.JsonToken current = jp.nextToken();
        List<String> attributeValues = new ArrayList<String>();
        while (jp.hasCurrentToken()) {
            if (null != jp.getCurrentName()
                    && jp.getCurrentName().equals(attributeName)) {
                org.codehaus.jackson.JsonToken currentValue = jp.nextValue();
                if (org.codehaus.jackson.JsonToken.VALUE_STRING == currentValue)
                    attributeValues.add(jp.getText());
                if (JsonToken.VALUE_NUMBER_INT == currentValue)
                    attributeValues.add(jp.getText());
                if (JsonToken.VALUE_NUMBER_FLOAT == currentValue)
                    attributeValues.add(jp.getText());
                if (org.codehaus.jackson.JsonToken.VALUE_TRUE == currentValue)
                    attributeValues.add(jp.getText());
                if (org.codehaus.jackson.JsonToken.START_ARRAY == currentValue) {
                    StringBuffer str = new StringBuffer();
                    while (org.codehaus.jackson.JsonToken.FIELD_NAME != jp.getCurrentToken()) {
                        str.append(jp.getText());
                        if (JsonToken.END_ARRAY == jp.getCurrentToken())
                            break;
                        JsonToken nextValue = jp.nextValue();
                        if ((!str.toString().equals("["))
                                && (JsonToken.END_ARRAY != nextValue))
                            str.append(",");
                    }
                    attributeValues.add(str.toString());
                }
            }
            current = jp.nextToken();
        }
        return attributeValues;
    }

    public class UpdateDbTask extends AsyncTask<String, Void, Void> {
        private final Context mContext;

        public UpdateDbTask(Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(String... params) {
            ContentValues values = new ContentValues();
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAV,"True");
            System.out.println("Update");
            int insertedUri = mContext.getContentResolver().update(MovieContract.MovieEntry.buildMovieUri(),values,MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",new String[] {String.valueOf(movieId)});
            return null;
        }
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
        movieId = data.getString(2);
        updateReviews(movieId);
        List<String> reviews = new ArrayList<String>();
        mForecastAdapter =new ArrayAdapter<String>(getActivity(), // The current context (this activity)
                R.layout.list_reviews, // The name of the layout ID.
                R.id.reviewsTextView, // The ID of the textview to populate.
                reviews);

        ListView listView = (ListView) getView().findViewById(R.id.listview_reviews);
        listView.setAdapter(mForecastAdapter);

//        if(videoId.equals("empty"))
//        {
//            ImageView img = (ImageView) getView().findViewById(R.id.imageButton);
//            img.setVisibility(View.INVISIBLE);
//        }

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
//        if (mShareActionProvider != null) {
//            mShareActionProvider.setShareIntent(createShareForecastIntent());
//        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

//    public void updateFav(View view) {
//        ToggleButton quantityTextView = (ToggleButton) getView().findViewById(
//                R.id.toggleButton);
//        ContentValues values = new ContentValues();
//        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAV,"True");
//        int insertedUri = mContext.getContentResolver().update(MovieContract.MovieEntry.buildMovieUri(),values,MovieContract.MovieEntry.COLUMN_MOVIE_ID + "?",new String[] {String.valueOf(movieId)});
//
//
//    }
}

}
