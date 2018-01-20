package com.marmot.intrepid.naturalhealer.control;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.marmot.intrepid.naturalhealer.R;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import static android.net.wifi.WifiConfiguration.Status.strings;

public class BrewListAdapter extends BaseAdapter{
    private Context mContext;
    private final ArrayList<String> names;
    private final ArrayList<String> numbers;
    private final ArrayList<String> pictures;

    public BrewListAdapter(Context c, ArrayList<String> names,  ArrayList<String> numbers, ArrayList<String> pictures) {
        mContext = c;
        this.names = names;
        this.pictures = pictures;
        this.numbers = numbers;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return pictures.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return pictures.get(position);
    }

    public String getName(int position) {
        return names.get(position);
    }

    public String getNumber(int position) {
        return numbers.get(position);
    }

    public String getPicture(int position) {
        return pictures.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.brew_list_content_layout, null);
        } else {
            convertView = (View) convertView;
        }

        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        TextView itemNumber = (TextView) convertView.findViewById(R.id.itemNumber);
        ImageView itemIcon = (ImageView) convertView.findViewById(R.id.itemIcon);

        if (pictures.get(position) != null) {
            int img = mContext.getResources().getIdentifier(pictures.get(position), "mipmap", convertView.getContext().getPackageName());
            if (numbers.size() != 0) {
                itemNumber.setText(" |  " + numbers.get(position));
            }
            if (names.size() != 0) {
                itemName.setText(names.get(position));
            }
            itemIcon.setImageResource(img);
            System.out.println(pictures.get(position));
        }

        return convertView;
    }

    public void remove(int pos) {

        for (int i = 0; i < names.size(); i++) {
            if (i == pos) {
                names.remove(names.get(i));
            }
        }
        for (int i = 0; i < numbers.size(); i++) {
            if (i == pos) {
                numbers.remove(numbers.get(i));
            }
        }
        for (int i = 0; i < pictures.size(); i++) {
            if (i == pos) {
                pictures.remove(pictures.get(i));
            }
        }

    }

}
