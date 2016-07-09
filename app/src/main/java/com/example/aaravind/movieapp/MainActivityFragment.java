package com.example.aaravind.movieapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public ArrayAdapter<String> arrayAdapter;

    public ImageListAdapter imageListAdapter;

    public MainActivityFragment() {
    }

    //String[] posterUrls = new String[25];
    //List<String> posters = ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        updateMovies();
        //arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.grid_view_item, R.id.grid_view_item, new ArrayList<String>());
        imageListAdapter = new ImageListAdapter(getActivity(), new ArrayList<String>());
        final GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(imageListAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {

        new FetchMoviesTask().execute();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJson = null;
            String[] movieData = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                Uri.Builder uriBuilder = new Uri.Builder();
                uriBuilder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendEncodedPath("/3/movie/popular")
                        .appendQueryParameter("api_key", "482d773e4db3cdbe6bda1bf5a8d89506");
                String urlString = uriBuilder.build().toString();
                URL url = new URL(urlString);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    movieJson = null;
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
                    movieJson = null;
                }
                movieJson = buffer.toString();

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                movieJson = null;
            } finally {
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
            try {
                movieData = getMovieDataFromJson(movieJson);
            } catch (JSONException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                movieData = null;
            }

            return movieData;
        }

        private String[] getMovieDataFromJson(String movieJson) throws JSONException{
            String[] posters = new String[100];
            JSONObject movieResult = new JSONObject(movieJson);
            JSONArray resultsArray = movieResult.getJSONArray("results");
            for(int i=0;i<resultsArray.length();i++){
                JSONObject movieDetails = resultsArray.getJSONObject(i);
                posters[i] = movieDetails.getString("poster_path");
            }
            return posters;
        }

        @Override
        protected void onPostExecute(String[] strings) {
//            arrayAdapter.clear();
            int j=0;
            if(strings != null) {
                for (String s : strings) {
                    if(s != null) {
                       // posterUrls[j] = "http://image.tmdb.org/t/p/w185/"+s;
                        //j++;
                        imageListAdapter.add("http://image.tmdb.org/t/p/w185/"+s);
                    }
                }
            }
         //   String[] sample = posterUrls;
        }
    }
}
