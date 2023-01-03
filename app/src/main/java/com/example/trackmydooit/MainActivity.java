package com.example.trackmydooit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CardView expenseCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expenseCardView = findViewById(R.id.expenseCardView);

        expenseCardView.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ExpenseActivity.class);
            startActivity(intent);
        });

    }

}