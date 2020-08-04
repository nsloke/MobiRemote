package com.ashw.mobiremote.mobiremote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FetchInfex extends AppCompatActivity {

    EditText etxnum,etxremkey;
    Button btnumg,btnwion,btnwiof,btnfcont,btnbtry,btnmscall,btnlcs,btnevz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_infex);
        etxnum=(EditText)findViewById(R.id.etrnumbr);
        etxremkey=(EditText)findViewById(R.id.etremkey);

        btnumg=(Button)findViewById(R.id.btnumsgs);
        btnwion=(Button)findViewById(R.id.btnwifion);
        btnwiof=(Button)findViewById(R.id.btnwifioff);
        btnfcont=(Button)findViewById(R.id.btnfetchcon);
        btnbtry=(Button)findViewById(R.id.btnbattery);
        btnmscall=(Button)findViewById(R.id.btnmisscalls);
        btnlcs=(Button)findViewById(R.id.btnlocs);
        btnevz=(Button)findViewById(R.id.btneventz);

        btnumg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String numz=etxnum.getText().toString();
                String pask=etxremkey.getText().toString();

                if(pask.length()<16 || numz.length()<10)
                {
                    Toast.makeText(getApplicationContext(),"Invalid Number or Passkey. Passkey should be 16 characters.",Toast.LENGTH_SHORT).show();
                    return;
                }

                AESUtils.keyValue=pask.getBytes();
                String encrypted = "";
                String sourceStr = "unread messages";
                try {
                    encrypted = AESUtils.encrypt(sourceStr);
                    Log.d("TEST", "encrypted:" + encrypted);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(numz, null, encrypted, null, null);

                /*
                String decrypted = "";
                try {
                    decrypted = AESUtils.decrypt(encrypted);
                    Log.d("TEST", "decrypted:" + decrypted);
                } catch (Exception e) {
                    e.printStackTrace();
                }
*/


            }
        });


        btnwion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String numz=etxnum.getText().toString();
                String pask=etxremkey.getText().toString();

                if(pask.length()<16 || numz.length()<10)
                {
                    Toast.makeText(getApplicationContext(),"Invalid Number or Passkey. Passkey should be 16 characters.",Toast.LENGTH_SHORT).show();
                    return;
                }

                AESUtils.keyValue=pask.getBytes();
                String encrypted = "";
                String sourceStr = "wifi on";
                try {
                    encrypted = AESUtils.encrypt(sourceStr);
                    Log.d("TEST", "encrypted:" + encrypted);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(numz, null, encrypted, null, null);

/*
                String decrypted = "";
                try {
                    decrypted = AESUtils.decrypt(encrypted);
                    Log.d("TEST", "decrypted:" + decrypted);
                } catch (Exception e) {
                    e.printStackTrace();
                }
*/


            }
        });


        btnwiof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String numz=etxnum.getText().toString();
                String pask=etxremkey.getText().toString();

                if(pask.length()<16 || numz.length()<10)
                {
                    Toast.makeText(getApplicationContext(),"Invalid Number or Passkey. Passkey should be 16 characters.",Toast.LENGTH_SHORT).show();
                    return;
                }

                AESUtils.keyValue=pask.getBytes();
                String encrypted = "";
                String sourceStr = "wifi off";
                try {
                    encrypted = AESUtils.encrypt(sourceStr);
                    Log.d("TEST", "encrypted:" + encrypted);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(numz, null, encrypted, null, null);


            }
        });


        btnbtry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String numz=etxnum.getText().toString();
                String pask=etxremkey.getText().toString();

                if(pask.length()<16 || numz.length()<10)
                {
                    Toast.makeText(getApplicationContext(),"Invalid Number or Passkey. Passkey should be 16 characters.",Toast.LENGTH_SHORT).show();
                    return;
                }

                AESUtils.keyValue=pask.getBytes();
                String encrypted = "";
                String sourceStr = "battery status";
                try {
                    encrypted = AESUtils.encrypt(sourceStr);
                    Log.d("TEST", "encrypted:" + encrypted);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(numz, null, encrypted, null, null);

            }
        });



        btnmscall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String numz=etxnum.getText().toString();
                String pask=etxremkey.getText().toString();

                if(pask.length()<16 || numz.length()<10)
                {
                    Toast.makeText(getApplicationContext(),"Invalid Number or Passkey. Passkey should be 16 characters.",Toast.LENGTH_SHORT).show();
                    return;
                }


                AESUtils.keyValue=pask.getBytes();
                String encrypted = "";
                String sourceStr = "missed calls";
                try {
                    encrypted = AESUtils.encrypt(sourceStr);
                    Log.d("TEST", "encrypted:" + encrypted);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(numz, null, encrypted, null, null);


            }
        });



        btnlcs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String numz=etxnum.getText().toString();
                String pask=etxremkey.getText().toString();

                if(pask.length()<16 || numz.length()<10)
                {
                    Toast.makeText(getApplicationContext(),"Invalid Number or Passkey. Passkey should be 16 characters.",Toast.LENGTH_SHORT).show();
                    return;
                }


                AESUtils.keyValue=pask.getBytes();
                String encrypted = "";
                String sourceStr = "location fetch";
                try {
                    encrypted = AESUtils.encrypt(sourceStr);
                    Log.d("TEST", "encrypted:" + encrypted);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(numz, null, encrypted, null, null);


            }
        });



        btnfcont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numz=etxnum.getText().toString();
                String pask=etxremkey.getText().toString();
                if(pask.length()<16 || numz.length()<10)
                {
                    Toast.makeText(getApplicationContext(),"Invalid Number or Passkey. Passkey should be 16 characters.",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent(FetchInfex.this, ContactActivity.class);
                i.putExtra("phno", numz);
                i.putExtra("passkey", pask);
                startActivity(i);

            }
        });



        btnevz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String numz=etxnum.getText().toString();
                String pask=etxremkey.getText().toString();
                if(pask.length()<16 || numz.length()<10)
                {
                    Toast.makeText(getApplicationContext(),"Invalid Number or Passkey. Passkey should be 16 characters.",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent(FetchInfex.this, EventzActivity.class);
                i.putExtra("phno", numz);
                i.putExtra("passkey", pask);
                startActivity(i);


            }
        });




    }
}
