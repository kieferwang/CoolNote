package com.krisitine.coolnote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kristine on 2016/1/20.
 */
public class NoteDataBaseHelper extends SQLiteOpenHelper {

    public static final String CreateNote="create table note("
            +"id integer primary key autoincrement,"
            +"content text,"
            +"date text)";
    public NoteDataBaseHelper(Context context) {
        super(context, "note", null, 1);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateNote);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
