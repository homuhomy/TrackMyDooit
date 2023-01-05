package com.example.trackmydooit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
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
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

public class ExpenseActivity extends AppCompatActivity {
    private Toolbar TBExpense;
    private TextView TVTotalSpent;
    private ProgressBar PBExpense;
    private RecyclerView RVExpense;
    private ExtendedFloatingActionButton FABAddTrans;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private String onlineUserId = "";
    private DatabaseReference expenseRef;

    private ImageView addCategory;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        setSupportActionBar(findViewById(R.id.TBExpense));
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

    private void addItemSpentOn(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout_expense, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner SPWallet = myView.findViewById(R.id.SPWallet);
        final Spinner SPCategory = myView.findViewById(R.id.SPCategory);
        final EditText amount = myView.findViewById(R.id.amount);
        final EditText note = myView.findViewById(R.id.note);
        final Button BTCancel = myView.findViewById(R.id.BTCancel);
        final Button BTadd = myView.findViewById(R.id.BTadd);
        final Button BTAddReceipt = myView.findViewById(R.id.BTAddReceipt);
        final ImageView addCategory = myView.findViewById(R.id.addCategory);

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (ExpenseActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });

        //note.setVisibility(View.VISIBLE); //set this as visible if we need the user to add note
        BTAddReceipt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //to camera activity
            }
        });


        BTadd.setOnClickListener((view -> {
            String Amount = amount.getText().toString();
            String WalletItems = SPWallet.getSelectedItem().toString();
            String CategoryItems = SPCategory.getSelectedItem().toString();
            String notes = note.getText().toString();

            if(WalletItems.equals("Select Wallet")){
                Toast.makeText(ExpenseActivity.this, "Select a wallet", Toast.LENGTH_SHORT).show();
            }

            if(CategoryItems.equals("Select Category")){
                Toast.makeText(ExpenseActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
            }

            //change this to make the notes optional
            /*if(TextUtils.isEmpty(notes)){
                note.setError("Note is required");
                return;
            }*/

            if(TextUtils.isEmpty(Amount)){
                amount.setError("Amount is required!");
                return;
            }

            else{
                progressDialog.setMessage("Adding expense item");
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

                Data data = new Data(CategoryItems, date, id, notes, Integer.parseInt(Amount), months.getMonths());
                expenseRef.child(id).setValue(data).addOnCompleteListener((task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(ExpenseActivity.this, "Budget CategoryItems added successfully ", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ExpenseActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }));

            }
            dialog.dismiss();
        }));
        BTCancel.setOnClickListener((view -> {dialog.dismiss();}));
        dialog.show();
    }
}
