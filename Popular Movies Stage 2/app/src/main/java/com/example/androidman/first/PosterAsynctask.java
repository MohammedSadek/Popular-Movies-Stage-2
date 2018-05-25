package com.example.androidman.first;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

public class PosterAsynctask extends AsyncTask<String, Void, String> {
    private final Context context;
    String k = null;

    public PosterAsynctask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String s = strings[0];
        try {
            HttpURLConnection httpURLConnection = InternetDetails.openconnection(s);
            k = InternetDetails.getdata(httpURLConnection);
        } catch(IOException e){
            e.printStackTrace();
        }
        return k;
    }



    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray array = jsonObject.getJSONArray("results");
            MainActivity.movie_data.clear();

            for (int i = 0; i < array.length(); i++) {
                MovieData movie = new MovieData();
                JSONObject jso = array.getJSONObject(i);
                movie.setImageSource(jso.getString("poster_path"));
                movie.setTitle(jso.getString("title"));
                movie.setOverview(jso.getString("overview"));
                movie.setVote(jso.getString("vote_count"));
                movie.setId(jso.getString("id"));
                movie.setRelaseDate(jso.getString("release_date"));
                MainActivity.movie_data.add(movie);

            }
            MainActivity.gridView.setAdapter(new PosterAdapter(context, MainActivity.movie_data));

        } catch (JSONException e) {


            e.printStackTrace();
        }
    }
}