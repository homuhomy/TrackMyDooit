package com.example.trackmydooit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class ExpenseActivity extends AppCompatActivity {

    private TextView expenzeTV;
    private RecyclerView RVExpense;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private ExtendedFloatingActionButton FABAddExpense;

    private FirebaseAuth mAuth;
    private String onlineUserID = "";
    private ProgressDialog loader;
    private DatabaseReference expenseRef;

    private ExpensesAdapter expensesAdapter;
    private List<Data> myDataList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("My Expenses");
        //to add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        expenzeTV = findViewById(R.id.expenseTV);
        //progressBar = findViewById(R.id.progressBar);
        RVExpense = findViewById(R.id.RVExpense);
        FABAddExpense = findViewById(R.id.FABAddExpense);
        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        onlineUserID = mAuth.getCurrentUser().getUid();
        expenseRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        RVExpense.setHasFixedSize(true);
        RVExpense.setLayoutManager(linearLayoutManager);

        myDataList = new ArrayList<>();
        expensesAdapter = new ExpensesAdapter(ExpenseActivity.this, myDataList);
        RVExpense.setAdapter(expensesAdapter);

        readItems();


        FABAddExpense.setOnClickListener(new View.OnClickListener() {
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myDataList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Data data = dataSnapshot.getValue(Data.class);
                    myDataList.add(data);
                }

                expensesAdapter.notifyDataSetChanged();
                //progressBar.setVisibility(View.GONE);

                int totalAmount = 0;
                for (DataSnapshot ds : snapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>)ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;

                    expenzeTV.setText("Total Spendings: RM" + totalAmount);
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
        View myView = inflater.inflate(R.layout.input_layout_expense, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner spinnerExpense = myView.findViewById(R.id.spinnerExpense);
        final Spinner spinnerWallet = myView.findViewById(R.id.spinnerWallet);
        final EditText ExpenseAmountET = myView.findViewById(R.id.ExpenseAmountET);
        final EditText ExpenseDescriptionET = myView.findViewById(R.id.ExpenseDescriptionET);
        final Button cancelTransBTN = myView.findViewById(R.id.cancelTransBTN);
        final Button addTransBTN = myView.findViewById(R.id.addTransBTN);
        final Button addReceiptBTN = myView.findViewById(R.id.addReceiptBTN);

        addReceiptBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReceiptBTN.getContext().startActivity(new Intent(addReceiptBTN.getContext(), CameraActivity.class));
            }
        });

        addTransBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String expenseAmount = ExpenseAmountET.getText().toString();
                String expenseItem = spinnerExpense.getSelectedItem().toString();
                String walletItem = spinnerWallet.getSelectedItem().toString();
                String notes = ExpenseDescriptionET.getText().toString();


                if (TextUtils.isEmpty(expenseAmount)){
                    ExpenseAmountET.setError("Amount is required!");
                    return;
                }

                if(expenseItem.equals("Select an expense category")){
                    Toast.makeText(ExpenseActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
                }

                if(walletItem.equals("Select a wallet")){
                    Toast.makeText(ExpenseActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(notes)){
                    ExpenseDescriptionET.setError("Note is required!!");
                    return;
                }

                else {
                    loader.setMessage("Adding an expense item..");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    String id = expenseRef.push().getKey();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar cal = Calendar.getInstance();
                    String date = dateFormat.format(cal.getTime());

                    MutableDateTime epoch = new MutableDateTime();
                    epoch.setDate(0);
                    DateTime now = new DateTime();
                    Months months = Months.monthsBetween(epoch, now);

                    Data data = new Data(expenseItem, date, id, walletItem, notes, Integer.parseInt(expenseAmount), months.getMonths());
                    expenseRef.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ExpenseActivity.this, "Expense item added successfully!", Toast.LENGTH_SHORT).show();
                            }

                            else {
                                Toast.makeText(ExpenseActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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