package com.example.trackmydooit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;




public class Wallet_Activity extends AppCompatActivity {
    private ArrayList<ExampleItem> mExampleList;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button buttonInsert;
    private EditText editTextInsert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createExampleList();
        buildRecyclerView();
        setButtons();



    }

    public void insertItem(int position){
        mExampleList.add(position,new ExampleItem(R.drawable.wallet_ic,"NEW WALLET"+position,"Current Balance","RM0.00"));
        mAdapter.notifyItemInserted(position);

    }

    public void removeItem(int position){
        mExampleList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }
    public void changeItem(int position,String text){
        mExampleList.get(position).changeText1(text);
        mAdapter.notifyItemChanged(position);
    }

    public void createExampleList(){
        mExampleList = new ArrayList<>();
        mExampleList.add(new ExampleItem(R.drawable.wallet_ic,"EXAMPLE WALLET ","Current Balance","RM120.70"));
        mExampleList.add(new ExampleItem(R.drawable.wallet_ic,"EXAMPLE WALLET ","Current Balance","RM320.50"));
        mExampleList.add(new ExampleItem(R.drawable.wallet_ic,"EXAMPLE WALLET ","Current Balance","RM120.70"));
    }
    public void buildRecyclerView(){
        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
        mAdapter=new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position,"Wallet Name");
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });



    }
    public void setButtons(){

        buttonInsert=findViewById(R.id.button_insert);
        buttonInsert=findViewById(R.id.button_insert);
        editTextInsert=findViewById(R.id.edittext_insert);

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(editTextInsert.getText().toString());
                insertItem(position);
            }
        });


    }
}