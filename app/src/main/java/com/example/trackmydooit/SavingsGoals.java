package com.example.trackmydooit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

public class SavingsGoals extends AppCompatActivity {

    private Button update;
    private EditText amount;
    private DatabaseReference savingRef;
    String savings = "";

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_savings_goal);

        update = findViewById(R.id.updateGoal);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //brings user back to previous page
                Intent intent = new Intent(SavingsGoals.this, CreateSavingsGoalActivity.class);
                startActivity(intent);
            }
        });

        savingRef.child(savings).setValue(amount).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SavingsGoals.this, "Updated was successfully!", Toast.LENGTH_SHORT).show();
                }

                else {
                    Toast.makeText(SavingsGoals.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
