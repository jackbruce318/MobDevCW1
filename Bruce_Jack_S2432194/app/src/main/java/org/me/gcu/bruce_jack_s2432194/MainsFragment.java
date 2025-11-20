package org.me.gcu.bruce_jack_s2432194;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class MainsFragment extends Fragment implements View.OnClickListener {

    private CurrencyViewModel currencyViewModel;
    private CustomAdapter adapter;
    private ListView lv;

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup container, @Nullable android.os.Bundle savedInstanceState) {
        //inflate layout
        return inflater.inflate(R.layout.layout_mainfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize the adapter with an empty list
        lv = view.findViewById(R.id.listView);
        adapter = new CustomAdapter(requireContext(), new ArrayList<>());
        lv.setAdapter(adapter);

        //Get the ViewModel and observe the mainList
        currencyViewModel = new ViewModelProvider(requireActivity()).get(CurrencyViewModel.class);

        //Observe changes to the main list and update adapter
        currencyViewModel.getMainList().observe(getViewLifecycleOwner(), currencies -> {
            if (currencies != null) {
                adapter.updateData(currencies);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    public interface MainFragmentListener {
        void onInputSendCurrency(Currency input);
    }
}