package com.ktpm.vehiclebooking.ui.driver.process_booking;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ktpm.vehiclebooking.model.Booking;
import com.ktpm.vehiclebooking.model.User;

public class DriverProcessBookingViewModel extends ViewModel {
    private MutableLiveData<User> currentUserObject;
    private MutableLiveData<Boolean> checkoutDone;

    public DriverProcessBookingViewModel() {
        currentUserObject = new MutableLiveData<>();
        checkoutDone = new MutableLiveData<>();
    }

    public void setCurrentUserObject(User currentUserObject) {
        this.currentUserObject.setValue(currentUserObject);
    }

    public void setCheckoutDone(Boolean checkoutDone) {
        this.checkoutDone.setValue(checkoutDone);
    }

    public MutableLiveData<Boolean> getCheckoutDone() {
        return checkoutDone;
    }


    public MutableLiveData<User> getCurrentUserObject(){
        return this.currentUserObject;
    }

}
