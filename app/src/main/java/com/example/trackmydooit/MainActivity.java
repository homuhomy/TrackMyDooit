package com.example.trackmydooit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private CardView CVExpense;
    private CardView CVBudget;
    private TextView BudgetAmount;
    //private TextView CVTest;
    //private TextView mainTitle;

    private FirebaseAuth mAuth;
    private DatabaseReference budgetRef, expenseRef, personalRef;
    private String onlineUserID = "";

    private int totalAmountMonth = 0;
    private int totalAmountBudget = 0;
    private int totalAmountBudgetD = 0;
    private int totalAmountBudgetC = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BudgetAmount = findViewById(R.id.BudgetAmount);

        mAuth = FirebaseAuth.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        budgetRef = FirebaseDatabase.getInstance().getReference("budget").child(onlineUserID);
        expenseRef = FirebaseDatabase.getInstance().getReference("expense").child(onlineUserID);
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

                    case R.id.DestWallet:
                        startActivity(new Intent(getApplicationContext(),WalletFragment.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.DestAddExpense:
                        startActivity(new Intent(getApplicationContext(),ExpenseActivity.class));
                        overridePendingTransition(0,0);
                        return true;
 //                   case R.id.DestNotifications:
 //                       startActivity(new Intent(getApplicationContext(), Notification.class));
 //                       overridePendingTransition(0,0);
 //                       return true;
                }
                return false;
            }
        });

        CVExpense = findViewById(R.id.CVExpense);
        CVBudget = findViewById(R.id.CVBudget);
        //CVTest = findViewById(R.id.CVTest);
        //mainTitle = findViewById(R.id.mainTitle);

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

        //to call the amount  from respective page
        getBudgetAmount();

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

}