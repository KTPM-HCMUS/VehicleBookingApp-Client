package com.ktpm.vehiclebooking.ui.customer.booking;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.libraries.places.api.model.Place;
import com.ktpm.vehiclebooking.model.User;

public class BookingViewModel extends ViewModel {
    private MutableLiveData<User> currentUserObject;
    private MutableLiveData<Place> customerSelectedDropOffPlace;
    private MutableLiveData<Place> customerSelectedPickupPlace;
    private MutableLiveData<Integer> transportationType;
    private MutableLiveData<Boolean> bookBtnPressed;
    private MutableLiveData<Boolean> cancelBookingBtnPressed;

    public BookingViewModel() {
        currentUserObject = new MutableLiveData<>();
        customerSelectedDropOffPlace = new MutableLiveData<>();
        customerSelectedPickupPlace = new MutableLiveData<>();
        transportationType = new MutableLiveData<>();
        bookBtnPressed = new MutableLiveData<>();
        cancelBookingBtnPressed = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(int transportationType) {
        this.transportationType.setValue(transportationType);
    }

    public void setCurrentUserObject(User currentUserObject) {
        this.currentUserObject.setValue(currentUserObject);
    }

    public void setCustomerSelectedDropOffPlace(Place customerSelectedDropOffPlace) {
        this.customerSelectedDropOffPlace.setValue(customerSelectedDropOffPlace);
    }

    public void setCustomerSelectedPickupPlace(Place customerSelectedPickupPlace) {
        this.customerSelectedPickupPlace.setValue(customerSelectedPickupPlace);
    }

    public void setBookBtnPressed(Boolean bookBtnPressed) {
        this.bookBtnPressed.setValue(bookBtnPressed);
    }

    public void setCancelBookingBtnPressed(Boolean cancelBookingBtnPressed) {
        this.cancelBookingBtnPressed.setValue(cancelBookingBtnPressed);
    }

    public MutableLiveData<Place> getCustomerSelectedPickupPlace() {
        return customerSelectedPickupPlace;
    }

    public MutableLiveData<User> getCurrentUserObject(){
        return this.currentUserObject;
    }

    public MutableLiveData<Place> getCustomerSelectedDropOffPlace() {
        return customerSelectedDropOffPlace;
    }

    public MutableLiveData<Boolean> getBookBtnPressed() {
        return bookBtnPressed;
    }

    public MutableLiveData<Boolean> getCancelBookingBtnPressed() {
        return cancelBookingBtnPressed;
    }

}
