package com.ktpm.vehiclebooking.ui.customer.booking;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.ClusterManager;
import com.ktpm.vehiclebooking.Constants;
import com.ktpm.vehiclebooking.LocationOuterClass;
import com.ktpm.vehiclebooking.R;
import com.ktpm.vehiclebooking.api.BookingAPI;
import com.ktpm.vehiclebooking.database.DBHandler;
import com.ktpm.vehiclebooking.model.Booking;
import com.ktpm.vehiclebooking.model.DriverBookingAccepted;
import com.ktpm.vehiclebooking.model.GoogleMaps.MyClusterItem;
import com.ktpm.vehiclebooking.model.ResponseTT;
import com.ktpm.vehiclebooking.model.User;
import com.ktpm.vehiclebooking.ui.customer.booking.checkout.CheckoutFragment;
import com.ktpm.vehiclebooking.ui.customer.booking.checkout.CheckoutViewModel;
import com.ktpm.vehiclebooking.ui.customer.booking.driver_info_bar.DriverInfoBarFragment;
import com.ktpm.vehiclebooking.ui.customer.booking.driver_info_bar.DriverInfoBarViewModel;
import com.ktpm.vehiclebooking.ui.customer.booking.dropoff.DropoffFragment;
import com.ktpm.vehiclebooking.ui.customer.booking.pickup.PickupFragment;
import com.ktpm.vehiclebooking.ui.customer.booking.popup_driver_arrived.PopupDriverArrivalFragment;
import com.ktpm.vehiclebooking.ui.customer.booking.popup_driver_arrived.PopupDriverArrivalViewModel;
import com.ktpm.vehiclebooking.ui.customer.booking.popup_driver_info.PopupDriverInfoFragment;
import com.ktpm.vehiclebooking.ui.customer.booking.popup_driver_info.PopupDriverInfoViewModel;
import com.ktpm.vehiclebooking.ui.customer.booking.processing_booking.ProcessingBookingFragment;
import com.ktpm.vehiclebooking.ui.customer.booking.processing_booking.ProcessingBookingViewModel;
import com.ktpm.vehiclebooking.utilities.DirectionsJSONParser;
import com.ktpm.vehiclebooking.utilities.LocationStreamingClient;
import com.ktpm.vehiclebooking.utilities.RadiusDistanceCalculation;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.netty.util.concurrent.CompleteFuture;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingFragment extends Fragment implements OnMapReadyCallback {

    private BookingViewModel mViewModel;
    DBHandler handler;
    //View elements
    private FloatingActionButton getMyLocationBtn;
    private FloatingActionButton restartBookingBtn;
    //Maps marker clustering
    private ClusterManager<MyClusterItem> clusterManager;

    //Google maps variables
    private static final int MY_LOCATION_REQUEST = 99;
    private SupportMapFragment supportMapFragment; //maps view
    private GoogleMap mMap;
    private FusedLocationProviderClient locationClient;
    private LocationRequest locationRequest;
    private Marker currentPickupLocationMarker;
    private Marker currentDropOffLocationMarker;
    private Marker currentUserLocationMarker;
    private Marker currentDriverLocationMarker;
    private Location currentUserLocation;
    private LatLng prevUserLocation;
    private MyClusterItem currentTargetLocationClusterItem;
    private LatLng prevTargetLocation;
    private ArrayList<Polyline> currentRoute = new ArrayList<>();
    private PlacesClient placesClient;

    User currentUserObject = null;

    //Booking info
    Place customerDropOffPlace;
    Place customerPickupPlace;
    Integer transportationType;
    Double distanceInKm;
    String distanceInKmString;
    String priceInVNDString;

    //Booking flow
    Boolean bookBtnPressed;
    Boolean cancelBookingBtnPressed;
    User currentDriver;
    User currentUser;

    public static BookingFragment newInstance() {
        return new BookingFragment();
    }

    private void initMapsFragment() {
        supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.fragment_maps);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_customer_booking, container, false);
        linkViewElements(view);
        initMapsFragment();
        setActionHandlers();
        handler = new DBHandler(getActivity());
        currentUser = (User) getActivity().getIntent().getExtras().get("user");
        return view;
    }



    private void linkViewElements(View rootView) {
        getMyLocationBtn = rootView.findViewById(R.id.fragmentMapsFindMyLocationBtn);
        restartBookingBtn = rootView.findViewById(R.id.fragmentMapsBackBtn);
    }


    private void setActionHandlers() {
        setGetMyLocationBtnHandler();
        setRestartBtnHandler();
    }

    private void setRestartBtnHandler() {
        restartBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBookingFlow();
            }
        });
    }


    private void resetBookingFlow() {
        removeAllMarkers();
        removeCurrentRoute();
        loadDropOffPlacePickerFragment();
        restartBookingBtn.setVisibility(View.GONE);
    }


    private void removeAllMarkers() {
        if (currentPickupLocationMarker != null) {
            currentPickupLocationMarker.remove();
            currentPickupLocationMarker = null;
        }
        if (currentDropOffLocationMarker != null) {
            currentDropOffLocationMarker.remove();
            currentDropOffLocationMarker = null;
        }
        if (currentDriverLocationMarker != null) {
            currentDriverLocationMarker.remove();
            currentDriverLocationMarker = null;
        }

    }


    private void loadDropOffPlacePickerFragment() {
        DropoffFragment dropoffFragment = new DropoffFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.booking_info, dropoffFragment).commit();
    }


    private void loadPickupPlacePickerFragment() {
        PickupFragment pickupFragment = new PickupFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.booking_info, pickupFragment).commit();
    }


    private void loadCheckoutFragment() {
        CheckoutFragment checkoutFragment = new CheckoutFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.booking_info, checkoutFragment).commit();
    }

    private void drawDropOffAndPickupMarkers() {
        currentPickupLocationMarker = mMap.addMarker(new MarkerOptions()
                .position(Objects.requireNonNull(customerPickupPlace.getLatLng()))
                .icon(bitmapDescriptorFromVector(
                        getActivity(),
                        R.drawable.ic_location_blue, Color.BLUE)
                )
                .title(customerPickupPlace.getAddress()));

        currentDropOffLocationMarker = mMap.addMarker(new MarkerOptions()
                .position(customerDropOffPlace.getLatLng())
                .icon(bitmapDescriptorFromVector(
                        getActivity(),
                        R.drawable.ic_location_red, Color.RED)
                )
                .title(customerDropOffPlace.getAddress()));

        currentPickupLocationMarker.showInfoWindow();
        currentDropOffLocationMarker.showInfoWindow();

        LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();
        latLngBounds.include(customerDropOffPlace.getLatLng());
        latLngBounds.include(customerPickupPlace.getLatLng());
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 200));
    }


    private void drawRouteFromPickupToDropOff() {
        String url = getRouteUrl(customerPickupPlace.getLatLng(), customerDropOffPlace.getLatLng(), "driving");
        FetchRouteDataTask fetchRouteDataTask = new FetchRouteDataTask();
        fetchRouteDataTask.execute(url);
    }


    private void setGetMyLocationBtnHandler() {
        getMyLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGetPositionClick();
            }
        });
    }


    private void smoothlyMoveCameraToPosition(LatLng latLng, float zoomLevel) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }


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


    private void updateCurrentUserLocationMarker(LatLng newLatLng) {
        if (currentUserLocationMarker != null) {
            currentUserLocationMarker.remove();
        }
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        resetBookingFlow();
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_LOCATION_REQUEST);
    }


    @SuppressLint({"MissingPermission", "RestrictedApi"})
    private void startLocationUpdate() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5 * 1000); //5s
        locationRequest.setFastestInterval(5 * 1000); //5s
        locationClient.requestLocationUpdates(locationRequest,
                new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        Location location = locationResult.getLastLocation();
                        LatLng latLng = new LatLng(location.getLatitude(),
                                location.getLongitude());
                        updateCurrentUserLocationMarker(latLng);
                    }
                }
                , null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        String apiKey = getString(R.string.google_maps_key);
        if (!Places.isInitialized()) {
            Places.initialize(requireActivity().getApplicationContext(), apiKey);
        }
        this.placesClient = Places.createClient(requireActivity().getApplicationContext());
        mMap = googleMap;
        requestPermission();
        locationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mMap.getUiSettings().setZoomControlsEnabled(true);
        startLocationUpdate();
        onGetPositionClick();
    }

    private void removeCurrentRoute() {
        if (currentRoute.isEmpty()) return;
        for (Polyline polyline : currentRoute) {
            polyline.remove();
        }
        currentRoute.clear();
    }


    @SuppressLint("DefaultLocale")
    private void sendCheckoutInfoToCheckoutFragment() {
        CheckoutViewModel checkoutViewModel = ViewModelProviders.of(requireActivity()).get(CheckoutViewModel.class);
        distanceInKmString = String.format("%.1fkm", distanceInKm);
        checkoutViewModel.setDistanceInKmString(distanceInKmString);
        int price;
        if (transportationType.equals(Constants.Transportation.Type.bikeType)) {
            price = (int) (distanceInKm * Constants.Transportation.UnitPrice.bikeType);
        } else {
            price = (int) (distanceInKm * Constants.Transportation.UnitPrice.carType);
        }
        priceInVNDString = Integer.toString(price) + " VND";
        checkoutViewModel.setPriceInVNDString(priceInVNDString);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.setCustomerSelectedDropOffPlace(null);
        mViewModel.setCustomerSelectedPickupPlace(null);
        mViewModel.setTransportationType(0);
        mViewModel.setBookBtnPressed(null);
        mViewModel.setCancelBookingBtnPressed(null);
    }

    private void sendDataToProcessBookingViewModel() {
        ProcessingBookingViewModel processingBookingViewModel = ViewModelProviders.of(requireActivity()).get(ProcessingBookingViewModel.class);
        processingBookingViewModel.setDropOffPlaceString(customerDropOffPlace.getName());
        processingBookingViewModel.setPickupPlaceString(customerPickupPlace.getName());
        processingBookingViewModel.setPriceInVNDString(priceInVNDString);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(BookingViewModel.class);

        mViewModel.getCurrentUserObject().observe(requireActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUserObject = user;
            }
        });

        mViewModel.getCustomerSelectedDropOffPlace().observe(getViewLifecycleOwner(), new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                if (place == null) return;
                customerDropOffPlace = place;
                restartBookingBtn.setVisibility(View.VISIBLE); //Show back button
                loadPickupPlacePickerFragment();
                smoothlyMoveCameraToPosition(
                        new LatLng(currentUserLocation.getLatitude(), currentUserLocation.getLongitude()),
                        Constants.GoogleMaps.CameraZoomLevel.betweenStreetsAndBuildings);
            }
        });

        mViewModel.getCustomerSelectedPickupPlace().observe(getViewLifecycleOwner(), new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                if (place == null) return;
                customerPickupPlace = place;
                loadCheckoutFragment();
                drawDropOffAndPickupMarkers();
                drawRouteFromPickupToDropOff();
            }
        });

        mViewModel.getTransportationType().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer s) {
                if (s == null) return;
                transportationType = s;
            }
        });

        mViewModel.getBookBtnPressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null) return;
                restartBookingBtn.setVisibility(View.GONE);
                removeCurrentRoute();
                String token = handler.readDB().get(1);
                Booking booking = new Booking(customerPickupPlace.getAddress(), customerDropOffPlace.getAddress(),
                        customerPickupPlace.getLatLng().latitude, customerPickupPlace.getLatLng().longitude,
                        customerDropOffPlace.getLatLng().latitude, customerDropOffPlace.getLatLng().longitude, transportationType, priceInVNDString);
                CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
                    createNewBooking(token, booking);
                });
                try {
                    future.get(30, TimeUnit.SECONDS);
                }
                catch (ExecutionException e){
                    Toast.makeText(requireActivity(), Constants.ToastMessage.addNewBookingToDbFail, Toast.LENGTH_SHORT).show();
                    resetBookingFlow();
                }
                catch (InterruptedException e){
                    Toast.makeText(requireActivity(), Constants.ToastMessage.addNewBookingToDbFail, Toast.LENGTH_SHORT).show();
                    resetBookingFlow();
                }
                catch (TimeoutException e){
                    Toast.makeText(requireActivity(), Constants.ToastMessage.addNewBookingToDbFail, Toast.LENGTH_SHORT).show();
                    resetBookingFlow();
                }
                setDetectAcceptedDriver();
                sendDataToProcessBookingViewModel();
                loadProcessingBookingFragment();
                setListenerForDriverRoute();
            }
        });

        mViewModel.getCancelBookingBtnPressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null) return;
                resetBookingFlow();
                cancelBooking();
            }
        });
    }


    private void loadProcessingBookingFragment() {
        ProcessingBookingFragment processingBookingFragment = new ProcessingBookingFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.booking_info, processingBookingFragment).commit();
    }


    private void loadDriverInfoBarFragment() {
        DriverInfoBarFragment driverInfoBarFragment = new DriverInfoBarFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.booking_info, driverInfoBarFragment).commit();
    }

    private void loadPopupFoundedDriverInfo() {
        FragmentManager fm = getChildFragmentManager();
        PopupDriverInfoFragment popupDriverInfoFragment = PopupDriverInfoFragment.newInstance();
        popupDriverInfoFragment.show(fm, "fragment_notify_founded_driver");
    }

    private void loadPopupDriverArrivalFragment() {
        FragmentManager fm = getChildFragmentManager();
        PopupDriverArrivalFragment popUpDriverArrivalFragment = PopupDriverArrivalFragment.newInstance();
        popUpDriverArrivalFragment.show(fm, "fragment_notify_driver_arrived");
    }


    private void loadCustomerRatingFragment() {
        FragmentManager fm = getChildFragmentManager();
    }


    private void createNewBooking(String token, Booking booking) {
        try {
            Response<DriverBookingAccepted> response = BookingAPI.apiService.booking(token, booking).execute();
            if (response.body().getUserId() == null){
                createNewBooking(token, booking);
            }
            else {
                User driver = new User();
                driver.setUserID(response.body().getUserId());
                driver.setname(response.body().getName());
                driver.setvehicle_plate(response.body().getVehiclePlate());
                driver.settype(response.body().getTypeOfVehicle());
                driver.setRole(1);
                currentDriver = driver;
            }
        }
        catch (IOException e) {
            Toast.makeText(requireActivity(), Constants.ToastMessage.addNewBookingToDbFail, Toast.LENGTH_SHORT).show();
            resetBookingFlow();
        }
//        User driver = new User();
//        driver.setUserID("0909090901");
//        driver.setname("chien");
//        driver.setvehicle_plate("123AB123");
//        driver.settype(1);
//        driver.setRole(1);
//        currentDriver = driver;
    }

    private void sendDataToInfoBarViewModel() {
        DriverInfoBarViewModel driverInfoBarViewModel = ViewModelProviders.of(requireActivity()).get(DriverInfoBarViewModel.class);
        driverInfoBarViewModel.setDriver(currentDriver);
    }


    private void sendDataToPopupDriverArrivalViewModel() {
        PopupDriverArrivalViewModel popupDriverArrivalViewModel = ViewModelProviders.of(requireActivity()).get(PopupDriverArrivalViewModel.class);
        popupDriverArrivalViewModel.setDriver(currentDriver);
    }


    private void setDetectAcceptedDriver() {
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa");
        sendDriverObjectToPopupDriverViewModel();
        loadPopupFoundedDriverInfo();
        sendDataToInfoBarViewModel();
        loadDriverInfoBarFragment();
    }


    private void setListenerForDriverRoute() {
        int resourceType;
        LocationStreamingClient client = new LocationStreamingClient();
        ExecutorService sendUserLocationExecutor = Executors.newSingleThreadExecutor();
        ExecutorService getDriverLocationExecutor = Executors.newSingleThreadExecutor();
        if (transportationType.equals(Constants.Transportation.Type.carType)) {
            resourceType = R.drawable.ic_checkout_car;
        } else {
            resourceType = R.drawable.ic_checkout_bike;
        }
        client.sendLocation(currentUser.getUserID(),() -> {
            return LocationOuterClass.Location.newBuilder()
                        .setLatitude(customerPickupPlace.getLatLng().latitude)
                        .setLongitude(customerPickupPlace.getLatLng().longitude)
                        .build();
        }, sendUserLocationExecutor);
        client.getLocation(currentUser.getUserID(), currentDriver.getUserID(), response->{
            if (currentDriverLocationMarker != null) {
                currentDriverLocationMarker.remove();
                currentDriverLocationMarker = null;
            }
            currentDriverLocationMarker = mMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(response.getDriverLocation().getLatitude(),
                                    response.getDriverLocation().getLongitude()))
                            .icon(bitmapDescriptorFromVector(
                                    getActivity(),
                                    resourceType, Color.RED)
                            )
                            .title("Driver is here!")
            );
            double distanceToArrival = RadiusDistanceCalculation.distance(response.getDriverLocation().getLatitude(), response.getDriverLocation().getLongitude(),
                    response.getCustomerLocation().getLatitude(), response.getCustomerLocation().getLongitude(), 'M');
            if (distanceToArrival < 0.05D){
                sendDataToPopupDriverArrivalViewModel();
                loadPopupDriverArrivalFragment();
            }
            double distanceToFinished = RadiusDistanceCalculation.distance(response.getDriverLocation().getLatitude(), response.getDriverLocation().getLongitude(),
                    customerDropOffPlace.getLatLng().latitude, customerDropOffPlace.getLatLng().longitude, 'M');
            if (distanceToFinished < 0.05D){
                Toast.makeText(requireActivity(), "Your trip was over. Thank you", Toast.LENGTH_SHORT).show();
                resetBookingFlow();
                getDriverLocationExecutor.shutdown();
                sendUserLocationExecutor.shutdown();
            }
        }, getDriverLocationExecutor);
    }



    private void sendDriverObjectToPopupDriverViewModel() {
        PopupDriverInfoViewModel popupDriverInfoViewModel = ViewModelProviders.of(requireActivity()).get(PopupDriverInfoViewModel.class);
        popupDriverInfoViewModel.setDriver(currentDriver);
    }

    private void cancelBooking() {

    }


    private void drawRoute(List<List<HashMap<String, String>>> result) {
        for (Polyline polyline : currentRoute) {
            polyline.remove();
        }
        currentRoute.clear();

        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;

        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList();
            lineOptions = new PolylineOptions();

            List<HashMap<String, String>> route = result.get(i);

            for (int j = 0; j < route.size(); j++) {
                HashMap<String, String> point = route.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            lineOptions.addAll(points);
            lineOptions.width(12);
            lineOptions.color(Color.RED);
            lineOptions.geodesic(true);

        }
        currentRoute.add(mMap.addPolyline(lineOptions));
    }

    private class FetchRouteDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = fetchDataFromURL(url[0]);
            } catch (Exception ignored) {
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            RouteParserTask routeParserTask = new RouteParserTask();
            routeParserTask.execute(result);
        }
    }



    private class RouteParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser(jObject);
                routes = parser.getRoutes();
                distanceInKm = parser.getTotalDistanceInKm();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            sendCheckoutInfoToCheckoutFragment();
            drawRoute(result);
        }
    }

    private String getRouteUrl(LatLng origin, LatLng destination, String directionMode) {
        String originParam = Constants.GoogleMaps.DirectionApi.originParam +
                "=" + origin.latitude + "," + origin.longitude;
        String destinationParam = Constants.GoogleMaps.DirectionApi.destinationParam +
                "=" + destination.latitude + "," + destination.longitude;
        String modeParam = Constants.GoogleMaps.DirectionApi.modeParam + "=" + directionMode;
        String params = originParam + "&" + destinationParam + "&" + modeParam;
        String output = Constants.GoogleMaps.DirectionApi.outputParam;
        return Constants.GoogleMaps.DirectionApi.baseUrl + output + "?" + params
                + "&key=" + getString(R.string.google_maps_key);
    }

    private String fetchDataFromURL(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}