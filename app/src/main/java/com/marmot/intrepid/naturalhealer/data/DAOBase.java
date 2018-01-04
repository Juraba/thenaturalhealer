package com.marmot.intrepid.naturalhealer.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.marmot.intrepid.naturalhealer.data.tables.InventoryDAO;
import com.marmot.intrepid.naturalhealer.data.tables.ItemDAO;
import com.marmot.intrepid.naturalhealer.data.tables.PlayerDAO;
import com.marmot.intrepid.naturalhealer.data.tables.QuestBookDAO;
import com.marmot.intrepid.naturalhealer.data.tables.QuestDAO;
import com.marmot.intrepid.naturalhealer.data.tables.QuestListDAO;
import com.marmot.intrepid.naturalhealer.data.tables.VillagerDAO;
import com.marmot.intrepid.naturalhealer.model.Player;
import com.marmot.intrepid.naturalhealer.model.Quest;
import com.marmot.intrepid.naturalhealer.model.Villager;

@Database(entities = {
        Player.class,
        Item.class,
        Quest.class,
        Villager.class,
        QuestBook.class,
        QuestList.class,
        Inventory.class
}, version = 1)

public abstract class DAOBase extends RoomDatabase {

    private static DAOBase INSTANCE;

    public abstract PlayerDAO playerDAO();
    public abstract ItemDAO itemDAO();
    public abstract QuestDAO questDAO();
    public abstract VillagerDAO villagerDAO();
    public abstract InventoryDAO inventoryDAO();
    public abstract QuestListDAO questListDAO();
    public abstract QuestBookDAO questBookDAO();

    public static DAOBase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), DAOBase.class, "user-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
