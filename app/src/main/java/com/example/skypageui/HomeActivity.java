package com.example.skypageui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        findViewById(R.id.p3).setOnClickListener(this);
        findViewById(R.id.p4).setOnClickListener(this);
        findViewById(R.id.p1).setOnClickListener(this);

        ImageView button = findViewById(R.id.p2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                创建Intent对象
                Intent intent = new Intent();
//                为Intent设置动作
                intent.setAction(Intent.ACTION_VIEW);
//                设置数据
                intent.setData(Uri.parse("http://www.baidu.com"));
//                把Intent传递给Activity
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.p3:
                intent = new Intent(HomeActivity.this, GetApiActivity.class);
                break;
            case R.id.p4:
                intent = new Intent(HomeActivity.this, MusicPlayer.class);
                break;
            case R.id.p1:
                intent = new Intent(HomeActivity.this, NoteBookActivity.class);
                break;
        }
        startActivity(intent);
    }
}