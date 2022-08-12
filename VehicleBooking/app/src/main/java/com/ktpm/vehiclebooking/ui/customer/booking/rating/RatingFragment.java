package com.ktpm.vehiclebooking.ui.customer.booking.rating;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ktpm.vehiclebooking.ui.customer.booking.CustomerBookingViewModel;

public class RatingFragment extends DialogFragment {

    private RatingViewModel mViewModel;
    private RatingBar mRatingBar;
    private TextView mRatingScale;
    private Button mSendFeedback;
    private EditText mFeedback;
    private ImageView profileImage;


    private User driver;


    public static RatingFragment newInstance() {
        return new RatingFragment();
    }

    /**
     * Link view elements from xml file
     * @param view
     */
    private void linkViewElements(View view) {
        mRatingBar = view.findViewById(R.id.rating_bar);
        mRatingScale = view.findViewById(R.id.rating_scale_text_view);
        mFeedback = view.findViewById(R.id.feed_back_edit_text);
        mSendFeedback = view.findViewById(R.id.send_feedback_btn);
        profileImage = view.findViewById(R.id.profile_avatar);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_rating, container, false);
        linkViewElements(view);

        mFeedback.setText("");
        mRatingBar.setRating(0);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRatingScale.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        mRatingScale.setText("Very bad");
                        break;
                    case 2:
                        mRatingScale.setText("Need some improvement");
                        break;
                    case 3:
                        mRatingScale.setText("Good");
                        break;
                    case 4:
                        mRatingScale.setText("Great");
                        break;
                    case 5:
                        mRatingScale.setText("Awesome");
                        break;
                    default:
                        mRatingScale.setText("");
                }
            }
        });

        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
                CustomerBookingViewModel customerBookingViewModel = ViewModelProviders.of(requireActivity()).get(CustomerBookingViewModel.class);
                customerBookingViewModel.setFeedBackRating((int) mRatingBar.getRating());
                dismiss();
            }
        });
        return view;
    }

    /**
     * Set profile image
     */
    private void setProfileImage(){
    }

    @SuppressLint("SetTextI18n")
    private void setDriverInfo(){
        setProfileImage();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel =  ViewModelProviders.of(requireActivity()).get(RatingViewModel.class);
        mViewModel.getDriver().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                driver = user;
                setDriverInfo();
            }
        });
    }

}
