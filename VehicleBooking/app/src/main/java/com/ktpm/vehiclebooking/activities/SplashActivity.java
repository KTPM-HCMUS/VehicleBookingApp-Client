package com.ktpm.vehiclebooking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ktpm.vehiclebooking.R;

/**
 * Loading screen activity
 */
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);

//        Thread thread = new Thread(() -> {
//            LocationStreamingClient client = new LocationStreamingClient();
//            ExecutorService sendDriverLocationExecutor = Executors.newSingleThreadExecutor();
//            ExecutorService getLocationExecutor = Executors.newSingleThreadExecutor();
//            ExecutorService sendCustomerLocationExecutor = Executors.newSingleThreadExecutor();
//
//            client.sendLocation("customer_test", () -> {
//                Random rd = new Random();
//                return LocationOuterClass.Location.newBuilder()
//                        .setLatitude(rd.nextDouble())
//                        .setLongitude(rd.nextDouble())
//                        .build();
//            }, sendCustomerLocationExecutor);
//            client.sendLocation("driver_test", () -> {
//                Random rd = new Random();
//                return LocationOuterClass.Location.newBuilder()
//                        .setLatitude(rd.nextDouble())
//                        .setLongitude(rd.nextDouble())
//                        .build();
//            }, sendDriverLocationExecutor);
//            client.getLocation("customer_test", "driver_test", response -> {
//                System.out.println("======================================");
//                System.out.println(Thread.currentThread().getName());
//                System.out.println("customer_test latitude=" + response.getCustomerLocation().getLatitude() + ", longitude=" + response.getCustomerLocation().getLongitude());
//                System.out.println("driver_test location=" + response.getDriverLocation().getLatitude() + ", longitude=" + response.getDriverLocation().getLongitude());
//                System.out.println("======================================");
//            }, getLocationExecutor);
//            CountDownLatch countDownLatch = new CountDownLatch(1);
//            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//                sendCustomerLocationExecutor.shutdown();
//                sendDriverLocationExecutor.shutdown();
//                getLocationExecutor.shutdown();
//                countDownLatch.countDown();
//            }));
//            try {
//                countDownLatch.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        thread.start();
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            thread.stop();
//        }));
    }
}