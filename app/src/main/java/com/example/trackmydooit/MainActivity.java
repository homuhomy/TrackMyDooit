package com.example.trackmydooit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private CardView CVExpense, CVIncome;
    private CardView CVBudget;
    private TextView BudgetAmount, ExpenseAmount, IncomeAmount;
    private TextView CVTest;
    private TextView mainTitle;

    private FirebaseAuth mAuth;
    private DatabaseReference budgetRef, expenseRef, personalRef, incomeRef;
    private String onlineUserID = "";

    private int totalAmountMonth = 0;
    private int totalAmountBudget = 0;
    private int totalAmountBudgetD = 0;
    private int totalAmountBudgetC = 0;

    private int totalAmountIncome = 0;
    private int totalAmountIncomeD = 0;
    private int totalAmountIncomeC = 0;

    private int totalAmountExpense = 0;
    private int totalAmountExpenseD = 0;
    private int totalAmountExpenseC = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BudgetAmount = findViewById(R.id.BudgetAmount);
        ExpenseAmount = findViewById(R.id.ExpenseAmount);
        IncomeAmount = findViewById(R.id.IncomeAmount);


        mAuth = FirebaseAuth.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        budgetRef = FirebaseDatabase.getInstance().getReference("budget").child(onlineUserID);
        incomeRef = FirebaseDatabase.getInstance().getReference("income").child(onlineUserID);
        expenseRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserID);


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

                    case R.id.wallet_Activity:
                        startActivity(new Intent(getApplicationContext(),Wallet_Activity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.DestAddExpense:
                        startActivity(new Intent(getApplicationContext(),ExpenseActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        CVExpense = findViewById(R.id.CVExpense);
        CVBudget = findViewById(R.id.CVBudget);
        CVIncome = findViewById(R.id.CVIncome);
        //CVTest = findViewById(R.id.CVTest);
        //mainTitle = findViewById(R.id.mainTitle);

        CVExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CVExpense.getContext().startActivity(new Intent(CVExpense.getContext(), ExpenseActivity.class));
            }
        });

        CVIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CVIncome.getContext().startActivity(new Intent(CVIncome.getContext(), IncomeActivity.class));
            }
        });

        CVBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CVBudget.getContext().startActivity(new Intent(CVBudget.getContext(), BudgetActivity.class));
            }
        });

        //dummy click to weekly report
        /*
        CVTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WeekSpendingActivity.class);
                intent.putExtra("type", "week");
                startActivity(intent);
            }
        });

        //dummy to click on monthly report
        mainTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WeekSpendingActivity.class);
                intent.putExtra("type", "month");
                startActivity(intent);
            }
        });
         */

        //to check if budget exists or not
        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmountBudgetD+=pTotal;
                    }
                    totalAmountBudgetC = totalAmountBudgetD;
                } else {
                    personalRef.child("budget").setValue(0);
                    Toast.makeText(MainActivity.this, "Please select a budget!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //to check if expense exists or not
        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmountExpenseD+=pTotal;
                    }
                    totalAmountExpenseC = totalAmountExpenseD;
                } else {
                    personalRef.child("expense").setValue(0);
                    Toast.makeText(MainActivity.this, "Please select expense!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //to check if income exists or not
        incomeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmountIncomeD+=pTotal;
                    }
                    totalAmountIncomeC = totalAmountIncomeD;
                } else {
                    personalRef.child("income").setValue(0);
                    Toast.makeText(MainActivity.this, "Please select income!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //to call the amount  from respective page
        getBudgetAmount();
        getIncomeAmount();
        getExpenseAmount();

    }

    //display budget to main activity xml
    private void getBudgetAmount() {
        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmountBudget+=pTotal;
                        BudgetAmount.setText("RM " + String.valueOf(totalAmountBudget));
                    }
                } else {
                    totalAmountBudget=0;
                    BudgetAmount.setText("RM " + String.valueOf(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getIncomeAmount() {

        incomeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmountIncome+=pTotal;
                        IncomeAmount.setText("RM " + String.valueOf(totalAmountIncome));
                    }
                } else {
                    totalAmountIncome=0;
                    IncomeAmount.setText("RM " + String.valueOf(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getExpenseAmount() {

        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmountExpense+=pTotal;
                        ExpenseAmount.setText("RM " + String.valueOf(totalAmountExpense));
                    }
                } else {
                    totalAmountExpense=0;
                    ExpenseAmount.setText("RM " + String.valueOf(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}