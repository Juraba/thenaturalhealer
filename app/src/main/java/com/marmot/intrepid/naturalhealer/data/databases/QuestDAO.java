package com.marmot.intrepid.naturalhealer.data.databases;

import android.content.ContentValues;
import android.content.Context;
import com.marmot.intrepid.naturalhealer.data.DAOBase;
import com.marmot.intrepid.naturalhealer.model.Quest;


/**
 * Created by Camille K on 02/01/2018.
 */

//TODO : XP integer ou double? 
public class QuestDAO extends DAOBase{
    public static final String TABLE_NAME = "item";

    public static final String REQUIREMENTS = "history";
    public static final String GOALS = "goals";
    public static final String REWARD_MONEY = "reward_money";
    public static final String REWARD_XP = "reward_xp";
    public static final String CANCELABLE = "cancelable";
    public static final String NAME_QUEST = "name_quest";
    public static final String DESCRIPTION = "description";
    public static final String DONE = "done";
    public static final String TYPE = "type";


    public static final String TABLE_CREATE = "CREATE TABLE "+ TABLE_NAME + " ("+
            NAME_QUEST+" TEXT PRIMARY KEY,"+DESCRIPTION+" TEXT,"+ REQUIREMENTS+" TEXT,"+
            GOALS+" INTEGER, "+REWARD_MONEY+" INTEGER,"+REWARD_XP+" INTEGER,"+CANCELABLE+" TEXT,"+DONE+" TEXT,"+TYPE+"TEXT);";
    public static final String TABLE_DROP = "DROP TALE IF EXISTS " + TABLE_NAME + ";";


    public QuestDAO(Context c){
        super(c);
    }

    public void ajouter (Quest m){
        ContentValues values = new ContentValues();
        values.put(QuestDAO.NAME_QUEST, m.getName());
        values.put(QuestDAO.GOALS, m.getGoals().toString());
        values.put(QuestDAO.REWARD_MONEY, m.getRewardMoney());
        values.put(QuestDAO.TYPE, m.getType().getEn());
        values.put(QuestDAO.REQUIREMENTS, m.getRequirements().toString());
        values.put(QuestDAO.REWARD_XP, m.getRewardXp());
        values.put(QuestDAO.CANCELABLE, m.isCancelable());
        values.put(QuestDAO.DONE, m.isDone());
        values.put(QuestDAO.DESCRIPTION, m.getDescription());
        values.put(QuestDAO.TYPE, m.getType().getEn());
        mDb.insert(QuestDAO.TABLE_NAME, null, values);
    }


    public void supprimer(String key, String value){
        mDb.delete(TABLE_NAME, key + " = ", new String[]{String.valueOf(value)});
    }

    public void modifier(Quest m) {
        ContentValues values = new ContentValues();
        values.put(QuestDAO.NAME_QUEST, m.getName());
        values.put(QuestDAO.GOALS, m.getGoals().toString());
        values.put(QuestDAO.REWARD_MONEY, m.getRewardMoney());
        values.put(QuestDAO.TYPE, m.getType().getEn());
        values.put(QuestDAO.REQUIREMENTS, m.getRequirements().toString());
        values.put(QuestDAO.REWARD_XP, m.getRewardXp());
        values.put(QuestDAO.CANCELABLE, m.isCancelable());
        values.put(QuestDAO.DONE, m.isDone());
        values.put(QuestDAO.DESCRIPTION, m.getDescription());
        values.put(QuestDAO.TYPE, m.getType().getEn());
        mDb.update(TABLE_NAME, values, NAME_QUEST + " = ?", new String[]{String.valueOf(m.getName())});
    }

    //TODO : voir avec done
   /* public Herb find (String param, String value){
        Cursor c = mDb.rawQuery("select * from "+TABLE_NAME+" where "+param+" = ?", new String[]{value});
        Herb p= new Herb(c.getString(0), c.getString(1), c.getString(2), c.getDouble(3), new Rank(RankEnum.valueOf(c.getString(4))),
                c.getString(5), HerbRarity.valueOf(c.getString(6)), c.getString(7), c.getString(8), HerbType.valueOf(c.getString(9)));
        return p;
    }*/
}

