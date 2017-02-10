package io.github.x_wei.popularmovies;


import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Utilities {
    private static final String API_KEY = "a2f6ea4770d9469d2bfd337401a1e880";

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static Movie[] getMovies(String url) {
        ArrayList<Movie> moviesList = new ArrayList<>();
        try {
            String rawjson = getResponseFromHttpUrl(new URL(url));
            JSONObject moviesJson = new JSONObject(rawjson);
            JSONArray moviesJsonArr = moviesJson.getJSONArray("results");
            for (int i = 0; i < moviesJsonArr.length(); i++) {
                JSONObject movieJsonObj = moviesJsonArr.getJSONObject(i);
                Movie movie = new Movie(movieJsonObj);
                moviesList.add(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moviesList.toArray(new Movie[moviesList.size()]);
    }

    public static String decode(String encoded){
        byte[] dd = Base64.decode(Base64.decode(encoded, Base64.DEFAULT), Base64.DEFAULT);
        try {
            String res = new String(dd, "UTF-8");
            return res;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
