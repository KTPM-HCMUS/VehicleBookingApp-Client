package com.ktpm.vehiclebooking.ui.customer.booking.dropoff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.ktpm.vehiclebooking.Constants;
import com.ktpm.vehiclebooking.R;
import com.ktpm.vehiclebooking.ui.customer.booking.BookingViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class DropoffFragment extends Fragment {
    private DropoffViewModel mViewModel;
    private PlacesClient placesClient;
    private AutocompleteSupportFragment autocompleteFragment;


    public static DropoffFragment newInstance() {
        return new DropoffFragment();
    }


    private void initGooglePlacesAutocomplete() {
        String apiKey = getString(R.string.google_maps_key);
        if (!Places.isInitialized()) {
            Places.initialize(requireActivity().getApplicationContext(), apiKey);
        }

        this.placesClient = Places.createClient(requireActivity().getApplicationContext());

        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.maps_place_autocomplete_fragment);
        autocompleteFragment.setPlaceFields(
                Arrays.asList(
                        Place.Field.ID,
                        Place.Field.NAME,
                        Place.Field.LAT_LNG,
                        Place.Field.ADDRESS,
                        Place.Field.ADDRESS_COMPONENTS,
                        Place.Field.PLUS_CODE
                ));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dropoff, container, false);
        //linkViewElements()
        initGooglePlacesAutocomplete();
        setActionHandlers();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BookingViewModel bookingViewModel = ViewModelProviders.of(requireActivity()).get(BookingViewModel.class);
        autocompleteFragment.setOnPlaceSelectedListener(null);
    }

    public void setActionHandlers(){
        setPlaceSelectedActionHandler();
    }


    private void setPlaceSelectedActionHandler() {
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                if (place == null) return;
                BookingViewModel bookingViewModel = ViewModelProviders.of(requireActivity()).get(BookingViewModel.class);
                bookingViewModel.setCustomerSelectedDropOffPlace(place);
            }

            @Override
            public void onError(@NotNull Status status) {
                Toast.makeText(getActivity().getApplicationContext(),
                        Constants.ToastMessage.placeAutocompleteError + status, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(DropoffViewModel.class);
    }

}