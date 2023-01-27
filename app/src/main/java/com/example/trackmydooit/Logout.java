package com.example.trackmydooit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Logout extends AppCompatActivity {

    private Button logout, cancel;

    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        cancel = findViewById(R.id.goback);
        logout = findViewById(R.id.yesLogout);

        progressDialog = new ProgressDialog(this);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                progressDialog.setMessage("Logout in progress");
//                progressDialog.setCanceledOnTouchOutside(false);
//                progressDialog.show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), SplashScreenActivity.class));

                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout.super.onBackPressed();
//                Intent intent = new Intent(Logout.this,SettingsActivity.class);
//                startActivity(intent);
            }
        });

//        public void logout(View view){
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//            finish();
//        }
    }
}