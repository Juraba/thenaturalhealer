package com.marmot.intrepid.naturalhealer.control;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marmot.intrepid.naturalhealer.R;
import com.marmot.intrepid.naturalhealer.model.Item;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InventoryBrewAdapter extends BaseAdapter {

    // Declare Variables

    GameService game;
    Context mContext;
    LayoutInflater inflater;
    private List<String> inventory = null;
    private ArrayList<String> arraylist;

    public InventoryBrewAdapter(Context context, List<String> inventory) {
        mContext = context;
        this.inventory = inventory;
        this.arraylist = new ArrayList<String>();
        this.arraylist.addAll(inventory);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return inventory.size();
    }

    @Override
    public String getItem(int position) {
        return inventory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.brew_list_content_layout, null);
        } else {
            convertView = (View) convertView;
        }

        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        TextView itemNumber = (TextView) convertView.findViewById(R.id.itemNumber);
        ImageView itemIcon = (ImageView) convertView.findViewById(R.id.itemIcon);

        game = GameService.getInstance();

        for (Map.Entry<Item, Integer> i : game.getPlayer().getInventory().entrySet()) {
            Item item = i.getKey();

            if (inventory.get(position) != null) {
                if (inventory.get(position).equals(item.getName())) {
                    int img = mContext.getResources().getIdentifier(item.getPicName(), "mipmap", convertView.getContext().getPackageName());
                    itemNumber.setText(" |  " + i.getValue().toString());
                    itemName.setText(item.getName());
                    itemIcon.setImageResource(img);
                }
            }
        }

        return convertView;
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        inventory.clear();
        if (charText.length() == 0) {
            inventory.addAll(arraylist);
        } else {
            for (String wp : arraylist) {
                if (wp.toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    inventory.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}