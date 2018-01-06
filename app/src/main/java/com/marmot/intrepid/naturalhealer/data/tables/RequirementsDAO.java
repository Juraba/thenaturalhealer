package com.marmot.intrepid.naturalhealer.data.tables;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.marmot.intrepid.naturalhealer.data.Requirements;

import java.util.List;

@Dao
public interface RequirementsDAO {
    @Query("SELECT * FROM Requirements")
    List<Requirements> getAll();

    @Query("SELECT * FROM Requirements WHERE item_name =:itemname")
    Requirements getRequirements(String itemname);

    @Insert
    void insertRequirements(Requirements inventory);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Requirements requirements);

    @Update
    void updateRequirements(Requirements inventory);

    @Delete
    void deleteRequirements(Requirements inventory);

    @Query("DELETE FROM Requirements")
    void nukeTable();
}
