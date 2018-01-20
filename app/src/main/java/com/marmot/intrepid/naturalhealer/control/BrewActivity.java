package com.marmot.intrepid.naturalhealer.control;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SearchView;

import com.marmot.intrepid.naturalhealer.R;
import com.marmot.intrepid.naturalhealer.model.Item;
import com.marmot.intrepid.naturalhealer.model.Recipe;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrewActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private GameService game;
    private HashMap<Item, Integer> inventory;
    private InventoryBrewAdapter brewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew);

        game = GameService.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SearchView searchItem = (SearchView) findViewById(R.id.searchItem);
        searchItem.setQueryHint("Item search to brew...");
        final ListView resultList = (ListView) findViewById(R.id.resultList);
        Button brewList = (Button) findViewById(R.id.brewList);
        Button brew = (Button) findViewById(R.id.brew);

        inventory = game.getPlayer().getInventory();

        final ArrayList<String> names = new ArrayList<String>();
        final ArrayList<String> numbers = new ArrayList<String>();
        final ArrayList<String> pictures = new ArrayList<String>();

        final BrewListAdapter[] arrayAdapter = {new BrewListAdapter(getApplicationContext(), names, numbers, pictures)};

        final ArrayList<String> itemNames = new ArrayList<String>();

        for (Map.Entry<Item, Integer> i : inventory.entrySet()) {
            Item item = i.getKey();

            if (item.getClass() != Recipe.class) {
                if (item.getName().startsWith(searchItem.getQuery().toString())) {
                    itemNames.add(item.getName());
                }
            }
        }

        brewAdapter = new InventoryBrewAdapter(getApplicationContext(), itemNames);
        resultList.setAdapter(brewAdapter);

        searchItem.setOnQueryTextListener(this);

        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = resultList.getAdapter().getItem(position);
                String value = obj.toString();

                final String[] itemNumber = {""};
                String itemName = "";
                String itemPicture = "";
                int itemInventoryNumber = 0;

                for (Map.Entry<Item, Integer> i : inventory.entrySet()) {
                    Item item = i.getKey();
                    int number = i.getValue();

                    if (item.getName().startsWith(value)) {
                        itemName = item.getName();
                        itemPicture = item.getPicName();
                        itemInventoryNumber = number;
                    }
                }

                final int numberMax = itemInventoryNumber;

                ConstraintLayout constLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_item_transaction, null);
                final NumberPicker number = (NumberPicker) constLayout.findViewById(R.id.number);
                number.setMaxValue(100);
                number.setMinValue(0);
                number.setValue(5);

                final String finalItemName = itemName;
                final String finalItemPicture = itemPicture;
                new AlertDialog.Builder(BrewActivity.this)
                        .setView(constLayout)
                        .setMessage("How many do you want ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int nbChosen = number.getValue();

                                System.out.println("VALEUR DE NBCHOSE, LE NUMBERPICKER : " + nbChosen);

                                if (nbChosen <= numberMax) {
                                    itemNumber[0] = String.valueOf(nbChosen);
                                    System.out.println("VALEUR DE ITEMNUMBER APRES VERIF AVEC NBCHOSE : " + itemNumber[0]);
                                } else {
                                    itemNumber[0] = "0";
                                }

                                if (!itemNumber[0].equals("0")) {

                                    System.out.println("===== AFFICHAGE AVANT AJOUT DANS BREW LIST =====");
                                    System.out.println("Name : " + finalItemName);
                                    System.out.println("Nombre : " + itemNumber[0]);
                                    System.out.println("Picture : " + finalItemPicture);

                                    boolean check = false;
                                    int index = 0;
                                    for (int i = 0; i < names.size(); i++) {
                                        if (names.get(i).equals(finalItemName)) {
                                            check = true;
                                            index = i;
                                        }
                                    }

                                    if (!check) {
                                        names.add(finalItemName);
                                        numbers.add(itemNumber[0]);
                                        pictures.add(finalItemPicture);
                                    } else {
                                        if (index > 0) {
                                            int tmp = Integer.parseInt(numbers.get(index)) + Integer.parseInt(itemNumber[0]);
                                            if (tmp <= numberMax) {
                                                System.out.println("VALEUR A RAJOUTER DANS NUMBERS : " + tmp);
                                                numbers.add(index, String.valueOf(tmp));
                                            }
                                        }
                                    }
                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .show();
            }
        });

        brewList.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                for (int i = 0; i < names.size(); i++) {
                    System.out.println("BEFORE : " + names.get(i));
                }
                for (int i = 0; i < numbers.size(); i++) {
                    System.out.println("BEFORE : " + numbers.get(i));
                }
                for (int i = 0; i < pictures.size(); i++) {
                    System.out.println("BEFORE : " + pictures.get(i));
                }

                final BrewListAdapter adapter = new BrewListAdapter(getApplicationContext(), names, numbers, pictures);

                final String[] snackRender = {""};

                final Dialog dialog = new Dialog(BrewActivity.this);
                dialog.setContentView(R.layout.brew_list_layout);
                dialog.setTitle("Here is a view of your current item list");

                final ListView brewList = (ListView) dialog.findViewById(R.id.brewList);
                brewList.setAdapter(adapter);

                Button hide = (Button) dialog.findViewById(R.id.hide);
                hide.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                brewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        Object obj = brewList.getAdapter().getItem(position);
                        String value = obj.toString();

                        AlertDialog.Builder builderInner = new AlertDialog.Builder(BrewActivity.this);
                        builderInner.setTitle("Selected item : " + value);
                        builderInner.setMessage("Do you want to keep it in the list\nor do you want to delete it ?");
                        builderInner.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                adapter.remove(position);
                                dialog.dismiss();
                            }
                        });
                        builderInner.setPositiveButton("Keep", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
            }
        });
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        brewAdapter.filter(text);
        return false;
    }
}