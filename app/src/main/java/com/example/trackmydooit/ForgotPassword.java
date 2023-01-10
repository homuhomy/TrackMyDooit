package com.example.trackmydooit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.trackmydooit.databinding.ActivityForgotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;
    ProgressDialog dialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(ForgotPassword.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading");

        binding.ForgotBtn.setOnClickListener(view -> {
            forgotpassword();
        });

//        //go to ForgotPassword
//        password = findViewById(R.id.FPassword_btn);
//        password.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ForgotPassword.this,ChangePassword.class);
//                startActivity(intent);
//            }
//        });
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
                    startActivity(new Intent(ForgotPassword.this,LoginActivity.class));
                    finish();
                    Toast.makeText(ForgotPassword.this,"Please Check your Email",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ForgotPassword.this,"Enter correct Email Address", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}