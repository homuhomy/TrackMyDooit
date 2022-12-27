package com.example.trackmydooit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    private EditText email, password, confirmpassword;
    private Button registerBtn;
    private TextView registerQn;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.ConfirmPassword);
        registerBtn = findViewById(R.id.registerBtn);
        registerQn = findViewById(R.id.registerQn);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        //user will be directed to login page
        registerQn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //user is registering
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                String conPasswordString = confirmpassword.getText().toString();

//                if (validateEmail()&&validatePassword()){
//
//                }
//
//                if (TextUtils.isEmpty(emailString)){
//                    email.setError("Email is required!");
//                }
//                if (TextUtils.isEmpty(passwordString)){
//                    password.setError("Password is required!");
//                }

                if(validateEmail()&&validatePassword()){
                    progressDialog.setMessage("Registration in progress");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent (RegistrationActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                progressDialog.dismiss();
                            }
                            else{
                                Toast.makeText(RegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });

    }

    private boolean validateEmail(){
        String emailInput = email.getText().toString();
        if (emailInput.isEmpty()){
            email.setError("Field can't be empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            email.setError("Please enter a valid email address");
            return false;
        }
        else{
            email.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String passwordInput = password.getText().toString();
        if(passwordInput.isEmpty()){
            password.setError("Field can't be empty");
            return false;
        }
        else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Password too weak");
            return false;
        }
        else {
            password.setError(null);
            return true;
        }
    }
}