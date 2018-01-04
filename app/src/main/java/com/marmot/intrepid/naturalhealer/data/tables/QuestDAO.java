package com.marmot.intrepid.naturalhealer.data.tables;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.marmot.intrepid.naturalhealer.model.Quest;

import java.util.List;

@Dao
public interface QuestDAO{
    @Query("SELECT * FROM Quest")
    List<Quest> getAll();

    @Query("SELECT * FROM Quest WHERE quest_type =:questtype")
    List<Quest> getQuestByType(String questtype);

    @Query("SELECT * FROM Quest WHERE name =:questname")
    Quest getQuest(String questname);

    @Insert
    void insertQuest(Quest quest);

    @Update
    void updateQuest(Quest quest);

    @Delete
    void deleteQuest(Quest quest);
}

