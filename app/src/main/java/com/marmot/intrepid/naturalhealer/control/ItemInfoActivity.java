package com.marmot.intrepid.naturalhealer.control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.media.tv.TvContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.marmot.intrepid.naturalhealer.R;
import com.marmot.intrepid.naturalhealer.data.Inventory;
import com.marmot.intrepid.naturalhealer.model.Herb;
import com.marmot.intrepid.naturalhealer.model.Item;
import com.marmot.intrepid.naturalhealer.model.OtherIngredients;
import com.marmot.intrepid.naturalhealer.model.Recipe;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemInfoActivity extends AppCompatActivity {

    private GameService game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        game = GameService.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ImageView picture = (ImageView) findViewById(R.id.picture);

        TextView name = (TextView) findViewById(R.id.name);
        TextView basicInfos = (TextView) findViewById(R.id.basicInfos);
        TextView description = (TextView) findViewById(R.id.description);
        TextView properties = (TextView) findViewById(R.id.properties);
        TextView combiOrSymp = (TextView) findViewById(R.id.combiOrSymp);
        TextView combiOrSympTitle = (TextView) findViewById(R.id.combiOrSympTitle);
        TextView quantity = (TextView) findViewById(R.id.quantity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.get("herb") != null) {

                String herb = bundle.get("herb").toString();

                setTitle("Herb : " + herb);

                ArrayList<Herb> herbs = game.getGrimoire().getHerbs();
                for (int i = 0; i < herbs.size(); i++) {
                    if (herbs.get(i).getName().equals(herb)) {
                        int img = getApplicationContext().getResources().getIdentifier(herbs.get(i).getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                        picture.setImageResource(img);
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
                        int img = getApplicationContext().getResources().getIdentifier(recipes.get(i).getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                        picture.setImageResource(img);
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
                        int img = getApplicationContext().getResources().getIdentifier(others.get(i).getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                        picture.setImageResource(img);
                        name.setText(others.get(i).getName());
                        basicInfos.setVisibility(View.INVISIBLE);
                        description.setText(others.get(i).getDescription());
                        properties.setText(others.get(i).getProperties());
                        combiOrSympTitle.setVisibility(View.INVISIBLE);
                        combiOrSymp.setVisibility(View.INVISIBLE);
                    }
                }
            }
            if (bundle.get("item") != null) {

                String item = bundle.get("item").toString();

                final ArrayList<Item> items = game.getItems();
                String itemName = "";

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getPicName().equals(item)) {
                        if (items.get(i).getClass() == Herb.class) {

                            Herb h = (Herb) items.get(i);
                            setTitle("Item : " + h.getName());
                            itemName = h.getName();

                            int img = getApplicationContext().getResources().getIdentifier(h.getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                            picture.setImageResource(img);
                            name.setText(h.getName());
                            basicInfos.setText(h.getType().getEn() + " / " + h.getRace() + " / " + h.getRarity().getEn());
                            description.setText(h.getDescription());
                            properties.setText(h.getProperties());
                            combiOrSympTitle.setText("COMBINATIONS");
                            combiOrSymp.setText(h.getCombination());
                        } else if (items.get(i).getClass() == Recipe.class) {

                            Recipe r = (Recipe) items.get(i);
                            setTitle("Item : " + r.getName());
                            itemName = r.getName();

                            int img = getApplicationContext().getResources().getIdentifier(r.getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                            picture.setImageResource(img);
                            name.setText(r.getName());
                            String str = "";
                            for (int j = 0; j < r.getSymptoms().length; j++) {
                                if (j == 0) {
                                    str = "- " + r.getSymptoms()[j].getEn();
                                }
                                else {
                                    str += "\n- " + r.getSymptoms()[j].getEn();
                                }
                            }
                            basicInfos.setText(r.getDifficulty().getEn());
                            description.setText(r.getDescription());
                            properties.setText(r.getProperties());
                            combiOrSympTitle.setText("SYMPTOMS");
                            combiOrSymp.setText(str);
                        } else if (items.get(i).getClass() == OtherIngredients.class) {

                            OtherIngredients o = (OtherIngredients) items.get(i);
                            setTitle("Item : " + o.getName());
                            itemName = o.getName();

                            int img = getApplicationContext().getResources().getIdentifier(o.getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                            picture.setImageResource(img);
                            name.setText(o.getName());
                            basicInfos.setVisibility(View.INVISIBLE);
                            description.setText(o.getDescription());
                            properties.setText(o.getProperties());
                            combiOrSympTitle.setVisibility(View.INVISIBLE);
                            combiOrSymp.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                final String render = itemName;

                for(HashMap.Entry<Item, Integer> entry : game.getPlayer().getInventory().entrySet()){
                    if(entry.getKey().getName().equals(itemName)){
                        quantity.setText(Integer.toString(entry.getValue()));
                    }
                    else {
                        quantity.setText("La ressource n'est pas présente dans l'inventaire.");
                    }
                }

                final Button buyOrSell = (Button) findViewById(R.id.buyOrSell);

                // WINDOW POPUP
                /*
                final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                @SuppressLint("ResourceType") final View windowView = inflater.inflate(R.layout.popup_transaction_layout, (ViewGroup) findViewById(R.layout.activity_item_info));
                final PopupWindow window = new PopupWindow(windowView, 0, 0, true);
                window.setAnimationStyle(android.R.anim.fade_in);

                //if onclick written here, it gives null pointer exception.
                final Button confirm = (Button) windowView.findViewById(R.id.confirm);
                final ImageView itemPicture = (ImageView) windowView.findViewById(R.id.itemPicture);
                final TextView itemName = (TextView) windowView.findViewById(R.id.itemName);
                final NumberPicker number = (NumberPicker) windowView.findViewById(R.id.number);
                */

                if (bundle.get("inventory") != null) {
                    buyOrSell.setVisibility(View.VISIBLE);
                    buyOrSell.setText("SELL");
                    buyOrSell.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            System.out.println("ITEM VALUE AFTER AFTER : " + render);

                            Intent intentList = new Intent(getApplicationContext(), ItemTransactionActivity.class);
                            intentList.putExtra("sell", render);
                            startActivity(intentList);

                            // WINDOW POPUP
                            /*
                            int img = 0;
                            double itemPrice = 0;
                            Item itemSold = null;
                            for (int i = 0; i < h.size(); i++) {
                                if (h.get(i).getPicName().equals(item)) {
                                    for (Map.Entry<Item, Integer> item : inventory.entrySet()) {
                                        Item key = item.getKey();

                                        if (h.get(i).getName() == key.getName()) {
                                            itemSold = h.get(i);

                                            img = getApplicationContext().getResources().getIdentifier(h.get(i).getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                            itemName.setText(h.get(i).getName());

                                            itemPrice = h.get(i).getPrice();
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < r.size(); i++) {
                                if (r.get(i).getPicName().equals(item)) {
                                    for (Map.Entry<Item, Integer> item : inventory.entrySet()) {
                                        Item key = item.getKey();

                                        if (r.get(i).getName() == key.getName()) {
                                            itemSold = r.get(i);

                                            img = getApplicationContext().getResources().getIdentifier(r.get(i).getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                            itemName.setText(r.get(i).getName());

                                            itemPrice = r.get(i).getPrice();
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < o.size(); i++) {
                                if (o.get(i).getPicName().equals(item)) {
                                    for (Map.Entry<Item, Integer> item : inventory.entrySet()) {
                                        Item key = item.getKey();

                                        if (o.get(i).getName() == key.getName()) {
                                            itemSold = o.get(i);

                                            img = getApplicationContext().getResources().getIdentifier(o.get(i).getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                            itemName.setText(o.get(i).getName());

                                            itemPrice = o.get(i).getPrice();
                                        }
                                    }
                                }
                            }

                            final double finalItemPrice = itemPrice;
                            final Item finalItemSold = itemSold;

                            itemPicture.setImageResource(img);

                            windowView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.AT_MOST), View.MeasureSpec.UNSPECIFIED);
                            window.setWidth(windowView.getMeasuredWidth());
                            window.setHeight(windowView.getMeasuredHeight());
                            window.showAtLocation(view, Gravity.CENTER, 0, 0);

                            confirm.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    final String[] snackRender = {""};

                                    final int numberSold = number.getValue();

                                    new AlertDialog.Builder(v.getContext())
                                            .setMessage("Are you sure you want to sell this item ?")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    if (numberSold == 0) {
                                                        Toast.makeText(getApplicationContext(), "You're not selling any item !", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        double price = numberSold * finalItemPrice;

                                                        snackRender[0] = game.getPlayer().sellItems(finalItemSold ,numberSold);

                                                        if (snackRender[0] != "") {
                                                            snackRender[0] = snackRender[0] + "You sold " + numberSold + " " + itemName.getText() + " for " + price + "$";
                                                        }
                                                    }
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    snackRender[0] = "You canceled the item selling";
                                                }
                                            })
                                            .show();

                                    Snackbar.make(v, snackRender[0], Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            });
                            */
                        }
                    });
                }

                if (bundle.get("shop") != null) {
                    //buyOrSell = new Button(this);
                    //Button buyOrSell = (Button) findViewById(R.id.buyOrSell);

                    buyOrSell.setVisibility(View.VISIBLE);
                    buyOrSell.setText("BUY");
                    buyOrSell.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intentList = new Intent(getApplicationContext(), ItemTransactionActivity.class);
                            intentList.putExtra("buy", render);
                            startActivity(intentList);

                            // WINDOW POPUP
                            /*
                            int img = 0;
                            double itemPrice = 0;
                            Item itemSold = null;
                            for (int i = 0; i < h.size(); i++) {
                                if (h.get(i).getPicName().equals(item)) {
                                    for (Map.Entry<Item, Integer> item : inventory.entrySet()) {
                                        Item key = item.getKey();

                                        if (h.get(i).getName() == key.getName()) {
                                            itemSold = h.get(i);

                                            img = getApplicationContext().getResources().getIdentifier(h.get(i).getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                            itemName.setText(h.get(i).getName());

                                            itemPrice = h.get(i).getPrice();
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < r.size(); i++) {
                                if (r.get(i).getPicName().equals(item)) {
                                    for (Map.Entry<Item, Integer> item : inventory.entrySet()) {
                                        Item key = item.getKey();

                                        if (r.get(i).getName() == key.getName()) {
                                            itemSold = r.get(i);

                                            img = getApplicationContext().getResources().getIdentifier(r.get(i).getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                            itemName.setText(r.get(i).getName());

                                            itemPrice = r.get(i).getPrice();
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < o.size(); i++) {
                                if (o.get(i).getPicName().equals(item)) {
                                    for (Map.Entry<Item, Integer> item : inventory.entrySet()) {
                                        Item key = item.getKey();

                                        if (o.get(i).getName() == key.getName()) {
                                            itemSold = o.get(i);

                                            img = getApplicationContext().getResources().getIdentifier(o.get(i).getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                            itemName.setText(o.get(i).getName());

                                            itemPrice = o.get(i).getPrice();
                                        }
                                    }
                                }
                            }

                            final double finalItemPrice = itemPrice;
                            final Item finalItemSold = itemSold;

                            itemPicture.setImageResource(img);

                            windowView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.AT_MOST), View.MeasureSpec.UNSPECIFIED);
                            window.setWidth(windowView.getMeasuredWidth());
                            window.setHeight(windowView.getMeasuredHeight());
                            window.showAtLocation(view, Gravity.CENTER, 0, 0);

                            confirm.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    final String[] snackRender = {""};

                                    final int numberSold = number.getValue();

                                    new AlertDialog.Builder(v.getContext())
                                            .setMessage("Are you sure you want to sell this item ?")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    if (numberSold == 0) {
                                                        Toast.makeText(getApplicationContext(), "You're not buying any item !", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        double price = numberSold * finalItemPrice;

                                                        snackRender[0] = game.getPlayer().buyItems(finalItemSold ,numberSold);

                                                        if (snackRender[0] != "") {
                                                            snackRender[0] = snackRender[0] + "You bought " + numberSold + " " + itemName.getText() + " for " + price + "$";
                                                        }
                                                    }
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    snackRender[0] = "You canceled the item selling";
                                                }
                                            })
                                            .show();

                                    Snackbar.make(v, snackRender[0], Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            });
                            */
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