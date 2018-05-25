package com.example.androidman.first;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class Details extends AppCompatActivity {
   public static ArrayList<MovieData> movie_data = new ArrayList<>();
   public static ListView list;
   public static TextView review;
   public static Intent intent;
    boolean flag=false;
    String id;
    public Button button;
    boolean networkExist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        networkExist= InternetDetails.networkinfo(Details.this);

        TextView Title=(TextView)findViewById(R.id.textView2);
        TextView vote=(TextView)findViewById(R.id.textView4);
        TextView overview=(TextView)findViewById(R.id.textView8);
        TextView release_date=(TextView)findViewById(R.id.textView3);
         review=(TextView)findViewById(R.id.review);
        ImageView image=(ImageView)findViewById(R.id.imageView2);
         list=(ListView)findViewById(R.id.list);
         button=findViewById(R.id.fav);
         intent=getIntent();
        Title.setText(intent.getStringExtra("title"));
        Picasso.with(this).load("http://image.tmdb.org/t/p/w342/"+ intent.getStringExtra("image") +"").into(image);
        overview.setText(intent.getStringExtra("overview"));
        release_date.setText(intent.getStringExtra("date_relase"));
        vote.setText(intent.getStringExtra("vote"));
        id=intent.getStringExtra("id");
        if(networkExist==false)
        {

        }
        else {
            new TrailorAsynctask(this).execute("https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=" + BuildConfig.MOVIE_API_KEY);
            new ReviewAsynctask(this).execute("https://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=" + BuildConfig.MOVIE_API_KEY);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                MovieData movieData = movie_data.get(i);
                if(networkExist==false)
                    print();
                else
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+movieData.getKey()+"")));
            }
        });
        button.setText("favourit");
        Cursor cursor = getContentResolver().query(MovieContentProvider.Content_Uri, null, "title=?", new String[]{intent.getStringExtra("title")}, null);
        if (cursor.getCount() != 0) {
            flag = true;
            button.setText("delete");
        }
        cursor.close();
    }
    public void onclick(View view)
    {
        Toast.makeText(this,button.getText().toString(),Toast.LENGTH_LONG).show();
        String name=button.getText().toString();
        String buttonname="favourit";
        if(name.equals(buttonname))
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContentProvider.Image_Source, intent.getStringExtra("image"));
            contentValues.put(MovieContentProvider.Title, intent.getStringExtra("title"));
            contentValues.put(MovieContentProvider.Overview, intent.getStringExtra("overview"));
            contentValues.put(MovieContentProvider.Relase_date, intent.getStringExtra("date_relase"));
            contentValues.put(MovieContentProvider.Vote, intent.getStringExtra("vote"));
            contentValues.put(MovieContentProvider.Id, intent.getStringExtra("id"));
            ContentResolver contentResolver = getContentResolver();
            contentResolver.insert(MovieContentProvider.Content_Uri, contentValues);
            button.setText("delete");
            flag=true;
        }
         else
        {
            getContentResolver().delete(MovieContentProvider.Content_Uri,"title=?",new String[]{intent.getStringExtra("title")});
            button.setText("favourit");
            flag=false;

        }

    }
    public  void print()
    {
        new AlertDialog.Builder(this).setMessage("Please Check Your Internet Connection and Try Again")
                .setTitle("Network Error")
                .setCancelable(true)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                finish();
                            }
                        })
                .show();
    }
}
