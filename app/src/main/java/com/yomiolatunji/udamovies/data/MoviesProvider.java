package com.yomiolatunji.udamovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by oluwayomi on 13/05/2017.
 */

public class MoviesProvider extends ContentProvider {
    private MoviesDbHelper mDbHelper;

    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIES_WITH_ID = 101;
    public static UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = MoviesContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, MoviesContract.PATH_MOVIES, CODE_MOVIES);
        uriMatcher.addURI(authority, MoviesContract.PATH_MOVIES + "/#", CODE_MOVIES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        switch (match) {
            case CODE_MOVIES:
                db.beginTransaction();
                int rowInserted = 0;
                try {
                    for (ContentValues value :
                            values) {
                        long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, value);
                        if (_id > 0)
                            rowInserted++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowInserted;
            default:
                return super.bulkInsert(uri, values);
        }

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        switch (match) {
            case CODE_MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(2);

                cursor = db.query(MoviesContract.MoviesEntry.TABLE_NAME, projection, MoviesContract.MoviesEntry._ID + "=?", new String[]{id}, null, null, sortOrder);
                break;
            case CODE_MOVIES:
                cursor = db.query(MoviesContract.MoviesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri.toString());
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long _id = -1;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case CODE_MOVIES_WITH_ID:
                _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
                break;
            case CODE_MOVIES:
                _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri.toString());

        }
        if (_id > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int rowDeleted = 0;
        switch (match) {
            case CODE_MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(2);
                rowDeleted = db.delete(MoviesContract.MoviesEntry.TABLE_NAME, MoviesContract.MoviesEntry._ID + "=?", new String[]{id});
                break;
            case CODE_MOVIES:
                rowDeleted = db.delete(MoviesContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri.toString());

        }
        if (rowDeleted > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowUpdated = -1;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case CODE_MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(2);

                rowUpdated = db.update(MoviesContract.MoviesEntry.TABLE_NAME, values, MoviesContract.MoviesEntry._ID + "=?", new String[]{id});
                break;
            case CODE_MOVIES:
                rowUpdated = db.update(MoviesContract.MoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri.toString());

        }
        if (rowUpdated > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowUpdated;
    }

    @Override
    public void shutdown() {
        mDbHelper.close();
        super.shutdown();
    }
}
