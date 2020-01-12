package com.example.skypageui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private MyDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.edit_count).setOnClickListener(this);

        dbHelper = new MyDBHelper(this, "UserStore.db", null, 1);
    }

    public void onClick(View view) {

        EditText editText3 = (EditText) findViewById(R.id.edit_1);
        EditText editText4 = (EditText) findViewById(R.id.edit_2);
        String newname = editText3.getText().toString().trim();
        String password = editText4.getText().toString().trim();
        if (CheckIsDataAlreadyInDBorNot(newname)) {
            Toast.makeText(this, "该用户名已被注册，注册失败", Toast.LENGTH_LONG).show();
        } else {
            if (!register(newname, password)) {
                Toast.makeText(this, "请输入非空数据", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
            }
        }
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    //向数据库插入数据
    public boolean register(String username, String password) {
        if (username == null || password == null)
            return false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        String sql = "insert into userData(name,password) value(?,?)";
//        Object[] obj = {username, password};
//        db.execSQL(sql,obj);
        ContentValues values = new ContentValues();
        values.put("nametext", username);
        values.put("password", password);
        db.insert("userData", null, values);
        db.close();
//        db.execSQL("insert into userData (name,password) values (?,?)",new String[]{username,password});
        return true;
    }

    //检验用户名是否已存在
    public boolean CheckIsDataAlreadyInDBorNot(String value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String Query = "Select * from userData where nametext =?";
        Cursor cursor = db.rawQuery(Query, new String[]{value});
//        游标查询
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

}
