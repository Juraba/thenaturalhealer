package com.marmot.intrepid.naturalhealer.data.tables;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.marmot.intrepid.naturalhealer.data.Inventory;

import java.util.List;

@Dao
public interface InventoryDAO{
    @Query("SELECT * FROM Inventory")
    List<Inventory> getAll();

    @Query("SELECT * FROM Inventory WHERE item_name =:itemname")
    Inventory getInventory(String itemname);

    @Insert
    void insertInventory(Inventory inventory);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Inventory inventory);

    @Update
    void updateInventory(Inventory inventory);

    @Delete
    void deleteInventory(Inventory inventory);

    @Query("DELETE FROM Inventory")
    void nukeTable();
}

