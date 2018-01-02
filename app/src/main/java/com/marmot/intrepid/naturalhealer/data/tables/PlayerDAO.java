package com.marmot.intrepid.naturalhealer.data.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.marmot.intrepid.naturalhealer.data.DAOBase;
import com.marmot.intrepid.naturalhealer.model.Player;
import com.marmot.intrepid.naturalhealer.model.Rank;
import com.marmot.intrepid.naturalhealer.model.enumerations.RankEnum;

public class PlayerDAO extends DAOBase{
    public static final String TABLE_NAME = "player";
    public static final String NAME_PLAYER = "name_player";
    public static final String PIC_NAME = "pic_name";
    public static final String RANK = "rank";
    public static final String XP = "xp";
    public static final String PURSE = "purse";

    public static final String TABLE_CREATE = "CREATE TABLE "+ TABLE_NAME + " ("+
        NAME_PLAYER + " TEXT PRIMARY KEY, " + PIC_NAME + " TEXT, " + RANK + " TEXT, " + XP + " INTEGER, " + PURSE + "REAL);";
    public static final String TABLE_DROP = "DROP TALE IF EXISTS " + TABLE_NAME + ";";

    //TODO : qu'est ce que contexte? MainActivity normalement
    public PlayerDAO(Context c){
        super(c, TABLE_CREATE, TABLE_DROP);
    }

    public void ajouter (Player m){
        ContentValues values = new ContentValues();
        values.put(PlayerDAO.NAME_PLAYER, m.getNickname());
        values.put(PlayerDAO.PIC_NAME, m.getPicName());
        values.put(PlayerDAO.RANK, m.getRank().getName().getEn());
        values.put(PlayerDAO.XP, m.getXp());
        values.put(PlayerDAO.PURSE, m.getPurse());
        mDb.insert(PlayerDAO.TABLE_NAME, null, values);
    }


    public void supprimer(String key, String value){
        mDb.delete(TABLE_NAME, key + " = ", new String[]{String.valueOf(value)});
    }


    public void modifier(Player m) {
        ContentValues values = new ContentValues();
        values.put(PlayerDAO.NAME_PLAYER, m.getNickname());
        values.put(PlayerDAO.PIC_NAME, m.getPicName());
        values.put(PlayerDAO.RANK, m.getRank().getName().getEn());
        values.put(PlayerDAO.XP, m.getXp());
        values.put(PlayerDAO.PURSE, m.getPurse());
        mDb.update(TABLE_NAME, values, NAME_PLAYER + " = ?", new String[]{m.getNickname()});
    }


    public Player find (String param, String value){
        //retourne un tableau
        Cursor c = mDb.rawQuery("select * from "+TABLE_NAME+" where "+param+" = ?", new String[]{value});
        Player p= new Player(c.getString(0), c.getString(1), new Rank(RankEnum.valueOf(c.getString(2))), c.getInt(3), c.getLong(4));
        return p;
    }
}

