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
    private final String[] picture;

    public GridAdapter(Context c, String[] number, String[] picture) {
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
        return picture[position];
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

        Context context = imageView.getContext();
        int img = context.getResources().getIdentifier(picture[position], "mipmap", convertView.getContext().getPackageName());

        textView.setText(number[position]);
        imageView.setImageResource(img);

        return convertView;
    }
}
