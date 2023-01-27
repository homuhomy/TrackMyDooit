package com.example.trackmydooit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.trackmydooit.databinding.ActivityUpdateTransBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateTransActivity extends AppCompatActivity {

    ActivityUpdateTransBinding binding;
    String newType;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateTransBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        String id = getIntent().getStringExtra("id");
        String amount = getIntent().getStringExtra("amount");
        String note = getIntent().getStringExtra("note");
        String type = getIntent().getStringExtra("type");
        String incomeCategory = getIntent().getStringExtra("income category");
        String expenseCategory = getIntent().getStringExtra("expense category");
        String wallet = getIntent().getStringExtra("wallet");

        binding.amountTransET.setText(amount);
        binding.note.setText(note);
        binding.walletName.setText(wallet);
        binding.itemName.setText(incomeCategory);

        switch (type){
            case "Income":
                newType = "Income";
                binding.itemName.setText(incomeCategory);
                break;
            case "Expense":
                newType = "Expense";
                binding.itemName.setText(expenseCategory);
                break;
        }



        binding.updateTransBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = binding.amountTransET.getText().toString();
                String note = binding.note.getText().toString();

                firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid())
                        .collection("Transaction")
                        .document(id)
                        .update("amount", amount, "note", note)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UpdateTransActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UpdateTransActivity.this, ExpenseActivity2.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateTransActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UpdateTransActivity.this, ExpenseActivity2.class));
                            }
                        });
            }
        });
        binding.cancelAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateTransActivity.this, ExpenseActivity2.class));
            }
        });
        binding.deleteTransBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid())
                        .collection("Transaction")
                        .document(id).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                onBackPressed();
                                Toast.makeText(UpdateTransActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UpdateTransActivity.this, ExpenseActivity2.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateTransActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UpdateTransActivity.this, ExpenseActivity2.class));
                            }
                        });
            }
        });
    }
}