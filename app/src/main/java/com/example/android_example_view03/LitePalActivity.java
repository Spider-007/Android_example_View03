package com.example.android_example_view03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.security.acl.Permission;

public class LitePalActivity extends AppCompatActivity {

    private Button mCreateBtn, mAddBtn, mUpdateBtn, mDeleteBtn, callPhoneBtn;
    public static final int REQUEST_CALL_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lite_pal);
        initView();
    }

    private void initView() {
        mCreateBtn = findViewById(R.id.createBtn);
        mAddBtn = findViewById(R.id.AddBtn);
        mUpdateBtn = findViewById(R.id.updateBtn);
        mDeleteBtn = findViewById(R.id.deleteBtn);
        callPhoneBtn = findViewById(R.id.callPhoneBtn);
        callPhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call phone
                if ((ContextCompat.checkSelfPermission(LitePalActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(LitePalActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION_CODE);
                } else {
                    callPhone();
                }
            }
        });

        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete the date todo
                Book mBook = new Book();
                mBook.delete();
            }
        });

        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update data method one use Storage
/*                Book mBook = new Book();
                mBook.setAuthor("SpiderLine");
                mBook.setName("Spider");
                mBook.setPages(100);
                mBook.setPrices(10.2);
                mBook.setRead(false);
                mBook.save();
                mBook.setAuthor("Spider-007");
                mBook.save();*/

                //method two
                Book mBooks = new Book();
                mBooks.setPrices(222.11);
                mBooks.setPages(200);
                mBooks.updateAll();
            }
        });
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insert data
                Book mBook = new Book();
                mBook.setAuthor("SpiderLine");
                mBook.setName("Spider");
                mBook.setPages(100);
                mBook.setPrices(10.2);
                mBook.setRead(false);
                mBook.save();
            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //create LitePal Table
                LitePal.getDatabase();

            }
        });
    }

    private void callPhone() {
        try {
            Intent mIntent = new Intent(Intent.ACTION_CALL);
            mIntent.setData(Uri.parse("tel:10086"));
            startActivity(mIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            callPhone();
        } else {
            Toast.makeText(this, "lost the permissions", Toast.LENGTH_SHORT).show();
        }
    }
}
