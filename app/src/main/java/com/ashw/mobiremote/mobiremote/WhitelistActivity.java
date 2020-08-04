package com.ashw.mobiremote.mobiremote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WhitelistActivity extends AppCompatActivity {
    SharedPreferences sp;
    Button btnwts;
    Button btnfetchr;
    EditText etx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whitelist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        btnwts=(Button) findViewById(R.id.btnvps);
        etx=(EditText) findViewById(R.id.txtvpsval);





        btnwts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texr=etx.getText().toString();
                SharedPreferences.Editor e=sp.edit();
                e.putString("passkey",texr);
                e.commit();
                Toast.makeText(getApplicationContext(),"Updated Passkey",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(WhitelistActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
