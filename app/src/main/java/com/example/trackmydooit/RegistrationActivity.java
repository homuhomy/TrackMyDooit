package com.example.trackmydooit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
//                    "(?=.*[0-9])" +         //at least 1 digit
//                    "(?=.*[a-z])" +         //at least 1 lower case letter
//                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%!*?<>~`.^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    private TextInputLayout email, username, password, confirmpassword;
    private Button registerBtn;
    private TextView registerQn;
    private CheckBox checkBox;
    private MaterialAlertDialogBuilder materialAlertDialogBuilder;


    private FirebaseAuth mAuth;
//    private DatabaseReference userRef;
    private ProgressDialog progressDialog;

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
//                .setQuery(userRef, Data.class)
//                .build();
//
//    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        email = findViewById(R.id.email);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.ConfirmPassword);
        registerBtn = findViewById(R.id.registerBtn);
        registerQn = findViewById(R.id.registerQn);
        checkBox = findViewById(R.id.checkbox);
        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);

//checkbox for the terms and conditions
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
//                    materialAlertDialogBuilder.setTitle("Terms and Conditions");
//                    materialAlertDialogBuilder.setMessage("You need to accept the terms and conditions to proceed");
//                    materialAlertDialogBuilder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            registerBtn.setEnabled(true);
//                            dialogInterface.dismiss();
//                        }
//                    });
//                    materialAlertDialogBuilder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                            checkBox.setChecked(false);
//                        }
//                    });
//
//                    materialAlertDialogBuilder.show();
                    checkBox.setChecked(true);
                    registerBtn.setEnabled(true);
                }
                else{
                    registerBtn.setEnabled(false);
                    materialAlertDialogBuilder.setMessage("You need to accept the terms and conditions to proceed");
                    materialAlertDialogBuilder.setPositiveButton("okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                }
            }
        });

//        registerBtn.setEnabled(false);

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

                String emailString = email.getEditText().getText().toString();
                String passwordString = password.getEditText().getText().toString();
                String conPasswordString = confirmpassword.getEditText().getText().toString();

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
                if(samePassword()){

                }
//                Data data = new Data(email, username);
                //Create a new node with some data
                Map<String, String> newData = new HashMap<>();
                newData.put("username", username.getEditText().getText().toString());
                newData.put("email", email.getEditText().getText().toString());


//                newData.put("email", email);

                //Adding a new node to the database
                FirebaseDatabase.getInstance().getReference().child("users").push().setValue(newData);

//        userRef = FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid());
//        userRef.child("user").setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(RegistrationActivity.this, "Budget item added successfully!", Toast.LENGTH_SHORT).show();
//                }
//
//                else {
//                    Toast.makeText(RegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

            }
        });


    }

    private boolean validateEmail(){
        String emailInput = email.getEditText().getText().toString();
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
        String passwordInput = password.getEditText().getText().toString();
        String confirmPassInput = confirmpassword.getEditText().getText().toString();
        if(passwordInput.isEmpty()){
            password.setError("Field can't be empty");
            return false;
        }
        else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Password too weak. \n Make sure there's at least one number and " +
                    "\n one special character");
            return false;
        }
        else if(samePassword()){
            return samePassword();
        }
        else {
            password.setError(null);
            return true;
        }
    }

    private boolean samePassword(){
        String passwordInput = password.getEditText().getText().toString();
        String conPasswordInput = confirmpassword.getEditText().getText().toString();
        if(!passwordInput.equals(conPasswordInput)){
            confirmpassword.setError("The password doesn't match");
            return false;
        }
        else {
            confirmpassword.setError(null);
            return true;
        }
    }
}