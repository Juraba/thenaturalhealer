package com.marmot.intrepid.naturalhealer.data.tables;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.marmot.intrepid.naturalhealer.model.Villager;

import java.util.List;

@Dao
public interface VillagerDAO{
    @Query("SELECT * FROM Villager")
    List<Villager> getAll();

    @Query("SELECT * FROM Villager WHERE name =:villagername")
    Villager getVillager(String villagername);

    @Insert
    void insertVillager(Villager villager);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Villager villager);

    @Update
    void updateVillager(Villager villager);

    @Delete
    void deleteVillager(Villager villager);

    @Query("DELETE FROM Villager")
    void nukeTable();
}

