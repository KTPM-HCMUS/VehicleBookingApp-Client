package com.ktpm.vehiclebooking.ui.customer.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import com.ktpm.vehiclebooking.Constants;
import com.ktpm.vehiclebooking.R;
import com.ktpm.vehiclebooking.model.User;
import com.ktpm.vehiclebooking.ui.customer.booking.BookingViewModel;
import com.ktpm.vehiclebooking.ui.customer.booking.checkout.CheckoutViewModel;

public class CustomerHomeFragment extends Fragment {
    private CustomerHomeViewModel customerHomeViewModel;
    private ImageButton bikeBtn;
    private ImageButton carBtn;
    private User currentUserObject;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_home, container, false);
        linkViewElements(view);
        setActionHandlers();
        return view;
    }

    private void linkViewElements(View rootView){
        bikeBtn = rootView.findViewById(R.id.bike_image_button);
        carBtn = rootView.findViewById(R.id.car_image_button);
    }

    private void setActionHandlers(){
        setBikeBtnActionHandler();
        setCarBtnActionHandler();
    }

    private void setBikeBtnActionHandler(){
        bikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckoutViewModel checkoutViewModel = ViewModelProviders.of(requireActivity()).get(CheckoutViewModel.class);
                checkoutViewModel.setTransportationType(Constants.Transportation.Type.bikeType);
                BookingViewModel bookingViewModel = ViewModelProviders.of(requireActivity()).get(BookingViewModel.class);
                bookingViewModel.setTransportationType(Constants.Transportation.Type.bikeType);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_customer_booking);
            }
        });
    }

    private void setCarBtnActionHandler(){
        carBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckoutViewModel checkoutViewModel = ViewModelProviders.of(requireActivity()).get(CheckoutViewModel.class);
                checkoutViewModel.setTransportationType(Constants.Transportation.Type.carType);
                BookingViewModel bookingViewModel = ViewModelProviders.of(requireActivity()).get(BookingViewModel.class);
                bookingViewModel.setTransportationType(Constants.Transportation.Type.carType);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_customer_booking);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customerHomeViewModel = ViewModelProviders.of(this).get(CustomerHomeViewModel.class);
        customerHomeViewModel.getCurrentUserObject().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUserObject = user;
            }
        });
    }
}