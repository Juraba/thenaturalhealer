package com.marmot.intrepid.naturalhealer.data.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.marmot.intrepid.naturalhealer.data.DAOBase;
import com.marmot.intrepid.naturalhealer.model.Herb;
import com.marmot.intrepid.naturalhealer.model.Rank;
import com.marmot.intrepid.naturalhealer.model.enumerations.HerbRarity;
import com.marmot.intrepid.naturalhealer.model.enumerations.HerbType;
import com.marmot.intrepid.naturalhealer.model.enumerations.RankEnum;

//TODO : XP integer ou double? 
public class HerbDAO extends DAOBase{

    public static final String TABLE_NAME = "item";
    public static final String PIC_NAME = "picname";
    public static final String RACE = "race";
    public static final String HISTORY = "history";
    public static final String COMBINATION = "combination";
    public static final String RARITY = "rarity";
    public static final String TYPE= "type";
    public static final String AVAILABLE = "available";
    public static final String NAME_HERB = "name_herb";
    public static final String DESCRIPTION = "description";
    public static final String PROPERTIES = "properties";
    public static final String PRICE = "price";
    public static final String RANK = "rank";


    public static final String TABLE_CREATE = "CREATE TABLE "+ TABLE_NAME + " ("+
            NAME_HERB+" TEXT PRIMARY KEY,"+PIC_NAME+" TEXT,"+DESCRIPTION+" TEXT,"+PROPERTIES+" TEXT, "+
            PRICE+" REAL,"+RANK+" TEXT,"+RACE+" TEXT,"+RARITY+" TEXT,"+HISTORY+" TEXT,"+
            COMBINATION+" TEXT,"+TYPE+"TEXT);";
    public static final String TABLE_DROP = "DROP TALE IF EXISTS " + TABLE_NAME + ";";


    public HerbDAO(Context c){
        super(c, TABLE_CREATE, TABLE_DROP);
    }

    public void ajouter(Herb m){
        ContentValues values = new ContentValues();
        values.put(HerbDAO.NAME_HERB, m.getName());
        values.put(HerbDAO.PIC_NAME, m.getPicName());
        values.put(HerbDAO.PROPERTIES, m.getProperties());
        values.put(HerbDAO.PRICE, m.getPrice());
        values.put(HerbDAO.RANK, m.getRank().getName().getEn());
        values.put(HerbDAO.RACE, m.getRace());
        values.put(HerbDAO.RARITY, m.getRarity().getEn());
        values.put(HerbDAO.HISTORY, m.getHistory());
        values.put(HerbDAO.COMBINATION, m.getCombination());
        values.put(HerbDAO.DESCRIPTION, m.getDescription());
        values.put(HerbDAO.AVAILABLE, m.isAvailable());
        values.put(HerbDAO.TYPE, m.getType().getEn());
        if(mDb==null){
            mDb = open();
        }
        mDb.insert(HerbDAO.TABLE_NAME, null, values);

    }


    public void supprimer(String key, String value){
        if(mDb == null){
            mDb = open();
        }
        mDb.delete(TABLE_NAME, key + " = ", new String[]{String.valueOf(value)});
    }

    public void modifier(Herb m) {
        ContentValues values = new ContentValues();
        values.put(HerbDAO.NAME_HERB, m.getName());
        values.put(HerbDAO.PIC_NAME, m.getPicName());
        values.put(HerbDAO.PROPERTIES, m.getProperties());
        values.put(HerbDAO.PRICE, m.getPrice());
        values.put(HerbDAO.RANK, m.getRank().getName().getEn());
        values.put(HerbDAO.RACE, m.getRace());
        values.put(HerbDAO.RARITY, m.getRarity().getEn());
        values.put(HerbDAO.HISTORY, m.getHistory());
        values.put(HerbDAO.COMBINATION, m.getCombination());
        values.put(HerbDAO.DESCRIPTION, m.getDescription());
        values.put(HerbDAO.TYPE, m.getType().getEn());
        values.put(HerbDAO.AVAILABLE, m.isAvailable());
        if(mDb == null){
            mDb = open();
        }
        mDb.update(TABLE_NAME, values, NAME_HERB + " = ?", new String[]{String.valueOf(m.getName())});
    }

    //TODO : voir avec available
   public Herb find (String param, String value){
        Cursor c = mDb.rawQuery("select * from "+TABLE_NAME+" where "+param+" = ?", new String[]{value});
        Herb p= new Herb(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getDouble(4), new Rank(RankEnum.valueOf(c.getString(5))),
                c.getString(6), HerbRarity.valueOf(c.getString(7)), c.getString(8), c.getString(9), HerbType.valueOf(c.getString(10)));
        return p;
   }
}

