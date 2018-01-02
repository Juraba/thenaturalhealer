package com.marmot.intrepid.naturalhealer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Map;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String TABLE_CREATE = null;
    public static final String TABLE_DROP = null;

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TABLE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(TABLE_DROP);
        onCreate(db);
    }
}
