package com.marmot.intrepid.naturalhealer.data.tables;

import android.content.ContentValues;
import android.content.Context;


import com.marmot.intrepid.naturalhealer.data.DAOBase;

public class InventoryDAO extends DAOBase{
    public static final String TABLE_NAME = "inventory";

    public static final String NAME_ITEM = "name_item";
    public static final String QUANTITY_ITEM = "quantity_item";
    public static final String NAME_PLAYER = "name_player";

    public static final String TABLE_CREATE = "CREATE TABLE "+ TABLE_NAME + " ("+ //KEY??+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            NAME_ITEM + " TEXT PRIMARY KEY, " + NAME_PLAYER + " TEXT, " + QUANTITY_ITEM + " TEXT);";
    public static final String TABLE_DROP = "DROP TALE IF EXISTS " + TABLE_NAME + ";";

    //TODO : qu'est ce que contexte? MainActivity normalement
    public InventoryDAO(Context c){
        super(c, TABLE_CREATE, TABLE_DROP);
    }


    public void ajouter (String objet, int quantite, String namePlayer){
        ContentValues values = new ContentValues();
        values.put(InventoryDAO.NAME_ITEM, objet);
        values.put(InventoryDAO.QUANTITY_ITEM, quantite);
        values.put(InventoryDAO.NAME_PLAYER, namePlayer);
        mDb.insert(InventoryDAO.TABLE_NAME, null, values);
    }


    public void supprimer(String key, String value){
        mDb.delete(TABLE_NAME, key + " = ", new String[]{String.valueOf(value)});
    }

    public void modifier(String objet, int quantite, String namePlayer) {
        ContentValues values = new ContentValues();
        values.put(InventoryDAO.NAME_ITEM, objet);
        values.put(InventoryDAO.QUANTITY_ITEM, quantite);
        values.put(InventoryDAO.NAME_PLAYER, namePlayer);
        mDb.update(TABLE_NAME, values, NAME_ITEM + " = ?", new String[]{objet});
    }

    //TODO : en discuter
    /*public Cursor find (String param, String value){
        //retourne un tableau
        Cursor c = mDb.rawQuery("select * from "+TABLE_NAME+" where "+param+" = ?", new String[]{value});
        Inventory p= new Inventory(c.getString(1), c.getString(2), c.getLong(3), c.getLong(4));
        return p;
    }*/
}

