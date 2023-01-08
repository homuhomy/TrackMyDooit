package com.example.trackmydooit;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;

public class SavingsGoals extends AppCompatActivity {

    private Button button;
    private DatabaseReference savingRef;
    String savings = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_savings_goals);

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SavingsGoals.this, CreateSavingsGoalActivity.class);
                startActivity(intent);
            }
        });

//        savingRef.child(savings).setValue()
    }

}
