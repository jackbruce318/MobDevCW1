package org.me.gcu.bruce_jack_s2432194;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter implements Filterable{
    Context context;

    private List<Currency> filteredCntryList; //Holds what is currently being displayed
    private List<Currency> cntryList; //storage for textual data
    int[] pics;  //storage for icons ids
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, List<Currency> countryList) {
        this.context = applicationContext;
        this.cntryList = countryList;
        this.filteredCntryList = countryList;
        //this.pics = images;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return filteredCntryList.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredCntryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
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
        holder.name.setText(filteredCntryList.get(i).getName());
        //holder.pic.setImageResource(pics[i]);
        return view;
    }
    static class WidgetsHolder { //preserve objects bound to widgets
        TextView name;
        //ImageView pic;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String query = constraint.toString().toLowerCase().trim(); //cut out spaces and turn all lowercase

                if (query.isEmpty()) {
                    //If nothing in search bar, just show full list
                    results.values = cntryList;
                } else {
                    List<Currency> filtered = new ArrayList<>();
                    for (Currency currency : cntryList) {

                        //search for currency name or code (contained in name or description)
                        if (currency.getName().toLowerCase().contains(query) ||
                                currency.getDescription().toLowerCase().contains(query)) {
                            filtered.add(currency);
                        }
                    }
                    results.values = filtered;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //update list with filtered data
                filteredCntryList = (List<Currency>) results.values;
                //tell listview to refresh
                notifyDataSetChanged();
            }
        };
    }

    public void updateData(List<Currency> newCurrencyList) {
        //update original and filtered lists
        this.cntryList = newCurrencyList;
        this.filteredCntryList = newCurrencyList;
        //tell adapter that data has changed so listview must refresh
        notifyDataSetChanged();
    }
}