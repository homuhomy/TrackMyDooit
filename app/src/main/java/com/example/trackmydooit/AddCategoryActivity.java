package com.example.trackmydooit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddCategoryActivity extends AppCompatActivity {

    private Button addOn;
    private EditText addCategory;
    private Spinner spinner;



    String ss [] = getResources().getStringArray(R.array.expensesCategory);
    ArrayList<String> Category = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.expensesCategory)));

    String data[] = {"Fruits"};
    ArrayList list = new ArrayList(Arrays.asList(ss));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        addOn = findViewById(R.id.addon);
        addCategory = findViewById(R.id.addCategory);
        spinner = findViewById(R.id.spinner);

        ArrayAdapter adapter = new ArrayAdapter(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);

//        for( int i = 0  ; i< spinner.getAdapter().getCount() ; i++ ){
//            list.add ( Category.getAdapter().getItem( i ) );
//
//        }
        addOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userAdd = addCategory.getText().toString();
                list.add(userAdd);
                adapter.notifyDataSetChanged();
                spinner.setAdapter(adapter);
                Toast.makeText(getApplicationContext(),"Category added", Toast.LENGTH_LONG).show();

            }
        });


    }
}