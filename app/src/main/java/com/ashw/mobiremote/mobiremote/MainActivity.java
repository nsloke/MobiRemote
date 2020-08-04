 package com.ashw.mobiremote.mobiremote;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

 public class MainActivity extends AppCompatActivity {
    Button btnStrt,btnStp,btnWht,btnftc;
     private Context mContext;
     private Activity mActivity;
     private static final int MY_PERMISSIONS_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mActivity = MainActivity.this;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermission();
        }
        btnStrt=(Button) findViewById(R.id.btnStartService);
        btnStp=(Button) findViewById(R.id.btnStopService);
        btnWht=(Button) findViewById(R.id.btnWhitelists);
        btnftc=(Button) findViewById(R.id.btnfetchinf);

        btnStrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MyService.class);
               // i.putExtra("name", "SurvivingwithAndroid");
                MainActivity.this.startService(i);
            }
        });


        btnStp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MyService.class);
                MainActivity.this.stopService(i);
            }
        });

        btnWht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WhitelistActivity.class);
                // i.putExtra("name", "SurvivingwithAndroid");
                MainActivity.this.startActivity(i);

            }
        });

        btnftc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FetchInfex.class);
                // i.putExtra("name", "SurvivingwithAndroid");
                MainActivity.this.startActivity(i);

            }
        });




    }





     protected void checkPermission(){
         if(ContextCompat.checkSelfPermission(mActivity,Manifest.permission.BLUETOOTH)
                 + ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.READ_CONTACTS)
                 + ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.READ_CALL_LOG)
                 +ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.BLUETOOTH_ADMIN)
                 +ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.RECEIVE_SMS)
                 +ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.SEND_SMS)
                 +ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.ACCESS_FINE_LOCATION)
                 +ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.INTERNET)
                 +ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.CHANGE_WIFI_STATE)
                 +ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.ACCESS_WIFI_STATE)
                 +ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.CHANGE_NETWORK_STATE)
                 +ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.READ_CONTACTS)
                 +ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.WRITE_CONTACTS)
                 +ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.READ_CALL_LOG)
                 +ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.WRITE_CALL_LOG)
                 +ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.READ_PHONE_STATE)
                 +ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.PROCESS_OUTGOING_CALLS)
                 +ContextCompat.checkSelfPermission(
                 mActivity,Manifest.permission.WRITE_CALENDAR)

                 != PackageManager.PERMISSION_GRANTED){

             // Do something, when permissions not granted
             if(ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.BLUETOOTH)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.READ_CONTACTS)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.READ_CALL_LOG)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.BLUETOOTH_ADMIN)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.RECEIVE_SMS)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.SEND_SMS)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.ACCESS_FINE_LOCATION)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.INTERNET)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.CHANGE_WIFI_STATE)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.ACCESS_WIFI_STATE)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.CHANGE_NETWORK_STATE)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.READ_CONTACTS)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.WRITE_CONTACTS)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.READ_CALL_LOG)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.WRITE_CALL_LOG)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.READ_PHONE_STATE)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.PROCESS_OUTGOING_CALLS)
                     || ActivityCompat.shouldShowRequestPermissionRationale(
                     mActivity,Manifest.permission.WRITE_CALENDAR)
                     ){
                 // If we should give explanation of requested permissions

                 // Show an alert dialog here with request explanation
                 AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                 builder.setMessage("Camera, Read Contacts and Write External" +
                         " Storage permissions are required to do the task.");
                 builder.setTitle("Please grant those permissions");
                 builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         ActivityCompat.requestPermissions(
                                 mActivity,
                                 new String[]{
                                         Manifest.permission.BLUETOOTH,
                                         Manifest.permission.READ_CONTACTS,
                                         Manifest.permission.READ_CALL_LOG,
                                         Manifest.permission.BLUETOOTH_ADMIN,
                                         Manifest.permission.RECEIVE_SMS,
                                         Manifest.permission.SEND_SMS,
                                         Manifest.permission.ACCESS_FINE_LOCATION,
                                         Manifest.permission.INTERNET,
                                         Manifest.permission.CHANGE_WIFI_STATE,
                                         Manifest.permission.ACCESS_WIFI_STATE,
                                         Manifest.permission.CHANGE_NETWORK_STATE,
                                         Manifest.permission.READ_CONTACTS,
                                         Manifest.permission.WRITE_CONTACTS,
                                         Manifest.permission.READ_CALL_LOG,
                                         Manifest.permission.WRITE_CALL_LOG,
                                         Manifest.permission.READ_PHONE_STATE,
                                         Manifest.permission.PROCESS_OUTGOING_CALLS,
                                         Manifest.permission.WRITE_CALENDAR

                                 },
                                 MY_PERMISSIONS_REQUEST_CODE
                         );
                     }
                 });
                 builder.setNeutralButton("Cancel",null);
                 AlertDialog dialog = builder.create();
                 dialog.show();
             }else{
                 // Directly request for required permissions, without explanation
                 ActivityCompat.requestPermissions(
                         mActivity,
                         new String[]{
                                 Manifest.permission.BLUETOOTH,
                                 Manifest.permission.READ_CONTACTS,
                                 Manifest.permission.READ_CALL_LOG,
                                 Manifest.permission.BLUETOOTH_ADMIN,
                                 Manifest.permission.RECEIVE_SMS,
                                 Manifest.permission.SEND_SMS,
                                 Manifest.permission.ACCESS_FINE_LOCATION,
                                 Manifest.permission.INTERNET,
                                 Manifest.permission.CHANGE_WIFI_STATE,
                                 Manifest.permission.ACCESS_WIFI_STATE,
                                 Manifest.permission.CHANGE_NETWORK_STATE,
                                 Manifest.permission.READ_CONTACTS,
                                 Manifest.permission.WRITE_CONTACTS,
                                 Manifest.permission.READ_CALL_LOG,
                                 Manifest.permission.WRITE_CALL_LOG,
                                 Manifest.permission.READ_PHONE_STATE,
                                 Manifest.permission.PROCESS_OUTGOING_CALLS,
                                 Manifest.permission.WRITE_CALENDAR
                         },
                         MY_PERMISSIONS_REQUEST_CODE
                 );
             }
         }else {
             // Do something, when permissions are already granted
             Toast.makeText(mContext,"Permissions already granted",Toast.LENGTH_SHORT).show();
         }
     }

     @Override
     public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
         switch (requestCode){
             case MY_PERMISSIONS_REQUEST_CODE:{
                 // When request is cancelled, the results array are empty
                 if(
                         (grantResults.length >0) &&
                                 (grantResults[0]
                                         + grantResults[1]
                                         + grantResults[2]
                                         == PackageManager.PERMISSION_GRANTED
                                 )
                         ){
                     // Permissions are granted
                     Toast.makeText(mContext,"Permissions granted.",Toast.LENGTH_SHORT).show();
                 }else {
                     // Permissions are denied
                     Toast.makeText(mContext,"Permissions denied.",Toast.LENGTH_SHORT).show();
                 }
                 return;
             }
         }
     }






}
