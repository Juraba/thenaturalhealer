package com.marmot.intrepid.naturalhealer.data.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.marmot.intrepid.naturalhealer.data.DAOBase;
import com.marmot.intrepid.naturalhealer.model.OtherIngredients;
import com.marmot.intrepid.naturalhealer.model.Rank;
import com.marmot.intrepid.naturalhealer.model.Recipe;
import com.marmot.intrepid.naturalhealer.model.enumerations.RankEnum;

//TODO : XP integer ou double? 
public class OtherIngredientsDAO extends DAOBase{
    public static final String TABLE_NAME = "other_ingredients";

    public static final String NAME_OTHER = "name_other";
    public static final String DESCRIPTION = "description";
    public static final String PROPERTIES = "properties";
    public static final String PRICE = "price";
    public static final String RANK = "rank";


    public static final String TABLE_CREATE = "CREATE TABLE "+ TABLE_NAME + " ("+
            NAME_OTHER + " TEXT PRIMARY KEY, " + DESCRIPTION + " TEXT, "+ PROPERTIES + " TEXT, " + PRICE + " REAL, " + RANK + " TEXT);";
    public static final String TABLE_DROP = "DROP TALE IF EXISTS " + TABLE_NAME + ";";


    public OtherIngredientsDAO(Context c){
        super(c, TABLE_CREATE, TABLE_DROP);
    }

    public void ajouter (Recipe m){
        ContentValues values = new ContentValues();
        values.put(OtherIngredientsDAO.NAME_OTHER, m.getName());
        values.put(OtherIngredientsDAO.DESCRIPTION, m.getDescription());
        values.put(OtherIngredientsDAO.PROPERTIES, m.getProperties());
        values.put(OtherIngredientsDAO.PRICE, m.getPrice());
        values.put(OtherIngredientsDAO.RANK, m.getRank().getName().getEn());
        mDb.insert(OtherIngredientsDAO.TABLE_NAME, null, values);
    }


    public void supprimer(String key, String value){
        mDb.delete(TABLE_NAME, key + " = ", new String[]{String.valueOf(value)});
    }


    public void modifier(Recipe m) {
        ContentValues values = new ContentValues();
        values.put(OtherIngredientsDAO.NAME_OTHER, m.getName());
        values.put(OtherIngredientsDAO.DESCRIPTION, m.getDescription());
        values.put(OtherIngredientsDAO.PROPERTIES, m.getProperties());
        values.put(OtherIngredientsDAO.PRICE, m.getPrice());
        values.put(OtherIngredientsDAO.RANK, m.getRank().getName().getEn());
        mDb.update(TABLE_NAME, values, NAME_OTHER + " = ?", new String[]{String.valueOf(m.getName())});
    }

    /*
    public OtherIngredients find (String param, String value){
        //retourne un tableau
        Cursor c = mDb.rawQuery("select * from "+TABLE_NAME+" where "+param+" = ?", new String[]{value});
        OtherIngredients p= new OtherIngredients(c.getString(0), c.getString(1), c.getString(2), c.getLong(3), new Rank(RankEnum.valueOf(c.getString(4))));
        return p;
    }
    */
}


