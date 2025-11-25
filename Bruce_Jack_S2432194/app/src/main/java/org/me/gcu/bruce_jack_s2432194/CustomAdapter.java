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
        this.cntryList = countryList != null ? countryList : new ArrayList<>();
        this.filteredCntryList = this.cntryList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return filteredCntryList != null ? filteredCntryList.size() : 0;
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
            holder.icon = view.findViewById(R.id.icon);
            view.setTag(holder); //store bound objects into the view
        }
        else{ //already existing, just reuse bound objects
            holder = (CustomAdapter.WidgetsHolder)view.getTag();
        }
        holder.icon.setImageResource(filteredCntryList.get(i).getFlagId());
        holder.name.setText(filteredCntryList.get(i).getName());
        return view;
    }

    static class WidgetsHolder { //preserve objects bound to widgets
        TextView name;
        ImageView icon;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (cntryList == null) {
                    results.values = new ArrayList<>();
                    return results;
                }

                String query = constraint.toString().toLowerCase().trim();

                if (query.isEmpty()) {
                    results.values = cntryList;
                } else {
                    List<Currency> filtered = new ArrayList<>();
                    for (Currency currency : cntryList) {
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
                if (results != null && results.values != null) {
                    filteredCntryList = (List<Currency>) results.values;
                } else {
                    filteredCntryList = new ArrayList<>();
                }
                notifyDataSetChanged();
            }
        };
    }

    public void updateData(List<Currency> newCurrencyList) {
        this.cntryList = newCurrencyList != null ? newCurrencyList : new ArrayList<>();
        this.filteredCntryList = this.cntryList;
        notifyDataSetChanged();
    }
}
