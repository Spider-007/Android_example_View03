package com.example.android_example_view03;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MySqlite extends SQLiteOpenHelper {

    private Context mContext;
    private static final String BOOK_TABLE = "create table Book(" +
            "id Integer primary key autoincrement," +
            "author text," +
            "price real," +
            "pages integer," +
            "name text," +
            "isBug blob)";
    private static final String TABLE_CATEGORY = "create table Category(" +
            "id Integer primary key autoincrement," +
            "category_name text," +
            "category_code integer)";

    public MySqlite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BOOK_TABLE);
        db.execSQL(TABLE_CATEGORY);
        Toast.makeText(mContext, "create BOOK_TABLE Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists Category");
        onCreate(db);
    }
}
