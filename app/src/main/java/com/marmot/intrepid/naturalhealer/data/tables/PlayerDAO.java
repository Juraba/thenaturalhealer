package com.marmot.intrepid.naturalhealer.data.tables;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.marmot.intrepid.naturalhealer.model.Player;

import java.util.List;

@Dao
public interface PlayerDAO {
    @Query("SELECT * FROM Player")
    List<Player> getAll();

    @Query("SELECT * FROM player WHERE nickname =:playername")
    List<Player> getPlayer(String playername);

    @Insert
    void insertOne(Player player);

    @Insert
    void insertMany(List<Player> players);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Player player);

    @Update
    void updatePlayer(Player player);

    @Delete
    void deletePlayer(Player player);

    @Query("DELETE FROM Player")
    void nukeTable();
}

