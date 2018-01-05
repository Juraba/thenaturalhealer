package com.marmot.intrepid.naturalhealer.data;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

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
    public abstract PlayerDAO playerDAO();
    public abstract ItemDAO itemDAO();
    public abstract QuestDAO questDAO();
    public abstract VillagerDAO villagerDAO();
    public abstract InventoryDAO inventoryDAO();
    public abstract QuestListDAO questListDAO();
    public abstract QuestBookDAO questBookDAO();
}
