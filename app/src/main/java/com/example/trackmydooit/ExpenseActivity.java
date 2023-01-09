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
import com.google.android.material.button.MaterialButtonToggleGroup;
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

public class ExpenseActivity extends AppCompatActivity {

    //TODO: Add wallet
    private TextView expenseTV;
    private RecyclerView RVExpense;
    private Toolbar ToolBar;

    private ExtendedFloatingActionButton FABAddExpense;
    private MaterialButtonToggleGroup toggleButton;

    private DatabaseReference expenseRef;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;
    private Button expenseTB, incomeTB;

    private String postKey = "";
    private String item = "";
    private int amount  = 0;
    private String wallet = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        //toolbar title
        ToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(ToolBar);
        getSupportActionBar().setTitle("My Expenses");

        mAuth = FirebaseAuth.getInstance();
        expenseRef = FirebaseDatabase.getInstance().getReference().child("expense").child(mAuth.getCurrentUser().getUid());
        loader = new ProgressDialog(this);

        expenseTV = findViewById(R.id.expenseTV);
        RVExpense = findViewById(R.id.RVExpense);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        RVExpense.setHasFixedSize(true);
        RVExpense.setLayoutManager(linearLayoutManager);

//        expenseTB = findViewById(R.id.expenseTB);
//        incomeTB = findViewById(R.id.incomeTB);

        //expenseTB.setOnClickListener(view -> expenseTB.getContext().startActivity(new Intent(expenseTB.getContext(), ExpenseActivity.class)));
        //incomeTB.setOnClickListener(view -> incomeTB.getContext().startActivity(new Intent(incomeTB.getContext(), IncomeActivity.class)));

        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalAmount = 0;

                for (DataSnapshot snap: snapshot.getChildren()){
                    Data data = snap.getValue(Data.class);
                    totalAmount = totalAmount + data.getAmount();
                    String sTotal = "Expense this month: RM" + totalAmount;
                    expenseTV.setText(sTotal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FABAddExpense = findViewById(R.id.FABAddExpense);

        FABAddExpense.setOnClickListener(view -> additem());
    }


    private void additem() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout_expense, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner spinnerExpense = myView.findViewById(R.id.spinnerExpense);
        final Spinner spinnerWallet = myView.findViewById(R.id.spinnerWallet);
        final EditText ExpenseAmountET = myView.findViewById(R.id.ExpenseAmountET);
        final Button cancelTransBTN = myView.findViewById(R.id.cancelTransBTN);
        final Button addTransBTN = myView.findViewById(R.id.addTransBTN);
        final Button addReceiptBTN = myView.findViewById(R.id.addReceiptBTN);
        final EditText note = myView.findViewById(R.id.note);

        addReceiptBTN.setOnClickListener(view -> addReceiptBTN.getContext().startActivity(new Intent(addReceiptBTN.getContext(), CameraActivity.class)));

        addTransBTN.setOnClickListener(view -> {
            String expenseAmount = ExpenseAmountET.getText().toString();
            String expenseItem = spinnerExpense.getSelectedItem().toString();
            String walletItem = spinnerWallet.getSelectedItem().toString();

            if (TextUtils.isEmpty(expenseAmount)){
                ExpenseAmountET.setError("Amount is required!");
                return;
            }

            if(expenseItem.equals("Select an expense category")){
                Toast.makeText(ExpenseActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
            }

            if(walletItem.equals("Select a wallet")){
                Toast.makeText(ExpenseActivity.this, "Select a valid wallet", Toast.LENGTH_SHORT).show();
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

                Data data = new Data(expenseItem, date, id, walletItem, null, Integer.parseInt(expenseAmount), months.getMonths());
                expenseRef.child(id).setValue(data).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(ExpenseActivity.this, "Expense item added successfully!", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        Toast.makeText(ExpenseActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }

                    loader.dismiss();
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
                .setQuery(expenseRef, Data.class)
                .build();

        FirebaseRecyclerAdapter<Data,MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Data model) {

                //TODO : WALLET
                holder.setItemAmount("Allocated amount: RM" + model.getAmount());
                holder.setDate("Date Created: " + model.getDate());
                holder.setItemName("Category: " + model.getItem());
                holder.setWalletName("Wallet: " + model.getWallet());

                holder.notes.setVisibility(View.GONE);

                switch (model.getItem()){
                    case "Rent":
                        holder.itemIV.setImageResource(R.drawable.home_fill1_wght300_grad0_opsz24);
                        break;
                    case "Utilities":
                        holder.itemIV.setImageResource(R.drawable.water_drop_fill0_wght300_grad0_opsz40);
                        break;
                    case "Dental":
                        holder.itemIV.setImageResource(R.drawable.dentistry_fill1_wght300_grad0_opsz40);
                        break;
                    case "Transportation":
                        holder.itemIV.setImageResource(R.drawable.directions_bus_fill1_wght300_grad0_opsz40);
                        break;
                }

                /*switch (model.getWallet()){
                    case "Maybank":
                        break;
                    case "Boost":
                        holder.itemIV.setImageResource(R.drawable.water_drop_fill0_wght300_grad0_opsz40);
                        break;
                    case "GrabPay":
                        holder.itemIV.setImageResource(R.drawable.dentistry_fill1_wght300_grad0_opsz40);
                        break;
                    case "CIMB":
                        holder.itemIV.setImageResource(R.drawable.directions_bus_fill1_wght300_grad0_opsz40);
                        break;
                }*/

                //edit expense
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postKey = getRef(position).getKey();
                        item = model.getItem();
                        amount = model.getAmount();
                        wallet = model.getWallet();
                        updateData();
                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout_expense, parent, false);
                return new MyViewHolder(view);
            }
        };

        RVExpense.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public ImageView itemIV;
        public TextView notes, date, wallet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            itemIV = itemView.findViewById(R.id.itemIV);
            //wallet = itemView.findViewById(R.id.wallet);
            notes = itemView.findViewById(R.id.note);
            date = itemView.findViewById(R.id.date);
        }

        // item from update expense layout xml
        public void setItemName (String itemName){
            TextView item = mView.findViewById(R.id.item);
            item.setText(itemName);
        }

        public void setWalletName (String walletName){
            TextView item = mView.findViewById(R.id.wallet);
            item.setText(walletName);
        }

        //amount from retreive layout expense xml
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
        View mView = inflater.inflate(R.layout.update_layout_expense,null);

        myDialog.setView(mView);
        final AlertDialog dialog = myDialog.create();

        // elements to edit the budget
        final TextView mItem = mView.findViewById(R.id.itemName);
        //final TextView mWallet = mView.findViewById(R.id.wallet);
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

            Data data = new Data(item, date, postKey, wallet, null, amount, months.getMonths());
            expenseRef.child(postKey).setValue(data).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(ExpenseActivity.this, "Expense was updated successfully!", Toast.LENGTH_SHORT).show();
                }

                else {
                    Toast.makeText(ExpenseActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            });
            dialog.dismiss();
        });

        delBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenseRef.child(postKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ExpenseActivity.this, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            Toast.makeText(ExpenseActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}