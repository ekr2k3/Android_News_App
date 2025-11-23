package com.example.myrssreaderapp.DataHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myrssreaderapp.models.BookmarkItem;
import com.example.myrssreaderapp.models.Comment;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app_data.db";
    private static final int DATABASE_VERSION = 1;

    // Bảng USER
    public static final String TABLE_USER = "USER";
    public static final String COL_USER_ID = "id";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";

    // Bảng bookmark
    public static final String TABLE_BOOKMARK = "BOOKMARK";
    public static final String COL_BOOKMARK_ID = "id";
    public static final String COL_BOOKMARK_USER_ID = "user_id";
    public static final String COL_ARTICLE_URL = "url";
    public static final String COL_ARTICLE_TITLE = "title";
    public static final String COL_ARTICLE_DESCRIPTION = "description";
    public static final String COL_ARTICLE_IMAGE_URL = "image_url";

    // Bảng COMMENT (Day 8)
    public static final String TABLE_COMMENT = "COMMENT";
    public static final String COL_COMMENT_ID = "idComment";
    public static final String COL_COMMENT_URL = "urlArticle";
    public static final String COL_COMMENT_EMAIL = "email";
    public static final String COL_COMMENT_CONTENT = "content";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng USER
        String createUserTable = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_EMAIL + " TEXT UNIQUE," +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createUserTable);

        // Tạo bảng BOOKMARK
        String createBookmarkTable = "CREATE TABLE IF NOT EXISTS " + TABLE_BOOKMARK + " (" +
                COL_BOOKMARK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_BOOKMARK_USER_ID + " INTEGER," +
                COL_ARTICLE_TITLE + " TEXT," +
                COL_ARTICLE_DESCRIPTION + " TEXT," +
                COL_ARTICLE_IMAGE_URL + " TEXT," +
                COL_ARTICLE_URL + " TEXT," +
                "FOREIGN KEY(" + COL_BOOKMARK_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COL_USER_ID + "))";
        db.execSQL(createBookmarkTable);

        // Tạo bảng COMMENT nếu nó chưa tồn tại
        String createCommentTable = "CREATE TABLE IF NOT EXISTS " + TABLE_COMMENT + " (" +
                COL_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_COMMENT_URL + " TEXT NOT NULL," +
                COL_COMMENT_EMAIL + " TEXT NOT NULL," +
                COL_COMMENT_CONTENT + " TEXT NOT NULL)";
        db.execSQL(createCommentTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        if(oldVersion < 2){
            // nếu version tăng thì tạo bảng Comment nếu chua có chứ không xóa dữ liệu cũ
            String createCommentTable = "CREATE TABLE IF NOT EXISTS " + TABLE_COMMENT + " (" +
                    COL_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_COMMENT_URL + " TEXT NOT NULL," +
                    COL_COMMENT_EMAIL + " TEXT NOT NULL," +
                    COL_COMMENT_CONTENT + " TEXT NOT NULL)";
            db.execSQL(createCommentTable);
        }
        onCreate(db);
    }

    // -----------------------
    // Phương thức quản lý USER
    // -----------------------
    public long addUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        return db.insert(TABLE_USER, null, values);
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER,
                new String[]{COL_USER_ID},
                COL_EMAIL + "=? AND " + COL_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);

        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    public int getUserId(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER,
                new String[]{COL_USER_ID},
                COL_EMAIL + "=? AND " + COL_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);

        int userId = -1;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID));
            }
            cursor.close();
        }
        return userId;
    }

    public boolean checkUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER,
                new String[]{COL_USER_ID},
                COL_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    public String getEmailById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String email = null;

        try {
            cursor = db.query(TABLE_USER,
                    new String[]{COL_EMAIL},
                    COL_USER_ID + "=?",
                    new String[]{String.valueOf(userId)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL));
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        return email; // sẽ trả về null nếu không tìm thấy
    }
    // Phương thức quản lý bookmark
    // -----------------------
    // ...
    public long addBookmark(int userId, String articleUrl, String articleTitle, String articleDescription, String articleImageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BOOKMARK_USER_ID, userId);
        values.put(COL_ARTICLE_URL, articleUrl);
        values.put(COL_ARTICLE_TITLE, articleTitle);
        values.put(COL_ARTICLE_DESCRIPTION, articleDescription);
        values.put(COL_ARTICLE_IMAGE_URL, articleImageUrl);
        return db.insert(TABLE_BOOKMARK, null, values);
    }

    public List<BookmarkItem> getBookmarksByUser(int userId) {
        List<BookmarkItem> bookmarks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BOOKMARK,
                new String[]{COL_BOOKMARK_ID, COL_ARTICLE_URL, COL_BOOKMARK_USER_ID, COL_ARTICLE_TITLE, COL_ARTICLE_DESCRIPTION, COL_ARTICLE_IMAGE_URL},
                COL_BOOKMARK_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int bookmarkId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOKMARK_ID));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(COL_ARTICLE_URL));
                int uid = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOKMARK_USER_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_ARTICLE_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_ARTICLE_DESCRIPTION));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COL_ARTICLE_IMAGE_URL));

                BookmarkItem item = new BookmarkItem(url, uid, title, description, imageUrl);
                item.setBookMarkId(bookmarkId); // set id tự tăng
                bookmarks.add(item);
            }
            cursor.close();
        }

        return bookmarks;
    }


    public boolean deleteBookmark(int userId, String articleUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Xóa bookmark theo userId và articleUrl
        return db.delete(TABLE_BOOKMARK,
                COL_BOOKMARK_USER_ID + "=? AND " + COL_ARTICLE_URL + "=?",
                new String[]{String.valueOf(userId), articleUrl}) > 0;
    }
    public boolean isBookmarked(long userId, String articleUrl) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKMARK,
                new String[]{COL_BOOKMARK_ID},
                COL_BOOKMARK_USER_ID + "=? AND " + COL_ARTICLE_URL + "=?",
                new String[]{String.valueOf(userId), articleUrl},
                null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


    // -----------------------
    // COMMENT FUNCTIONS (mới)
    // -----------------------
    public long addComment(Comment comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_COMMENT_URL, comment.getUrlArticle());
        values.put(COL_COMMENT_EMAIL, comment.getEmail());
        values.put(COL_COMMENT_CONTENT, comment.getContent());
        return db.insert(TABLE_COMMENT, null, values);
    }

    public List<Comment> getCommentsByArticle(String urlArticle) {
        List<Comment> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COMMENT,
                null,
                COL_COMMENT_URL + "=?",
                new String[]{urlArticle},
                null, null,
                COL_COMMENT_ID + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Comment c = new Comment();
                c.setIdComment(cursor.getInt(cursor.getColumnIndexOrThrow(COL_COMMENT_ID)));
                c.setUrlArticle(cursor.getString(cursor.getColumnIndexOrThrow(COL_COMMENT_URL)));
                c.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_COMMENT_EMAIL)));
                c.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COL_COMMENT_CONTENT)));
                list.add(c);
            }
            cursor.close();
        }

        return list;
    }

    public int deleteComment(int idComment) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_COMMENT, COL_COMMENT_ID + "=?", new String[]{String.valueOf(idComment)});
    }

}
