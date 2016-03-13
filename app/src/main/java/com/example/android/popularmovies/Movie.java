package com.example.android.popularmovies;

import java.io.Serializable;

/**
 * Created by vaibhav.seth on 12/19/15.
 */
public class Movie implements Serializable {

    String Id;
    String OriginalTitle;
    String Synopsis;
    String ReleaseDate;
    String PosterPath;
    String Popularity;
    String UserRating;

    Movie(String id,
          String originalTitle,
          String synopsis,
          String releaseDate,
          String relativePosterPath,
          String popularity,
          String userRating)
    {
        Id = id;
        OriginalTitle = originalTitle;
        Synopsis = synopsis;
        ReleaseDate = releaseDate;
        PosterPath = relativePosterPath;
        Popularity = popularity;
        UserRating = userRating;
    }
}