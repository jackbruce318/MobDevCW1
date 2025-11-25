package org.me.gcu.bruce_jack_s2432194;


import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private CurrencyViewModel currencyViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        //requireActivity() will return the activity the fragment is associated with (only one activity anyway)
        //and share the ViewModel
        currencyViewModel = new ViewModelProvider(requireActivity()).get(CurrencyViewModel.class);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        //Observe the selected currency
        currencyViewModel.getSelectedCurrency().observe(getViewLifecycleOwner(), currency -> {
            //Will run every time a new currency is selected
            if (mMap != null && currency != null) {
                mMap.clear(); //Clear previous marker
                LatLng location = new LatLng(currency.getLatitude(), currency.getLongitude());
                mMap.addMarker(new MarkerOptions().position(location).title(currency.getName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 6f)); //Zoom in on the location
            }
        });
    }

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup container, @Nullable android.os.Bundle savedInstanceState) {
        //inflate layout
        return inflater.inflate(R.layout.activity_maps_fragment, container, false);
    }
}
