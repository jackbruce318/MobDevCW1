package org.me.gcu.bruce_jack_s2432194;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

public class CurrencyViewModel extends ViewModel {
    private ArrayList<Currency> currencyList = new ArrayList<>();

    public void setCurrencies(ArrayList<Currency> currencyList) {
        this.currencyList = currencyList;
    }

    public ArrayList<Currency> getCurrencyList() {
        return currencyList;
    }
}
