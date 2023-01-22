package com.example.trackmydooit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionAdapter extends  RecyclerView.Adapter<TransactionAdapter.MyViewHolder>{
    Context context;
    ArrayList<TransactionModel> transactionModelArrayList;

    public TransactionAdapter(Context context, ArrayList<TransactionModel> transactionModelArrayList) {
        this.context = context;
        this.transactionModelArrayList = transactionModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout_expense, parent,false);

        return new MyViewHolder(view);
    }

    public void add(TransactionModel transactionModel){
        transactionModelArrayList.add(transactionModel);
        notifyDataSetChanged();
    }

    public void clear(){
        transactionModelArrayList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TransactionModel model = transactionModelArrayList.get(position);
        String priority = model.getType();
        if(priority.equalsIgnoreCase("Expense")){
            holder.amount.setTextColor(0xAAEE0000);
            holder.item.setText("Category: " + model.getExpenseCategory());

        }else if(priority.equalsIgnoreCase("Income")){
            holder.amount.setTextColor(0xAA138600);
            holder.item.setText("Category: " + model.getIncomeCategory());
        }
        holder.amount.setText("RM " + model.getAmount());
        holder.date.setText("Date: " + model.getTime());
        holder.note.setText("Note: " + model.getNote());
        holder.wallet.setText("Wallet: " + model.getWalletCategory());


        //to update
        holder.priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateTransActivity.class);
                intent.putExtra("id", transactionModelArrayList.get(position).getExpenseId());
                intent.putExtra("amount", transactionModelArrayList.get(position).getAmount());
                intent.putExtra("note", transactionModelArrayList.get(position).getNote());
                intent.putExtra("type", transactionModelArrayList.get(position).getType());
                intent.putExtra("wallet", transactionModelArrayList.get(position).getWalletCategory());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView note,wallet,amount,item,date;
        ImageView itemIV;
        View priority;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            note = itemView.findViewById(R.id.note);
            item = itemView.findViewById(R.id.item);
            wallet = itemView.findViewById(R.id.wallet);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            itemIV = itemView.findViewById(R.id.itemIV);
            priority = itemView.findViewById(R.id.priority);
        }
    }
}
