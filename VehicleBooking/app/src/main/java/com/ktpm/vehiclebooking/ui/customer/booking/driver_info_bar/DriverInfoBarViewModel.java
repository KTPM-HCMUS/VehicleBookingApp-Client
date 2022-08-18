package com.ktpm.vehiclebooking.ui.customer.booking.driver_info_bar;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ktpm.vehiclebooking.model.User;

/**
 * View model for DriveInforBarFragment
 */
public class DriverInfoBarViewModel extends ViewModel {
    private MutableLiveData<User> driver;

    public DriverInfoBarViewModel() {
        driver = new MutableLiveData<>();
    }

    public void setDriver(User driver) {
        this.driver.setValue(driver);
    }

    public MutableLiveData<User> getDriver() {
        return driver;
    }
}