package com.ktpm.vehiclebooking.ui.driver.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ktpm.vehiclebooking.Constants;
import com.ktpm.vehiclebooking.R;
import com.ktpm.vehiclebooking.activities.MainActivity;
import com.ktpm.vehiclebooking.model.Booking;
import com.ktpm.vehiclebooking.model.DriverLocation;
import com.ktpm.vehiclebooking.model.User;
import com.ktpm.vehiclebooking.ui.customer.booking.dropoff.DropoffFragment;
import com.ktpm.vehiclebooking.ui.customer.home.CustomerHomeViewModel;
import com.ktpm.vehiclebooking.ui.driver.alert.DriverAlertFragment;
import com.ktpm.vehiclebooking.ui.driver.alert.DriverAlertViewModel;
import com.ktpm.vehiclebooking.ui.driver.driver_info.DriverInfoFragment;
import com.ktpm.vehiclebooking.ui.driver.driver_info.DriverInfoViewModel;
import com.ktpm.vehiclebooking.ui.driver.process_booking.DriverProcessBookingFragment;
import com.ktpm.vehiclebooking.ui.driver.process_booking.DriverProcessBookingViewModel;

import java.util.ArrayList;

public class DriverHomeFragment extends Fragment implements OnMapReadyCallback {

    private DriverHomeViewModel driverHomeViewModel;

    //Google maps variables
    private static final int MY_LOCATION_REQUEST = 99;
    private SupportMapFragment supportMapFragment; //maps view
    private GoogleMap mMap;
    private FusedLocationProviderClient locationClient;
    private LocationRequest locationRequest;
    private PlacesClient placesClient;
    private Marker currentUserLocationMarker;
    private Marker currentOriginLocationMarker;
    private Marker currentDestinationLocationMarker;
    private Location currentUserLocation;
    private ArrayList<Polyline> currentRoute = new ArrayList<>();


    private FloatingActionButton getMyLocationBtn;


    private User currentUserObject;

    //Booking flow
    Boolean isBusy;
//    Boolean isUpdateLocationOnDatabase;


    /**
     * Connect view elements for further use
     * @param rootView
     */
    private void linkViewElements(View rootView) {
        getMyLocationBtn = rootView.findViewById(R.id.fragmentMapsFindMyLocationBtn);

    }


    /**
     * Request user for location permission
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_LOCATION_REQUEST);
    }

//    @SuppressLint("SetTextI18n")
//    private void setDriverInfo() {
//        driverName.setText("Username: " + currentUserObject.getUsername());
//        vehicleTypeTextView.setText("Vehicle type: " + currentUserObject.getTransportationType());
//        vehiclePlateNumberTextView.setText("Plate number: " + currentUserObject.getVehiclePlateNumber());
//        driverRating.setText(Double.toString(currentUserObject.getRating()));
//    }

    /**
     * Set Action Handlers
     */
    private void setActionHandlers() {
        setGetMyLocationBtnHandler(); //Find My location Button listener
    }

