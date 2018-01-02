package com.marmot.intrepid.naturalhealer.data.databases;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


import com.marmot.intrepid.naturalhealer.data.DAOBase;


/**
 * Created by Camille K on 02/01/2018.
 */


public class QuestBookDAO extends DAOBase{
    public static final String TABLE_NAME = "inventory";

    public static final String NAME_VILLAGER = "name_item";
    public static final String NAME_QUEST = "quantity_item";
    public static final String NAME_PLAYER = "name_player";

    public static final String TABLE_CREATE = "CREATE TABLE "+ TABLE_NAME + " ("+
            NAME_QUEST + " TEXT PRIMARY KEY, " + NAME_PLAYER + " TEXT, " + NAME_VILLAGER + " TEXT);";
    public static final String TABLE_DROP = "DROP TALE IF EXISTS " + TABLE_NAME + ";";

    //TODO : qu'est ce que contexte? MainActivity normalement
    public QuestBookDAO(Context c){
        super(c);
    }


    public void ajouter (String nameQuest, String nameVillager, String namePlayer){
        ContentValues values = new ContentValues();
        values.put(QuestBookDAO.NAME_QUEST, nameQuest);
        values.put(QuestBookDAO.NAME_VILLAGER, nameVillager);
        values.put(QuestBookDAO.NAME_PLAYER, namePlayer);
        mDb.insert(QuestBookDAO.TABLE_NAME, null, values);
    }


    public void supprimer(String key, String value){
        mDb.delete(TABLE_NAME, key + " = ", new String[]{String.valueOf(value)});
    }

    public void modifier(String nameQuest, String nameVillager, String namePlayer) {
        ContentValues values = new ContentValues();
        values.put(QuestBookDAO.NAME_QUEST, nameQuest);
        values.put(QuestBookDAO.NAME_VILLAGER, nameVillager);
        values.put(QuestBookDAO.NAME_PLAYER, namePlayer);
        mDb.update(TABLE_NAME, values, NAME_QUEST + " = ?", new String[]{nameQuest});
    }

    //TODO : findAll
    /*public Cursor find (String param, String value){
        //retourne un tableau
        Cursor c = mDb.rawQuery("select * from "+TABLE_NAME+" where "+param+" = ?", new String[]{value});
        Inventory p= new Inventory(c.getString(1), c.getString(2), c.getLong(3), c.getLong(4));
        return p;
    }*/
}

