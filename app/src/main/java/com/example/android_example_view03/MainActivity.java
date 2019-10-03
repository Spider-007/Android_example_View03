package com.example.android_example_view03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
    private Button mButton;
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
        mMySqlite = new MySqlite(this, "BOOK_TABLE.db", null, 2);
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

