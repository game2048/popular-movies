package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private final String FORECASTFRAGMENT_TAG = "FFTAG";
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private String sortOrder;
    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sortOrder = getSortOrder(this);
////        mLocation = Utility.getPreferredLocation(this);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new MainActivityFragment(), FORECASTFRAGMENT_TAG)
//                    .commit();
//        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment, new MainActivityFragment(), FORECASTFRAGMENT_TAG)
//                    .commit();

            if (findViewById(R.id.movie_detail_container) != null) {
             // The detail container view will be present only in the large-screen layouts
             // (res/layout-sw600dp). If this view is present, then the activity should be
             // in two-pane mode.
             mTwoPane = true;
             // In two-pane mode, show the detail view in this activity by
             // adding or replacing the detail fragment using a
             // fragment transaction.
             if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailActivityFragment(), FORECASTFRAGMENT_TAG)
                           .commit();
                    }
                } else {
                  mTwoPane = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sort =getSortOrder(this);
        // update the location in our second pane using the fragment manager
        if (sort != null && !sort.equals(sortOrder)) {
            MainActivityFragment ff = (MainActivityFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
            if ( null != ff ) {
                ff.onLocationChanged();
            }
            sortOrder = sort;
        }
    }

    private String getSortOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(getString(R.string.pref_metric_key),
                getString(R.string.pref_location_default));

    }
}
