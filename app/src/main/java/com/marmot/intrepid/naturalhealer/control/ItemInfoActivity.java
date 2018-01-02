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
import com.marmot.intrepid.naturalhealer.model.Herb;
import com.marmot.intrepid.naturalhealer.model.OtherIngredients;
import com.marmot.intrepid.naturalhealer.model.Quest;
import com.marmot.intrepid.naturalhealer.model.Recipe;
import com.marmot.intrepid.naturalhealer.model.Villager;
import com.marmot.intrepid.naturalhealer.model.enumerations.QuestType;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.ArrayList;

public class ItemInfoActivity extends AppCompatActivity {

    private GameService game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        game = GameService.getInstance();

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView name = (TextView) findViewById(R.id.name);
        TextView basicInfos = (TextView) findViewById(R.id.basicInfos);
        TextView description = (TextView) findViewById(R.id.description);
        TextView properties = (TextView) findViewById(R.id.properties);
        TextView combiOrSymp = (TextView) findViewById(R.id.combiOrSymp);
        TextView combiOrSympTitle = (TextView) findViewById(R.id.combiOrSympTitle);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.get("herb") != null) {

                String herb = bundle.get("herb").toString();

                setTitle("Herb : " + herb);

                ArrayList<Herb> herbs = game.getGrimoire().getHerbs();
                for (int i = 0; i < herbs.size(); i++) {
                    if (herbs.get(i).getName().equals(herb)) {
                        name.setText(herbs.get(i).getName());
                        basicInfos.setText(herbs.get(i).getType().getEn() + " / " + herbs.get(i).getRace() + " / " + herbs.get(i).getRarity().getEn());
                        description.setText(herbs.get(i).getDescription());
                        properties.setText(herbs.get(i).getProperties());
                        combiOrSympTitle.setText("COMBINATIONS");
                        combiOrSymp.setText(herbs.get(i).getCombination());
                    }
                }
            }
            if (bundle.get("recipe") != null) {

                String recipe = bundle.get("recipe").toString();

                setTitle("Recipe : " + recipe);

                ArrayList<Recipe> recipes = game.getGrimoire().getRecipes();
                for (int i = 0; i < recipes.size(); i++) {
                    if (recipes.get(i).getName().equals(recipe)) {
                        name.setText(recipes.get(i).getName());
                        String str = "";
                        for (int j = 0; j < recipes.get(i).getSymptoms().length; j++) {
                            if (j == 0) {
                                str = "- " + recipes.get(i).getSymptoms()[j].getEn();
                            }
                            else {
                                str += "\n- " + recipes.get(i).getSymptoms()[j].getEn();
                            }
                        }
                        basicInfos.setText(recipes.get(i).getDifficulty().getEn());
                        description.setText(recipes.get(i).getDescription());
                        properties.setText(recipes.get(i).getProperties());
                        combiOrSympTitle.setText("SYMPTOMS");
                        combiOrSymp.setText(str);
                    }
                }
            }
            if (bundle.get("other") != null) {

                String other = bundle.get("other").toString();

                setTitle("Ingredient : " + other);

                ArrayList<OtherIngredients> others = game.getGrimoire().getOtherIngredients();
                for (int i = 0; i < others.size(); i++) {
                    if (others.get(i).getName().equals(other)) {
                        name.setText(others.get(i).getName());
                        basicInfos.setVisibility(View.INVISIBLE);
                        description.setText(others.get(i).getDescription());
                        properties.setText(others.get(i).getProperties());
                        combiOrSympTitle.setVisibility(View.INVISIBLE);
                        combiOrSymp.setVisibility(View.INVISIBLE);
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