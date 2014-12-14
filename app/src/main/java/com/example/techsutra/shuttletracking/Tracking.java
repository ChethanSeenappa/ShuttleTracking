package com.example.techsutra.shuttletracking;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
public class Tracking extends Activity implements LocationListener {

    Button btnGPSShowLocation;
    Button btnNWShowLocation;
    AppLocationService appLocationService;
    private AppController appController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tracking);

        appLocationService = new AppLocationService(Tracking.this);

        btnGPSShowLocation = (Button) findViewById(R.id.gpsLocation);
        btnGPSShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Location location = appLocationService
                        .getLocation(LocationManager.GPS_PROVIDER);

                sendData(location,LocationManager.GPS_PROVIDER);
            }
        });
        btnNWShowLocation = (Button) findViewById(R.id.showLocationNetwork);
        btnNWShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Location nwLocation = appLocationService
                        .getLocation(LocationManager.NETWORK_PROVIDER);
                sendData(nwLocation,LocationManager.NETWORK_PROVIDER);
            }
        });

    }

    private void sendData(Location location, String dataprovider){
        try{


        if(location != null){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getAddressLine(1);
            String country = addresses.get(0).getAddressLine(2);

            appController  = new AppController("128.199.218.81",5005,"City"+ city +"Address"+ address +"Latitude"+String.valueOf(latitude) +"Longitude"+String.valueOf(longitude));
            appController.sendData();
            Toast.makeText(
                    getApplicationContext(),
                    "Mobile Location (NW): \nLatitude: " + latitude
                            + "\nLongitude: " + longitude,
                    Toast.LENGTH_LONG).show();
        }else {
            switch (dataprovider){
                case LocationManager.NETWORK_PROVIDER:
                    showSettingsAlert("NETWORK");
                    break;
                case LocationManager.GPS_PROVIDER:
                    showSettingsAlert("GPS");
            }
        }
        }catch(IOException io){

        }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tracking, menu);
        return true;
    }


    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Tracking.this);

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        Tracking.this.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        appController  = new AppController("128.199.218.81",5005,"Location Change");
        appController.sendData();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


}
