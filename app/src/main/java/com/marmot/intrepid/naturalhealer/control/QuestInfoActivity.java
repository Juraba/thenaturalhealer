package com.marmot.intrepid.naturalhealer.control;

import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marmot.intrepid.naturalhealer.R;
import com.marmot.intrepid.naturalhealer.model.Player;
import com.marmot.intrepid.naturalhealer.model.Quest;
import com.marmot.intrepid.naturalhealer.model.Villager;
import com.marmot.intrepid.naturalhealer.model.enumerations.QuestType;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.ArrayList;
import java.util.Map;

public class QuestInfoActivity extends AppCompatActivity {

    private GameService game;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_info);

        game = GameService.getInstance();
        player = game.getPlayer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView originDemand = (TextView) findViewById(R.id.originDemand);
        TextView description = (TextView) findViewById(R.id.description);
        TextView goals = (TextView) findViewById(R.id.goals);
        TextView rewards = (TextView) findViewById(R.id.reward);

        ImageView villager = (ImageView) findViewById(R.id.villager);

        Button acceptOrGiveUp = (Button) findViewById(R.id.acceptOrGiveUp);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.get("quest") != null) {

                if (bundle.get("surrender") != null) {
                    acceptOrGiveUp.setVisibility(View.VISIBLE);
                    acceptOrGiveUp.setText("SURRENDER QUEST");
                }
                if (bundle.get("accept") != null) {
                    acceptOrGiveUp.setVisibility(View.VISIBLE);
                    acceptOrGiveUp.setText("ACCEPT QUEST");
                }

                setTitle("Quest : " + bundle.get("quest").toString());

                Quest qNotFinal = null;
                Villager vNotFinal = null;

                ArrayList<Villager> villagers = game.getVillagers();
                for (int i = 0; i < villagers.size(); i++) {
                    final ArrayList<Quest> quests = villagers.get(i).getQuests();
                    for (int j = 0; j < quests.size(); j++) {
                        if (quests.get(j).getName().equals(bundle.get("quest").toString())) {
                            if (quests.get(j).getType() == QuestType.MAIN) {
                                int img = getApplicationContext().getResources().getIdentifier(villagers.get(i).getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                villager.setImageResource(img);

                                originDemand.setText(villagers.get(i).getName());
                                description.setText(quests.get(j).getDescription());
                                goals.setText("Nothing for the moment because we did not set a variable with a text for this ahah :D");
                                rewards.setText("XP : " + quests.get(j).getRewardXp() + "\nMoney : " + quests.get(j).getRewardMoney());

                                qNotFinal = quests.get(j);
                                vNotFinal = villagers.get(i);
                            }
                            else if (quests.get(j).getType() == QuestType.DAILY) {
                                int img = getApplicationContext().getResources().getIdentifier(villagers.get(i).getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                villager.setImageResource(img);

                                originDemand.setText(villagers.get(i).getName());
                                description.setText(quests.get(j).getDescription());
                                goals.setText("Nothing for the moment because we did not set a variable with a text for this ahah :D");
                                rewards.setText("XP : " + quests.get(j).getRewardXp() + "\nMoney : " + quests.get(j).getRewardMoney());

                                qNotFinal = quests.get(j);
                                vNotFinal = villagers.get(i);
                            }
                            else if (quests.get(j).getType() == QuestType.EVENT) {
                                int img = getApplicationContext().getResources().getIdentifier(villagers.get(i).getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                villager.setImageResource(img);

                                originDemand.setText(villagers.get(i).getName());
                                description.setText(quests.get(j).getDescription());
                                goals.setText("Nothing for the moment because we did not set a variable with a text for this ahah :D");
                                rewards.setText("XP : " + quests.get(j).getRewardXp() + "\nMoney : " + quests.get(j).getRewardMoney());

                                qNotFinal = quests.get(j);
                                vNotFinal = villagers.get(i);
                            }
                        }
                    }
                }

                final Quest q = qNotFinal;
                final Villager v = vNotFinal;

                for (Map.Entry<String, Quest> quest : player.getQuests().entrySet()) {
                    String key = quest.getKey();
                    Quest val = quest.getValue();

                    System.out.println(key + "'s Quest : " + val.getName());
                }

                if (bundle.get("surrender") != null) {
                    acceptOrGiveUp.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            player.cancelQuest(q);
                            Toast.makeText(view.getContext(), "Quest surrendered !", Toast.LENGTH_SHORT).show();

                            for (Map.Entry<String, Quest> quest : player.getQuests().entrySet()) {
                                String key = quest.getKey();
                                Quest val = quest.getValue();

                                System.out.println(key + "'s Quest : " + val.getName());
                            }

                            MainActivity.quickSave();
                        }
                    });
                }
                if (bundle.get("accept") != null) {
                    acceptOrGiveUp.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {

                            boolean check = false;

                            for (Map.Entry<String, Quest> i : player.getQuests().entrySet()) {
                                String key = i.getKey();
                                Quest val = i.getValue();

                                if (key.equals(v.getName())) {
                                    check = true;
                                }
                            }

                            if (!check) {
                                player.acceptQuest(q, v.getName());
                                Toast.makeText(view.getContext(), "Quest accepted !", Toast.LENGTH_SHORT).show();
                                MainActivity.quickSave();
                            } else {
                                Toast.makeText(view.getContext(), "You already have accepted a quest from this villager !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
