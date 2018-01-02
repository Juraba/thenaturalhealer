package com.marmot.intrepid.naturalhealer.control;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.marmot.intrepid.naturalhealer.R;
import com.marmot.intrepid.naturalhealer.model.Quest;
import com.marmot.intrepid.naturalhealer.model.Villager;
import com.marmot.intrepid.naturalhealer.model.enumerations.QuestType;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.ArrayList;

public class QuestInfoActivity extends AppCompatActivity {

    private GameService game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_info);

        game = GameService.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView originDemand = (TextView) findViewById(R.id.originDemand);
        TextView description = (TextView) findViewById(R.id.description);
        TextView goals = (TextView) findViewById(R.id.goals);
        TextView rewards = (TextView) findViewById(R.id.reward);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.get("quest") != null) {

                setTitle("Quest : " + bundle.get("quest").toString());

                ArrayList<Villager> villagers = game.getVillagers();
                for (int i = 0; i < villagers.size(); i++) {
                    ArrayList<Quest> quests = villagers.get(i).getQuests();
                    for (int j = 0; j < quests.size(); j++) {
                        if (quests.get(j).getName().equals(bundle.get("quest").toString())) {
                            if (quests.get(j).getType() == QuestType.MAIN) {
                                Toast.makeText(getApplicationContext(), "Quête principale", Toast.LENGTH_SHORT).show();
                                originDemand.setText(villagers.get(i).getName());
                                description.setText(quests.get(j).getDescription());
                                goals.setText("Nothing for the moment because we did not set a variable with a text for this ahah :D");
                                rewards.setText("XP : " + quests.get(j).getRewardXp() + "\nMoney : " + quests.get(j).getRewardMoney());
                            }
                            else if (quests.get(j).getType() == QuestType.DAILY) {
                                Toast.makeText(getApplicationContext(), "Quête journalière", Toast.LENGTH_SHORT).show();
                                originDemand.setText(villagers.get(i).getName());
                                description.setText(quests.get(j).getDescription());
                                goals.setText("Nothing for the moment because we did not set a variable with a text for this ahah :D");
                                rewards.setText("XP : " + quests.get(j).getRewardXp() + "\nMoney : " + quests.get(j).getRewardMoney());
                            }
                            else if (quests.get(j).getType() == QuestType.EVENT) {
                                Toast.makeText(getApplicationContext(), "Quête évènementielle", Toast.LENGTH_SHORT).show();
                                originDemand.setText(villagers.get(i).getName());
                                description.setText(quests.get(j).getDescription());
                                goals.setText("Nothing for the moment because we did not set a variable with a text for this ahah :D");
                                rewards.setText("XP : " + quests.get(j).getRewardXp() + "\nMoney : " + quests.get(j).getRewardMoney());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
