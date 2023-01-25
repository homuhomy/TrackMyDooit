package com.example.trackmydooit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.trackmydooit.databinding.ActivityExpense2Binding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class ExpenseActivity2 extends AppCompatActivity {

    ArrayList barArrayList;

    ActivityExpense2Binding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    int sumExpense = 0;
    int sumIncome = 0;

    private Integer income = 0, expense = 0;

    ArrayList<TransactionModel> transactionModelArrayList;
    TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpense2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = new Intent(ExpenseActivity2.this, AddExpenseActivity.class);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        transactionModelArrayList = new ArrayList<>();
        binding.RVTransaction.setLayoutManager(new LinearLayoutManager(this));
        binding.RVTransaction.setHasFixedSize(true);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        //to add back button
        getSupportActionBar().setTitle("My Transaction");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_ios_24px);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //bottom nav bar code
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.DestAddExpense);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.DestHome:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.DestGoals:
                        startActivity(new Intent(getApplicationContext(),CreateSavingsGoalActivity.class));
                        overridePendingTransition(0,0);
                        return true;

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
                }
                return false;
            }
        });
        //bottom nav bar code

        binding.FABAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", "Income");
                startActivity(intent);
            }
        });

        binding.FABAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", "Expense");
                startActivity(intent);
            }
        });

        binding.refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(ExpenseActivity2.this, ExpenseActivity2.class));
                }catch (Exception e){

                }
            }
        });
        loadData();
    }


    private void loadData() {
        firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Transaction")
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
                                    ds.getString(firebaseAuth.getUid())
                                    );
                            int amount = Integer.parseInt(ds.getString("amount"));
                            if(ds.getString("type").equals("Expense")){
                                sumExpense = sumExpense + amount;
                            }else{
                                sumIncome = sumIncome + amount;
                            }
                            transactionModelArrayList.add(model);
                            //later check if this is working correctly
                            binding.expenseTV.setText("Balance: RM " + (sumIncome-sumExpense));

                            transactionAdapter = new TransactionAdapter(ExpenseActivity2.this, transactionModelArrayList);
                            binding.RVTransaction.setAdapter(transactionAdapter);
                            //later check if this is working correctly
                        }

                        //binding.expenseTV.setText("Expenses this month : RM " + sumExpense);
                        //binding.incomeTV.setText("Expenses this month :" + sumIncome);

                        setUpGraph();
                        //setUpBarChart();
                    }
                });
    }

    private void setUpGraph() {
        ArrayList<PieEntry> pieEntryArrayList = new ArrayList<>();
        ArrayList<Integer> colorsList = new ArrayList<>();
        if(sumIncome!=0){
            pieEntryArrayList.add(new PieEntry(sumIncome,"Income"));
            colorsList.add(getResources().getColor(R.color.orange));
        }
        if(sumExpense!=0){
            pieEntryArrayList.add(new PieEntry(sumExpense,"Expense"));
            colorsList.add(getResources().getColor(R.color.red));
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntryArrayList,String.valueOf(sumIncome = sumExpense));
        PieData pieData = new PieData(pieDataSet);
        pieDataSet.setColors(colorsList);

        pieData.setValueTextSize(20);

        binding.PieChart.setData(pieData);
        binding.PieChart.invalidate();
    }

   /* private void setUpBarChart() {
        BarChart barChart = binding.barChart;

        ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
        ArrayList<Integer> colorsList = new ArrayList<>();
        *//*if(sumIncome!=0){
            barEntryArrayList.add(new BarEntry(sumIncome,"Income"));
            colorsList.add(getResources().getColor(R.color.orange));
        }
        if(sumExpense!=0){
            barEntryArrayList.add(new BarEntry(sumExpense,"Expense"));
            colorsList.add(getResources().getColor(R.color.red));
        }*//*
        BarDataSet barDataSet= new BarDataSet(barEntryArrayList,String.valueOf(sumIncome = sumExpense));
        BarData barData = new BarData(barDataSet);
        getBarData();

        binding.barChart.setData(barData);
        barDataSet.setColors(colorsList);
        barDataSet.setValueTextColor(Color.BLACK);
        binding.barChart.getDescription().setEnabled(true);

        barData.setValueTextSize(20);

        binding.barChart.setData(barData);
        binding.barChart.invalidate();
    }

    private void getBarData(){
        barArrayList = new ArrayList<>();
        barArrayList.add(new BarEntry(2f,10));
        barArrayList.add(new BarEntry(3f,20));
        barArrayList.add(new BarEntry(4f,30));
        barArrayList.add(new BarEntry(5f,40));
        barArrayList.add(new BarEntry(6f,50));
        barArrayList.add(new BarEntry(7f,60));
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        income = 0;
        expense = 0;
        getData();
    }

    private void getData() {
        FirebaseFirestore
                .getInstance()
                .collection("Expenses")
                .whereEqualTo("uid", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds:dsList){
                            TransactionModel transactionModel = ds.toObject(TransactionModel.class);

                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //loadData();
    }
}