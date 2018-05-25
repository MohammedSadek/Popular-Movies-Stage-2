package com.example.androidman.first;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

public class TrailorAsynctask extends AsyncTask<String, Void, String> {
    private final Context context;
    String k = null;

    public TrailorAsynctask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String s = strings[0];

        try {
            HttpURLConnection httpURLConnection = InternetDetails.openconnection(s);
            k = InternetDetails.getdata(httpURLConnection);
        } catch (IOException e) {
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
            Details.movie_data.clear();

            for (int i = 0; i < array.length(); i++) {
                MovieData movie = new MovieData();
                JSONObject jso = array.getJSONObject(i);
                movie.setKey(jso.getString("key"));

                Details.movie_data.add(movie);

            }
            Details.list.setAdapter(new TrailorAdapter(context, Details.movie_data));
            ComputeListHeight height = new ComputeListHeight();
            height.setListheight(Details.list);

        } catch (JSONException e) {


            e.printStackTrace();
        }
    }

    public class ComputeListHeight {
        public void setListheight(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {

                return;
            }

            int totalHeight = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }
}
