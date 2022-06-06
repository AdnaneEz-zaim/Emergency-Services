package com.example.emergencysystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emergencysystem.Models.Role;
import com.example.emergencysystem.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button goToHome;
    TextView newAcompteBtn;
    TextInputLayout login_email,login_password;
    FirebaseAuth auth;
    User user;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToHome = findViewById(R.id.goToHome);
        newAcompteBtn = findViewById(R.id.newAcompteBtn);

        login_email = findViewById(R.id.id_email_log);
        login_password = findViewById(R.id.id_pass_log);
        auth = FirebaseAuth.getInstance();

        goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = login_email.getEditText().getText().toString();
                String password = login_password.getEditText().getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                }else if(!email.matches(emailPattern)){
                    login_email.setError("Invalid Email");
                    Toast.makeText(MainActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }else if(password.length()<6){
                    login_password.setError("Invalid Password");
                    Toast.makeText(MainActivity.this, "Your password must be at least 7 characters long", Toast.LENGTH_SHORT).show();
                }else{
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getUid());
                                user =new User();
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        user = dataSnapshot.getValue(User.class);
                                        Intent switchToArView;
                                        if(user.getRole() == Role.Admin)
                                             switchToArView = new Intent(MainActivity.this, ListUrgencesPage.class);
                                        else
                                            switchToArView = new Intent(MainActivity.this, AddUrgence.class);
                                        startActivity(switchToArView);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "Error in Login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        newAcompteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchToArView = new Intent(MainActivity.this,CreateNewAcompte.class);
                startActivity(switchToArView);
            }
        });

    }
}