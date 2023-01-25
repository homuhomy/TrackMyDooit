package com.example.trackmydooit;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private CardView CVExpense, CVIncome, CVBudget, CVReport;
    private TextView BudgetAmount, ExpenseAmount, IncomeAmount, BalanceAmount;
    private TextView CVTest;
    private TextView mainTitle;

    private FirebaseAuth mAuth;
    private DatabaseReference budgetRef, personalRef;
    private String onlineUserID = "";

    private int totalAmountMonth = 0;
    private int totalAmountBudget = 0;
    private int totalAmountBudgetD = 0;
    private int totalAmountBudgetC = 0;

    ArrayList<TransactionModel> transactionModelArrayList;
    TransactionAdapter transactionAdapter;

    int sumExpense = 0;
    int sumIncome = 0;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    private TextView hiUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BudgetAmount = findViewById(R.id.BudgetAmount);
        ExpenseAmount = findViewById(R.id.ExpenseAmount);
        IncomeAmount = findViewById(R.id.IncomeAmount);
        BalanceAmount = findViewById(R.id.BalanceText);
        hiUser = findViewById(R.id.hiUser);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        budgetRef = FirebaseDatabase.getInstance().getReference("budget").child(onlineUserID);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserID);

        //for transaction reference
        firebaseFirestore = FirebaseFirestore.getInstance();
        //DocumentReference documentReference = firebaseFirestore.collection("Expenses").document(onlineUserID);



//
//        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());;
//        ValueEventListener eventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    String username = dataSnapshot.child("username").getValue(String.class);
//                    hiUser.setText("Hi, "+username);
////                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle error
//            }
//        };
//        usersRef.addValueEventListener(eventListener);


        //bottom nav bar code

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue(String.class);
                hiUser.setText("Hi, "+username);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        };
        userRef.addValueEventListener(eventListener);


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.DestHome);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.DestWallet:
                        startActivity(new Intent(getApplicationContext(),Wallet_Activity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.DestAddExpense:
                        startActivity(new Intent(getApplicationContext(),ExpenseActivity2.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.DestSettings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.DestGoals:
                        startActivity(new Intent(getApplicationContext(), CreateSavingsGoalActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //bottom nav bar code

//        UserRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
//                    Object total = map.get("username");
//                    hiUser = (TextView) total;
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        CVExpense = findViewById(R.id.CVExpense);
        CVBudget = findViewById(R.id.CVBudget);
        CVIncome = findViewById(R.id.CVIncome);
        CVReport = findViewById(R.id.CVReport);
        //CVTest = findViewById(R.id.CVTest);
        //mainTitle = findViewById(R.id.mainTitle);

        CVReport.setOnClickListener(view -> CVReport.getContext().startActivity(new Intent(CVReport.getContext(), MonthlyAnalyticsActivity.class)));

        CVExpense.setOnClickListener(view -> CVExpense.getContext().startActivity(new Intent(CVExpense.getContext(), ExpenseActivity2.class)));

        CVIncome.setOnClickListener(view -> CVIncome.getContext().startActivity(new Intent(CVIncome.getContext(), ExpenseActivity2.class)));

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

    @Override
    protected void onStart() {
        super.onStart();
        loadData();

    }

    private void loadData(){
        firebaseFirestore.collection("Expenses").document(mAuth.getUid()).collection("Transaction")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds:task.getResult()){
                            //String expenseId, String type, String note, String expenseCategory, String incomeCategory, String walletCategory, String amount, String time, String uid
                            TransactionModel model = new TransactionModel(
                                    ds.getString("id"),
                                    ds.getString("type"),
                                    ds.getString("note"),
                                    ds.getString("expense category"),
                                    ds.getString("income category"),
                                    ds.getString("wallet"),
                                    ds.getString("amount"),
                                    ds.getString("date"),
                                    ds.getString(mAuth.getUid())
                            );
                            int amount = Integer.parseInt(ds.getString("amount"));
                            if(ds.getString("type").equals("Expense")){
                                sumExpense = sumExpense + amount;
                            }else{
                                sumIncome = sumIncome + amount;
                            }
                            //later check if this is working correctly
                            BalanceAmount.setText("RM " + (sumIncome-sumExpense));
                            ExpenseAmount.setText("RM " + sumExpense);
                            IncomeAmount.setText("RM " + sumIncome);

                            transactionAdapter = new TransactionAdapter(MainActivity.this, transactionModelArrayList);
                            //binding.RVTransaction.setAdapter(transactionAdapter);
                            //later check if this is working correctly
                        }
                    }
                });
    }
}