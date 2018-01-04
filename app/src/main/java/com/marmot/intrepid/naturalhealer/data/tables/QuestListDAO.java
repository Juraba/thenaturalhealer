package com.marmot.intrepid.naturalhealer.data.tables;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.marmot.intrepid.naturalhealer.data.QuestList;

import java.util.List;

@Dao
public interface QuestListDAO {
    @Query("SELECT * FROM QuestList")
    List<QuestList> getAll();

    @Query("SELECT * FROM QuestList WHERE villager_name =:villagername")
    QuestList getQuestList(String villagername);

    @Insert
    void insertQuestList(QuestList questlist);

    @Update
    void updateQuestList(QuestList questlist);

    @Delete
    void deleteQuestList(QuestList questlist);
}

