package com.example.trackmydooit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;;

import com.anychart.AnyChartView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MonthlyAnalyticsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private String onlineUserID = "";
    private DatabaseReference expenseRef,personalRef;

    private TextView monthRatioSpending, monthSpentAmount, totalBudgetAmountTextView, analyticsTransportAmount, analyticsFoodAmount, analyticsEntertainmentAmount, analyticsPersonalAmount, analyticsHomeAmount, analyticsUtilitiesAmount;

    private RelativeLayout relativeLayoutTransport, relativeFood, relativeEntertainment, relativePersonal, relativeHome, relativeUtilities;

    private AnyChartView anyChartView;

    private ImageView transport_status, food_status, entertainment_status, personal_status, home_status, utilities_status, status_image_red, status_image_green, status_image_brown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_analytics);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Analytics: ");

        mAuth = FirebaseAuth.getInstance();
        onlineUserID = mAuth.getCurrentUser().getUid();
        expenseRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserID);

        //totalBudgetAmountTextView = findViewById(R.id.totalBudgetAmountTextView);

        //analytics?
        monthSpentAmount = findViewById(R.id.monthSpentAmount);
        monthRatioSpending = findViewById(R.id.monthRatioSpending);

        //idk wtf
        analyticsTransportAmount = findViewById(R.id.analyticsTransportAmount);
        analyticsFoodAmount = findViewById(R.id.analyticsFoodAmount);
        analyticsEntertainmentAmount = findViewById(R.id.analyticsEntertainmentAmount);
        analyticsPersonalAmount = findViewById(R.id.analyticsPersonalAmount);
        analyticsUtilitiesAmount = findViewById(R.id.analyticsUtilitiesAmount);


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

        anyChartView = findViewById(R.id.anyChartView);

        






    }
}