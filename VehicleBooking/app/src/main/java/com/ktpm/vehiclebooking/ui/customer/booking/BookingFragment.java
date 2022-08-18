package com.ktpm.vehiclebooking.ui.customer.booking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.ClusterManager;
import com.ktpm.vehiclebooking.R;
import com.ktpm.vehiclebooking.model.GoogleMaps.MyClusterItem;

import java.util.Objects;

public class BookingFragment extends Fragment implements OnMapReadyCallback {
    private BookingViewModel mViewModel;

    //View elements
    private FloatingActionButton getMyLocationBtn;
    private FloatingActionButton restartBookingBtn;

    //Maps marker clustering
    private ClusterManager<MyClusterItem> clusterManager;

    //Google maps variables
    private static final int MY_LOCATION_REQUEST = 99;
    private SupportMapFragment supportMapFragment; //maps view
    private GoogleMap mMap;

    private LocationRequest locationRequest;
    private Marker currentPickupLocationMarker;
    private Marker currentDropOffLocationMarker;
    private Marker currentUserLocationMarker;
    private Marker currentDriverLocationMarker;
    private Location currentUserLocation;

    Place customerDropOffPlace;
    Place customerPickupPlace;

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

        //Smoothly move camera to include 2 points in the map
        LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();
        latLngBounds.include(customerDropOffPlace.getLatLng());
        latLngBounds.include(customerPickupPlace.getLatLng());
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 200));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

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
}
