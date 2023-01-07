package com.example.trackmydooit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private CardView CVExpense;
    private CardView CVBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.DestGoals:
                        startActivity(new Intent(getApplicationContext(),CreateSavingsGoalActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.DestWallet:
                        startActivity(new Intent(getApplicationContext(),WalletFragment.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.DestAddExpense:
                        startActivity(new Intent(getApplicationContext(),ExpenseActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });



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