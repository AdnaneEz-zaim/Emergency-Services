package com.example.emergencysystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.emergencysystem.Adapters.ListUrgencesAdapter;
import com.example.emergencysystem.Models.Position;
import com.example.emergencysystem.Models.RecycleClickListener;
import com.example.emergencysystem.Models.UrgenceModel;

import java.util.ArrayList;

public class ListUrgencesPage extends AppCompatActivity implements RecycleClickListener {

    RecyclerView recyclerViewUrgences;
    ArrayList<UrgenceModel> urgenceArrayList;
    LinearLayoutManager linearLayoutManagerUrgences;
    ListUrgencesAdapter urgencesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_urgences_page);
        urgenceArrayList = new ArrayList<>();
        urgenceArrayList.add(new UrgenceModel(1,new Position(10,19), "title 1","10/10/2010", 3, "descreption position 10 10 decreption", "", 5) );
        urgenceArrayList.add(new UrgenceModel(1,new Position(35.759465,-5.833954),"title 2" ,"19/10/2019", 2, "descreption position 100 190 decreption", "", 10) );
        urgenceArrayList.add(new UrgenceModel(1,new Position(200,100), "title 3","22/10/2022 09:19", 1, "descreption position 200 100 decreption", "", 15) );
//        FirebaseAuth auth=FirebaseAuth.getInstance();

//        getContactsFromFirebase(new UserListCallback() {
//            @Override
//            public void onCallback(ArrayList<ProductChatModel> users) {
//                System.out.println("Loaded "+users.size()+" contacts");
//            }
//        },auth);

        //list product traitmant
        recyclerViewUrgences = findViewById(R.id.listUrgences);

        linearLayoutManagerUrgences = new LinearLayoutManager(ListUrgencesPage.this,LinearLayoutManager.VERTICAL,false);
        urgencesAdapter = new ListUrgencesAdapter(urgenceArrayList ,this);
        recyclerViewUrgences.setLayoutManager(linearLayoutManagerUrgences);
        recyclerViewUrgences.setAdapter(urgencesAdapter);
    }

    @Override
    public void onItemClickListener(int position) {
        switchToMaps(position);
    }


    private void switchToMaps(int position) {
            Intent switchActivityIntent = new Intent(this, DetailsUrgence.class);
//            switchActivityIntent.putExtra("name",productArrayList.get(position).getName());
//            switchActivityIntent.putExtra("image",productArrayList.get(position).getImage());
//            switchActivityIntent.putExtra("id",productArrayList.get(position).getId() );
            startActivity(switchActivityIntent);
    }
    public interface UserListCallback {
        void onCallback(ArrayList<UrgenceModel> messagesArrayList);
    }
}


