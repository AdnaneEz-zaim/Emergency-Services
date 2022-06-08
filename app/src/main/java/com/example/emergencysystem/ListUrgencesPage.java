package com.example.emergencysystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.emergencysystem.Adapters.ListUrgencesAdapter;
import com.example.emergencysystem.GlobalVariables.MySingleton;
import com.example.emergencysystem.Models.Position;
import com.example.emergencysystem.Models.RecycleClickListener;
import com.example.emergencysystem.Models.Role;
import com.example.emergencysystem.Models.UrgenceModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListUrgencesPage extends AppCompatActivity implements RecycleClickListener {

    RecyclerView recyclerViewUrgences;
    LinearLayoutManager linearLayoutManagerUrgences;
    ListUrgencesAdapter urgencesAdapter;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_urgences_page);
        database= FirebaseDatabase.getInstance();


        FirebaseAuth auth=FirebaseAuth.getInstance();

        getContactsFromFirebase(new UserListCallback() {
            @Override
            public void onCallback(ArrayList<UrgenceModel> urgences) {
                System.out.println("Loaded "+urgences.size()+" contacts");
            }
        },auth);

        //list product traitmant
        recyclerViewUrgences = findViewById(R.id.listUrgences);

        linearLayoutManagerUrgences = new LinearLayoutManager(ListUrgencesPage.this,LinearLayoutManager.VERTICAL,false);
        urgencesAdapter = new ListUrgencesAdapter(MySingleton.getInstance().getUrgenceArrayList(),this);
        recyclerViewUrgences.setLayoutManager(linearLayoutManagerUrgences);
        recyclerViewUrgences.setAdapter(urgencesAdapter);
    }

    @Override
    public void onItemClickListener(int position) {
        switchToMaps(position);
    }


    private void switchToMaps(int position) {
            Intent switchActivityIntent = new Intent(this, DetailsUrgence.class);
            startActivity(switchActivityIntent);
    }
    public interface UserListCallback {
        void onCallback(ArrayList<UrgenceModel> urgencesArrayList);
    }

    public void getContactsFromFirebase(final UserListCallback myCallback, FirebaseAuth auth) {
        database.getReference().child("emergency").orderByChild("role").equalTo("Admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MySingleton.getInstance().clearListUrgences();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    UrgenceModel urgence = dataSnapshot.getValue(UrgenceModel.class);
                    MySingleton.getInstance().addUrgence(urgence);
                }
                myCallback.onCallback(MySingleton.getInstance().getUrgenceArrayList());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


