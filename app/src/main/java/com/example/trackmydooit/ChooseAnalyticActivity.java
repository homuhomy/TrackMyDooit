package com.example.trackmydooit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseAnalyticActivity extends AppCompatActivity {

    private CardView todayCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_analytic);

        todayCardView = findViewById(R.id.todayCardView);

        todayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseAnalyticActivity.this, DailyAnalyticsActivity.class);
                startActivity(intent);
            }
        });
    }
}