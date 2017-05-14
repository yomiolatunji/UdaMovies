package com.yomiolatunji.udamovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yomiolatunji.udamovies.data.MoviesContract.MoviesEntry;

/**
 * Created by oluwayomi on 13/05/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                MoviesEntry._ID + " INTEGER PRIMARY KEY," +
                MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_POSTER_PATH + " TEXT," +
                MoviesEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                MoviesEntry.COLUMN_ADULT + " bit DEFAULT 1," +
                MoviesEntry.COLUMN_FAVORITE + " BIT DEFAULT 1, " +
                MoviesEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MoviesEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                MoviesEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                MoviesEntry.COLUMN_SYNOPSIS + " TEXT, " +
                "UNIQUE (" + MoviesEntry._ID + ") ON CONFLICT IGNORE);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
