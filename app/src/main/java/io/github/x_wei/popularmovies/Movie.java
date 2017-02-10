package io.github.x_wei.popularmovies;

import org.json.JSONException;
import org.json.JSONObject;

class Movie {
    String title;
    String overview;
    float rating;
    String posterPath;
    int votCnt;
    String releaseDate;
    String lang;
    String rawjson;
    // using this `rawjson` field, we can pass a string instead of a Movie obj between activities

    public Movie(JSONObject jsonMovieItem) {
        try {
            this.rawjson = jsonMovieItem.toString();
            this.title = jsonMovieItem.getString("title");
            this.overview = jsonMovieItem.getString("overview");
            this.releaseDate = jsonMovieItem.getString("release_date");
            this.posterPath = jsonMovieItem.getString("poster_path");
            this.rating = (float) jsonMovieItem.getDouble("vote_average");
            this.lang = jsonMovieItem.getString("original_language");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Movie MovieFromJson(String rawjson) {
        try {
            JSONObject jsonObj = new JSONObject(rawjson);
            return new Movie(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPosterImgURL() {
        return "http://image.tmdb.org/t/p/w185/" + this.posterPath;
    }

    public String getFacts() {
        String facts = String.format("Release date:%s \n\nLanguage:%s \n\nRating:%.1f",
                this.releaseDate, this.lang, this.rating);
        return facts;
    }
}
