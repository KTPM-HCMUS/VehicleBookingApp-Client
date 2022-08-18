package com.ktpm.vehiclebooking.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;
import com.ktpm.vehiclebooking.R;
import com.ktpm.vehiclebooking.model.User;
import com.ktpm.vehiclebooking.ui.customer.booking.BookingViewModel;
import com.ktpm.vehiclebooking.ui.customer.booking.checkout.CheckoutViewModel;
import com.ktpm.vehiclebooking.ui.customer.booking.dropoff.DropoffViewModel;
import com.ktpm.vehiclebooking.ui.customer.booking.pickup.PickupViewModel;
import com.ktpm.vehiclebooking.ui.customer.home.CustomerHomeViewModel;
import com.ktpm.vehiclebooking.ui.user_profile.UserProfileViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private TextView navHeaderEmailTextView;
    private TextView navHeaderUsernameTextView;

    User currentUserObject = null;

    CustomerHomeViewModel customerHomeViewModel;
    DropoffViewModel dropoffViewModel;
    PickupViewModel pickupViewModel;
    BookingViewModel bookingViewModel;
    CheckoutViewModel checkoutViewModel;
    UserProfileViewModel userProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linkViewElements();
        initAllChildFragmentsViewModel();
        initCurrentUserInfo();
        setNavHeaderEmailAndUsername();
        setAllChildFragmentsViewModelData();
        navigationDrawerSetup();
    }

    private void linkViewElements() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        LinearLayout navHeaderView = (LinearLayout) navigationView.getHeaderView(0);
        navHeaderUsernameTextView = (TextView) navHeaderView.getChildAt(1);
        navHeaderEmailTextView = (TextView) navHeaderView.getChildAt(2);
    }

    private void navigationDrawerSetup() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_customer_home,
                R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigateAndHideAccordingMenuBasedOnRole(navController);
    }


    private void onLogoutOptionClick() {
        Intent i = new Intent(MainActivity.this, StartActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_logout:
                onLogoutOptionClick();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void initCurrentUserInfo(){
        this.currentUserObject = getCurrentUserObject();
    }

    private User getCurrentUserObject() {
        User user = null;
        if (getIntent().getExtras()!=null){
             user = (User) getIntent().getExtras().get("user");
        }
        return user;
    }


    private void navigateAndHideAccordingMenuBasedOnRole(NavController navController){
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        if (currentUserObject.getRole()==2) {
            MenuItem driverHomeMenuItem = menu.getItem(1);
            driverHomeMenuItem.setVisible(false);
            navController.navigate(R.id.nav_customer_home);
        }
    }


    private void initAllChildFragmentsViewModel() {
        customerHomeViewModel = ViewModelProviders.of(this).get(CustomerHomeViewModel.class);
        dropoffViewModel = ViewModelProviders.of(this).get(DropoffViewModel.class);
        pickupViewModel = ViewModelProviders.of(this).get(PickupViewModel.class);
        bookingViewModel = ViewModelProviders.of(this).get(BookingViewModel.class);
        checkoutViewModel = ViewModelProviders.of(this).get(CheckoutViewModel.class);
        userProfileViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
    }

    private void setNavHeaderEmailAndUsername() {
        navHeaderEmailTextView.setText(currentUserObject.getEmail());
        navHeaderUsernameTextView.setText(currentUserObject.getname());
    }

    private void setAllChildFragmentsViewModelData() {
        if (currentUserObject.getRole()==2) {
            customerHomeViewModel.setCurrentUserObject(currentUserObject);
        }
        dropoffViewModel.setCurrentUserObject(currentUserObject);
        pickupViewModel.setCurrentUserObject(currentUserObject);
        bookingViewModel.setCurrentUserObject(currentUserObject);
        userProfileViewModel.setCurrentUserObject(currentUserObject);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}