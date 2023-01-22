package com.example.trackmydooit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;




public class Wallet_Activity extends AppCompatActivity {
    private ArrayList<ExampleItem> mExampleList;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button buttonInsert;
    private EditText editTextInsert;
    private Toolbar toolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_activity);

        createExampleList();
        buildRecyclerView();
        setButtons();

        //toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //to add back button
        getSupportActionBar().setTitle("EXPENSES");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_ios_24px);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //bottom nav bar code
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.DestWallet);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.DestHome:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.DestGoals:
                        startActivity(new Intent(getApplicationContext(),CreateSavingsGoalActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.DestWallet:
                        startActivity(new Intent(getApplicationContext(),Wallet_Activity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.DestAddExpense:
                        startActivity(new Intent(getApplicationContext(),ExpenseActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.DestSettings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //bottom nav bar code


    }

    public void clickbtn(View view)
    {
        Intent intent=new Intent(this,IncomeActivity.class);
        EditText walletName=(EditText) findViewById(R.id.textview01);
        String name = walletName.getText().toString();

        intent.putExtra("Wallet Name",name);

        startActivity(intent);
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
        mAdapter=new ExampleAdapter(this, mExampleList);

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