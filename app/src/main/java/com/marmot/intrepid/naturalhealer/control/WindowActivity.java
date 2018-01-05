package com.marmot.intrepid.naturalhealer.control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.marmot.intrepid.naturalhealer.R;
import com.marmot.intrepid.naturalhealer.model.Herb;
import com.marmot.intrepid.naturalhealer.model.Item;
import com.marmot.intrepid.naturalhealer.model.OtherIngredients;
import com.marmot.intrepid.naturalhealer.model.Recipe;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WindowActivity extends AppCompatActivity {

    private GameService game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);

        game = GameService.getInstance();

        final HashMap<Item, Integer> inventory = game.getPlayer().getInventory();

        final ArrayList<Herb> h = game.getShop().getHerbs();
        final ArrayList<Recipe> r = game.getShop().getRecipes();
        final ArrayList<OtherIngredients> o = game.getShop().getOtherIngredients();

        final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ResourceType") final View windowView = inflater.inflate(R.layout.popup_layout, (ViewGroup) findViewById(R.layout.activity_item_info));
        final View view = inflater.inflate(R.layout.activity_window, null);
        final PopupWindow window = new PopupWindow(windowView, 0, 0, true);
        window.setAnimationStyle(android.R.anim.fade_in);

        //if onclick written here, it gives null pointer exception.
        final Button confirm = (Button) windowView.findViewById(R.id.confirm);
        final ImageView itemPicture = (ImageView) windowView.findViewById(R.id.itemPicture);
        final TextView itemName = (TextView) windowView.findViewById(R.id.itemName);
        final NumberPicker number = (NumberPicker) windowView.findViewById(R.id.number);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.get("sell") != null) {

                final String item = bundle.get("sell").toString();

                confirm.setText("SELL");

                int img1 = 0;
                double itemPrice = 0;
                Item itemSold = null;
                for (int i = 0; i < h.size(); i++) {
                    if (h.get(i).getPicName().equals(item)) {
                        for (Map.Entry<Item, Integer> mapItem : inventory.entrySet()) {
                            Item key = mapItem.getKey();

                            if (h.get(i).getName() == key.getName()) {
                                itemSold = h.get(i);

                                img1 = getApplicationContext().getResources().getIdentifier(h.get(i).getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                itemName.setText(h.get(i).getName());

                                itemPrice = h.get(i).getPrice();
                            }
                        }
                    }
                }
                for (int i = 0; i < r.size(); i++) {
                    if (r.get(i).getPicName().equals(item)) {
                        for (Map.Entry<Item, Integer> mapItem : inventory.entrySet()) {
                            Item key = mapItem.getKey();

                            if (r.get(i).getName() == key.getName()) {
                                itemSold = r.get(i);

                                img1 = getApplicationContext().getResources().getIdentifier(r.get(i).getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                itemName.setText(r.get(i).getName());

                                itemPrice = r.get(i).getPrice();
                            }
                        }
                    }
                }
                for (int i = 0; i < o.size(); i++) {
                    if (o.get(i).getPicName().equals(item)) {
                        for (Map.Entry<Item, Integer> mapItem : inventory.entrySet()) {
                            Item key = mapItem.getKey();

                            if (o.get(i).getName() == key.getName()) {
                                itemSold = o.get(i);

                                img1 = getApplicationContext().getResources().getIdentifier(o.get(i).getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                itemName.setText(o.get(i).getName());

                                itemPrice = o.get(i).getPrice();
                            }
                        }
                    }
                }

                final double finalItemPrice = itemPrice;
                final Item finalItemSold = itemSold;

                itemPicture.setImageResource(img1);

                windowView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.AT_MOST), View.MeasureSpec.UNSPECIFIED);
                window.setWidth(windowView.getMeasuredWidth()*2);
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
            }
        }
    }
}

