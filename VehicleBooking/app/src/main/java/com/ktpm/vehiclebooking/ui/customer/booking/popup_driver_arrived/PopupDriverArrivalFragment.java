package com.ktpm.vehiclebooking.ui.customer.booking.popup_driver_arrived;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ktpm.vehiclebooking.R;
import com.ktpm.vehiclebooking.model.User;

public class PopupDriverArrivalFragment extends DialogFragment {

    private PopupDriverArrivalViewModel mViewModel;
    private TextView driverUsernameTextView;
    private TextView vehicleInfo;
    private Button closeBtn;

    public static PopupDriverArrivalFragment newInstance() {
        return new PopupDriverArrivalFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pop_up_driver_arrival, container, false);
        linkViewElements(view);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private void linkViewElements(View rootView){
        driverUsernameTextView = rootView.findViewById(R.id.driverUsernameTextView);
        vehicleInfo = rootView.findViewById(R.id.vehicleInfo);
        closeBtn = rootView.findViewById(R.id.closeBtn);
    }


    @SuppressLint("SetTextI18n")
    private void setDriverInfo(User driver){
        driverUsernameTextView.setText(driver.getname());
        String typeOfVehicle = driver.gettype()==2 ? "Car" : "Bike";
        vehicleInfo.setText(driver.getvehicle_plate() + " ● " + typeOfVehicle);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(com.ktpm.vehiclebooking.ui.customer.booking.popup_driver_arrived.PopupDriverArrivalViewModel.class);
        mViewModel.getDriver().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                setDriverInfo(user);
            }
        });
    }

}