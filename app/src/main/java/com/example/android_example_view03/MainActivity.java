package com.example.android_example_view03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.Buffer;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;
    private Button mButton, mAddButton, mUpdateButton, mInquireButton, mDeleteButton, mlitepalButton;
    private MySqlite mMySqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //判断 是否读取到文件内容
        String load = load();
        if (!TextUtils.isEmpty(load)) {
            mEditText.setText(load);
            Toast.makeText(this,
                    "restore Success", Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        mEditText = findViewById(R.id.editText);
        mButton = findViewById(R.id.CreateTabBtn);
        mAddButton = findViewById(R.id.addSqBtn);
        mUpdateButton = findViewById(R.id.updateSqBtn);
        mInquireButton = findViewById(R.id.inquireSqBtn);
        mDeleteButton = findViewById(R.id.daleteSqBtn);
        mlitepalButton = findViewById(R.id.litepalBtn);
        mlitepalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到 litepal 界面 使用关系映射去处理 数据库的CRUD操作
                startActivity(new Intent(MainActivity.this,LitePalActivity.class));
            }
        });
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add 表数据
                SQLiteDatabase db = mMySqlite.getWritableDatabase();
                ContentValues mContentValues = new ContentValues();
                mContentValues.put("name", "Spider-007");
                mContentValues.put("author", "SpiderLine");
                mContentValues.put("pages", 10);
                mContentValues.put("price", 100.68);
                db.insert("Book", null, mContentValues);

                mContentValues.clear();

                //insert 第二条数据
                ContentValues mContentValuesTwo = new ContentValues();
                mContentValuesTwo.put("author", "007");
                mContentValuesTwo.put("name", "Spider");
                mContentValuesTwo.put("pages", 20);
                mContentValuesTwo.put("price", 199.99);
                db.insert("Book", null, mContentValuesTwo); //表名一定要记住 这是一个坑呀！
            }
        });
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update table Data
                SQLiteDatabase db = mMySqlite.getWritableDatabase();
                ContentValues mContentValues = new ContentValues();
                mContentValues.put("price", 222.88);
                db.update("Book", mContentValues, "name = ?", new String[]{"Spider-007"});
            }
        });
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete the line Data
                SQLiteDatabase db = mMySqlite.getWritableDatabase();
                db.delete("Book", "pages > ?", new String[]{"10"});
            }
        });
        mInquireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select the Data
                SQLiteDatabase db = mMySqlite.getWritableDatabase();
                Cursor cursor = db.query("Book", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        Double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.e("SpiderLine", "name: " + name + "pages->::" + pages + "price->::" + price + "author->::" + author);

                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
        mMySqlite = new MySqlite(this, "BOOK_TABLE.db", null, 3);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新写入文件如果
                try {
                    mMySqlite.getWritableDatabase();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //save edit Content
        save(mEditText.getText().toString().trim());
    }

    //把内容存入到指定文件
    private void save(String mString) {
        FileOutputStream mFileOutPutStream;
        BufferedWriter mBufferedWriter = null;
        try {
            mFileOutPutStream = openFileOutput("data", Context.MODE_PRIVATE);
            mBufferedWriter = new BufferedWriter(new OutputStreamWriter(mFileOutPutStream));
            mBufferedWriter.write(mString);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (mBufferedWriter != null) {
                    mBufferedWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //读取文件目录下的文件内容
    private String load() {
        FileInputStream mFileInputStream;
        BufferedReader mBufferedRead = null;
        StringBuilder mStringBuilder = new StringBuilder();
        try {
            mFileInputStream = openFileInput("data");
            mBufferedRead = new BufferedReader(new InputStreamReader(mFileInputStream));
            String line = "";
            while ((line = mBufferedRead.readLine()) != null) {
                mStringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (mBufferedRead != null) {
                    mBufferedRead.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mStringBuilder.toString();
    }
}

