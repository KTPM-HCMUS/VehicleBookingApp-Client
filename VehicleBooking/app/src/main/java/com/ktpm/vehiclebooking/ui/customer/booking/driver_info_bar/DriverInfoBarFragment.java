package com.ktpm.vehiclebooking.ui.customer.booking.driver_info_bar;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ktpm.vehiclebooking.Constants;
import com.ktpm.vehiclebooking.R;
import com.ktpm.vehiclebooking.model.User;

public class DriverInfoBarFragment extends Fragment {

    private DriverInfoBarViewModel mViewModel;

    private TextView driverUsernameTextView;
    private TextView plateNumberTextView;
    private TextView transportationTypeTextView;
    private RatingBar ratingBar;
    private ImageView profileImage;



    private User driver;

    public static DriverInfoBarFragment newInstance() {
        return new DriverInfoBarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_info_bar, container, false);
        linkViewElement(view);
        return view;
    }
    /**
     * Link view elements from xml file
     * @param rootView
     */
    private void linkViewElement(View rootView) {
        driverUsernameTextView = rootView.findViewById(R.id.driverUsernameTextView);
        plateNumberTextView = rootView.findViewById(R.id.plateNumberTextView);
        transportationTypeTextView = rootView.findViewById(R.id.transportationTypeTextView);
        ratingBar = rootView.findViewById(R.id.score_rating_bar);
        profileImage = rootView.findViewById(R.id.profile_avatar);
    }

    /**
     * Render driver information
     * @param driver
     */
    private void setDriverInfo(User driver) {
        driverUsernameTextView.setText(driver.getUsername());
        plateNumberTextView.setText(driver.getVehiclePlateNumber());
        transportationTypeTextView.setText(driver.getTransportationType());
        ratingBar.setRating(getRatingAverage(driver));
        setProfileImage();
    }

    /**
     * Get driver profile image
     */
    private void setProfileImage() {
        // Retrieve driver information
    }

    /**
     * Get driver avg rating
     * @param driver
     * @return avgRating
     */
    public float getRatingAverage(User driver) {
        double total = 0;
        for (int _rating : driver.getRating()) {
            total += _rating;
        }
        return (float) (total / driver.getRating().size());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(DriverInfoBarViewModel.class);
        mViewModel.getDriver().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                driver = user;
                setDriverInfo(user);
            }
        });
    }

}