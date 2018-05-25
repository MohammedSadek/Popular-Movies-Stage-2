package com.example.androidman.first;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static Context context;
    public static GridView gridView;
    public  static ArrayList<MovieData> movie_data = new ArrayList<>();
    boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.gridview);
        flag= InternetDetails.networkinfo(MainActivity.this);
        if(flag==false) {
           print();
        }
        else {

            new PosterAsynctask(MainActivity.this).execute("http://api.themoviedb.org/3/movie/top_rated?api_key=" + BuildConfig.MOVIE_API_KEY);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(MainActivity.this, Details.class);
                    MovieData data = movie_data.get(i);
                    intent.putExtra("title", data.getTitle());
                    intent.putExtra("image", data.getImageSource());
                    intent.putExtra("overview", data.getOverview());
                    intent.putExtra("date_relase", data.getRelaseDate());
                    intent.putExtra("vote", data.getVote());
                    intent.putExtra("id",data.getId());
                   // Toast.makeText(MainActivity.this,""+data.getId(),Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("index", gridView.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        gridView.setSelection(savedInstanceState.getInt("index"));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        flag= InternetDetails.networkinfo(MainActivity.this);
        if (item.getItemId() == R.id.top) {
            if(flag==false) {
                print();
            }
            else {

                new PosterAsynctask(MainActivity.this).execute("http://api.themoviedb.org/3/movie/top_rated?api_key=" + BuildConfig.MOVIE_API_KEY);
            }
        }
        if (item.getItemId() == R.id.pop) {
            if (flag == false) {
                print();
            } else {
                new PosterAsynctask(MainActivity.this).execute("http://api.themoviedb.org/3/movie/popular?api_key=" + BuildConfig.MOVIE_API_KEY);
            }
        }
        if (item.getItemId() == R.id.fav) {
            movie_data.clear();
            ContentResolver contentResolver=getContentResolver();
            Cursor c=contentResolver.query(MovieContentProvider.Content_Uri,null,null,null,null);
            while(c.moveToNext()){
                Toast.makeText(this,"dfe",Toast.LENGTH_SHORT).show();
                MovieData movie = new MovieData();
                movie.setImageSource(c.getString(c.getColumnIndex(MovieContentProvider.Image_Source)));
                movie.setTitle(c.getString(c.getColumnIndex(MovieContentProvider.Title)));
                movie.setOverview(c.getString(c.getColumnIndex(MovieContentProvider.Overview)));
                movie.setVote(c.getString(c.getColumnIndex(MovieContentProvider.Vote)));
                movie.setRelaseDate(c.getString(c.getColumnIndex(MovieContentProvider.Relase_date)));
                movie.setId(c.getString(c.getColumnIndex(MovieContentProvider.Id)));

                movie_data.add(movie);

            }
            if(flag==false)
                print();
            else
           gridView.setAdapter(new PosterAdapter(MainActivity.this, movie_data));

        }



        return super.onOptionsItemSelected(item);
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


