package com.example.smsmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main extends AppCompatActivity implements LocationListener  {
    protected static final String TAG = "MainActivity";
    Integer i = 0;
    Address finalad;
    int permsRequestCode = 200;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    LocationManager locationManager;
    TextView help,loc,status;
    ImageView set;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    boolean GpsStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        set=findViewById(R.id.setting);
        loc=findViewById(R.id.add);
        status=findViewById(R.id.status);
        help=findViewById(R.id.help);
        sharedPreferences =getSharedPreferences("myname",MODE_PRIVATE);
        final String msg1=sharedPreferences.getString("msg1","");
        final String msg2=sharedPreferences.getString("msg2","");
        final String msg3=sharedPreferences.getString("msg3","");
        final String name = sharedPreferences.getString("username","");
        final String no1 = sharedPreferences.getString("number1","");
        final String no2 = sharedPreferences.getString("number2","");
        final String no3 = sharedPreferences.getString("number3","");
        final String numbers[]={no1,no2,no3};


        if (ContextCompat.checkSelfPermission(Main.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED&& ContextCompat.checkSelfPermission(Main.
                this, Manifest.permission.SEND_SMS)  == PackageManager.PERMISSION_GRANTED)
        {
            locationManager = (LocationManager) getSystemService(Main.this.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 50, this);


            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
             locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,50,this);
            }

        }
        else
        {
            ActivityCompat.requestPermissions(Main.this,new String[]{Manifest.permission.
                    ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.
                    permission.SEND_SMS},permsRequestCode);
        }

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Main.this,Setting.class);
                startActivity(intent);

            }
        });


//for checking internet connectivity
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Main.this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else {
            connected = false;
        }

        if (!connected)
        {

            Toast.makeText(this, "Please turn on mobile data or connect to wi-fi for access your current location", Toast.LENGTH_SHORT).show();

        }

    help.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckGpsStatus();

            if (GpsStatus) {
if (no1.isEmpty()&no2.isEmpty()&no3.isEmpty())
{
    Toast.makeText(Main.this, "Please set contact numbers !!", Toast.LENGTH_SHORT).show();
}
                final SmsManager smsManager = SmsManager.getDefault();
                for (int j = 0; j < 3; j++)//for using set array list index in sms manager

                {
                    if (numbers[j].equals("")) {



                    } else {
                        if (!msg1.isEmpty()) {
                            smsManager.sendTextMessage("" + numbers[j], null,
                                    "" + msg1 + " - " + name, null, null);
                        }
                        Handler handler = new Handler();

                        final int finalJ = j;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
if (!msg2.isEmpty()) {
    smsManager.sendTextMessage("" + numbers[finalJ],
            null, "" + msg2 + " - " + name, null,
            null);
} }
                        }, 1000);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
if (!msg3.isEmpty()) {
    smsManager.sendTextMessage("" + numbers[finalJ],
            null, "" + msg3 + " - " + name, null,
            null);
}  }
                        }, 5000);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                smsManager.sendTextMessage("" + numbers[finalJ],
                                        null, "Its my Location - " + finalad.getAddressLine(0), null,
                                        null);
                            }
                        }, 3000);
                        Toast.makeText(Main.this, "Messages send successfully...!", Toast.LENGTH_LONG).show();
                    }
                }
            }