    /**
     * //Find My location Button listener
     */
    private void setGetMyLocationBtnHandler() {
        getMyLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGetPositionClick();
            }
        });
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_driver_home, container, false);
        linkViewElements(root);
        initMapsFragment();
        setActionHandlers();
        return root;
    }

    private void loadDriverInfoFragment(){
        //Load driver info fragment
        DriverInfoFragment driverInfoFragment = new DriverInfoFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.driver_info, driverInfoFragment).commit();
    }

    private void sendDriverInfoDataToViewModel(){
        DriverInfoViewModel driverInfoViewModel = ViewModelProviders.of(requireActivity()).get(DriverInfoViewModel.class);
        driverInfoViewModel.setCurrentUserObject(currentUserObject);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resetBookingFlow();
//        loadDriverInfoFragment();
        setListenerForBooking();
    }

    /**
     * Init Google MapsFragment
     */
    private void initMapsFragment() {
        supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.fragment_maps);
        supportMapFragment.getMapAsync(this);
    }

    /**
     * //Start location update listener
     */
    @SuppressLint({"MissingPermission", "RestrictedApi"})
    private void startLocationUpdate() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); //5s
        locationRequest.setFastestInterval(10 * 1000); //5s
        locationClient.requestLocationUpdates(locationRequest,
                new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        Location location = locationResult.getLastLocation();
                        LatLng latLng = new LatLng(location.getLatitude(),
                                location.getLongitude());
                        updateCurrentUserLocationMarker(latLng);
//                        updateCurrentRoute();
                        updateCurrentDriverLocationOnDB(latLng); //TODO change this shitttttt

                    }
                }
                , null);
    }

    /**
     * Find my position action handler
     */
    @SuppressLint("MissingPermission")
    public void onGetPositionClick() {
        locationClient.getLastLocation().
                addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            Toast.makeText(getActivity(),
                                    Constants.ToastMessage.currentLocationNotUpdatedYet,
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        currentUserLocation = location;
                        if (currentUserLocationMarker == null) {
                            updateCurrentUserLocationMarker(latLng);
                        }
                        smoothlyMoveCameraToPosition(latLng, Constants.GoogleMaps.CameraZoomLevel.streets);
                    }
                });
    }

    private void updateCurrentDriverLocationOnDB(LatLng newLatLng){
//        currentUserObject.setCurrentPositionLatitude(newLatLng.latitude);
//        currentUserObject.setCurrentPositionLongitude(newLatLng.longitude);

        DriverLocation driverLocation = new DriverLocation();
        driverLocation.setCurrentPositionLatitude(newLatLng.latitude);
        driverLocation.setCurrentPositionLongitude(newLatLng.longitude);

    }

    /**
     * Update current user location marker
     * @param newLatLng
     */
    private void updateCurrentUserLocationMarker(LatLng newLatLng) {
        if (currentUserLocationMarker != null) {
            currentUserLocationMarker.remove();
        }
        drawNewCurrentUserLocationMarker(newLatLng);

//        updateCurrentDriverLocationOnDB(newLatLng);
    }

    private void drawNewCurrentUserLocationMarker(LatLng newLatLng) {
        currentUserLocationMarker = mMap.addMarker(
                new MarkerOptions()
                        .position(newLatLng)
                        .icon(bitmapDescriptorFromVector(
                                getActivity(),
                                R.drawable.ic_current_location_marker, Color.BLUE)
                        )
                        .title("You are here!")
        );
    }

    /**
     * Get BitmapDescriptor from drawable vector asset, for custom cluster marker
     * @param context
     * @param vectorResId
     * @param color
     * @return
     */
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId, int color) {
        if (context == null) {
            return null;
        }
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        DrawableCompat.setTint(vectorDrawable, color);
        DrawableCompat.setTintMode(vectorDrawable, PorterDuff.Mode.SRC_IN);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /**
     * Smoothly change camera position with zoom level
     * @param latLng
     * @param zoomLevel
     */
    private void smoothlyMoveCameraToPosition(LatLng latLng, float zoomLevel) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }

    /*************************************************** For booking synchronization *****************************************************/
    private void resetBookingFlow(){
        isBusy = false;
        loadDriverInfoFragment();
    }

    private void setListenerForBooking() {
    }

    /**
     * Send data to alertViewModel
     * @param booking
     */
    private void sendDataToAlertViewModel(Booking booking){
        DriverAlertViewModel driverAlertViewModel = ViewModelProviders.of(requireActivity()).get(DriverAlertViewModel.class);
        driverAlertViewModel.setBooking(booking);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        driverHomeViewModel.setAcceptBookingBtnPressed(null);
    }

    /**
     * Show the driver alert dialog
     */
    private void loadDriverAlertFragment(){
        FragmentManager fm = getChildFragmentManager();
        DriverAlertFragment driverAlertFragment = DriverAlertFragment.newInstance();
        driverAlertFragment.show(fm, "fragment_notify_booking");
    }

    private void checkBookingStillAvailable(){

    }

    private void handleAcceptBooking() {
        isBusy = true;
        checkBookingStillAvailable();
    }

    private void sendDataToDriverProcessBookingViewModel() {
        DriverProcessBookingViewModel driverProcessBookingViewModel = ViewModelProviders.of(requireActivity()).get(DriverProcessBookingViewModel.class);
        driverProcessBookingViewModel.setCurrentUserObject(currentUserObject);
    }

    private void loadDriverProcessBookingFragment(){
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_driver_booking);
    }

    private void setDriverOfCurrentBooking() {
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        driverHomeViewModel = ViewModelProviders.of(requireActivity()).get(DriverHomeViewModel.class);
        driverHomeViewModel.getCurrentUserObject().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user == null) return;
                currentUserObject = user;
                sendDriverInfoDataToViewModel();
            }
        });

        //****************************** For booking synchronization ******************************//
        driverHomeViewModel.getAcceptBookingBtnPressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null) return;
                if (aBoolean) {
                    handleAcceptBooking();
                } else {
                    resetBookingFlow();
                }
                driverHomeViewModel.setAcceptBookingBtnPressed(null);
            }
        });


    }

    /*************************************************** For booking synchronization *****************************************************/


    @Override
    public void onMapReady(GoogleMap googleMap) {
        String apiKey = getString(R.string.google_maps_key);
        if (!Places.isInitialized()) { //Init GooglePlaceAutocomplete if not existed
            Places.initialize(requireActivity().getApplicationContext(), apiKey);
        }
        this.placesClient = Places.createClient(requireActivity().getApplicationContext());
        mMap = googleMap;
        requestPermission(); //Request user for location permission
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        mMap.getUiSettings().setZoomControlsEnabled(true);
        startLocationUpdate(); //Start location update listener
//        setUpCluster(); //Set up cluster on Google Map
        onGetPositionClick();  // Position the map.
    }
}