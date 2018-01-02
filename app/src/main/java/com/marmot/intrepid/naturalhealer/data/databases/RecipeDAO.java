package com.marmot.intrepid.naturalhealer.data.databases;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.marmot.intrepid.naturalhealer.data.DAOBase;
import com.marmot.intrepid.naturalhealer.model.OtherIngredients;
import com.marmot.intrepid.naturalhealer.model.Player;
import com.marmot.intrepid.naturalhealer.model.Rank;
import com.marmot.intrepid.naturalhealer.model.Recipe;
import com.marmot.intrepid.naturalhealer.model.enumerations.RankEnum;

/**
 * Created by Camille K on 02/01/2018.
 */

//TODO : XP integer ou double?
public class RecipeDAO extends DAOBase{
    public static final String TABLE_NAME = "recipe";

    public static final String NAME_RECIPE = "name_recipe";
    public static final String DESCRIPTION = "description";
    public static final String PROPERTIES = "properties";
    public static final String PRICE = "price";
    public static final String RANK = "rank";
    public static final String DIFFICULTY = "difficulty";
    public static final String SYMPTOMS = "symptoms";
    public static final String PROTOCOL = "protocol";
    public static final String DISCOVERED = "discovered";
    public static final String AVAILABLE = "available";



    public static final String TABLE_CREATE = "CREATE TABLE "+ TABLE_NAME + " ("+
            NAME_RECIPE + " TEXT PRIMARY KEY, " + DESCRIPTION + " TEXT, "+ PROPERTIES + " TEXT, " + PRICE + " REAL, " + RANK + " TEXT, " + DIFFICULTY + " TEXT, " +
            SYMPTOMS+ " TEXT, " + PROTOCOL + " TEXT, " +DISCOVERED+ " TEXT," +AVAILABLE+ "TEXT);";
    public static final String TABLE_DROP = "DROP TALE IF EXISTS " + TABLE_NAME + ";";

    //TODO : qu'est ce que contexte? MainActivity normalement
    public RecipeDAO(Context c){
        super(c);
    }

    //TODO : determnier comment gérer rank (contracdictoire entre discord et code
    public void ajouter (Recipe m){
        ContentValues values = new ContentValues();
        values.put(RecipeDAO.NAME_RECIPE, m.getName());
        values.put(RecipeDAO.DESCRIPTION, m.getDescription());
        values.put(RecipeDAO.PROPERTIES, m.getProperties());
        values.put(RecipeDAO.PRICE, m.getPrice());
        values.put(RecipeDAO.RANK, m.getRank().getName().getEn());
        values.put(RecipeDAO.DIFFICULTY, m.getDifficulty().getEn());
        values.put(RecipeDAO.PROTOCOL, m.getProtocol());
        values.put(RecipeDAO.SYMPTOMS, m.getSymptoms().toString());
        values.put(RecipeDAO.DISCOVERED, m.isDiscovered());
        values.put(RecipeDAO.AVAILABLE, m.isAvailable());
        mDb.insert(RecipeDAO.TABLE_NAME, null, values);
    }


    public void supprimer(String key, String value){
        mDb.delete(TABLE_NAME, key + " = ", new String[]{String.valueOf(value)});
    }


    public void modifier(Recipe m) {
        ContentValues values = new ContentValues();
        values.put(RecipeDAO.NAME_RECIPE, m.getName());
        values.put(RecipeDAO.DESCRIPTION, m.getDescription());
        values.put(RecipeDAO.PROPERTIES, m.getProperties());
        values.put(RecipeDAO.PRICE, m.getPrice());
        values.put(RecipeDAO.RANK, m.getRank().getName().getEn());
        values.put(RecipeDAO.DIFFICULTY, m.getDifficulty().getEn());
        values.put(RecipeDAO.PROTOCOL, m.getProtocol());
        values.put(RecipeDAO.SYMPTOMS, m.getSymptoms().toString());
        values.put(RecipeDAO.DISCOVERED, m.isDiscovered());
        values.put(RecipeDAO.AVAILABLE, m.isAvailable());
        values.put(RecipeDAO.DISCOVERED, m.isDiscovered());
        //mDb.update(TABLE_NAME, values, KEY + " = ?", new String[]{String.valueOf(m.getId())});
    }

    //TODO : voir avec les available et discovered
    /*public Cursor find (String param, String value){
        //retourne un tableau
        Cursor c = mDb.rawQuery("select * from "+TABLE_NAME+" where "+param+" = ?", new String[]{value});
        Player p= new Player(c.getString(1), c.getString(2), c.getLong(3), c.getLong(4));
        return p;
    }*/
}