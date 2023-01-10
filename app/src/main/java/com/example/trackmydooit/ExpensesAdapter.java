package com.example.trackmydooit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {

    private ExpenseActivity mContext;
    private List<Data> myDataList;
    private String postKey = "";
    private String item = "";
    private String note = "";
    private String wallet = "";
    private int amount  = 0;

    public ExpensesAdapter(ExpenseActivity mContext, List<Data> myDataList) {
        this.mContext = mContext;
        this.myDataList = myDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.retrieve_layout_income, parent,false);
        return new ExpensesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Data data = myDataList.get(position);

        holder.item.setText("Item: " + data.getItem());
        holder.amount.setText("Amount: " + data.getAmount());
        holder.date.setText("Date: " + data.getDate());
        holder.notes.setText("Note: " + data.getNotes());
        holder.wallet.setText("Wallet: " + data.getWallet());


//        switch (data.getItem()){
//            case "Transport":
//                holder.imageView.setImageResource(R.drawable.directions_bus_fill1_wght300_grad0_opsz40);
//                break;
//            case "Food":
//                holder.imageView.setImageResource(R.drawable.ic_home);
//                break;
//            case "Entertainment":
//                holder.imageView.setImageResource(R.drawable.ic_home);
//                break;
//            case "Home":
//                holder.imageView.setImageResource(R.drawable.ic_home);
//                break;
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postKey = data.getId();
                item = data.getItem();
                amount = data.getAmount();
                note = data.getNotes();
                wallet = data.getWallet();
                updateData();
            }
        });
    }

    private void updateData() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.update_layout_expense,null);

        myDialog.setView(mView);
        final AlertDialog dialog = myDialog.create();

        // elements to edit the budget
        final TextView mItem = mView.findViewById(R.id.itemName);
        final TextView mWallet = mView.findViewById(R.id.walletName);
        final EditText mAmount = mView.findViewById(R.id.amountTransET);
        final EditText mNotes = mView.findViewById(R.id.note);

        mItem.setText(item);
        mWallet.setText(wallet);


        mAmount.setText(String.valueOf(amount));
        mAmount.setSelection(String.valueOf(amount).length());

        mNotes.setText(note);
        mNotes.setSelection(note.length());

        Button delBut = mView.findViewById(R.id.deleteTransBTN);
        Button updateBut = mView.findViewById(R.id.updateTransBTN);

        updateBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Integer.parseInt(mAmount.getText().toString());
                note = mNotes.getText().toString();

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                MutableDateTime epoch = new MutableDateTime();
                epoch.setDate(0);
                DateTime now = new DateTime();
                Months months = Months.monthsBetween(epoch, now);

                Data data = new Data(item, date, postKey, wallet, note, amount, months.getMonths());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.child(postKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(mContext, "Updated was successfully!", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            Toast.makeText(mContext, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.dismiss();
            }
        });

        delBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.child(postKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(mContext, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            Toast.makeText(mContext, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item, amount, date, notes, wallet;
        public ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            notes = itemView.findViewById(R.id.note);
            wallet = itemView.findViewById(R.id.wallet);

            //imageView = itemView.findViewById(R.id.imageView);

        }
    }
}