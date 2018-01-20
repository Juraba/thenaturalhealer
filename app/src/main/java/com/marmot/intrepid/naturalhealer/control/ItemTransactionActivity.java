package com.marmot.intrepid.naturalhealer.control;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
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

public class ItemTransactionActivity extends AppCompatActivity {

    private GameService game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_transaction);

        game = GameService.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final HashMap<Item, Integer> inventory = game.getPlayer().getInventory();

        /*
        final ArrayList<Herb> h = game.getShop().getHerbs();
        final ArrayList<Recipe> r = game.getShop().getRecipes();
        final ArrayList<OtherIngredients> o = game.getShop().getOtherIngredients();
        */

        final ArrayList<Item> items = game.getItems();

        if ((inventory != null) && (game.getShop() != null)) {

            // WINDOW POPUP
            /*
            final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ResourceType") final View windowView = inflater.inflate(R.layout.popup_transaction_layout, (ViewGroup) findViewById(R.layout.activity_item_info));
            final View view = inflater.inflate(R.layout.activity_window, null);
            final PopupWindow window = new PopupWindow(windowView, 0, 0, true);
            window.setAnimationStyle(android.R.anim.fade_in);

            //if onclick written here, it gives null pointer exception.
            final Button confirm = (Button) windowView.findViewById(R.id.confirm);
            final ImageView itemPicture = (ImageView) windowView.findViewById(R.id.itemPicture);
            final TextView itemName = (TextView) windowView.findViewById(R.id.itemName);
            final NumberPicker number = (NumberPicker) windowView.findViewById(R.id.number);
            */

            Button confirm = (Button) findViewById(R.id.confirm);
            confirm.setVisibility(View.VISIBLE);
            ImageView itemPicture = (ImageView) findViewById(R.id.itemPicture);
            itemPicture.setVisibility(View.VISIBLE);
            final TextView itemName = (TextView) findViewById(R.id.itemName);
            itemName.setVisibility(View.VISIBLE);
            final NumberPicker number = (NumberPicker) findViewById(R.id.number);

            number.setMaxValue(100);
            number.setMinValue(0);
            number.setValue(5);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                if (bundle.get("sell") != null) {

                    final String item = bundle.get("sell").toString();

                    confirm.setText("SELL");

                    System.out.println("BUNDLE GET SELL : " + item);

                    int img = 0;
                    double itemPrice = 0;
                    Item itemSold = null;

                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getName().equals(item)) {
                            if (items.get(i).getClass() == Herb.class) {

                                Toast.makeText(getApplicationContext(), "HERB", Toast.LENGTH_LONG).show();

                                Herb h = (Herb) items.get(i);

                                img = getApplicationContext().getResources().getIdentifier(h.getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                itemName.setText(h.getName());

                                itemSold = h;
                                itemPrice = h.getPrice();
                            } else if (items.get(i).getClass() == Recipe.class) {

                                Toast.makeText(getApplicationContext(), "RECIPE", Toast.LENGTH_LONG).show();

                                Recipe r = (Recipe) items.get(i);

                                img = getApplicationContext().getResources().getIdentifier(r.getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                itemName.setText(r.getName());

                                itemSold = r;
                                itemPrice = r.getPrice();
                            } else if (items.get(i).getClass() == OtherIngredients.class) {

                                Toast.makeText(getApplicationContext(), "OTHER", Toast.LENGTH_LONG).show();

                                OtherIngredients o = (OtherIngredients) items.get(i);

                                img = getApplicationContext().getResources().getIdentifier(o.getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                itemName.setText(o.getName());

                                itemSold = o;
                                itemPrice = o.getPrice();
                            }
                        }
                    }

                    final double finalItemPrice = itemPrice;
                    final Item finalItemSold = itemSold;

                    itemPicture.setImageResource(img);

                /*
                windowView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.AT_MOST), View.MeasureSpec.UNSPECIFIED);
                window.setWidth(windowView.getMeasuredWidth()*2);
                window.setHeight(windowView.getMeasuredHeight());
                window.showAtLocation(view, Gravity.CENTER, 0, 0);
                */

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

                                                if (snackRender[0].equals("")) {
                                                    snackRender[0] = snackRender[0] + "You sold " + numberSold + " " + itemName.getText() + " for " + price + "$";
                                                    Toast.makeText(getApplicationContext(), snackRender[0], Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), snackRender[0], Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            snackRender[0] = "You canceled the item selling";
                                            Toast.makeText(getApplicationContext(), snackRender[0], Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .show();

                            MainActivity.quickSave();

                            /*
                            Snackbar.make(v, snackRender[0], Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                                    */
                        }
                    });
                }
                else if (bundle.get("buy").toString() != null) {

                    final String item = bundle.get("buy").toString();

                    confirm.setText("BUY");

                    int img = 0;
                    double itemPrice = 0;
                    Item itemSold = null;

                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getName().equals(item)) {
                            if (items.get(i).getClass() == Herb.class) {

                                Toast.makeText(getApplicationContext(), "HERB", Toast.LENGTH_LONG).show();

                                Herb h = (Herb) items.get(i);

                                img = getApplicationContext().getResources().getIdentifier(h.getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                itemName.setText(h.getName());

                                itemSold = h;
                                itemPrice = h.getPrice();
                            } else if (items.get(i).getClass() == Recipe.class) {

                                Toast.makeText(getApplicationContext(), "RECIPE", Toast.LENGTH_LONG).show();

                                Recipe r = (Recipe) items.get(i);

                                img = getApplicationContext().getResources().getIdentifier(r.getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                itemName.setText(r.getName());

                                itemSold = r;
                                itemPrice = r.getPrice();
                            } else if (items.get(i).getClass() == OtherIngredients.class) {

                                Toast.makeText(getApplicationContext(), "OTHER", Toast.LENGTH_LONG).show();

                                OtherIngredients o = (OtherIngredients) items.get(i);

                                img = getApplicationContext().getResources().getIdentifier(o.getPicName(), "mipmap", getLayoutInflater().getContext().getPackageName());
                                itemName.setText(o.getName());

                                itemSold = o;
                                itemPrice = o.getPrice();
                            }
                        }
                    }

                    final double finalItemPrice = itemPrice;
                    final Item finalItemSold = itemSold;

                    itemPicture.setImageResource(img);

                    /*
                    windowView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.AT_MOST), View.MeasureSpec.UNSPECIFIED);
                    window.setWidth(windowView.getMeasuredWidth());
                    window.setHeight(windowView.getMeasuredHeight());
                    window.showAtLocation(view, Gravity.CENTER, 0, 0);
                    */

                    confirm.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            final String[] snackRender = {""};

                            final int numberSold = number.getValue();

                            System.out.println("NUMBER BUOUGHT : " + numberSold + " // FINAL ITEM BOUGHT : " + finalItemSold);

                            new AlertDialog.Builder(v.getContext())
                                    .setMessage("Are you sure you want to buy this item ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            if (numberSold == 0) {
                                                Toast.makeText(getApplicationContext(), "You're not buying any item !", Toast.LENGTH_SHORT).show();
                                            } else {
                                                double price = numberSold * finalItemPrice;

                                                snackRender[0] = game.getPlayer().buyItems(finalItemSold ,numberSold);

                                                if (snackRender[0].equals("")) {
                                                    snackRender[0] = snackRender[0] + "You bought " + numberSold + " " + itemName.getText() + " for " + price + "$";
                                                    Toast.makeText(getApplicationContext(), snackRender[0], Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), snackRender[0], Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            snackRender[0] = "You canceled the item buying";
                                            Toast.makeText(getApplicationContext(), snackRender[0], Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .show();

                            MainActivity.quickSave();

                            /*
                            Snackbar.make(v, snackRender[0], Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
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
