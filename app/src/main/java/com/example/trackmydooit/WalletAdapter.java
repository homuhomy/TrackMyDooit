package com.example.trackmydooit;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WalletAdapter extends  RecyclerView.Adapter<WalletAdapter.MyViewHolder>{
    Context context;
    ArrayList<WalletModel> walletModelArrayList;

    public WalletAdapter(Context context, ArrayList<WalletModel> walletModelArrayList) {
        this.context = context;
        this.walletModelArrayList = walletModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout_expense, parent,false);
        return new MyViewHolder(view);
    }

    public void add(WalletModel walletModel){
        walletModelArrayList.add(walletModel);
        notifyDataSetChanged();
    }

    public void clear(){
        walletModelArrayList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        WalletModel model = walletModelArrayList.get(position);
        /*String priority = model.getType();

        switch (model.getIncomeCategory()){
            case "Salary":
                holder.itemIV.setImageResource(R.drawable.ic_baseline_work_outline_24);
                holder.itemIV.setImageTintList(ColorStateList.valueOf(Color.parseColor("#AF002A")));
                holder.itemIV.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFCAD3")));
                break;
            case "Scholarship":
                holder.itemIV.setImageResource(R.drawable.school_black_24dp);
                holder.itemIV.setImageTintList(ColorStateList.valueOf(Color.parseColor("#0C3DBA")));
                holder.itemIV.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DEE5FB")));
                break;
            case "Allowance":
                holder.itemIV.setImageResource(R.drawable.family_restroom_fill0_wght300_grad0_opsz40);
                holder.itemIV.setImageTintList(ColorStateList.valueOf(Color.parseColor("#225503")));
                holder.itemIV.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DCFFDF")));
                break;
        }

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
        });*/
    }

    @Override
    public int getItemCount() {
        return walletModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView wallet,walletAmount,item;
        ImageView itemIV, walletLogo;
        View priority;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            walletLogo = itemView.findViewById(R.id.wallet_logo);
            item = itemView.findViewById(R.id.item);
            wallet = itemView.findViewById(R.id.wallet);
            walletAmount = itemView.findViewById(R.id.wallet_amount);
            itemIV = itemView.findViewById(R.id.itemIV);
            priority = itemView.findViewById(R.id.priority);
        }
    }
}
