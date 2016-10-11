package com.example.cr.danting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cr on 2016/10/10.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private Context mContext;
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table User(id INTEGER PRIMARY KEY AUTOINCREMENT,username varchar(20),password varchar(20))");
//        Toast.makeText(mContext,"create succeed",Toast.LENGTH_SHORT).show();
        System.out.println("create succeed");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
