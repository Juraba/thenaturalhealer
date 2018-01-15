package com.marmot.intrepid.naturalhealer.control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.marmot.intrepid.naturalhealer.R;
import com.marmot.intrepid.naturalhealer.model.Quest;
import com.marmot.intrepid.naturalhealer.model.Villager;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.ArrayList;

public class VillagerQuestsActivity extends AppCompatActivity {

    private GameService game;
    private ArrayList<Villager> villagers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_villager_quests);

        game = GameService.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ListView questsList = (ListView) findViewById(R.id.quests);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.get("villager") != null) {

                String villagerName = bundle.get("villager").toString();

                setTitle(bundle.get("villager").toString() + "'s Quests");

                villagers = game.getVillagers();

                if (villagers != null) {
                    ArrayList<Quest> quests = new ArrayList<Quest>();
                    for (int i = 0; i < villagers.size(); i ++) {
                        if (villagers.get(i).getName().equals(villagerName)) {
                            quests = villagers.get(i).getQuests();
                        }
                    }

                    ArrayList<String> questsName = new ArrayList<String>();
                    for (int i = 0; i < quests.size(); i++) {
                        questsName.add(quests.get(i).getName());
                    }

                    questsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, questsName));

                    questsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Object obj = questsList.getAdapter().getItem(position);
                            String value = obj.toString();

                            Intent intent = new Intent(getApplicationContext(), QuestInfoActivity.class);
                            intent.putExtra("quest", value);
                            intent.putExtra("accept", "accept");
                            startActivity(intent);
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