else
            {
                displayLocationSettingsRequest(Main.this);
            }
        }


    });
    status.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckGpsStatus();


            if (GpsStatus)
            {
                if (loc.length()<20) {
                    final ProgressDialog  progressDialog = new ProgressDialog(Main.this);
                    progressDialog.setMessage("Your Location Detecting!!!");
                    progressDialog.show();
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    },10000);

                }
            else
            {

                final SmsManager smsManager = SmsManager.getDefault();
                for (int j = 0; j < 3; j++)//for using set array list index in sms manager

                {
                    if (numbers[j].equals(""))
                    {
                        return;
                    }
                    else
                    {
                        smsManager.sendTextMessage("" + numbers[j], null,
                                "My location-"+finalad.getAddressLine(0), null, null);


                        Toast.makeText(Main.this,"Your Status send successfully...!",Toast.LENGTH_LONG).show();
                    }
                }

            }



        }


            else
            {

                displayLocationSettingsRequest(Main.this);
            }
        }




    });
    }
    public void CheckGpsStatus(){

        locationManager = (LocationManager)Main.this.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }




        @Override
        public void onLocationChanged(Location location) {

        double lat = location.getLatitude();
        double lng = location.getLongitude();




        Geocoder geocoder = new Geocoder(Main.this, Locale.ENGLISH);


        try {

            List<Address>ad=geocoder.getFromLocation(lat,lng,10);
            finalad = ad.get(0);
           loc.setText(""+getString(R.string.loc)+finalad.getAddressLine(0));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                       Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(Main.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    /*
        sharedPreferences = getSharedPreferences("myname",MODE_PRIVATE);

        String num1 = sharedPreferences.getString("number1","");
         String num2 = sharedPreferences.getString("number2","");
        String num3 = sharedPreferences.getString("number3","");


no1.setText(""+num1);
no2.setText(""+num2);
no3.setText(""+num3);



        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 50, this);


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences =getSharedPreferences("myname",MODE_PRIVATE);
                SharedPreferences.Editor s = sharedPreferences.edit();
                s.clear();
                s.commit();

                no.clear();
                no1.setText("");
                no2.setText("");
                no3.setText("");
            }
        });
        set.setOnClickListener(new View.OnClickListener() {


    @Override
    public void onClick(View v) {
        sharedPreferences = getSharedPreferences("myname",MODE_PRIVATE);

        String a,b,c;
        a=no1.getText().toString();
        b=no2.getText().toString();
        c=no3.getText().toString();

        SharedPreferences.Editor s = sharedPreferences.edit();
        s.putString("number1",a);
        s.putString("number2",b);
        s.putString("number3",c);
        s.commit();






    }
});

        no.add(num1);
        no.add(num2);
        no.add(num3);




            btn.setOnClickListener(new View.OnClickListener()
            {

                @Override

                public void onClick(View v) {


                    final SmsManager smsManager = SmsManager.getDefault();
                    for (int j = 0; j < 3; j++)//for using set array list index in sms manager

                    {

                        smsManager.sendTextMessage("" + no.get(j), null,
                                "Help me", null, null);

                        Handler handler = new Handler();

                            final int finalJ = j;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    smsManager.sendTextMessage("" + no.get(finalJ),
                                            null, "I am Trevelling", null,
                                            null);
                                }
                            }, 100);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                smsManager.sendTextMessage("" + no.get(finalJ), null, "help", null, null);
                            }
                        }, 1000);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                smsManager.sendTextMessage("" + no.get(finalJ), null, "Its my Location -"+finalad.getAddressLine(0), null, null);
                            }
                        }, 100);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                smsManager.sendTextMessage("" + no.get(finalJ), null, "Find me", null, null);
                            }
                        }, 100000);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                smsManager.sendTextMessage("" + no.get(finalJ), null, "Its my Location -"+finalad.getAddressLine(0), null, null);
                            }
                        }, 2000);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                smsManager.sendTextMessage("" + no.get(finalJ), null, "Its my Location -"+finalad.getAddressLine(0), null, null);
                            }
                        }, 20000);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                smsManager.sendTextMessage("" + no.get(finalJ), null, "Its my Location -"+finalad.getAddressLine(0), null, null);
                            }
                        }, 5000);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                smsManager.sendTextMessage("" + no.get(finalJ), null, "Its my Location -"+finalad.getAddressLine(0), null, null);
                            }
                        }, 50000);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                smsManager.sendTextMessage("" + no.get(finalJ), null, "Its my Location -"+finalad.getAddressLine(0), null, null);
                            }
                        }, 8000);
                        }

                       Toast.makeText(Main.this,"Sending Sms",Toast.LENGTH_LONG).show();




        }
});
    }

    @Override
    public void onLocationChanged(Location location) {

        double lat = location.getLatitude();
        double lng = location.getLongitude();




        Geocoder geocoder = new Geocoder(Main.this, Locale.ENGLISH);


        try {

           List<Address>ad=geocoder.getFromLocation(lat,lng,10);
        finalad = ad.get(0);
          h.setText(""+getString(R.string.loc)+finalad);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }*/

}