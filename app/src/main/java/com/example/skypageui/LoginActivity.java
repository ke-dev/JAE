package com.example.skypageui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private MyDBHelper dbHelper;
    private EditText username;
    private EditText userpassword;
    private Intent intent;

    //验证登录
    public boolean login(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from userData where nametext=? and password=?";
        Cursor cursor = db.rawQuery(sql, new String[]{username, password});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.tv_edit).setOnClickListener(this);
        findViewById(R.id.tv_login).setOnClickListener(this);

        dbHelper = new MyDBHelper(this, "UserStore.db", null, 1);
    }

    //点击登录按钮
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_edit:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                break;

            case R.id.tv_login:
                username = (EditText) findViewById(R.id.sky_acount);
                userpassword = (EditText) findViewById(R.id.pwd_accout);
                String userName = username.getText().toString().trim();
                String passWord = userpassword.getText().toString().trim();
                if (login(userName, passWord)) {
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "登陆失败,请输入正确账号密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(LoginActivity.this, HomeActivity.class);
                break;
        }
        startActivity(intent);
    }
}