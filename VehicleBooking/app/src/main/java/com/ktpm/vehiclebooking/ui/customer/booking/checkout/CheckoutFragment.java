package com.ktpm.vehiclebooking.ui.customer.booking.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ktpm.vehiclebooking.Constants;
import com.ktpm.vehiclebooking.R;
import com.ktpm.vehiclebooking.model.User;
import com.ktpm.vehiclebooking.ui.customer.booking.BookingViewModel;

public class CheckoutFragment extends Fragment {

    private CheckoutViewModel mViewModel;
    private Integer transportationType;
    private String distanceInKmString;
    private String priceInVNDString;


    private CardView carCardView;
    private CardView bikeCardView;
    private TextView distanceCarTextView;
    private TextView distanceBikeTextView;
    private TextView priceCarTextView;
    private TextView priceBikeTextView;
    private Button bookBtn;

    private User currentUserObject;

    public static CheckoutFragment newInstance() {
        return new CheckoutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_customer_checkout, container, false);
        linkViewElements(view);
        setActionHandlers();
        return view;
    }

    private void setActionHandlers(){
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookingViewModel bookingViewModel = ViewModelProviders.of(requireActivity()).get(BookingViewModel.class);
                bookingViewModel.setBookBtnPressed(true);
            }
        });
    }

    private void linkViewElements(View rootView){
        carCardView = rootView.findViewById(R.id.carCardView);
        bikeCardView = rootView.findViewById(R.id.bikeCardView);
        distanceCarTextView = rootView.findViewById(R.id.distanceCarTextView);
        distanceBikeTextView = rootView.findViewById(R.id.distanceBikeTextView);
        priceCarTextView = rootView.findViewById(R.id.priceCarTextView);
        priceBikeTextView = rootView.findViewById(R.id.priceBikeTextView);
        bookBtn = rootView.findViewById(R.id.bookBtn);
    }

    private void hideAccordingCardView(){
        if (transportationType.equals(Constants.Transportation.Type.carType)) {
            carCardView.setVisibility(View.VISIBLE);
            bikeCardView.setVisibility(View.GONE);
        } else {
            carCardView.setVisibility(View.GONE);
            bikeCardView.setVisibility(View.VISIBLE);
        }
    }

    private void setCheckoutInfo() {
        if (transportationType.equals(Constants.Transportation.Type.carType)) {
            distanceCarTextView.setText(distanceInKmString);
            priceCarTextView.setText(priceInVNDString);
        } else {
            distanceBikeTextView.setText(distanceInKmString);
            priceBikeTextView.setText(priceInVNDString);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.setDistanceInKmString(null);
        mViewModel.setPriceInVNDString(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(CheckoutViewModel.class);

        mViewModel.getTransportationType().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer s) {
                if (s == null) return;
                transportationType = s;
                hideAccordingCardView();
            }
        });

        mViewModel.getDistanceInKmString().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == null) return;
                distanceInKmString = s;
            }
        });

        mViewModel.getPriceInVNDString().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == null) return;
                priceInVNDString = s;
                setCheckoutInfo();
            }
        });

    }
}