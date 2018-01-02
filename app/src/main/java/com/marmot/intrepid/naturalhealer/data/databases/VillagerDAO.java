package com.marmot.intrepid.naturalhealer.data.databases;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.renderscript.Sampler;

import com.marmot.intrepid.naturalhealer.data.DAOBase;
import com.marmot.intrepid.naturalhealer.model.Player;
import com.marmot.intrepid.naturalhealer.model.Rank;
import com.marmot.intrepid.naturalhealer.model.Villager;
import com.marmot.intrepid.naturalhealer.model.enumerations.RankEnum;

/**
 * Created by Camille K on 02/01/2018.
 */


public class VillagerDAO extends DAOBase{
    public static final String TABLE_NAME = "player";
    public static final String NAME_VILLAGER = "name_player";
    public static final String PIC_NAME = "pic_name";


    public static final String TABLE_CREATE = "CREATE TABLE "+ TABLE_NAME + " ("+
            NAME_VILLAGER + " TEXT PRIMARY KEY, " + PIC_NAME + " TEXT);";
    public static final String TABLE_DROP = "DROP TALE IF EXISTS " + TABLE_NAME + ";";

    //TODO : qu'est ce que contexte? MainActivity normalement
    public VillagerDAO(Context c){
        super(c);
    }

    public void ajouter (Villager m){
        ContentValues values = new ContentValues();
        values.put(VillagerDAO.NAME_VILLAGER, m.getName());
        values.put(VillagerDAO.PIC_NAME, m.getPicName());
        mDb.insert(VillagerDAO.TABLE_NAME, null, values);
    }


    public void supprimer(String key, String value){
        mDb.delete(TABLE_NAME, key + " = ", new String[]{String.valueOf(value)});
    }


    public void modifier(Villager m) {
        ContentValues values = new ContentValues();
        values.put(VillagerDAO.NAME_VILLAGER, m.getName());
        values.put(VillagerDAO.PIC_NAME, m.getPicName());
        mDb.update(TABLE_NAME, values, NAME_VILLAGER + " = ?", new String[]{m.getName()});
    }


    public Villager find (String param, String value){
        //retourne un tableau
        Cursor c = mDb.rawQuery("select * from "+TABLE_NAME+" where "+param+" = ?", new String[]{value});
        Villager p= new Villager(c.getString(0), c.getString(1));
        return p;
    }
}

