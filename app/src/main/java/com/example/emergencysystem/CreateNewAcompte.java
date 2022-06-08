package com.example.emergencysystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emergencysystem.Models.Role;
import com.example.emergencysystem.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateNewAcompte extends AppCompatActivity {
    TextView dejaCreeBtn;
    TextInputLayout reg_prenom,reg_nom,reg_email,reg_password,reg_password_conf;
    Button singUp_btn;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_acompte);

        auth =FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        reg_nom = findViewById(R.id.id_nom_log);
        reg_prenom = (TextInputLayout)findViewById(R.id.id_prenom_log);
        reg_email = findViewById(R.id.id_email_log);
        reg_password = findViewById(R.id.id_pass_log);
        reg_password_conf = findViewById(R.id.id_comfPass_log);
        singUp_btn = findViewById(R.id.singUp_btn);


        singUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = reg_nom.getEditText().getText().toString();
                String prenom = reg_prenom.getEditText().getText().toString();
                String email = reg_email.getEditText().getText().toString();
                String password = reg_password.getEditText().getText().toString();
                String conPassword = reg_password_conf.getEditText().getText().toString();

                if(TextUtils.isEmpty(nom)||TextUtils.isEmpty(prenom)||TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(conPassword)){
                    Toast.makeText(CreateNewAcompte.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                } else if(!email.matches(emailPattern)){
                    reg_email.setError("Invalid Email");
                    Toast.makeText(CreateNewAcompte.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }else if(password.length()<6){
                    reg_password.setError("Invalid Password");
                    Toast.makeText(CreateNewAcompte.this, "Your password must be at least 7 characters long", Toast.LENGTH_SHORT).show();
                }else if(!password.equals(conPassword)){
                    Toast.makeText(CreateNewAcompte.this, "password does not match", Toast.LENGTH_SHORT).show();
                }else{
                    singUp_btn.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                DatabaseReference reference = database.getReference().child("users").child(auth.getUid());
                                User user =new User(auth.getUid().toString(), nom, prenom, email,Role.user);
                                reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(CreateNewAcompte.this, "User created successfully", Toast.LENGTH_SHORT).show();
                                            Intent switchActivityIntent = new Intent(CreateNewAcompte.this, UserListUrgencesActivity.class);
                                            startActivity(switchActivityIntent);
                                        }else
                                            Toast.makeText(CreateNewAcompte.this, task.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                singUp_btn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(CreateNewAcompte.this, "Error in Sing Up", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            };
        });

        dejaCreeBtn = findViewById(R.id.dejaCreeBtn);
        dejaCreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchToArView = new Intent(CreateNewAcompte.this, MainActivity.class);
                startActivity(switchToArView);
            }
        });

    }
}