package com.ktpm.vehiclebooking.ui.customer.booking.popup_driver_info;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ktpm.vehiclebooking.Constants;
import com.ktpm.vehiclebooking.R;
import com.ktpm.vehiclebooking.model.User;

public class PopupDriverInfoFragment extends DialogFragment {

    private PopupDriverInfoViewModel mViewModel;
    private TextView driverUsernameTextView;
    private TextView plateNumberAndBike;
    private RatingBar ratingBar;
    private ImageView profileImage;
    private User driver;

    public static PopupDriverInfoFragment newInstance() {
        return new PopupDriverInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popup_driver_info, container, false);
        linkViewElements(view);
        return view;
    }

    private void linkViewElements(View rootView){
        driverUsernameTextView = rootView.findViewById(R.id.driverUsernameTextView);
        plateNumberAndBike = rootView.findViewById(R.id.plateNumberAndBike);
        ratingBar = rootView.findViewById(R.id.ratingBar);
        profileImage = rootView.findViewById(R.id.profile_avatar);
    }

    @SuppressLint("SetTextI18n")
    private void setDriverInfo(){
        driverUsernameTextView.setText(driver.getname());
        plateNumberAndBike.setText(driver.getvehicle_plate() + " ● " + driver.gettype());
        ratingBar.setRating(getRatingAverage(driver));
        setProfileImage();
    }

    private void setProfileImage(){

    }

    public float getRatingAverage(User driver) {
       return (float) 0.0;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(com.ktpm.vehiclebooking.ui.customer.booking.popup_driver_info.PopupDriverInfoViewModel.class);
        mViewModel.getDriver().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                driver = user;
                setDriverInfo();
            }
        });
    }
}