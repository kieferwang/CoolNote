package com.krisitine.coolnote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener{
    private ListView listView;
    private SimpleAdapter simpleAdapter ;
    private List<Map<String, Object> > dataList;
    private Button addNote;
    private TextView tv_content;
    private NoteDataBaseHelper DBHelper;
    private SQLiteDatabase DB;
    private int name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    //在活动显示时更新listView
    @Override
    protected void onStart() {
        super.onStart();
        RefreshNotesList();
    }

    private void initView() {
        tv_content = (TextView) findViewById(R.id.tv_content);
        listView = (ListView) findViewById(R.id.listview);
        dataList = new ArrayList<Map<String, Object>>();
        addNote = (Button) findViewById(R.id.btn_editnote);
        DBHelper = new NoteDataBaseHelper(this);
        DB = DBHelper.getReadableDatabase();

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("info", "");
                bundle.putInt("enter_state", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }


        });
    }

    //刷新listview
    public void RefreshNotesList() {
        //如果dataList已经有的内容，全部删掉
        //并且更新simp_adapter
        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            simpleAdapter.notifyDataSetChanged();
        }

        //从数据库读取信息
        Cursor cursor = DB.query("note", null, null, null, null, null, null);
        startManagingCursor(cursor);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("content"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tv_content", name);
            map.put("tv_date", date);
            dataList.add(map);
        }
        simpleAdapter = new SimpleAdapter(this, dataList, R.layout.item,
                new String[]{"tv_content", "tv_date"}, new int[]{
                R.id.tv_content, R.id.tv_date});
        listView.setAdapter(simpleAdapter);
    }







    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除该日志");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //获取listview中此个item中的内容
                //删除该行后刷新listview的内容
                String content = listView.getItemAtPosition(position) + "";
                String content1 = content.substring(content.indexOf("=") + 1,
                        content.indexOf(","));
                DB.delete("note", "content = ?", new String[]{content1});
//                RefreshNotesListNotesList();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //获取listview中此个item中的内容
        String content = listView.getItemAtPosition(position) + "";
        String content1 = content.substring(content.indexOf("=") + 1,
                content.indexOf(","));

        Intent myIntent = new Intent(MainActivity.this, NoteEdit.class);
        Bundle bundle = new Bundle();
        bundle.putString("info", content1);
        bundle.putInt("enter_state", 1);
        myIntent.putExtras(bundle);
        startActivity(myIntent);
    }
}
