package com.example.trackmydooit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.example.trackmydooit.databinding.ActivityChangePasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ChangePasswordActivity extends AppCompatActivity {

    ActivityChangePasswordBinding binding;
    ProgressDialog dialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(ChangePasswordActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading");

        binding.sendEmail.setOnClickListener(view -> {
            forgotpassword();
        });
    }
    private boolean validateEmail(){
        String emailInput = binding.email.getEditText().getText().toString();
        if (emailInput.isEmpty()){
            binding.email.setError("Field can't be empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            binding.email.setError("Please enter a valid email address");
            return false;
        }
        else{
            binding.email.setError(null);
            return true;
        }
    }
        private void forgotpassword() {
            if (!validateEmail())
            {
                return;
            }
            dialog.show();

            auth.sendPasswordResetEmail(binding.email.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    dialog.dismiss();
                    if(task.isSuccessful()){
                        startActivity(new Intent(ChangePasswordActivity.this,LoginActivity.class));
                        finish();
                        Toast.makeText(ChangePasswordActivity.this,"Please Check your Email",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ChangePasswordActivity.this,"Enter correct Email Address", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }