package com.example.skypageui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private Button btn_save;
    private Button btn_return;
    private TextView tv_now;
    private EditText et_title;
    private EditText et_content;
    //记录当前编辑的笔记对象（用于比对是否改变）
    private NoteInfo currentNote;
    //记录是否是插入后更新状态
    private boolean insertFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initView();
        setListener();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //主界面点击ListView中的一个Item跳转时
        if(bundle != null){
            currentNote = (NoteInfo) bundle.getSerializable("noteInfo");
            et_title.setText(currentNote.getTitle());
            et_content.setText(currentNote.getContent());
            insertFlag = false;
        }
    }
    //初始化视图
    private void initView(){
        btn_save = findViewById(R.id.btn_save);
        btn_return = findViewById(R.id.btn_return);
        tv_now = findViewById(R.id.tv_now);
        et_content = findViewById(R.id.edit_content);
        et_title = findViewById(R.id.edit_title);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        tv_now.setText(sdf.format(date));

    }
    //设置监听器
    private void setListener(){
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( et_title.getText().toString().equals("") ||
                        et_content.getText().toString().equals("")){
                    Toast.makeText(EditActivity.this,R.string.save_fail,Toast.LENGTH_LONG).show();
                }else {
                    saveNote();
                    Intent intent = new Intent(EditActivity.this,NoteBookActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    //保存笔记到数据库 判断是新建还是更新
    private void saveNote(){
        NoteDataBaseHelper dbHelper = NoteBookActivity.getDbHelper();

        ContentValues values = new ContentValues();
        values.put(Note.title,et_title.getText().toString());
        values.put(Note.content,et_content.getText().toString());
        values.put(Note.time,tv_now.getText().toString());
        if (insertFlag){
            Note.insertNote(dbHelper,values);
        }else{
            Note.updateNote(dbHelper,Integer.parseInt(currentNote.getId()),values);
        }
    }
    //重写手机上返回按键处理函数，如果更改了提示保存 否则直接返回主界面
    @Override
    public void onBackPressed() {
        boolean display = false;
        if (insertFlag){
            if( !et_title.getText().toString().equals("") &&
                    !et_content.getText().toString().equals("")){
                display = true;
            }
        }else{
            if( !et_title.getText().toString().equals(currentNote.getTitle()) ||
                    !et_content.getText().toString().equals(currentNote.getContent())){
                display = true;
            }
        }
        if (display){
            String title = "警告";
            new AlertDialog.Builder(EditActivity.this)
                    .setIcon(R.drawable.ic_event_note_black_24dp)
                    .setTitle(title)
                    .setMessage("是否保存当前内容?")
                    .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveNote();
                            Toast.makeText(EditActivity.this,R.string.save_succ,Toast.LENGTH_LONG).show();
                            //更新当前Note对象的值 防止选择保存后按返回仍显示此警告对话框
                            currentNote.setTitle(et_title.getText().toString());
                            currentNote.setContent(et_content.getText().toString());
                        }
                    })
                    .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(EditActivity.this,NoteBookActivity.class);
                            startActivity(intent);
                        }
                    }).create().show();
        }else{
            Intent intent = new Intent(EditActivity.this,NoteBookActivity.class);
            startActivity(intent);
        }
    }
}