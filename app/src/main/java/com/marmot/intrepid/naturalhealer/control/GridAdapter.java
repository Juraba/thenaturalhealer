package com.marmot.intrepid.naturalhealer.control;

import android.content.Context;
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

public class GridAdapter extends BaseAdapter{
    private Context mContext;
    private final String[] numbers;
    private final String[] pictures;

    public GridAdapter(Context c, String[] numbers, String[] pictures) {
        mContext = c;
        this.pictures = pictures;
        this.numbers = numbers;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return pictures.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return pictures[position];
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
            convertView = inflater.inflate(R.layout.gridview_layout, null);
        } else {
            convertView = (View) convertView;
        }

        TextView textView = (TextView) convertView.findViewById(R.id.itemNumber);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.itemIcon);

        if (pictures[position] != null) {
            int img = mContext.getResources().getIdentifier(pictures[position], "mipmap", convertView.getContext().getPackageName());
            if (numbers.length != 0) {
                textView.setText(numbers[position]);
            }
            imageView.setImageResource(img);
            System.out.println(pictures[position]);
        }

        return convertView;
    }

}
