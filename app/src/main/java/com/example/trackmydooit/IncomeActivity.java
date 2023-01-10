package com.example.trackmydooit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class IncomeActivity extends AppCompatActivity {

    private TextView incomeTV;
    private RecyclerView RVIncome;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private ExtendedFloatingActionButton FABAddIncome;

    private FirebaseAuth mAuth;
    private String onlineUserID = "";
    private ProgressDialog loader;
    private DatabaseReference incomeRef;

    private IncomeAdapter incomeAdapter;
    private List<Data> myDataListIncome;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //to add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("My Incomes");


        incomeTV = findViewById(R.id.incomeTV);
        //progressBar = findViewById(R.id.progressBar);
        RVIncome = findViewById(R.id.RVIncome);
        FABAddIncome = findViewById(R.id.FABAddIncome);
        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        onlineUserID = mAuth.getCurrentUser().getUid();
        incomeRef = FirebaseDatabase.getInstance().getReference("income").child(onlineUserID);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        RVIncome.setHasFixedSize(true);
        RVIncome.setLayoutManager(linearLayoutManager);

        myDataListIncome = new ArrayList<>();
        incomeAdapter = new IncomeAdapter(IncomeActivity.this, myDataListIncome);
        RVIncome.setAdapter(incomeAdapter);

        readItems();

        FABAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemSpentOn();
            }
        });



    }

    private void readItems() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("income").child(onlineUserID);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myDataListIncome.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Data data = dataSnapshot.getValue(Data.class);
                    myDataListIncome.add(data);
                }

                incomeAdapter.notifyDataSetChanged();
                //progressBar.setVisibility(View.GONE);

                int totalAmount = 0;
                for (DataSnapshot ds : snapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>)ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;

                    incomeTV.setText("Total Income: RM" + totalAmount);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addItemSpentOn() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout_income, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner spinnerIncome = myView.findViewById(R.id.spinnerIncome);
        final Spinner spinnerWallet = myView.findViewById(R.id.spinnerWallet);
        final EditText IncomeAmountET = myView.findViewById(R.id.IncomeAmountET);
        final EditText incomeDescET = myView.findViewById(R.id.incomeDescET);
        final Button cancelTransBTN = myView.findViewById(R.id.cancelTransBTN);
        final Button addTransBTN = myView.findViewById(R.id.addTransBTN);

        addTransBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String incomeAmount = IncomeAmountET.getText().toString();
                String incomeItem = spinnerIncome.getSelectedItem().toString();
                String walletItem = spinnerWallet.getSelectedItem().toString();
                String notes = incomeDescET.getText().toString();


                if (TextUtils.isEmpty(incomeAmount)){
                    IncomeAmountET.setError("Amount is required!");
                    return;
                }

                if(incomeItem.equals("Select an income category")){
                    Toast.makeText(IncomeActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
                }

                if(walletItem.equals("Select a wallet")){
                    Toast.makeText(IncomeActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(notes)){
                    incomeDescET.setError("Note is required!!");
                    return;
                }

                else {
                    loader.setMessage("Adding a income..");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    String id = incomeRef.push().getKey();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar cal = Calendar.getInstance();
                    String date = dateFormat.format(cal.getTime());

                    MutableDateTime epoch = new MutableDateTime();
                    epoch.setDate(0);
                    DateTime now = new DateTime();
                    Months months = Months.monthsBetween(epoch, now);

                    Data data = new Data(incomeItem, date, id, walletItem, notes, Integer.parseInt(incomeAmount), months.getMonths());
                    incomeRef.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(IncomeActivity.this, "Income added successfully!", Toast.LENGTH_SHORT).show();
                            }

                            else {
                                Toast.makeText(IncomeActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }

                            loader.dismiss();
                        }
                    });

                }
                dialog.dismiss();
            }
        });

        cancelTransBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}