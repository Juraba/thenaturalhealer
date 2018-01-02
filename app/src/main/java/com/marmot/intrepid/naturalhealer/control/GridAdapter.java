package com.marmot.intrepid.naturalhealer.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marmot.intrepid.naturalhealer.R;

public class GridAdapter extends BaseAdapter{
    private Context mContext;
    private final String[] number;
    private final int[] picture;

    public GridAdapter(Context c, String[] number, int[] picture) {
        mContext = c;
        this.picture = picture;
        this.number = number;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return picture.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.gridview_layout, null);
            TextView textView = (TextView) grid.findViewById(R.id.itemNumber);
            ImageView imageView = (ImageView)grid.findViewById(R.id.itemIcon);
            textView.setText(number[position]);
            imageView.setImageResource(picture[position]);
        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}
