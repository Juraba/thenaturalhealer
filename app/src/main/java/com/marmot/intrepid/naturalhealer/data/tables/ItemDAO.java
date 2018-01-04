package com.marmot.intrepid.naturalhealer.data.tables;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.marmot.intrepid.naturalhealer.data.Item;

import java.util.List;

@Dao
public interface ItemDAO {
    @Query("SELECT * FROM Item")
    List<Item> getAll();

    @Query("SELECT * FROM Item WHERE name =:itemname")
    Item getItem(String itemname);

    @Insert
    void insertItem(Item item);

    @Update
    void updateItem(Item item);

    @Delete
    void deleteItem(Item item);
}
