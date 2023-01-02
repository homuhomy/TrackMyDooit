/*
package com.example.trackmydooit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ExpenseActivity extends AppCompatActivity {
    private Toolbar TBExpense;
    private TextView TVTotalSpent;
    private ProgressBar PBExpense;
    private RecyclerView RVExpense;
    private FloatingActionButton FABAddTrans;
    private ProgressDialog progressDialog;

    private RadioButton RBIncome;
    private RadioButton RBExpenses;

    private FirebaseAuth mAuth;
    private String onlineUserId = "";
    private DatabaseReference expenseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

TBExpense = findViewById(R.id.TBExpense);
        setSupportActionBar(TBExpense);
        getSupportActionBar().setTitle("Total Spending");


        TVTotalSpent = findViewById(R.id.TVTotalSpent);
        PBExpense = findViewById(R.id.PBExpense);
        RVExpense = findViewById(R.id.RVExpense);
        FABAddTrans = findViewById(R.id.FABAddTrans);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        expenseRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);

        FABAddTrans.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addItemSpentOn();
            }
        });


        }

radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (radioButton != null) {
                radioButton.setBackgroundColor(Color.TRANSPARENT);
                radioButton.setButtonDrawable(0); // removes the image
            }
            radioButton = (RadioButton) group.findViewById(checkedId);
            radioButton.setBackgroundColor(Color.YELLOW);
            radioButton.setButtonDrawable(R.drawable.icon); //sets the image
        }
    });


    private void addItemSpentOn(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout_expense, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner SPCategory = myView.findViewById(R.id.SPCategory);
        final EditText amount = myView.findViewById(R.id.amount);
        final EditText note = myView.findViewById(R.id.note);
        final Button BTcancel = findViewById(R.id.BTcancel);
        final Button BTadd = findViewById(R.id.BTadd); //why cannot use myView.xx?

        note.setVisibility(myView.VISIBLE);

        BTadd.setOnClickListener((view -> {
            String Amount = amount.getText().toString();
            String Item = SPCategory.getSelectedItem().toString();
            String notes = note.getText().toString();

            if(TextUtils.isEmpty(Amount)){
                amount.setError("Amount is required!");
                return;
            }

            if(Item.equals("Select Item")){
                Toast.makeText(ExpenseActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
            }

            //change this to make the notes optional
            if(TextUtils.isEmpty(notes)){
                note.setError("Note is required");
                return;
            }


            else{
                progressDialog.setMessage("Adding a budget item");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                String id = expenseRef.push().getKey();
                DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                MutableDateTime epoch = new MutableDateTime();
                epoch.setDate(0);
                DateTime now = new DateTime();
                Months months = Months.monthsBetween(epoch, now);

                Data data = new Data(Item, date, id, notes, Integer.parseInt(Amount), months.getMonths());
                expenseRef.child(id).setValue(data).addOnCompleteListener((task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(ExpenseActivity.this, "Budget Item added successfuly ", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ExpenseActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }));

            }
            dialog.dismiss();
        }));
        BTcancel.setOnClickListener((view -> {dialog.dismiss();}));
        dialog.show();
    }
}
*/
