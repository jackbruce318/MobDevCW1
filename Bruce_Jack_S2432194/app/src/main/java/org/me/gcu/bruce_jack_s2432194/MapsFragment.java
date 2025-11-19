package org.me.gcu.bruce_jack_s2432194;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.me.gcu.bruce_jack_s2432194.databinding.ActivityMapsFragmentBinding;

public class MapsFragment extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsFragmentBinding binding;
    private CurrencyViewModel currencyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsFragmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        currencyViewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);

        currencyViewModel.getSelectedCurrency().observe(this, currency -> {
            if (mMap != null && currency != null) {
                mMap.clear();
                LatLng location = new LatLng(currency.getLatitude(), currency.getLongitude());
                mMap.addMarker(new MarkerOptions().position(location).title(currency.getName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}