package com.example.android_example_view03;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

//共享表 数据 CRUD 操作
public class MyContentProvider extends ContentProvider {

    public static final String AUTHORITIES = "com.example.databasetest.provider";
    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORY_DIR = 2;
    public static final int CATEGORY_ITEM = 3;
    private MySqlite mMySqlite;
    private static UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITIES, "book", BOOK_DIR);
        mUriMatcher.addURI(AUTHORITIES, "book/#", BOOK_ITEM);
        mUriMatcher.addURI(AUTHORITIES, "category", CATEGORY_DIR);
        mUriMatcher.addURI(AUTHORITIES, "category/#", CATEGORY_ITEM);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db = mMySqlite.getWritableDatabase();
        int deleteRow = 0;
        switch (mUriMatcher.match(uri)) {
            case BOOK_DIR:
                deleteRow = db.delete("Book", selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deleteRow = db.delete("Book", "id = ?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deleteRow = db.delete("Category", selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deleteRow = db.delete("Category", "id = ?", new String[]{categoryId});
                break;
            default:
                break;
        }
        return deleteRow;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (mUriMatcher.match(uri)) {
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.category";
            default:
                throw new IllegalArgumentException("未知uri:" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        SQLiteDatabase db = mMySqlite.getWritableDatabase();
        Uri mUri = null;
        switch (mUriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                long book = db.insert("Book", null, values);
                mUri = Uri.parse("content://" + AUTHORITIES + "/book/" + book);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long category = db.insert("Category", null, values);
                mUri = Uri.parse("content://" + AUTHORITIES + "/category/" + category);
                break;
            default:
                break;
        }
        return mUri;
    }

    @Override
    public boolean onCreate() {
        mMySqlite = new MySqlite(getContext(), "BookStore.db3", null, 2);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteDatabase db = mMySqlite.getReadableDatabase();
        Cursor mCursor = null;
        switch (mUriMatcher.match(uri)) {
            case BOOK_DIR:
                mCursor = db.query("Book", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                mCursor = db.query("Book", projection, "id = ?", new String[]{bookId}, null, null, sortOrder);
                break;
            case CATEGORY_DIR:
                mCursor = db.query("Category", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ITEM:
                String categroyID = uri.getPathSegments().get(1);
                mCursor = db.query("Category", projection, "id = ?", new String[]{categroyID}, null, null, sortOrder);
                break;
        }
        return mCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        SQLiteDatabase db = mMySqlite.getWritableDatabase();
        int updataRow = 0;
        switch (mUriMatcher.match(uri)) {
            case BOOK_DIR:
                updataRow = db.update("Book", values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String updateBookId = uri.getPathSegments().get(1);
                updataRow = db.update("Book", values, "id = ?", new String[]{updateBookId});
                break;
            case CATEGORY_DIR:
                updataRow = db.update("Category", values, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String updateCateGoryId = uri.getPathSegments().get(1);
                updataRow = db.update("Category", values, "id = ?", new String[]{updateCateGoryId});
                break;
        }
        return updataRow;
    }
}
