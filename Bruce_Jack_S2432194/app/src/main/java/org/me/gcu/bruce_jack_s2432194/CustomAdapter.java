package org.me.gcu.bruce_jack_s2432194;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String[] cntryList; //storage for textual data
    int[] pics;  //storage for icons ids
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] countryList) {
        this.context = applicationContext;
        this.cntryList = countryList;
        //this.pics = images;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return cntryList.length;
    }

    @Override
    public Object getItem(int i) {
        return cntryList[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CustomAdapter.WidgetsHolder holder = null;
        if(view == null) { //first time, create view and bound to widgets
            view = inflter.inflate(R.layout.layout_listview, null);
            holder = new CustomAdapter.WidgetsHolder();
            holder.name = (TextView) view.findViewById(R.id.textView);
            //holder.pic = (ImageView) view.findViewById(R.id.icon);
            view.setTag(holder); //store bound objects into the view
        }
        else{ //already existing, just reuse bound objects
            holder = (CustomAdapter.WidgetsHolder)view.getTag();
        }
        holder.name.setText(cntryList[i]);
        //holder.pic.setImageResource(pics[i]);
        return view;
    }
    static class WidgetsHolder { //preserve objects bound to widgets
        TextView name;
        //ImageView pic;
    }
}