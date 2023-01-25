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
        String imageChangeExpense = model.getExpenseCategory();
        ArrayList<Integer> colorsList = new ArrayList<>();
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
        switch (model.getExpenseCategory()){
            case "Rent":
                holder.itemIV.setImageResource(R.drawable.home_fill1_wght300_grad0_opsz24);
                holder.itemIV.setImageTintList(ColorStateList.valueOf(Color.parseColor("#954707")));
                holder.itemIV.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FBEADC")));
                break;
            case "Utilities":
                holder.itemIV.setImageResource(R.drawable.water_drop_fill0_wght300_grad0_opsz40);
                holder.itemIV.setImageTintList(ColorStateList.valueOf(Color.parseColor("#3F278A")));
                holder.itemIV.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E5DCFF")));
                break;
            case "Education":
                holder.itemIV.setImageResource(R.drawable.school_black_24dp);
                holder.itemIV.setImageTintList(ColorStateList.valueOf(Color.parseColor("#225503")));
                holder.itemIV.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DCFFDF")));
                break;
            case "Travel":
                holder.itemIV.setImageResource(R.drawable.flight_fill0_wght300_grad0_opsz40);
                holder.itemIV.setImageTintList(ColorStateList.valueOf(Color.parseColor("#09878F")));
                holder.itemIV.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DCFBFF")));
                break;
            case "Food":
                holder.itemIV.setImageResource(R.drawable.restaurant_fill1_wght300_grad0_opsz20);
                holder.itemIV.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FB7840")));
                holder.itemIV.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FBEADC")));
                break;
            case "Entertainment":
                holder.itemIV.setImageResource(R.drawable.sports_esports_black_24dp);
                holder.itemIV.setImageTintList(ColorStateList.valueOf(Color.parseColor("#B17500")));
                holder.itemIV.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFF7DC")));
                break;
            case "Personal":
                holder.itemIV.setImageResource(R.drawable.person_black_24dp);
                holder.itemIV.setImageTintList(ColorStateList.valueOf(Color.parseColor("#396DF8")));
                holder.itemIV.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DEE5FB")));
                break;
            case "Transportation":
                holder.itemIV.setImageResource(R.drawable.directions_bus_fill1_wght300_grad0_opsz40);
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
