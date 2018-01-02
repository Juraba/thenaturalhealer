package com.marmot.intrepid.naturalhealer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Camille K on 02/01/2018.
 */

public abstract class DAOBase {
    //Version 1 de la base (mettre a jour cet attribut en cas de changement de la base)
    protected final static int VERSION = 1;
    //Le nom du fichier de la base
    protected final static String NOM = "database.db";

    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler mHandler = null;

    public DAOBase(Context pContext){
        this.mHandler = new DatabaseHandler(pContext, NOM, null, VERSION);
    }

    public SQLiteDatabase open(){
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public void close(){
        mDb.close();
    }

    public SQLiteDatabase getDatabase(){
        return mDb;
    }
}
