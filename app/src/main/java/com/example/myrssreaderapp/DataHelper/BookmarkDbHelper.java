package com.example.myrssreaderapp.DataHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BookmarkDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "news_bookmarks.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_BOOKMARK = "bookmarks";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_USER_ID = "userId";

    public BookmarkDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("DB", "onCreate BOOKMARK TABLE CALLED");

        String CREATE_BOOKMARK = "CREATE TABLE " + TABLE_BOOKMARK + " ("
                + COLUMN_URL + " TEXT NOT NULL,"
                + COLUMN_USER_ID + " INTEGER NOT NULL,"
                + "PRIMARY KEY(" + COLUMN_URL + ", " + COLUMN_USER_ID + ")"
                + ")";
        db.execSQL(CREATE_BOOKMARK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d("DB", "onUpgrade BOOKMARK TABLE CALLED");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARK);
        onCreate(db);
    }
}
