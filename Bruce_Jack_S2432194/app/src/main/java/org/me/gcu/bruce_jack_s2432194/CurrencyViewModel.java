package org.me.gcu.bruce_jack_s2432194;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

public class CurrencyViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Currency>> currencies = new MutableLiveData<>();
    private ArrayList<Currency> currencyList = new ArrayList<>();
    private final MutableLiveData<Currency> selectedCurrency = new MutableLiveData<>();

    public LiveData<ArrayList<Currency>> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(ArrayList<Currency> currencyList) {
        this.currencyList = currencyList;
        currencies.setValue(this.currencyList);
    }

    public ArrayList<Currency> getCurrencyList() {
        return currencyList;
    }

    public void selectCurrency(Currency currency) {
        selectedCurrency.setValue(currency);
    }

    public LiveData<Currency> getSelectedCurrency() {
        return selectedCurrency;
    }
}
