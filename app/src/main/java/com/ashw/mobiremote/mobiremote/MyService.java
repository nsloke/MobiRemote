package com.ashw.mobiremote.mobiremote;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.ashw.mobiremote.mobiremote.RinginCall;
import com.scottyab.aescrypt.AESCrypt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Vector;

public class MyService extends Service implements LocationListener {

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    public boolean canGetLocation = false;

    Location location;

    double latitude=0;
    double longitude=0;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;

    Context context;
    public String strbat = "";

    public MyService() {
        //this.context=getBaseContext().getApplicationContext();

    }


    SmsReceiver smsReceiver = new SmsReceiver();
    RinginCall ringcall=new RinginCall();
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(smsReceiver);
        unregisterReceiver(this.mBatInfoReceiver);
        unregisterReceiver(ringcall);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // return super.onStartCommand(intent, flags, startId);
        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        registerReceiver(ringcall,new IntentFilter("android.intent.action.PHONE_STATE"));

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            //batteryTxt.setText(String.valueOf(level) + "%");
            strbat = String.valueOf(level);
        }
    };


    class SmsReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            String telnr = "", nachricht = "";

            Bundle extras = intent.getExtras();

            if (extras != null) {
                Object[] pdus = (Object[]) extras.get("pdus");
                if (pdus != null) {

                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = getIncomingMessage(pdu, extras);
                        telnr = smsMessage.getDisplayOriginatingAddress();
                        Log.d("LogService", telnr);
                        nachricht += smsMessage.getDisplayMessageBody();
                        Log.d("LogServiceMessage", nachricht);


                        //SmsManager sm = SmsManager.getDefault();
                        // sm.sendTextMessage(telnr, null, nachricht, null, null);

                    }

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String value = preferences.getString("passkey", "");

                    AESUtils.keyValue=value.getBytes();
                    String password = "password";
                    String message = nachricht;
                    String decrypted = "";
                    try {
                        decrypted = AESUtils.decrypt(nachricht);
                        Log.d("TEST", "decrypted:" + decrypted);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    nachricht=decrypted;


                    String lstr = "",str3="";
                    StringTokenizer stk = new StringTokenizer(nachricht, " ");
                    int cx = stk.countTokens();

                   // String strk=stk.nextToken();
                    String str = stk.nextToken();
                    String str2 = stk.nextToken();




                        if (cx == 3) {
                            str3 = stk.nextToken();
                        }


                        if (str.equals("unread")) {
                            if (str2.equals("messages")) {
                                List<Sms> sss = getAllSms("inbox");
                                Log.d("testcomps", "Fetched " + sss.size() + " SMSes");
                                for (int i = 0; i < sss.size(); i++) {
                                    if (sss.get(i).getReadState().equals("0")) {
                                        Log.d("testcomps", sss.get(i).getAddress() + " " + sss.get(i).getMsg());
                                    }
                                }

                            }
                        } else if (str.equals("wifi")) {
                            if (str2.equals("on")) {
                                WifiManager wifiManager;
                                wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
                                wifiManager.setWifiEnabled(true);
                            }
                            else if (str2.equals("off")) {
                                WifiManager wifiManager;
                                wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
                                wifiManager.setWifiEnabled(false);
                            }
                        } else if (str.equals("contact")) {
                            String firstName = "", lastName = "", Company;
                            String contname = str2;
                            String contactNumber = "";
                            Uri lkup = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, contname);
                            Cursor idCursor = getContentResolver().query(lkup, null, null, null, null);
                            while (idCursor.moveToNext()) {
                                String id = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts._ID));
                                String key = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                                String name = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                Log.d("testconts", "search: " + id + " key: " + key + " name: " + name);
                                lstr = lstr + name + ":";
                                // NAME: FIRST AND LAST
                                String nameWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                                String[] nameWhereParams = new String[]{id, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
                                Cursor nameCur = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, nameWhere, nameWhereParams, null);
                                boolean valid = false;
                                while (nameCur.moveToNext()) {
                                    firstName = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                                    lastName = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                                    String idName = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName._ID));
                                    String contactidName = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID));
                                    Log.e("testconts", "(id:" + id + ") (fn:" + firstName + ")(ln:" + lastName + ")(rid:" + idName + ")(cid:" + contactidName + ")");


                                    Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                            new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                                                    ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                                                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                                            new String[]{id},
                                            null);

                                    if (cursorPhone.moveToFirst()) {
                                        contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    }

                                    cursorPhone.close();

                                    Log.d("testconts", "Contact Phone Number: " + contactNumber);

                                    lstr = lstr + contactNumber + "\n";


                                    if (firstName != null && lastName != null && (firstName.length() > 0 || lastName.length() > 0)) {
                                        valid = true;
                                        break;
                                    }


                                }
                                nameCur.close();
                                // ORGANISATIONs
                                String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                                String[] orgWhereParams = new String[]{id, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                                Cursor orgCur = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, orgWhere, orgWhereParams, null);
                                //if (orgCur.moveToFirst()) {
                                valid = false;
                                while (orgCur.moveToNext()) {
                                    Company = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                                    String idC = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization._ID));
                                    String contactidC = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.CONTACT_ID));
                                    //String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                                    Log.e("testconts", "(id:" + id + ") (c:" + Company + ")(rid:" + idC + ")(cid:" + contactidC + ")");
                                    if (Company != null && Company.length() > 0) {
                                        valid = true;
                                        break;
                                    }

                                }
                                orgCur.close();


                            }
                            idCursor.close();


                            SmsManager sm = SmsManager.getDefault();
                            sm.sendTextMessage(telnr, null, lstr, null, null);


                        } else if (str.equals("battery")) {
                            if (str2.equals("status")) {
                                String bstr = "Battery Level:" + strbat;
                                SmsManager sm = SmsManager.getDefault();
                                sm.sendTextMessage(telnr, null, bstr, null, null);
                            }
                        } else if (str.equals("missed")) {
                            if (str2.equals("calls")) {

                                String logz = "";
                                String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
                                Uri callUri = Uri.parse("content://call_log/calls");
                                Cursor cur = getContentResolver().query(callUri, null, "type = 3 AND new = 1", null, strOrder);   // loop through cursor
                                while (cur.moveToNext()) {
                                    String callNumber = cur.getString(cur
                                            .getColumnIndex(android.provider.CallLog.Calls.NUMBER));
                                    String callName = cur
                                            .getString(cur
                                                    .getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
                                    String callDate = cur.getString(cur
                                            .getColumnIndex(android.provider.CallLog.Calls.DATE));
                                    SimpleDateFormat formatter = new SimpleDateFormat(
                                            "dd-MMM-yyyy HH:mm");
                                    String dateString = formatter.format(new Date(Long
                                            .parseLong(callDate)));
                                    String callType = cur.getString(cur
                                            .getColumnIndex(android.provider.CallLog.Calls.TYPE));
                                    String isCallNew = cur.getString(cur
                                            .getColumnIndex(android.provider.CallLog.Calls.NEW));
                                    String duration = cur.getString(cur
                                            .getColumnIndex(android.provider.CallLog.Calls.DURATION));
                                    // process log data...
                                    logz = logz + "\nNum:" + callNumber + " Time:" + dateString + " Name:" + callName;
                                    Log.d("testconts", callNumber + " " + dateString + " " + callType);
                                }
                                SmsManager sm = SmsManager.getDefault();
                                sm.sendTextMessage(telnr, null, logz, null, null);

                                cur.close();


                            }
                        } else if (str.equals("location")) {
                            if (str2.equals("fetch")) {
                                getLocation(getApplicationContext());
                                String loc = "lat:" + latitude + "\n lon:" + longitude;
                                SmsManager sm = SmsManager.getDefault();
                                sm.sendTextMessage(telnr, null, loc, null, null);

                            }
                        } else if (str.equals("event")) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                            String dateInString = str2;
                            Date date = null;
                            long milliseconds = 0;
                            try {
                                date = sdf.parse(dateInString);
                                milliseconds = date.getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String string_date = "12-December-2012";

                            pushAppointmentsToCalender(str3, "AutoSyndicatedFeed", "regular", 1, milliseconds, true, false);

                        }




// Here the message content is processed within MainAct
                    //  MainActivity.instance().processSMS(telnr.replace("+49", "0").replace(" ", ""), nachricht);
                }
            }
        }

        private SmsMessage getIncomingMessage(Object object, Bundle bundle) {
            SmsMessage smsMessage;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");
                smsMessage = SmsMessage.createFromPdu((byte[]) object, format);
            } else {
                smsMessage = SmsMessage.createFromPdu((byte[]) object);
            }

            return smsMessage;
        }
    }


    public List<Sms> getAllSms(String folderName) {
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/"+folderName);
        ContentResolver cr = getContentResolver();




        Date datex = new Date();
        String strd=datex.getYear()+"-"+datex.getMonth()+"-"+(datex.getDay());
        Date dateStart=null;
        Date dateEnd=null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            dateStart = formatter.parse(strd+ "T00:00:00");
            dateEnd=formatter.parse(strd + "T23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String filter = "date>=" + dateStart.getTime() + " and date<=" + dateEnd.getTime();


        Cursor c = cr.query(message, null, null, null, null);
        //startManagingCursor(c);
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));

                lstSms.add(objSms);
                c.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS in " + folderName);
        // }
        c.close();

        return lstSms;
    }


    public Location getLocation(Context ctx) {
        try {
            locationManager = (LocationManager) ctx.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Log.d("testconts","Not Available");
                    }
                    else {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                }

                if (isGPSEnabled) {
                    if (location == null) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Log.d("testconts","Not Available");
                        } else {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                      }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    public void stopUsingGPS() {
        if(locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    public double getLatitude() {
        if(location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if(location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }









    @Override
    public void onLocationChanged(Location location) {

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






    public long pushAppointmentsToCalender(String title, String addInfo, String place, int status, long startDate, boolean needReminder, boolean needMailService) {
        /***************** Event: note(without alert) *******************/

        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();

        eventValues.put("calendar_id", 1);
        eventValues.put("title", title);
        eventValues.put("description", addInfo);
        eventValues.put("eventLocation", place);

        long endDate = startDate + 1000 * 60 * 60; // For next 1hr

        eventValues.put("dtstart", startDate);
        eventValues.put("dtend", endDate);

        eventValues.put("allDay", 0);
        eventValues.put("eventStatus", status);
        eventValues.put("eventTimezone", "UTC/GMT +2:00");

    /*eventValues.put("visibility", 3); // visibility to default (0),
                                        // confidential (1), private
                                        // (2), or public (3):
    eventValues.put("transparency", 0); // You can control whether
                                        // an event consumes time
                                        // opaque (0) or transparent
                                        // (1).
      */
        eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

        Uri eventUri = getApplicationContext().getContentResolver().insert(Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());

        if (needReminder) {
            String reminderUriString = "content://com.android.calendar/reminders";

            ContentValues reminderValues = new ContentValues();

            reminderValues.put("event_id", eventID);
            reminderValues.put("minutes", 5); // Default value of the
            // system. Minutes is a
            // integer
            reminderValues.put("method", 1); // Alert Methods: Default(0),
            // Alert(1), Email(2),
            // SMS(3)

            Uri reminderUri = getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
        }




        return eventID;

    }















}
