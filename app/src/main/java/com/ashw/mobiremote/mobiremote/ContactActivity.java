package com.ashw.mobiremote.mobiremote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ContactActivity extends AppCompatActivity {
    String numz,pask;
    EditText edtc;
    Button btnx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        edtc=(EditText)findViewById(R.id.edtcnt);
        btnx=(Button) findViewById(R.id.btnsendctr);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                numz=null;
                pask=null;
            } else {
                numz=extras.getString("phno");
                pask=extras.getString("passkey");
            }

        } else {
            numz= (String) savedInstanceState.getSerializable("phno");
            pask= (String) savedInstanceState.getSerializable("passkey");
        }

        Log.d("Test Number",numz);
        Log.d("Test Passkey",pask);

        btnx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Passkey Numers",pask);
                AESUtils.keyValue=pask.getBytes();
                String encrypted = "";
                String nmex=edtc.getText().toString();
                String sourceStr = "contact "+nmex;
                try {
                    encrypted = AESUtils.encrypt(sourceStr);
                    Log.d("TEST", "encrypted:" + encrypted);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String decrypted = "";
                try {
                    decrypted = AESUtils.decrypt(encrypted);
                    Log.d("TEST", "decrypted:" + decrypted);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(numz, null, encrypted, null, null);

                Intent i = new Intent(ContactActivity.this, MainActivity.class);
                startActivity(i);

            }
        });




    }
}
