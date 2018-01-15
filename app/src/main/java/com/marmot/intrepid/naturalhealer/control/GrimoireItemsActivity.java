package com.marmot.intrepid.naturalhealer.control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.marmot.intrepid.naturalhealer.R;
import com.marmot.intrepid.naturalhealer.model.Herb;
import com.marmot.intrepid.naturalhealer.model.OtherIngredients;
import com.marmot.intrepid.naturalhealer.model.Quest;
import com.marmot.intrepid.naturalhealer.model.Recipe;
import com.marmot.intrepid.naturalhealer.model.Villager;
import com.marmot.intrepid.naturalhealer.model.enumerations.QuestType;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.ArrayList;

public class GrimoireItemsActivity extends AppCompatActivity {

    private GameService game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grimoire_items);

        game = GameService.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ListView list = (ListView) findViewById(R.id.items);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.get("herb") != null) {

                String type = bundle.get("herb").toString();

                ArrayList<Herb> herbs = game.getGrimoire().getHerbs();
                ArrayList<String> herbList = new ArrayList<String>();
                for (int i = 0; i < herbs.size(); i++) {
                    if (herbs.get(i).getType().getEn().equals(type))
                    herbList.add(herbs.get(i).getName());
                }

                setTitle(type);
                list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, herbList));

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Object obj = list.getAdapter().getItem(position);
                        String value = obj.toString();

                        Intent intentItemInfo = new Intent(getApplicationContext(), ItemInfoActivity.class);
                        intentItemInfo.putExtra("herb", value);
                        startActivity(intentItemInfo);
                    }
                });


            }
            if (bundle.get("recipe") != null) {

                String symptom = bundle.get("recipe").toString();

                ArrayList<Recipe> recipes = game.getGrimoire().getRecipes();
                ArrayList<String> recipeList = new ArrayList<String>();
                for (int i = 0; i < recipes.size(); i++) {
                    for (int j = 0; j < recipes.get(i).getSymptoms().length; j++) {
                        if (recipes.get(i).getSymptoms()[j].getEn().equals(symptom)) {
                            recipeList.add(recipes.get(i).getName());
                        }
                    }
                }

                setTitle(symptom);
                list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recipeList));

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Object obj = list.getAdapter().getItem(position);
                        String value = obj.toString();

                        Intent intentItemInfo = new Intent(getApplicationContext(), ItemInfoActivity.class);
                        intentItemInfo.putExtra("recipe", value);
                        startActivity(intentItemInfo);
                    }
                });

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
