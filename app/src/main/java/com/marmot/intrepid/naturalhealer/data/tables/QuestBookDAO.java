package com.marmot.intrepid.naturalhealer.data.tables;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.marmot.intrepid.naturalhealer.data.DAOBase;
import com.marmot.intrepid.naturalhealer.data.QuestBook;

import java.util.List;

@Dao
public interface QuestBookDAO{
    @Query("SELECT * FROM QuestBook")
    List<QuestBook> getAll();

    @Query("SELECT * FROM QuestBook WHERE quest_name =:questname")
    QuestBook getQuestBook(String questname);

    @Insert
    void insertQuestBook(QuestBook questbook);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(QuestBook questbook);

    @Update
    void updateQuestBook(QuestBook questbook);

    @Delete
    void deleteQuestBook(QuestBook questbook);

    @Query("DELETE FROM QuestBook")
    void nukeTable();
}

