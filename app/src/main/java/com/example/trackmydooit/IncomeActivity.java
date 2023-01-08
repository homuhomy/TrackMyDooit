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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class IncomeActivity extends AppCompatActivity {

    private TextView incomeTV;
    private RecyclerView RVIncome;
    private Toolbar ToolBar;

    private ExtendedFloatingActionButton FABAddIncome;

    private DatabaseReference incomeRef;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    private String postKey = "";
    private String item = "";
    private int amount  = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        //toolbar title
        ToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(ToolBar);
        getSupportActionBar().setTitle("My Income");

        mAuth = FirebaseAuth.getInstance();
        incomeRef = FirebaseDatabase.getInstance().getReference().child("income").child(mAuth.getCurrentUser().getUid());
        loader = new ProgressDialog(this);

        incomeTV = findViewById(R.id.incomeTV);
        RVIncome = findViewById(R.id.RVIncome);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        RVIncome.setHasFixedSize(true);
        RVIncome.setLayoutManager(linearLayoutManager);

        incomeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalAmount = 0;

                for (DataSnapshot snap: snapshot.getChildren()){
                    Data data = snap.getValue(Data.class);
                    totalAmount = totalAmount + data.getAmount();
                    String sTotal = "Income this month: RM" + totalAmount;
                    incomeTV.setText(sTotal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FABAddIncome = findViewById(R.id.FABAddIncome);

        FABAddIncome.setOnClickListener(view -> additem());
    }


    private void additem() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout_income, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner spinnerIncome = myView.findViewById(R.id.spinnerIncome);
        final Spinner spinnerWallet = myView.findViewById(R.id.spinnerWallet);
        final EditText IncomeAmountET = myView.findViewById(R.id.IncomeAmountET);
        final Button cancelTransBTN = myView.findViewById(R.id.cancelTransBTN);
        final Button addTransBTN = myView.findViewById(R.id.addTransBTN);

        addTransBTN.setOnClickListener(view -> {
            String incomeAmount = IncomeAmountET.getText().toString();
            String incomeItem = spinnerIncome.getSelectedItem().toString();
            String walletItem = spinnerWallet.getSelectedItem().toString();

            if (TextUtils.isEmpty(incomeAmount)){
                IncomeAmountET.setError("Amount is required!");
                return;
            }

            if(incomeItem.equals("Select an expense category")){
                Toast.makeText(IncomeActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
            }

            if(walletItem.equals("Select a wallet")){
                Toast.makeText(IncomeActivity.this, "Select a valid wallet", Toast.LENGTH_SHORT).show();
            }
            else {
                loader.setMessage("Adding an income item..");
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

                Data data = new Data(incomeItem, date, id, null, Integer.parseInt(incomeAmount), months.getMonths());
                incomeRef.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(IncomeActivity.this, "Expense item added successfully!", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            Toast.makeText(IncomeActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                        loader.dismiss();
                    }
                });

            }
            dialog.dismiss();
        });

        cancelTransBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(incomeRef, Data.class)
                .build();

        FirebaseRecyclerAdapter<Data,MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Data model) {

                holder.setItemAmount("Allocated amount: RM" + model.getAmount());
                holder.setDate("Date Created: " + model.getDate());
                holder.setItemName("Category: " + model.getItem());

                holder.notes.setVisibility(View.GONE);

                switch (model.getItem()){
                    case "Salary":
                        holder.itemIV.setImageResource(R.drawable.home_fill1_wght300_grad0_opsz24);
                        break;
                    case "Scholarship":
                        holder.itemIV.setImageResource(R.drawable.water_drop_fill0_wght300_grad0_opsz40);
                        break;
                    case "Allowance":
                        holder.itemIV.setImageResource(R.drawable.dentistry_fill1_wght300_grad0_opsz40);
                        break;
                }

                //edit budget
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postKey = getRef(position).getKey();
                        item = model.getItem();
                        amount = model.getAmount();
                        updateData();
                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout_income, parent, false);
                return new MyViewHolder(view);
            }
        };

        RVIncome.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public ImageView itemIV;
        public TextView notes, date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            itemIV = itemView.findViewById(R.id.itemIV);
            notes = itemView.findViewById(R.id.note);
            date = itemView.findViewById(R.id.date);
        }

        // item from update budget layout xml
        public void setItemName (String itemName){
            TextView item = mView.findViewById(R.id.item);
            item.setText(itemName);
        }

        //amount from retreive layout budget xml
        public void setItemAmount (String itemAmount){
            TextView item = mView.findViewById(R.id.amount);
            item.setText(itemAmount);
        }

        //date from retrive layout budget xml
        public void setDate (String itemDate){
            TextView item = mView.findViewById(R.id.date);
            item.setText(itemDate);
        }
    }

    //to update budget
    private void updateData(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View mView = inflater.inflate(R.layout.update_layout_income,null);

        myDialog.setView(mView);
        final AlertDialog dialog = myDialog.create();

        // elements to edit the budget
        final TextView mItem = mView.findViewById(R.id.itemName);
        final EditText mAmount = mView.findViewById(R.id.amountTransET);
        final EditText mNotes = mView.findViewById(R.id.note);

        mNotes.setVisibility(View.GONE);

        mItem.setText(item);

        mAmount.setText(String.valueOf(amount));
        mAmount.setSelection(String.valueOf(amount).length());

        Button delBut = mView.findViewById(R.id.deleteTransBTN);
        Button updateBut = mView.findViewById(R.id.updateTransBTN);

        updateBut.setOnClickListener(view -> {
            amount = Integer.parseInt(mAmount.getText().toString());

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Calendar cal = Calendar.getInstance();
            String date = dateFormat.format(cal.getTime());

            MutableDateTime epoch = new MutableDateTime();
            epoch.setDate(0);
            DateTime now = new DateTime();
            Months months = Months.monthsBetween(epoch, now);

            Data data = new Data(item, date, postKey, null, amount, months.getMonths());
            incomeRef.child(postKey).setValue(data).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(IncomeActivity.this, "Income was updated successfully!", Toast.LENGTH_SHORT).show();
                }

                else {
                    Toast.makeText(IncomeActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            });
            dialog.dismiss();
        });

        delBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomeRef.child(postKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(IncomeActivity.this, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            Toast.makeText(IncomeActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}