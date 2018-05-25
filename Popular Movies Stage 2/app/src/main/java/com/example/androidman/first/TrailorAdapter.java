package com.example.androidman.first;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TrailorAdapter extends BaseAdapter {
    private final ArrayList<MovieData> movie_data;
    private final LayoutInflater inflater;
    private final Context context;
    int i=0;
    public TrailorAdapter(Context context, ArrayList<MovieData>movie_data)
    {
        this.inflater= LayoutInflater.from(context);
        this.movie_data=movie_data;
        this.context=context;
    }
    @Override
    public int getCount()

    {
        return movie_data.size();
    }
    @Override
    public Object getItem(int i)
    {
        return movie_data.get(i);
    }
    @Override
    public long getItemId(int i)
    {
        return 0;
    }

        public View getView(int i, View view, ViewGroup viewGroup)
        {
        View scope=inflater.inflate(R.layout.trailor,viewGroup,false);
        ImageView image=(ImageView)scope.findViewById(R.id.icon);
        TextView text=(TextView)scope.findViewById(R.id.trailor);
        text.setText("Trailor"+i++);
        return scope;
        }
}