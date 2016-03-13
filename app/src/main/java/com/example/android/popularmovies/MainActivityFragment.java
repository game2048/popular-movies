package com.example.android.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.Time;
//import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.example.android.popularmovies.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;

//
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;

/**
 * A placeholder fragment containing a simple view.
 */


public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private String [] s1 =   new String[100];
//    ArrayAdapter<String> mForecastAdapter = null;
    ArrayList<Movie> movie = null;
    ImageListAdapter mImage = null;
    private static final int MOVIE_LOADER = 0;

    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID
    };

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
//        return new R.layout.list_item_forecast;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                   mImage = new ImageListAdapter(getActivity(), null, 0);
        //mForecastAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textview,new ArrayList<String>());
        GridView listView = (GridView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mImage);
        //listView.setAdapter(mForecastAdapter);
        //ImageView imageView = (ImageView)rootView.findViewById(R.id.list_item_forecast_textview);


//        Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

                if (cursor != null) {
                    String locationSetting = "";//Utility.getPreferredLocation(getActivity());
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .setData(MovieContract.MovieEntry.buildMovieUri(cursor.getString(2))
                            );
                    startActivity(intent);
                }
//
//                Object forecast = mImage.getItem(position);
//
//                System.out.println("Beforeeeee" + forecast.Id);
//                Intent intent = new Intent(getActivity(), DetailActivity.class)
//                        .putExtra(Intent.EXTRA_TEXT, forecast);
//                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_refresh) {
//            updateWeather();
//        }

        return super.onOptionsItemSelected(item);
    }
//
    private void updateWeather() {
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = "";//prefs.getString(getString(R.string.pref_location_key),
//                getString(R.string.pref_location_default));
        String units = prefs.getString(getString(R.string.pref_metric_key),
                getString(R.string.pref_location_default));
        if(units.equalsIgnoreCase("most popular"))
            units = "popularity.desc";
        else if(units.equalsIgnoreCase("highest-rated"))
            units = "vote_average.desc";
        weatherTask.execute(location, units);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        String locationSetting = Utility.getPreferredLocation(getActivity());

        // Sort order:  Ascending, by date.
//        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri movieUri = MovieContract.MovieEntry.buildMovieUri();

        return new CursorLoader(getActivity(),
                movieUri,
                FORECAST_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mImage.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mImage.swapCursor(null);
    }

    //new String[1000];


    public class FetchWeatherTask extends AsyncTask<String, Void, Void>
    {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        private final Context mContext;

        public FetchWeatherTask(Context context) {
            mContext = context;
        }
        /**
         * Helper method to handle insertion of a new location in the weather database.
         *
         * @param movie The location string used to request updates from the server.
         * @return the row ID of the added location.
         */
        long addMovie(Movie movie) {
            long movieId;


            // First, check if the location with this city name exists in the db
            Cursor locationCursor = mContext.getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    new String[]{MovieContract.MovieEntry._ID},
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{movie.Id},
                    null);

            if (locationCursor.moveToFirst()) {
                int locationIdIndex = locationCursor.getColumnIndex(MovieContract.MovieEntry._ID);
                movieId = locationCursor.getLong(locationIdIndex);
            } else {
                // Now that the content provider is set up, inserting rows of data is pretty simple.
                // First create a ContentValues object to hold the data you want to insert.
                ContentValues locationValues = new ContentValues();

                // Then add the data, along with the corresponding name of the data type,
                // so the content provider knows what kind of value is being inserted.
                locationValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_DATE, movie.ReleaseDate);
                locationValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.Id);
                locationValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY, movie.Popularity);
                locationValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH, movie.PosterPath);
                locationValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, movie.Synopsis);
                locationValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.OriginalTitle);
                locationValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_USERRATING, movie.UserRating);

                // Finally, insert location data into the database.
                Uri insertedUri = mContext.getContentResolver().insert(
                        MovieContract.MovieEntry.CONTENT_URI,
                        locationValues
                );

                // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
                movieId = ContentUris.parseId(insertedUri);
            }

            locationCursor.close();
            // Wait, that worked?  Yes!
            return movieId;
        }

        @Override
        protected Void doInBackground(String... params) {
            // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
            ArrayList<Movie> m = new ArrayList<Movie>();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            List<String> id=null;
            List<String> popularity=null;
            List<String> posterPath=null;
            List<String> overview=null;
            List<String> title=null;
            List<String> rating=null;
            List<String> release=null;
//            String [] s1 = new String[1000];
// Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            if (params.length == 0) {
                return null;
            }

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String FORECAST_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "sort_by";
                final String UNITS_PARAM = "sort_by";
                final String DAYS_PARAM = "cnt";
                final String APPID_PARAM = "api_key";
                String format = "json";

                String units = "metric";
                int numDays = 7;
                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
//                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, params[1])
//                        .appendQueryParameter(UNITS_PARAM, params[1])
//                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .appendQueryParameter(APPID_PARAM, "")
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

                id = getValuesListFromJsonForGivenField(forecastJsonStr, "id");
                popularity = getValuesListFromJsonForGivenField(forecastJsonStr, "popularity");
                posterPath = getValuesListFromJsonForGivenField(forecastJsonStr, "poster_path");
                overview = getValuesListFromJsonForGivenField(forecastJsonStr, "overview");
                release = getValuesListFromJsonForGivenField(forecastJsonStr, "release_date");
                title = getValuesListFromJsonForGivenField(forecastJsonStr, "original_title");
                rating = getValuesListFromJsonForGivenField(forecastJsonStr, "vote_average");
                int i = 0;
                System.out.println("Hellow world " + id);
                System.out.println("Hellow world " + popularity);
                System.out.println("Hellow world " + posterPath);
                System.out.println("Hellow world " + overview);
                System.out.println("Hellow world " + release);
                System.out.println("Hellow world " + title);
                System.out.println("Hellow world " + rating);

                String [] s1 = new String[posterPath.size()];

                Vector<ContentValues> cVVector = new Vector<ContentValues>(posterPath.size());
                for(String imageurl : posterPath)
                {

                         s1[i] = "http://image.tmdb.org/t/p/w185/" + imageurl;

                        Movie m1 = new Movie(id.get(i),title.get(i),overview.get(i),release.get(i),s1[i],popularity.get(i),rating.get(i));
                        m.add(m1);

                        ContentValues movieValues = new ContentValues();

                        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id.get(i));
                        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, title.get(i));
                        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, overview.get(i));
                        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_DATE, release.get(i));
                        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH, s1[i]);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY, popularity.get(i));
                        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_USERRATING, rating.get(i));

                        cVVector.add(movieValues);

                        System.out.println(s1[i]);
                        i++;

                }
                int inserted = 0;
                // add to database
                if ( cVVector.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
                }

                Log.d(LOG_TAG, "FetchWeatherTask Complete. " + inserted + " Inserted");

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
            return null;
        }
//        @Override
//        protected void onPostExecute(ArrayList<Movie> result) {
//            if (result != null) {
//                System.out.println(result);
//                mImage.clear();
////                int i=0;
//                for(Movie dayForecastStr : result) {
//                    mImage.add(dayForecastStr);
////                    ImageView imageView = (ImageView)rootView.findViewById(R.id.list_item_forecast_textview);
////                    Picasso.with(getActivity()).load(dayForecastStr).into(imageView);
//                }
//                // New data is back from the server.  Hooray!
//            }
//        }
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
}

