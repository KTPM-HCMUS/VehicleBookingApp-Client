package com.ktpm.vehiclebooking.ui.customer.booking.dropoff;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ktpm.vehiclebooking.model.User;


public class DropoffViewModel extends ViewModel {
    private MutableLiveData<User> currentUserObject;


    public DropoffViewModel() {
        currentUserObject = new MutableLiveData<>();
    }

    public void setCurrentUserObject(User currentUserObject) {
        this.currentUserObject.setValue(currentUserObject);
    }

    public MutableLiveData<User> getCurrentUserObject(){
        return this.currentUserObject;
    }
}
