package org.me.gcu.bruce_jack_s2432194;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

public class CurrencyViewModel extends ViewModel {
    //This ViewModel uses LiveData so that any changes to either the List in ListView or the currently selected currency can be observed
    //and handled in MainActivity.

    private final MutableLiveData<ArrayList<Currency>> currencies = new MutableLiveData<>();
    private ArrayList<Currency> currencyList = new ArrayList<>();
    private final MutableLiveData<Currency> selectedCurrency = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentView = new MutableLiveData<>(0);

    //LiveData for conversion page UI
    private final MutableLiveData<String> convLeftText = new MutableLiveData<>();
    private final MutableLiveData<String> convRightText = new MutableLiveData<>();
    private final MutableLiveData<String> convEditText = new MutableLiveData<>();
    private final MutableLiveData<String> convResultText = new MutableLiveData<>();


    private final MutableLiveData<ArrayList<Currency>> mainList = new MutableLiveData<>(new ArrayList<>());
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

    public LiveData<ArrayList<Currency>> getMainList(){
        return mainList;
    }

    public LiveData<Integer> getCurrentView(){
        return currentView;
    }

    public LiveData<String> getConvLeftText() { return convLeftText; }
    public LiveData<String> getConvRightText() { return convRightText; }
    public LiveData<String> getConvEditText() { return convEditText; }
    public LiveData<String> getConvResultText() { return convResultText; }


    public void setCurrentView(int view){
        currentView.setValue(view);
    }


    public void setMainList(ArrayList<Currency> mainList){
        this.mainList.setValue(mainList);
    }

    public void setConvLeftText(String text) { convLeftText.setValue(text); }
    public void setConvRightText(String text) { convRightText.setValue(text); }
    public void setConvEditText(String text) { convEditText.setValue(text); }
    public void setConvResultText(String text) { convResultText.setValue(text); }
}
