package com.marmot.intrepid.naturalhealer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Camille K on 02/01/2018.
 */

public abstract class DAOBase extends SQLiteOpenHelper{

    String tableCreate, tableDrop;

    //Version 1 de la base (mettre a jour cet attribut en cas de changement de la base)
    protected final static int VERSION = 1;
    //Le nom du fichier de la base
    protected final static String NOM = "database.db";

    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler mHandler = null;

    public DAOBase(Context context, String tableCreate, String tableDrop){
        super(context,NOM, null, VERSION);
        this.tableCreate =  tableCreate;
        this.tableDrop = tableDrop;
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(tableCreate);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(tableDrop);
        onCreate(db);
    }

    public SQLiteDatabase open(){
        if(mDb==null){
            mDb = mHandler.getWritableDatabase();
        }
        return mDb;
    }

    public void close(){
        mDb.close();
    }

    public SQLiteDatabase getDb(){
        mDb = null;
        return mDb;
    }
}
