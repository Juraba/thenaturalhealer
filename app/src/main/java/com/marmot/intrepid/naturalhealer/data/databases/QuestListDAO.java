package com.marmot.intrepid.naturalhealer.data.databases;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.marmot.intrepid.naturalhealer.data.DAOBase;
import com.marmot.intrepid.naturalhealer.model.Player;
import com.marmot.intrepid.naturalhealer.model.Quest;

/**
 * Created by Camille K on 02/01/2018.
 */


public class QuestListDAO extends DAOBase{
    public static final String TABLE_NAME = "quest";
    
    public static final String NAME_QUEST = "name_quest";
    public static final String NAME_VILLAGER = "name_village";
    public static final String NAME_PLAYER = "name_player";

    public static final String TABLE_CREATE = "CREATE TABLE "+ TABLE_NAME + " ("+ //KEY??+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            NAME_QUEST + " TEXT PRIMARY KEY, " + NAME_VILLAGER + " TEXT,"+NAME_PLAYER+" TEXT);";
    public static final String TABLE_DROP = "DROP TALE IF EXISTS " + TABLE_NAME + ";";

    //TODO : qu'est ce que contexte? MainActivity normalement
    public QuestListDAO(Context c){
        super(c);
    }


    public void ajouter (String namePlayer, String nameQuest, String nameVillager){
        ContentValues values = new ContentValues();
        values.put(QuestListDAO.NAME_QUEST, nameQuest);
        values.put(QuestListDAO.NAME_VILLAGER, nameVillager);
        values.put(QuestListDAO.NAME_PLAYER, namePlayer);
        mDb.insert(QuestListDAO.TABLE_NAME, null, values);
    }


    public void supprimer(String key, String value){
        mDb.delete(TABLE_NAME, key + " = ", new String[]{String.valueOf(value)});
    }

    public void modifier(String namePlayer, String nameQuest, String nameVillager) {
        ContentValues values = new ContentValues();
        values.put(QuestListDAO.NAME_QUEST, nameQuest);
        values.put(QuestListDAO.NAME_VILLAGER, nameVillager);
        values.put(QuestListDAO.NAME_PLAYER, namePlayer);
        //mDb.update(TABLE_NAME, values, KEY + " = ?", new String[]{String.valueOf(m.getId())});
    }

    //TODO : findAll
    /*public Cursor find (String param, String value){
        //retourne un tableau
        Cursor c = mDb.rawQuery("select * from "+TABLE_NAME+" where "+param+" = ?", new String[]{value});
        Quest q= new Quest(c.getString(1), c.getString(2), c.getLong(3), c.getLong(4));
        return q;
    }*/
}

