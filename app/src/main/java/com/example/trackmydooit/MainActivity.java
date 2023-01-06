package com.example.trackmydooit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CardView CVExpense;
    private CardView CVBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CVExpense = findViewById(R.id.CVExpense);
        CVBudget = findViewById(R.id.CVBudget);

        CVExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CVExpense.getContext().startActivity(new Intent(CVExpense.getContext(), ExpenseActivity.class));
            }
        });

        CVBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CVBudget.getContext().startActivity(new Intent(CVBudget.getContext(), BudgetActivity.class));
                //its stopping cuz i think it has something to do with going froma ctivity to fragment
            }
        });

    }

}