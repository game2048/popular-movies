//package com.example.android.popularmovies;
//
//import android.app.Application;
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.test.ApplicationTestCase;
//
//import com.example.android.popularmovies.data.MovieContract;
//import com.example.android.popularmovies.data.MovieDbHelper;
//
//import java.util.Map;
//import java.util.Set;
//
///**
// * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
// */
//public class ApplicationTest extends ApplicationTestCase<Application> {
//    public ApplicationTest() {
//        super(Application.class);
//    }
//
////    public void testLocationTable() {
////        insertLocation();
////    }
//
//    /*
//        Students:  Here is where you will build code to test that we can insert and query the
//        database.  We've done a lot of work for you.  You'll want to look in TestUtilities
//        where you can use the "createWeatherValues" function.  You can
//        also make use of the validateCurrentRecord function from within TestUtilities.
//     */
////    public void testWeatherTable() {
////        // First insert the location, and then use the locationRowId to insert
////        // the weather. Make sure to cover as many failure cases as you can.
////
////        // Instead of rewriting all of the code we've already written in testLocationTable
////        // we can move this code to insertLocation and then call insertLocation from both
////        // tests. Why move it? We need the code to return the ID of the inserted location
////        // and our testLocationTable can only return void because it's a test.
////
//////        long locationRowId = insertLocation();
////
////        // Make sure we have a valid row ID.
//////        assertFalse("Error: Location Not Inserted Correctly", locationRowId == -1L);
////
////        // First step: Get reference to writable database
////        // If there's an error in those massive SQL table creation Strings,
////        // errors will be thrown here when you try to get a writable database.
////        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
////        SQLiteDatabase db = dbHelper.getWritableDatabase();
////
////        // Second Step (Weather): Create weather values
//////        ContentValues weatherValues = TestUtilities.createWeatherValues(locationRowId);
////
////        // Third Step (Weather): Insert ContentValues into database and get a row ID back
//////        long weatherRowId = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, weatherValues);
//////        assertTrue(weatherRowId != -1);
////
////        // Fourth Step: Query the database and receive a Cursor back
////        // A cursor is your primary interface to the query results.
////        Cursor weatherCursor = db.query(
////                MovieContract.MovieEntry.TABLE_NAME,  // Table to Query
////                null, // leaving "columns" null just returns all the columns.
////                null, // cols for "where" clause
////                null, // values for "where" clause
////                null, // columns to group by
////                null, // columns to filter by row groups
////                null  // sort order
////        );
////
////        // Move the cursor to the first valid database row and check to see if we have any rows
////        assertTrue( "Error: No Records returned from location query", weatherCursor.moveToFirst() );
////
//////        // Fifth Step: Validate the location Query
//////        validateCurrentRecord("testInsertReadDb weatherEntry failed to validate",
//////                weatherCursor);
////
//////        // Move the cursor to demonstrate that there is only one record in the database
//////        assertFalse( "Error: More than one record returned from weather query",
//////                weatherCursor.moveToNext() );
////
////        // Sixth Step: Close cursor and database
////        weatherCursor.close();
////        dbHelper.close();
////    }
//
////    static ContentValues createWeatherValues(long locationRowId) {
////        ContentValues weatherValues = new ContentValues();
////        weatherValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, locationRowId);
////        weatherValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, locationRowId);
////        weatherValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_USERRATING, locationRowId);
////        weatherValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH, locationRowId);
////        weatherValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_DATE, locationRowId);
////        weatherValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, locationRowId);
////
////        return weatherValues;
////    }
////
////    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
////        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
////        for (Map.Entry<String, Object> entry : valueSet) {
////            String columnName = entry.getKey();
////            int idx = valueCursor.getColumnIndex(columnName);
////            System.out.println("DB values");
////            System.out.println(valueCursor.getString(idx));
//////            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
//////            String expectedValue = entry.getValue().toString();
//////            assertEquals("Value '" + entry.getValue().toString() +
//////                    "' did not match the expected value '" +
//////                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
////        }
////    }
//}