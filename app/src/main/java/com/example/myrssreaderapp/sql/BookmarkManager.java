package com.example.myrssreaderapp.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myrssreaderapp.DataHelper.BookmarkDbHelper;
import com.example.myrssreaderapp.models.BookmarkItem;

import java.util.ArrayList;
import java.util.List;

public class BookmarkManager {

    private SQLiteDatabase db;
    private BookmarkDbHelper dbHelper;

    public BookmarkManager(Context context){
        dbHelper = new BookmarkDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void addBookmark(BookmarkItem bookmark){
        ContentValues values = new ContentValues();
        values.put(BookmarkDbHelper.COLUMN_URL, bookmark.getUrl());
        values.put(BookmarkDbHelper.COLUMN_USER_ID, bookmark.getUserId());
        db.insertWithOnConflict(BookmarkDbHelper.TABLE_BOOKMARK, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void removeBookmark(BookmarkItem bookmark){
        db.delete(BookmarkDbHelper.TABLE_BOOKMARK,
                BookmarkDbHelper.COLUMN_URL + "=? AND " + BookmarkDbHelper.COLUMN_USER_ID + "=?",
                new String[]{bookmark.getUrl(), String.valueOf(bookmark.getUserId())});
    }

    public boolean isBookmarked(BookmarkItem bookmark){
        Cursor cursor = db.query(BookmarkDbHelper.TABLE_BOOKMARK,
                new String[]{BookmarkDbHelper.COLUMN_URL},
                BookmarkDbHelper.COLUMN_URL + "=? AND " + BookmarkDbHelper.COLUMN_USER_ID + "=?",
                new String[]{bookmark.getUrl(), String.valueOf(bookmark.getUserId())},
                null,null,null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public List<BookmarkItem> getBookmarksForUser(int userId){
        List<BookmarkItem> list = new ArrayList<>();

        Log.d("DEBUG", "Query bookmark for userId = " + userId);

        Cursor cursor = db.query(BookmarkDbHelper.TABLE_BOOKMARK,
                new String[]{BookmarkDbHelper.COLUMN_URL, BookmarkDbHelper.COLUMN_USER_ID},
                BookmarkDbHelper.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null,null,null);

        Log.d("DEBUG", "Rows = " + cursor.getCount());

        while(cursor.moveToNext()){
            String url = cursor.getString(cursor.getColumnIndexOrThrow(BookmarkDbHelper.COLUMN_URL));
            list.add(new BookmarkItem(url, userId));
            Log.d("DEBUG", "Found bookmark: " + url);
        }
        cursor.close();
        return list;
    }

}
