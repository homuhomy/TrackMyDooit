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
import android.widget.Toolbar;

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

public class BudgetActivity extends AppCompatActivity {

    private TextView budgetTV;
    private RecyclerView RVBudget;
    private Toolbar settingsToolBar;

    private ExtendedFloatingActionButton FABAddBudget;

    private DatabaseReference budgetRef;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    private String postKey = "";
    private String item = "";
    private int amount  = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        mAuth = FirebaseAuth.getInstance();
        budgetRef = FirebaseDatabase.getInstance().getReference().child("budget").child(mAuth.getCurrentUser().getUid());
        loader = new ProgressDialog(this);

        budgetTV = findViewById(R.id.budgetTV);
        RVBudget = findViewById(R.id.RVBudget);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        RVBudget.setHasFixedSize(true);
        RVBudget.setLayoutManager(linearLayoutManager);

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalAmount = 0;

                for (DataSnapshot snap: snapshot.getChildren()){
                    Data data = snap.getValue(Data.class);
                    totalAmount = totalAmount + data.getAmount();
                    String sTotal = String.valueOf("Budget this month: RM" + totalAmount);
                    budgetTV.setText(sTotal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FABAddBudget = findViewById(R.id.FABAddBudget);

        FABAddBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additem();
            }
        });
    }


    private void additem() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout_budget, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner spinnerBudget = myView.findViewById(R.id.spinnerBudget);
        final EditText BudgetAmountET = myView.findViewById(R.id.BudgetAmountET);
        final Button cancelBudgetBTN = myView.findViewById(R.id.cancelBudgetBTN);
        final Button saveBudgetBTN = myView.findViewById(R.id.saveBudgetBTN);

        saveBudgetBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String budgetAmount = BudgetAmountET.getText().toString();
                String budgetItem = spinnerBudget.getSelectedItem().toString();

                if (TextUtils.isEmpty(budgetAmount)){
                    BudgetAmountET.setError("Amount is required!");
                    return;
                }

                if(budgetItem.equals("Click to select a budget category")){
                    Toast.makeText(BudgetActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
                }

                else {
                    loader.setMessage("Adding a budget item..");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    String id = budgetRef.push().getKey();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar cal = Calendar.getInstance();
                    String date = dateFormat.format(cal.getTime());

                    MutableDateTime epoch = new MutableDateTime();
                    epoch.setDate(0);
                    DateTime now = new DateTime();
                    Months months = Months.monthsBetween(epoch, now);

                    Data data = new Data(budgetItem, date, id, null, Integer.parseInt(budgetAmount), months.getMonths());
                    budgetRef.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(BudgetActivity.this, "Budget item added successfully!", Toast.LENGTH_SHORT).show();
                            }

                            else {
                                Toast.makeText(BudgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }

                            loader.dismiss();
                        }
                    });

                }
                dialog.dismiss();
            }
        });

        cancelBudgetBTN.setOnClickListener(new View.OnClickListener() {
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
                .setQuery(budgetRef, Data.class)
                .build();

        FirebaseRecyclerAdapter<Data,MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
                protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Data model) {

                holder.setItemAmount("Allocated amount: RM" + model.getAmount());
                holder.setDate("Date Created: " + model.getDate());
                holder.setItemName("Category: " + model.getItem());

                holder.notes.setVisibility(View.GONE);

                switch (model.getItem()){
                    case "Transport":
                        holder.itemIV.setImageResource(R.drawable.ic_home);
                        break;
                    case "Food":
                        holder.itemIV.setImageResource(R.drawable.ic_home);
                        break;
                    case "Entertainment":
                        holder.itemIV.setImageResource(R.drawable.ic_home);
                        break;
                    case "Home":
                        holder.itemIV.setImageResource(R.drawable.ic_home);
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout_budget, parent, false);
                return new MyViewHolder(view);
            }
        };

        RVBudget.setAdapter(adapter);
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
        View mView = inflater.inflate(R.layout.update_layout_budget,null);

        myDialog.setView(mView);
        final AlertDialog dialog = myDialog.create();

        // elements to edit the budget
        final TextView mItem = mView.findViewById(R.id.itemName);
        final EditText mAmount = mView.findViewById(R.id.amountBudgetET);
        final EditText mNotes = mView.findViewById(R.id.note);

        mNotes.setVisibility(View.GONE);

        mItem.setText(item);

        mAmount.setText(String.valueOf(amount));
        mAmount.setSelection(String.valueOf(amount).length());

        Button delBut = mView.findViewById(R.id.deleteBudgetBTN);
        Button updateBut = mView.findViewById(R.id.updateBudgetBTN);

        updateBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Integer.parseInt(mAmount.getText().toString());

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                MutableDateTime epoch = new MutableDateTime();
                epoch.setDate(0);
                DateTime now = new DateTime();
                Months months = Months.monthsBetween(epoch, now);

                Data data = new Data(item, date, postKey, null, amount, months.getMonths());
                budgetRef.child(postKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(BudgetActivity.this, "Updated was successfully!", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            Toast.makeText(BudgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.dismiss();
            }
        });

        delBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                budgetRef.child(postKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(BudgetActivity.this, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            Toast.makeText(BudgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}