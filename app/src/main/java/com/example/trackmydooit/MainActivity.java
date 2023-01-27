package com.example.trackmydooit;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.anychart.core.gauge.pointers.Bar;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
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
import java.util.Arrays;
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
    PieChart mainPieChart;
    BarChart mainBarChart;

    int sumExpense = 0;
    int sumIncome = 0;
    int sumUtilities = 0; int sumTransportation = 0; int sumFood = 0; int sumEntertainment = 0;
    int sumPersonal = 0; int sumRent = 0; int sumTravel = 0;
    int sumEducation = 0;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    private TextView hiUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mainPieChart = findViewById(R.id.mainPieChart);
        mainBarChart = findViewById(R.id.mainBarChart);
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

//        String username = getIntent().getStringExtra("username");
//        hiUser.setText(username);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        String username = prefs.getString("username", "");
        hiUser.setText("Hi, "+username);


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue(String.class);
                if(username==null){
                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                    String username1 = prefs.getString("username", "");
                    hiUser.setText("Hi, "+username1);
                }
                else{
                    hiUser.setText("Hi, "+username);
                }

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
        mainBarChart = findViewById(R.id.mainBarChart);
        //CVReport = findViewById(R.id.CVReport);
        //CVTest = findViewById(R.id.CVTest);
        //mainTitle = findViewById(R.id.mainTitle);

        //CVReport.setOnClickListener(view -> CVReport.getContext().startActivity(new Intent(CVReport.getContext(), MonthlyAnalyticsActivity.class)));

        CVExpense.setOnClickListener(view -> CVExpense.getContext().startActivity(new Intent(CVExpense.getContext(), ExpenseActivity2.class)));

        CVIncome.setOnClickListener(view -> CVIncome.getContext().startActivity(new Intent(CVIncome.getContext(), ExpenseActivity2.class)));

        mainBarChart.setOnClickListener(view -> mainBarChart.getContext().startActivity(new Intent(mainBarChart.getContext(), MonthlyAnalyticsActivity.class)));

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

    private void initBarChart(){
        //hiding the grey background of the chart, default false if not set
        mainBarChart.setDrawGridBackground(false);
        //remove the bar shadow, default false if not set
        mainBarChart.setDrawBarShadow(false);
        //remove border of the chart, default false if not set
        mainBarChart.setDrawBorders(false);

        //remove the description label text located at the lower right corner
        Description description = new Description();
        description.setEnabled(false);
        mainBarChart.setDescription(description);

        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
        mainBarChart.animateY(1000);
        //setting animation for x-axis, the bar will pop up separately within the time we set
        mainBarChart.animateX(1000);

        XAxis xAxis = mainBarChart.getXAxis();
        xAxis.setDrawLabels(false);
        //change the position of x-axis to the bottom
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //set the horizontal distance of the grid line
        xAxis.setGranularity(1f);
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false);
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = mainBarChart.getAxisLeft();
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = mainBarChart.getAxisRight();
        //hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false);

        Legend legend = mainBarChart.getLegend();
        //setting the shape of the legend form to line, default square shape
        legend.setForm(Legend.LegendForm.LINE);

        //setting the text size of the legend
        legend.setTextSize(11f);

        //setting the alignment of legend toward the chart
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);

        //setting the stacking direction of legend
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        //setting the location of legend outside the chart, default false if not set
        legend.setDrawInside(true);


    }

    private void initBarDataSet(BarDataSet barDataSet){
        //Setting the size of the form in the legend
        barDataSet.setFormSize(15f);
        //showing the value of the bar, default true if not set
        barDataSet.setDrawValues(false);
        //setting the text size of the value of the bar
        barDataSet.setValueTextSize(8f);

        Legend l = mainBarChart.getLegend();
        l.setFormSize(8f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        l.setTextSize(8f);
        l.setTextColor(Color.BLACK);

        l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis

        // set custom labels and colors
        //l.setCustom(pastelColors,labelArray);
        LegendEntry legendEntryA = new LegendEntry();
        LegendEntry legendEntryB = new LegendEntry();
        LegendEntry legendEntryC = new LegendEntry();
        LegendEntry legendEntryD = new LegendEntry();
        LegendEntry legendEntryE = new LegendEntry();
        LegendEntry legendEntryF = new LegendEntry();
        LegendEntry legendEntryG = new LegendEntry();
        LegendEntry legendEntryH = new LegendEntry();

        int[] pastelColors = {
                Color.rgb(175, 175, 238),
                Color.rgb(153, 204, 204),
                Color.rgb(187, 231, 241),
                Color.rgb(166, 238, 173),
                Color.rgb(222, 229, 251),
                Color.rgb(243, 182, 128),
                Color.rgb(140, 233, 247),
                Color.rgb(252, 233, 172)};
        barDataSet.setColors(pastelColors);

        legendEntryA.label = "Utilities";
        legendEntryA.formColor = Color.rgb(175, 175, 238);

        legendEntryB.label = "Transportation";
        legendEntryB.formColor = Color.rgb(153, 204, 204);

        legendEntryC.label = "Food";
        legendEntryC.formColor = Color.rgb(187, 231, 241);

        legendEntryH.label = "Entertainment";
        legendEntryH.formColor = Color.rgb(252, 233, 172);

        legendEntryE.label = "Personal";
        legendEntryE.formColor = Color.rgb(222, 229, 251);

        legendEntryF.label = "Rent";
        legendEntryF.formColor = Color.rgb(243, 182, 128);

        legendEntryG.label = "Travel";
        legendEntryG.formColor = Color.rgb(140, 233, 247);

        legendEntryD.label = "Education";
        legendEntryD.formColor = Color.rgb(166, 238, 173);

        l.setCustom(Arrays.asList(legendEntryA, legendEntryB, legendEntryC,
                legendEntryD, legendEntryE, legendEntryF,legendEntryG, legendEntryH));

        mainBarChart.getLegend().setWordWrapEnabled(true);
        mainBarChart.getLegend().setEnabled(true);
    }

    private void setBarGraph() {

        //this one just display data that i have to manually input
        ArrayList<Integer> valueList = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = "Total Monthly Expenses";

        //input Y data (name of expenses)
        valueList.add(sumUtilities);
        valueList.add(sumTransportation);
        valueList.add(sumFood);
        valueList.add(sumEducation);
        valueList.add(sumEntertainment);
        valueList.add(sumPersonal);
        valueList.add(sumRent);
        valueList.add(sumTravel);

        //fit the data into a bar
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, title);
        initBarDataSet(barDataSet);

        BarData data = new BarData(barDataSet);
        mainBarChart.setData(data);
        mainBarChart.invalidate();

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
                            int amount = Integer.parseInt(ds.getString("amount"));

                            if(ds.getString("type").equals("Expense")){
                                sumExpense = sumExpense + amount;
                                if(ds.getString("expense category").equals("Utilities")){
                                    sumUtilities = sumUtilities + amount;
                                } else if (ds.getString("expense category").equals("Transportation")){
                                    sumTransportation = sumTransportation + amount;
                                } else if (ds.getString("expense category").equals("Food")){
                                    sumFood = sumFood + amount;
                                } else if (ds.getString("expense category").equals("Entertainment")){
                                    sumEntertainment = sumEntertainment + amount;
                                } else if (ds.getString("expense category").equals("Personal")){
                                    sumPersonal = sumPersonal + amount;
                                } else if (ds.getString("expense category").equals("Rent")){
                                    sumRent = sumRent + amount;
                                } else if (ds.getString("expense category").equals("Education")) {
                                    sumEducation = sumEducation + amount;
                                } else {
                                    sumTravel = sumTravel + amount;
                                }
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

                        initBarChart();
                        setBarGraph();
                        //setUpGraph();
                    }

                });
    }


}