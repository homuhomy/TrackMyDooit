/*package com.example.trackmydooit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeekSpendingAdapter extends RecyclerView.Adapter<WeekSpendingAdapter.ViewHolder> {

    private Context mContext;
    private List<Data> myDataList;

    public WeekSpendingAdapter(Context mContext, List<Data> myDataList) {
        this.mContext = mContext;
        this.myDataList = myDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.retrieve_layout_budget, parent, false);
        return new WeekSpendingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Data data = myDataList.get(position);

        holder.item.setText("Item: "+data.getItem());
        holder.amount.setText("Amount: "+ data.getAmount());
        holder.date.setText("On: "+ data.getDate());
        holder.notes.setText("Notes: "+ data.getNotes());

        switch (data.getItem()){
            case "Transport":
                holder.imageView.setImageResource(R.drawable.train_fill1_wght300_grad0_opsz40);
                break;
            case "Food":
                holder.imageView.setImageResource(R.drawable.restaurant_fill1_wght300_grad0_opsz20);
                break;
            case "House":
                holder.imageView.setImageResource(R.drawable.home_fill1_wght300_grad0_opsz20);
                break;
            case "Entertainment":
                holder.imageView.setImageResource(R.drawable.headphones_fill1_wght300_grad0_opsz20);
                break;
            case "Education":
                holder.imageView.setImageResource(R.drawable.school_fill0_wght300_grad0_opsz40);
                break;
            case "Health":
                holder.imageView.setImageResource(R.drawable.dentistry_fill0_wght300_grad0_opsz40);
                break;
            case "Personal":
                holder.imageView.setImageResource(R.drawable.person_black_24dp);
                break;
            case "Other":
                holder.imageView.setImageResource(R.drawable.add_circle_fill1_wght300_grad0_opsz40);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item, amount, date, notes;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            item = itemView.findViewById(R.id.item);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            notes = itemView.findViewById(R.id.note);
            imageView = itemView.findViewById(R.id.imageView2);
        }
    }
}
*/