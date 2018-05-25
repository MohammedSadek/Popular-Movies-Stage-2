package com.example.androidman.first;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.widget.Toast;

import java.util.HashMap;

public class MovieContentProvider extends ContentProvider {
    static final String Provider_Name="Araby";
    static  final String URL="content://"+Provider_Name+"/movie";

    static final Uri Content_Uri=Uri.parse(URL);


    static final String Image_Source="image_source";
    static final String Title="title";
    static final String Overview="overview";
    static final String Vote="vote";
    static final String Id="id";
    static final String Relase_date="relase_date";

    private static HashMap<String,String>movie_fields;

    static  final int MY_MOVIES=1;

    static  final UriMatcher URI_MATCHER;
    static {
        URI_MATCHER=new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(Provider_Name,"Mymovies",MY_MOVIES);
    }


    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Repositiry";
    static final String TABLE_NAME = "MYMOVIES";
    static final int DATABASE_VERSION = 2;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + TABLE_NAME +
                    " ( image_source TEXT ," +
                    " title  TEXT ,"+
                    " overview  TEXT ,"+
                    " vote  TEXT ,"+
                    " id TEXT ," +
                    " relase_date TEXT );";
    private static class DatabaseHelper extends SQLiteOpenHelper{


        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_DB_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TAPLE IF EXIST "+TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }

    public MovieContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int count = 0;
        count = db.delete(TABLE_NAME, selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Long RowID=db.insert(TABLE_NAME,"",values);
        if (RowID>0){
            Uri _uri= ContentUris.withAppendedId(Content_Uri,RowID);
            getContext().getContentResolver().notifyChange(_uri,null);
            Toast.makeText(getContext(),"YES insert",Toast.LENGTH_LONG).show();
            return _uri;

        }
        throw new SQLException("FAILD TO ADD ARECORD INTO"+uri);
    }

    @Override
    public boolean onCreate() {
        Context context=getContext();
        DatabaseHelper databaseHelper=new DatabaseHelper(context);
        db=databaseHelper.getWritableDatabase();
        return (db==null)?false:true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        Cursor c = qb.query(db, projection, selection, selectionArgs,null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        count = db.update(TABLE_NAME,values,
                selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}