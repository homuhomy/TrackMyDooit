package com.example.trackmydooit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.trackmydooit.databinding.ActivityAddExpenseBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddExpenseActivity extends AppCompatActivity {

    ActivityAddExpenseBinding binding;
    private String type;
    FirebaseFirestore fStore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        type = getIntent().getStringExtra("type");


        if(type.equals("Income")){
            binding.RBIncome.setChecked(true);
            binding.spinnerExpense.setVisibility(View.GONE);
            binding.spinnerIncome.setVisibility(View.VISIBLE);
        }else{
            binding.RBExpense.setChecked(true);
            binding.spinnerExpense.setVisibility(View.VISIBLE);
            binding.spinnerIncome.setVisibility(View.GONE);
        }
        binding.RBIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "Income";
                binding.spinnerExpense.setVisibility(View.GONE);
                binding.spinnerIncome.setVisibility(View.VISIBLE);
            }
        });

        binding.RBExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "Expense";
                binding.spinnerExpense.setVisibility(View.VISIBLE);
                binding.spinnerIncome.setVisibility(View.GONE);
            }
        });

        binding.addTransBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = UUID.randomUUID().toString();
                String amount = binding.amount.getText().toString();
                String note = binding.ExpenseDescriptionET.getText().toString();

                String walletCategory = binding.spinnerWallet.getSelectedItem().toString();

                boolean incomeChecked = binding.RBIncome.isChecked();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String currentDate = sdf.format(new Date());
                //DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");


                if(incomeChecked){
                    type = "Income";
                    binding.spinnerExpense.setVisibility(View.GONE);
                    binding.spinnerIncome.setVisibility(View.VISIBLE);
                } else {
                    type = "Expense";
                    binding.spinnerExpense.setVisibility(View.VISIBLE);
                    binding.spinnerIncome.setVisibility(View.GONE);
                }
                if(amount.trim().length() == 0){
                    binding.amount.setError("Empty");
                    return;
                }

                String expenseCategory = binding.spinnerExpense.getSelectedItem().toString();
                String incomeCategory = binding.spinnerIncome.getSelectedItem().toString();

                Map<String,Object> transaction = new HashMap<>();
                transaction.put("date", currentDate);
                transaction.put("id", id);
                transaction.put("amount", amount);
                transaction.put("note", note);
                transaction.put("expense category", expenseCategory);
                transaction.put("income category", incomeCategory);
                transaction.put("type", type);
                transaction.put("wallet", walletCategory);


                fStore.collection("Expenses").document(firebaseAuth.getUid()).collection("Transaction").document(id)
                        .set(transaction)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddExpenseActivity.this, "Added", Toast.LENGTH_SHORT).show();
                                binding.ExpenseDescriptionET.setText("");
                                binding.amount.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddExpenseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        type = getIntent().getStringExtra("type");


        if(type.equals("Income")){
            binding.RBIncome.setChecked(true);
        }else{
            binding.RBExpense.setChecked(true);
        }
        binding.RBIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "Income";
            }
        });

        binding.RBExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "Expense";
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addTransBTN) {
            createExpense();
            return true;
        }
        return true;
    }


    private void createExpense() {
        String expenseId = UUID.randomUUID().toString();
        String amount = binding.amount.getText().toString();
        String note = binding.ExpenseDescriptionET.getText().toString();
        String expenseCategory = binding.spinnerExpense.getSelectedItem().toString();
        String walletCategory = binding.spinnerWallet.getSelectedItem().toString();

        boolean incomeChecked = binding.RBIncome.isChecked();

        if(incomeChecked){
            type = "Income";
        } else {
            type = "Expense";
        }
        if(amount.trim().length() == 0){
            binding.amount.setError("Empty");
            return;
        }

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);
        Weeks weeks = Weeks.weeksBetween(epoch,now);

        TransactionModel expenseModel = new TransactionModel(expenseId, note, type, expenseCategory,walletCategory, Long.parseLong(amount), date);

        FirebaseFirestore
                .getInstance()
                .collection("Expenses")
                .document(expenseId)
                .set(expenseModel);
        finish();
    }*/
}