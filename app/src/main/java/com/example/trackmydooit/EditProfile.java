package com.example.trackmydooit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {

    private Button save;
    private TextInputLayout email, username;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        save = findViewById(R.id.save);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUsername();
                progressDialog.setMessage("In progress...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        });
    }

    private void updateUsername() {
        final String newUsername = username.getEditText().getText().toString();
        if (newUsername.trim().length() > 0) {
            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").setValue(newUsername);
            progressDialog.dismiss();
        } else {
            // show an error message to the user indicating that the field is empty
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
}
//    private void updateUsername() {
//        final String newUsername = username.getEditText().getText().toString();
//        if (newUsername.trim().length() > 0) {
//            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
//            Query query = usersRef.orderByChild("username").equalTo(newUsername);
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        // show an error message indicating that the username is already taken
//                        Toast.makeText(EditProfile.this, "Username is already taken", Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//                    } else {
//                        // update the user's username in the database
//                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").setValue(newUsername);
//                        progressDialog.dismiss();
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // handle the error
//                }
//            });
//        } else {
//            // show an error message to the user indicating that the field is empty
//            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
//            progressDialog.dismiss();
//        }
//    }
//
//}