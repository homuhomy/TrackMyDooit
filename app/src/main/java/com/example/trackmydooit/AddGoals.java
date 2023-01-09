package com.example.trackmydooit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddGoals extends AppCompatActivity {


    private Button addGoal, cancel;
    private EditText item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddGoals.this, CreateSavingsGoalActivity.class);
                startActivity(intent);
            }
        });

        addGoal = findViewById(R.id.addGoal);
        addGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add goals in CreateSavingsGoal
                String Goal;

            }
        });
    }
}
