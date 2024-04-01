package com.leslie.socialink.activity.friend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.leslie.socialink.base.BaseActivity;
import com.leslie.socialink.utils.PermissionHelper;
import com.leslie.socialink.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class GPSActivity extends BaseActivity {
    private LocationManager locationManager;
    private String locationProvider = null;

    private final LocationListener locationListener = location -> {
        Log.i("GPSActivity", "location changed：[" + location.getLongitude() + ", " + location.getLatitude() + "]");
    };

    private final IPermissionsRequestListener permissionListener = new IPermissionsRequestListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onAllow() {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // 获取所有可用的位置提供器
            List<String> providers = locationManager.getProviders(true);
            if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                locationProvider = LocationManager.NETWORK_PROVIDER;
            } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
                locationProvider = LocationManager.GPS_PROVIDER;
            }
            locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location == null) {
                return;
            }
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            Log.i("GPSActivity", "经纬度：" + longitude + "， " + latitude);
            Intent intent = new Intent()
                    .putExtra("longitude", longitude)
                    .putExtra("latitude", latitude)
                    .setClass(GPSActivity.this, FriendActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onReject() {
            Utils.toastShort(GPSActivity.this, "缺少权限");
            finish();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission(Arrays.asList(PermissionHelper.PERMISSIONS_GPS), 200, permissionListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }

}