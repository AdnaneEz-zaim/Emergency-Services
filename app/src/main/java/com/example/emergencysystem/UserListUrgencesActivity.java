package com.example.emergencysystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emergencysystem.Adapters.ListUrgencesNormalUser;
import com.example.emergencysystem.GlobalVariables.MySingleton;
import com.example.emergencysystem.Managers.CacheHelper;
import com.example.emergencysystem.Models.RecycleClickListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class UserListUrgencesActivity extends AppCompatActivity implements RecycleClickListener {
    String[] courses = { "Select level danger", "Low", "Medium", "High"};
    EditText edtTitleEmergency,edtdescreption,edtNbrInjured;
    Spinner spino;
    RecyclerView recyclerViewUrgences;
    FloatingActionButton fab;
    ImageView emptyData;

    LinearLayoutManager linearLayoutManagerUrgences;
    ListUrgencesNormalUser urgencesAdapter;
    CacheHelper sqlLiteCacheHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_urgences);

        sqlLiteCacheHelper = new CacheHelper(this);
        emptyData = findViewById(R.id.emptyData);
        if(MySingleton.getInstance().getUrgenceArrayList().size()!=0)
            emptyData.setVisibility(View.INVISIBLE);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                showBottomSheetDialog();
            }
        });

        recyclerViewUrgences = findViewById(R.id.listUrgencesClient);
        linearLayoutManagerUrgences = new LinearLayoutManager(UserListUrgencesActivity.this,LinearLayoutManager.VERTICAL,false);
        urgencesAdapter = new ListUrgencesNormalUser(MySingleton.getInstance().getUrgenceArrayList(),this,getApplicationContext());
        recyclerViewUrgences.setLayoutManager(linearLayoutManagerUrgences);
        recyclerViewUrgences.setAdapter(urgencesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu,menu);
//        return true;

        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            // On peut créer le menu via le code
            case R.id.logOutBtn:
                Dialog dialog = new Dialog(UserListUrgencesActivity.this ,R.style.Dialoge);
                dialog.setContentView(R.layout.dialog_layout);
                TextView yesBtn,noBtn;
                yesBtn = dialog.findViewById(R.id.dialog_Yes);
                noBtn = dialog.findViewById(R.id.dialog_No);

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        Intent switchToArView = new Intent(UserListUrgencesActivity.this, MainActivity.class);
                        sqlLiteCacheHelper.removeUser();
                        startActivity(switchToArView);
                    }
                });

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onItemClickListener(int position) {
        //switchToMaps(position);
    }
    private void showBottomSheetDialog() {
        Button btnSelectPosition;

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this,R.style.DialogStyle);
        bottomSheetDialog.setContentView(R.layout.add_urgence_bottom_sheet);

        spino = bottomSheetDialog.findViewById(R.id.coursesspinner);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item,courses);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spino.setAdapter(ad);

        edtTitleEmergency = bottomSheetDialog.findViewById(R.id.edtTitleEmergency);
        edtdescreption = bottomSheetDialog.findViewById(R.id.edtdescreption);
        edtNbrInjured = bottomSheetDialog.findViewById(R.id.edtNbrInjured);


        btnSelectPosition = bottomSheetDialog.findViewById(R.id.btnSelectPosition);
        btnSelectPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titre = edtTitleEmergency.getText().toString();
                String description = edtdescreption.getText().toString();
                String nbrInjured = edtNbrInjured.getText().toString();
                String dangerLevel = spino.getSelectedItem().toString();
                if(TextUtils.isEmpty(titre)|| TextUtils.isEmpty(nbrInjured) || TextUtils.isEmpty(description) || TextUtils.isEmpty(dangerLevel) ||spino.getSelectedItem().toString()=="Select level danger") {
                    Toast.makeText(UserListUrgencesActivity.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent switchToArView = new Intent(UserListUrgencesActivity.this, PositionUrgence.class);
                    switchToArView.putExtra("titre", titre);
                    switchToArView.putExtra("description", description);
                    switchToArView.putExtra("dangerLevel", dangerLevel);
                    switchToArView.putExtra("nbrInjured", nbrInjured);
                    startActivity(switchToArView);
                }
            }
        });

        bottomSheetDialog.show();
    }
}