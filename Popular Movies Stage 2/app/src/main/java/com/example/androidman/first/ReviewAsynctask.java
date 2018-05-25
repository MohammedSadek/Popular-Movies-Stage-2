package com.example.androidman.first;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
public class ReviewAsynctask extends AsyncTask<String, Void, String>
{
    private final Context context;
    String k = null;
    public  StringBuffer str=new StringBuffer();
    public ReviewAsynctask(Context context)
    {
        this.context = context;
    }
    @Override
    protected String doInBackground(String... strings)
    {
        String s = strings[0];
        try
        {
            HttpURLConnection httpURLConnection = InternetDetails.openconnection(s);
            k = InternetDetails.getdata(httpURLConnection);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return k;
    }
    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        try {
            JSONObject obj = new JSONObject(s);
            JSONArray array = obj.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jso = array.getJSONObject(i);
                str.append(jso.getString("content"));
            }
            Details.review.setText(str.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}



