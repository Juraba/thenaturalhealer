package com.marmot.intrepid.naturalhealer.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.marmot.intrepid.naturalhealer.model.Quest;
import com.marmot.intrepid.naturalhealer.model.Villager;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = Villager.class, parentColumns = "name", childColumns = "villager_name"),
                @ForeignKey(entity = Quest.class, parentColumns = "name", childColumns = "quest_name")
        }
)
public class QuestList {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "villager_name")
    private String villagerName;
    @ColumnInfo(name = "quest_name")
    private String questName;

    public QuestList(int id, String villagerName, String questName) {
        this.id = id;
        this.villagerName = villagerName;
        this.questName = questName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVillagerName() {
        return villagerName;
    }

    public void setVillagerName(String villagerName) {
        this.villagerName = villagerName;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }
}
