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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emergencysystem.Adapters.ListUrgencesNormalUser;
import com.example.emergencysystem.GlobalVariables.MySingleton;
import com.example.emergencysystem.Managers.CacheHelper;
import com.example.emergencysystem.Models.EmergencyFirebaseModel;
import com.example.emergencysystem.Models.RecycleClickListener;
import com.example.emergencysystem.Models.UrgenceModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
    FirebaseDatabase database;
    ProgressBar progressBarDataUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_urgences);

        database= FirebaseDatabase.getInstance();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        emptyData = findViewById(R.id.emptyData);
        emptyData.setVisibility(View.INVISIBLE);
        progressBarDataUser = findViewById(R.id.progressBarDataUser);

        sqlLiteCacheHelper = new CacheHelper(this);
        getContactsFromFirebase(new UrgenceListCallback() {
            @Override
            public void onCallback(ArrayList<UrgenceModel> urgences) {
                System.out.println("Loaded "+urgences.size()+" contacts");
                if(urgences.size()!=0)
                    emptyData.setVisibility(View.INVISIBLE);
                else
                    emptyData.setVisibility(View.VISIBLE);
                urgencesAdapter.notifyDataSetChanged();
            }
        },auth);

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
                    switchToArView.putExtra("createBy", sqlLiteCacheHelper.getUser().getId());
                    startActivity(switchToArView);
                }
            }
        });

        bottomSheetDialog.show();
    }

    private interface UrgenceListCallback {
        void onCallback(ArrayList<UrgenceModel> urgencesArrayList);
    }
    public void getContactsFromFirebase(final UrgenceListCallback myCallback, FirebaseAuth auth) {
        database.getReference().child("data")
                .orderByChild("createBy").equalTo(sqlLiteCacheHelper.getUser().getId())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MySingleton.getInstance().clearListUrgences();
                System.out.println("#####++++++++++++++++++++++++++++++########\n");
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    EmergencyFirebaseModel urgence = dataSnapshot.getValue(EmergencyFirebaseModel.class);
                    if(!urgence.getStatus().equals("close"))
                    MySingleton.getInstance().addUrgence(new UrgenceModel(
                            urgence.getPosition(),urgence.getDate(),urgence.getNiveauxDanger(),
                            urgence.getDescription(),urgence.getPositionName(),urgence.getTitle(),urgence.getNombreBlesses(),
                            urgence.getTakeBy(),urgence.getStatus(),urgence.getDevice()
                    ));
                    System.out.println(dataSnapshot);
                }
                progressBarDataUser.setVisibility(View.INVISIBLE);
                myCallback.onCallback(MySingleton.getInstance().getUrgenceArrayList());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}