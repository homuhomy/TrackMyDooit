package com.example.trackmydooit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class History extends AppCompatActivity {

    private RecyclerView recyclerView;

    private TodayItemsAdapter todayItemsAdapter;
    private List<Data> myDataList;

    private FirebaseAuth mAuth;
    private String onlineUserId = "";
    private DatabaseReference expenseRef;

    private Toolbar settingsToolbar;

    private Button search;
    private TextView historyTotalAmountSpent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

//        settingsToolbar = findViewById(R.id.my_Feed_Toolbar);
//        getSupportActionBar(settingsToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("History");
//
//        search = findViewById(R.id.search);
//        historyTotalAmountSpent = findViewById(R.id.historyTotalAmountSpent);
//
//        mAuth = FirebaseAuth.getInstance();
//        onlineUserId = mAuth.getCurrentUser().getUid();
//
//        recyclerView = findViewById(R.id.recycler_View_Id_Feed);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setReverseLayout(true);
//        layoutManager.setStackFromEnd(true);
//        recyclerView.setLayoutManager(layoutManager);
//
//        myDataList = new ArrayList<>();
//        todayItemsAdapter = new TodayItemsAdapter(History.this, myDataList);
//        recyclerView.setAdapter(todayItemsAdapter);
//
//        search.setOnClickListener((view) -> {
//
//            showDatePickerDialog();
//
//        });
//    }

//    private ActionBar getSupportActionBar(Toolbar settingsToolbar) {
//        return null;
//    }
//
//    private void showDatePickerDialog(){
//        DatePickerDialog datePickerDialog = new DatePickerDialog(
//                this,
//                (DatePickerDialog.OnDateSetListener) this,
//                Calendar.getInstance().get(Calendar.YEAR),
//                Calendar.getInstance().get(Calendar.MONTH),
//                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
//        );
//        datePickerDialog.show();
//    }
//
//    public void onDataset(DatePicker datePicker, int year, int month, int dayOfMonth){
//        int months = month+1;
//        String date = dayOfMonth+"-"+"0"+months +"-"+year;
//        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
//        Query query = reference.orderByChild("date").equalTo(date);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                myDataList.clear();
//                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
//                    Data data = snapshot.getValue(Data.class);
//                    myDataList.add(data);
//                }
//                todayItemsAdapter.notifyDataSetChanged();
//                recyclerView.setVisibility(View.VISIBLE);
//
//                int totalAmount = 0;
//                for (DataSnapshot ds :dataSnapshot.getChildren()){
//                    Map<String, Object> map = (Map<String, Object>)ds.getValue();
//                    Object total = map.get("a mount");
//                    int pTotal = Integer.parseInt(String.valueOf(total));
//                    totalAmount+=pTotal;
//                    if(totalAmount>0){
//                        historyTotalAmountSpent.setVisibility(View.VISIBLE);
//                        historyTotalAmountSpent.setText("This day you spent RM"+ totalAmount);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(History.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    }
}