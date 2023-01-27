package com.example.trackmydooit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class WalletActivity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String [] walletArray = {"Maybank", "Cash"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet2);

        Spinner spinnerWallet = findViewById(R.id.spinnerWallet);
        spinnerWallet.setOnItemSelectedListener(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, walletArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerWallet.setAdapter(arrayAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, walletArray[i],Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}