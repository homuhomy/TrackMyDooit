package com.example.trackmydooit;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }


    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView  mTextView1;
        public TextView  mTextView2;
        public TextView  mTextView3;
        public ImageView mDeleteImage;
        public CardView cardView;



        public ExampleViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.imageview01);
            mTextView1=itemView.findViewById(R.id.textview01);
            mTextView2=itemView.findViewById(R.id.textview02);
            mTextView3=itemView.findViewById(R.id.textview03);
            mDeleteImage=itemView.findViewById(R.id.image_delete);
            cardView=itemView.findViewById(R.id.itemContainer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener !=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener !=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        }
    }

    public ExampleAdapter(Wallet_Activity wallet_activity, ArrayList<ExampleItem> exampleList){
        mExampleList=exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item,parent,false);
        ExampleViewHolder evh=new ExampleViewHolder(v,mListener);
        return evh;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {

        ExampleItem currentItem=mExampleList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.cardView.setCardBackgroundColor(holder.itemView.getResources().getColor(getRandomColor(),null));
    }

    private int getRandomColor(){
        List<Integer> colorcode= new ArrayList<>();
        colorcode.add(R.color.baby_yellow);
        colorcode.add(R.color.baby_blue);
        colorcode.add(R.color.baby_green);
        colorcode.add(R.color.baby_purple);
        colorcode.add(R.color.baby_pink);
        colorcode.add(R.color.baby_grey);

        Random random=new Random();
        int number=random.nextInt(colorcode.size());
        return colorcode.get(number);
    }
    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
