package com.example.trackmydooit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CardView expenseCardView;
    Button CreateSaving;
    TextView forgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //expenseCardView = findViewById(R.id.expenseCardView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));

        getWindow().setNavigationBarColor(SurfaceColors.SURFACE_2.getColor(this));

        //go to CreateSavings
        CreateSaving=findViewById(R.id.CreateSaving);
        CreateSaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateSavingsGoalActivity.class);
                startActivity(intent);
            }
        });

        //go to ForgotPassword
        forgotPassword = (TextView)findViewById(R.id.PasswordForgot);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ForgotPassword.class);
                startActivity(intent);
            }
        });

        /*expenseCardView.setOnClickListener(new View.OnClickListener(){
            Intent intent = new Intent(MainActivity.this, ExpenseActivity.class);
            startActivity(intent);
        });*/
    }
}