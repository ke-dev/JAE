package com.example.skypageui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoteBookActivity extends AppCompatActivity {

    private ListView noteListView;
    private Button addBtn;
    private List<NoteInfo> noteList = new ArrayList<>();
    private ListAdapter mListAdapter;

    private static NoteDataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notebook_activity);
        dbHelper = new NoteDataBaseHelper(this, "MyNote.db", null, 1);

        initView();
        setListener();
        //跳转回主界面 刷新列表
        Intent intent = getIntent();
        if (intent != null) {
            getNoteList();
            mListAdapter.refreshDataSet();
        }
    }

    //初始化视图
    private void initView() {
        noteListView = findViewById(R.id.note_list);
        addBtn = findViewById(R.id.btn_add);
        //获取noteList
        getNoteList();
        mListAdapter = new ListAdapter(NoteBookActivity.this, noteList);
        noteListView.setAdapter(mListAdapter);
    }

    //设置监听器
    private void setListener() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteBookActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteInfo noteInfo = noteList.get(position);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("noteInfo", (Serializable) noteInfo);
                intent.putExtras(bundle);
                intent.setClass(NoteBookActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        noteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final NoteInfo noteInfo = noteList.get(position);
                String title = "警告";
                new AlertDialog.Builder(NoteBookActivity.this)
                        .setIcon(R.drawable.ic_event_note_black_24dp)
                        .setTitle(title)
                        .setMessage("确定要删除吗?")
                        .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Note.deleteNote(dbHelper, Integer.parseInt(noteInfo.getId()));
                                noteList.remove(position);
                                mListAdapter.refreshDataSet();
                                Toast.makeText(NoteBookActivity.this, "删除成功！", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create().show();
                return true;
            }
        });
    }

    //从数据库中读取所有笔记 封装成List<NoteInfo>
    private void getNoteList() {
        noteList.clear();
        Cursor allNotes = Note.getAllNotes(dbHelper);
        for (allNotes.moveToFirst(); !allNotes.isAfterLast(); allNotes.moveToNext()) {
            NoteInfo noteInfo = new NoteInfo();
            noteInfo.setId(allNotes.getString(allNotes.getColumnIndex(Note._id)));
            noteInfo.setTitle(allNotes.getString(allNotes.getColumnIndex(Note.title)));
            noteInfo.setContent(allNotes.getString(allNotes.getColumnIndex(Note.content)));
            noteInfo.setDate(allNotes.getString(allNotes.getColumnIndex(Note.time)));
            noteList.add(noteInfo);
        }
    }

    //重写返回按钮处理事件
    @Override
    public void onBackPressed() {
        String title = "提示";
        new AlertDialog.Builder(NoteBookActivity.this)
                .setIcon(R.drawable.ic_event_note_black_24dp)
                .setTitle(title)
                .setMessage("确定要退出吗?")
                .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(NoteBookActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }

    //给其他类提供dbHelper
    public static NoteDataBaseHelper getDbHelper() {
        return dbHelper;
    }
}