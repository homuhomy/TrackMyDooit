package com.example.trackmydooit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.VirtualLayout;;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MonthlyAnalyticsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private String onlineUserID = "";
    private DatabaseReference expenseRef,personalRef,budgetRef;

    private TextView monthRatioSpending, monthSpentAmount, totalAmountSpentOn, analyticsTransportAmount, analyticsFoodAmount, analyticsEntertainmentAmount, analyticsPersonalAmount, analyticsHomeAmount, analyticsUtilitiesAmount;
    private TextView progress_ratio_transport, progress_ratio_food, progress_ratio_entertainment, progress_ratio_personal, progress_ratio_home, progress_ratio_utilities;

    private RelativeLayout relativeLayoutTransport, relativeFood, relativeEntertainment, relativePersonal, relativeHome, relativeUtilities;

    private AnyChartView anyChartView;
    private Pie analyticsChart;
    PieChart pieChart;
    int [] colorClassArry = new int[]{android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light, android.R.color.holo_purple, android.R.color.holo_blue_dark};



    private ImageView monthRatioSpending_image, transport_status, food_status, entertainment_status, personal_status, home_status, utilities_status, status_image_red, status_image_green, status_image_brown;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_analytics);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //to add back button
        getSupportActionBar().setTitle("Analytics");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_ios_24px);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        onlineUserID = mAuth.getCurrentUser().getUid();
        expenseRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserID);
        budgetRef = FirebaseDatabase.getInstance().getReference("budget").child(onlineUserID);

        totalAmountSpentOn = findViewById(R.id.totalAmountSpentOn);

        //analytics?
        monthSpentAmount = findViewById(R.id.monthSpentAmount);
        monthRatioSpending = findViewById(R.id.monthRatioSpending);
        monthRatioSpending_image = findViewById(R.id.monthRatioSpending_image);

        //idk wtf
        analyticsTransportAmount = findViewById(R.id.analyticsTransportAmount);
        analyticsFoodAmount = findViewById(R.id.analyticsFoodAmount);
        analyticsEntertainmentAmount = findViewById(R.id.analyticsEntertainmentAmount);
        analyticsPersonalAmount = findViewById(R.id.analyticsPersonalAmount);
        analyticsUtilitiesAmount = findViewById(R.id.analyticsUtilitiesAmount);
        analyticsHomeAmount = findViewById(R.id.analyticsHomeAmount);



        //relavtive layout
        relativeLayoutTransport = findViewById(R.id.relativeLayoutTransport);
        relativeFood = findViewById(R.id.relativeFood);
        relativeEntertainment = findViewById(R.id.relativeEntertainment);
        relativePersonal = findViewById(R.id.relativePersonal);
        relativeHome = findViewById(R.id.relativeHome);
        relativeUtilities = findViewById(R.id.relativeUtilities);


        //image view
        transport_status = findViewById(R.id.transport_status);
        food_status = findViewById(R.id.food_status);
        entertainment_status = findViewById(R.id.entertainment_status);
        personal_status = findViewById(R.id.personal_status);
        home_status = findViewById(R.id.home_status);
        utilities_status = findViewById(R.id.utilities_status);
        status_image_red = findViewById(R.id.status_image_red);
        status_image_green = findViewById(R.id.status_image_green);
        status_image_brown = findViewById(R.id.status_image_brown);

        //textview
        progress_ratio_transport = findViewById(R.id.progress_ratio_transport);
        progress_ratio_food = findViewById(R.id.progress_ratio_food);
        progress_ratio_entertainment = findViewById(R.id.progress_ratio_entertainment);
        progress_ratio_personal = findViewById(R.id.progress_ratio_personal);
        progress_ratio_home = findViewById(R.id.progress_ratio_home);
        progress_ratio_utilities = findViewById(R.id.progress_ratio_utilities);

        //anyChartView = findViewById(R.id.anyChartView);
        pieChart = findViewById(R.id.analyticsChart);

        getTotalMonthTransportExpense();
        getTotalMonthFoodExpense();
        getTotalMonthEntertainmentExpense();
        getTotalMonthPersonalExpense();
        getTotalMonthHomeExpense();
        getTotalMonthUtilitiesExpense();
        getTotaLMonthSpending();

        loadGraph();
        setStatusAndImageResources();

    //        new java.util.Timer().schedule(
    //                () + {
    //                    loadGraph();
    //                    setStatusAndImageResources();
    //                },
    //                    2000
    //        );
    }

    private void getTotalMonthUtilitiesExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "utilities"+months.getMonths();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String,Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsUtilitiesAmount.setText("Spent: RM" + totalAmount);
                    }
                    personalRef.child("monthUtilities").setValue(totalAmount);
                }
                else {
                    //relativeUtilities.setVisibility(View.GONE);
                    personalRef.child("monthUtilities").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getTotalMonthHomeExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        String itemMonth = "home"+months.getMonths();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemMonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String,Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsHomeAmount.setText("Spent: RM" + totalAmount);
                    }
                    personalRef.child("monthHome").setValue(totalAmount);
                }
                else {
                    //relativeHome.setVisibility(View.GONE);
                    personalRef.child("monthHome").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getTotalMonthPersonalExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        String itemMonth = "personal Expenses"+months.getMonths();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemMonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String,Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsPersonalAmount.setText("Spent: RM" + totalAmount);
                    }
                    personalRef.child("monthPersonal").setValue(totalAmount);
                }
                else {
                    //relativePersonal.setVisibility(View.GONE);
                    personalRef.child("monthPersonal").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getTotalMonthEntertainmentExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        String itemMonth = "entertainment"+months.getMonths();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemMonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String,Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsEntertainmentAmount.setText("Spent: RM" + totalAmount);
                    }
                    personalRef.child("monthEntertainment").setValue(totalAmount);
                }
                else {
                    //relativeEntertainment.setVisibility(View.GONE);
                    personalRef.child("monthEntertainment").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getTotalMonthFoodExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        String itemMonth = "food"+months.getMonths();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemMonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String,Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsFoodAmount.setText("Spent: RM" + totalAmount);
                    }
                    personalRef.child("monthFood").setValue(totalAmount);
                }
                else {
                    //relativeFood.setVisibility(View.GONE);
                    personalRef.child("monthFood").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getTotalMonthTransportExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        String itemMonth = "transport"+months.getMonths();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemMonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String,Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsTransportAmount.setText("Spent: RM" + totalAmount);
                    }
                    personalRef.child("monthTrans").setValue(totalAmount);
                }
                else {
                    //relativeLayoutTransport.setVisibility(View.GONE);
                    personalRef.child("monthTrans").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getTotaLMonthSpending() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        Query query = reference.orderByChild("month").equalTo(months.getMonths());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String,Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                    }
                    totalAmountSpentOn.setText("Total months spending: RM " + totalAmount);
                    monthSpentAmount.setText("Total spent: RM " + totalAmount);
                } else {
                    anyChartView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadGraph(){
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    int transTotal;
                    if (snapshot.hasChild("monthTrans")){
                        transTotal = Integer.parseInt(snapshot.child("monthTrans").getValue().toString());
                    } else {
                        transTotal = 0;
                    }

                    int foodTotal;
                    if (snapshot.hasChild("monthFood")){
                        foodTotal = Integer.parseInt(snapshot.child("monthFood").getValue().toString());
                    } else {
                        foodTotal = 0;
                    }

                    int entertainmentTotal;
                    if (snapshot.hasChild("monthEntertainment")){
                        entertainmentTotal = Integer.parseInt(snapshot.child("monthEntertainment").getValue().toString());
                    } else {
                        entertainmentTotal = 0;
                    }

                    int personalTotal;
                    if (snapshot.hasChild("monthPersonal")){
                        personalTotal = Integer.parseInt(snapshot.child("monthPersonal").getValue().toString());
                    } else {
                        personalTotal = 0;
                    }

                    int homeTotal;
                    if (snapshot.hasChild("monthHome")){
                        homeTotal = Integer.parseInt(snapshot.child("monthHome").getValue().toString());
                    } else {
                        homeTotal = 0;
                    }

                    int utilitiesTotal;
                    if (snapshot.hasChild("monthUtilities")){
                        utilitiesTotal = Integer.parseInt(snapshot.child("monthUtilities").getValue().toString());
                    } else {
                        utilitiesTotal = 0;
                    }

                    //PieChart pieChart = new PieChart()
                    ArrayList<PieEntry> data = new ArrayList<>();
                    data.add(new PieEntry(transTotal, "Transport"));
                    data.add(new PieEntry(foodTotal, "Food"));
                    data.add(new PieEntry(entertainmentTotal, "Entertainment"));
                    data.add(new PieEntry(personalTotal, "Personal"));
                    data.add(new PieEntry(homeTotal, "Home"));
                    data.add(new PieEntry(utilitiesTotal, "Utilities"));


                    PieDataSet pieDataSet = new PieDataSet(data,"");
                    pieDataSet.setColors(colorClassArry);
                    PieData pieData = new PieData(pieDataSet);
                    pieChart.setData(pieData);
                    pieChart.invalidate();



//                    Pie pie = AnyChart.pie();
//                    List<DataEntry> data = new ArrayList<>();
//                    data.add(new ValueDataEntry("Transport", transTotal));
//                    data.add(new ValueDataEntry("Food", foodTotal));
//                    data.add(new ValueDataEntry("Entertainment", entertainmentTotal));
//                    data.add(new ValueDataEntry("Personal", personalTotal));
//                    data.add(new ValueDataEntry("Home", homeTotal));
//                    data.add(new ValueDataEntry("Utilities", utilitiesTotal));

//                    pie.data(data);
//                    pie.title("Month Analytics");
//                    pie.labels().position("outside");
//
//                    pie.legend().title().enabled(true);
//                    pie.legend().title()
//                            .text("Items spent on: ")
//                            .padding(0d,0d,10d,0d);

//                    pie.legend()
//                            .position("center-bottom")
//                            .itemsLayout(LegendLayout.HORIZONTAL)
//                            .align(Align.CENTER);

                    //anyChartView.setChart(pie);
                }
                else {
                    Toast.makeText(MonthlyAnalyticsActivity.this, "Child does not exist! ", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setStatusAndImageResources(){
        //Query query = budgetRef.orderByChild("item").equalTo("Transport");
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    float transTotal;
                    if (snapshot.hasChild("monthTrans")){
                        transTotal = Integer.parseInt(snapshot.child("monthTrans").getValue().toString());
                    } else {
                        transTotal = 0;
                    }

                    float foodTotal;
                    if (snapshot.hasChild("monthFood")){
                        foodTotal = Integer.parseInt(snapshot.child("monthFood").getValue().toString());
                    } else {
                        foodTotal = 0;
                    }

                    float entertainmentTotal;
                    if (snapshot.hasChild("monthEntertainment")){
                        entertainmentTotal = Integer.parseInt(snapshot.child("monthEntertainment").getValue().toString());
                    } else {
                        entertainmentTotal = 0;
                    }

                    float personalTotal;
                    if (snapshot.hasChild("monthPersonal")){
                        personalTotal = Integer.parseInt(snapshot.child("monthPersonal").getValue().toString());
                    } else {
                        personalTotal = 0;
                    }

                    float homeTotal;
                    if (snapshot.hasChild("monthHome")){
                        homeTotal = Integer.parseInt(snapshot.child("monthHome").getValue().toString());
                    } else {
                        homeTotal = 0;
                    }

                    float utilitiesTotal;
                    if (snapshot.hasChild("monthUtilities")){
                        utilitiesTotal = Integer.parseInt(snapshot.child("monthUtilities").getValue().toString());
                    } else {
                        utilitiesTotal = 0;
                    }

                    float monthTotalSpentAmount;
                    if (snapshot.hasChild("month")){
                        monthTotalSpentAmount = Integer.parseInt(snapshot.child("month").getValue().toString());
                    } else {
                        monthTotalSpentAmount = 0;
                    }

                    //GETTING RATIOS IN FUTURE HERE

                    float transRatio;
                    if (snapshot.hasChild("monthTransRatio")){
                        transRatio= Integer.parseInt(snapshot.child("monthTransRatio").getValue().toString());
                    } else {
                        transRatio = 0;
                    }

                    float foodRatio;
                    if (snapshot.hasChild("monthFoodRatio")){
                        foodRatio= Integer.parseInt(snapshot.child("monthFoodRatio").getValue().toString());
                    } else {
                        foodRatio = 0;
                    }

                    float entertainmentRatio;
                    if (snapshot.hasChild("monthEntertainmentRatio")){
                        entertainmentRatio= Integer.parseInt(snapshot.child("monthEntertainmentRatio").getValue().toString());
                    } else {
                        entertainmentRatio = 0;
                    }

                    float personalRatio;
                    if (snapshot.hasChild("monthPersonalRatio")){
                        personalRatio= Integer.parseInt(snapshot.child("monthPersonalRatio").getValue().toString());
                    } else {
                        personalRatio = 0;
                    }

                    float homeRatio;
                    if (snapshot.hasChild("monthHomeRatio")){
                        homeRatio= Integer.parseInt(snapshot.child("monthHomeRatio").getValue().toString());
                    } else {
                        homeRatio = 0;
                    }

                    float utilitiesRatio;
                    if (snapshot.hasChild("monthUtilitiesRatio")){
                        utilitiesRatio= Integer.parseInt(snapshot.child("monthUtilitiesRatio").getValue().toString());
                    } else {
                        utilitiesRatio = 0;
                    }

                    float monthTotalSpentAmountRatio;
                    if (snapshot.hasChild("budget")){
                        monthTotalSpentAmountRatio= Integer.parseInt(snapshot.child("budget").getValue().toString());
                    } else {
                        monthTotalSpentAmountRatio = 0;
                    }


                    float monthPercent = (monthTotalSpentAmount/monthTotalSpentAmountRatio)*100;
                    if (monthPercent<50){
                        monthRatioSpending.setText(monthPercent+ " % " + " used of " + monthTotalSpentAmountRatio + " Status: ");
                        monthRatioSpending_image.setImageResource(R.drawable.green);
                    } else if (monthPercent>= 50 && monthPercent <100){
                        monthRatioSpending.setText(monthPercent+ " % " + " used of " + monthTotalSpentAmountRatio + " Status: ");
                        monthRatioSpending_image.setImageResource(R.drawable.brown);
                    } else {
                        monthRatioSpending.setText(monthPercent+ " % " + " used of " + monthTotalSpentAmountRatio + " Status: ");
                        monthRatioSpending_image.setImageResource(R.drawable.red);
                    }

                    float transportPercent = (transTotal/transRatio)*100;
                    if (transportPercent<50){
                        progress_ratio_transport.setText(transportPercent+ " % " + " used of " + transRatio + " Status: ");
                        transport_status.setImageResource(R.drawable.green);
                    } else if (transportPercent>= 50 && transportPercent <100){
                        progress_ratio_transport.setText(transportPercent+ " % " + " used of " + transRatio + " Status: ");
                        transport_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_transport.setText(transportPercent+ " % " + " used of " + transRatio + " Status: ");
                        transport_status.setImageResource(R.drawable.red);
                    }

                    float foodPercent = (foodTotal/foodRatio)*100;
                    if (foodPercent<50){
                        progress_ratio_food.setText(foodPercent+ " % " + " used of " + foodRatio + " Status: ");
                        food_status.setImageResource(R.drawable.green);
                    } else if (foodPercent>= 50 && foodPercent <100){
                        progress_ratio_food.setText(foodPercent+ " % " + " used of " + foodRatio + " Status: ");
                        food_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_food.setText(foodPercent+ " % " + " used of " + foodRatio + " Status: ");
                        food_status.setImageResource(R.drawable.red);
                    }

                    float entertainmentPercent = (entertainmentTotal/entertainmentRatio)*100;
                    if (entertainmentPercent<50){
                        progress_ratio_entertainment.setText(entertainmentPercent+ " % " + " used of " + entertainmentRatio + " Status: ");
                        entertainment_status.setImageResource(R.drawable.green);
                    } else if (entertainmentPercent>= 50 && entertainmentPercent <100){
                        progress_ratio_entertainment.setText(entertainmentPercent+ " % " + " used of " + entertainmentRatio + " Status: ");
                        entertainment_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_entertainment.setText(entertainmentPercent+ " % " + " used of " + entertainmentRatio + " Status: ");
                        entertainment_status.setImageResource(R.drawable.red);
                    }

                    float personalPercent = (personalTotal/personalRatio)*100;
                    if (personalPercent<50){
                        progress_ratio_personal.setText(personalPercent+ " % " + " used of " + personalRatio + " Status: ");
                        personal_status.setImageResource(R.drawable.green);
                    } else if (personalPercent>= 50 && personalPercent <100){
                        progress_ratio_personal.setText(personalPercent+ " % " + " used of " + personalRatio + " Status: ");
                        personal_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_personal.setText(personalPercent+ " % " + " used of " + personalRatio + " Status: ");
                        personal_status.setImageResource(R.drawable.red);
                    }

                    float homePercent = (homeTotal/homeRatio)*100;
                    if (homePercent<50){
                        progress_ratio_home.setText(homePercent+ " % " + " used of " + homeRatio + " Status: ");
                        home_status.setImageResource(R.drawable.green);
                    } else if (homePercent>= 50 && homePercent <100){
                        progress_ratio_home.setText(homePercent+ " % " + " used of " + homeRatio + " Status: ");
                        home_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_home.setText(homePercent+ " % " + " used of " + homeRatio + " Status: ");
                        home_status.setImageResource(R.drawable.red);
                    }

                    float utilitiesPercent = (utilitiesTotal/utilitiesRatio)*100;
                    if (utilitiesPercent<50){
                        progress_ratio_utilities.setText(utilitiesPercent+ " % " + " used of " + utilitiesRatio + " Status: ");
                        utilities_status.setImageResource(R.drawable.green);
                    } else if (utilitiesPercent>= 50 && utilitiesPercent <100){
                        progress_ratio_utilities.setText(utilitiesPercent+ " % " + " used of " + utilitiesRatio + " Status: ");
                        utilities_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_utilities.setText(utilitiesPercent+ " % " + " used of " + utilitiesRatio + " Status: ");
                        utilities_status.setImageResource(R.drawable.red);
                    }
                } else {
                    Toast.makeText(MonthlyAnalyticsActivity.this, "setStatusAnImageRecourses Errors", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



}