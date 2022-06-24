package com.example.emergencysystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.emergencysystem.Adapters.ListUrgencesAdapter;
import com.example.emergencysystem.GlobalVariables.MySingleton;
import com.example.emergencysystem.Managers.CacheHelper;
import com.example.emergencysystem.Models.EmergencyFirebaseModel;
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
import java.util.Random;

public class ListUrgencesPage extends AppCompatActivity implements RecycleClickListener {

    RecyclerView recyclerViewUrgences;
    LinearLayoutManager linearLayoutManagerUrgences;
    ListUrgencesAdapter urgencesAdapter;
    FirebaseDatabase database;
    ImageView emptyData;
    boolean loadData=true;
    ProgressBar progressBarDataAdmin;

//notification System
    NotificationManagerCompat notificationManagerCompat;
    Notification notification;
//cache helper
    CacheHelper sqlLiteCacheHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_urgences_page);

        sqlLiteCacheHelper = new CacheHelper(this);

        setTitle(sqlLiteCacheHelper.getUser().getRole().toString()+" Emergency");
        database= FirebaseDatabase.getInstance();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        creatDialogNoInternet();
//        creatNotification();
//        notificationManagerCompat.notify(1, notification);
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

        emptyData = findViewById(R.id.emptyDataAdmin);
        emptyData.setVisibility(View.INVISIBLE);
        progressBarDataAdmin = findViewById(R.id.progressBarDataAdmin);

        //list product traitmant
        recyclerViewUrgences = findViewById(R.id.listUrgences);
        linearLayoutManagerUrgences = new LinearLayoutManager(ListUrgencesPage.this,LinearLayoutManager.VERTICAL,false);
        urgencesAdapter = new ListUrgencesAdapter(MySingleton.getInstance().getUrgenceArrayList(),this,sqlLiteCacheHelper.getUser().getId());
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
                Dialog dialog = new Dialog(ListUrgencesPage.this ,R.style.Dialoge);
                dialog.setContentView(R.layout.dialog_layout);
                TextView yesBtn,noBtn;
                yesBtn = dialog.findViewById(R.id.dialog_Yes);
                noBtn = dialog.findViewById(R.id.dialog_No);

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        Intent switchToArView = new Intent(ListUrgencesPage.this, MainActivity.class);
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
        switchToMaps(position);
    }

    private void switchToMaps(int position) {
            Intent switchActivityIntent = new Intent(this, DetailsUrgence.class);
            UrgenceModel urgence = MySingleton.getInstance().getUrgenceArrayList().get(position);
            switchActivityIntent.putExtra("date", urgence.getDate());
            switchActivityIntent.putExtra("description", urgence.getDescription());
            switchActivityIntent.putExtra("niveauxDanger", urgence.getNiveauxDanger()+"");
            switchActivityIntent.putExtra("title", urgence.getTitle());
            switchActivityIntent.putExtra("positionName", urgence.getPositionName());
            switchActivityIntent.putExtra("positionY", urgence.getPosition().getPositionY()+"");
            switchActivityIntent.putExtra("positionX", urgence.getPosition().getPositionX()+"");
            switchActivityIntent.putExtra("nombreBlesses", urgence.getNombreBlesses()+"");
            switchActivityIntent.putExtra("status", urgence.getStatus()+"");
            switchActivityIntent.putExtra("takeBy", urgence.getTakeBy()+"");
            switchActivityIntent.putExtra("myId", sqlLiteCacheHelper.getUser().getId());
            switchActivityIntent.putExtra("emergencyId", urgence.getId());
            startActivity(switchActivityIntent);
    }

    private interface UrgenceListCallback {
        void onCallback(ArrayList<UrgenceModel> urgencesArrayList);
    }

    public void getContactsFromFirebase(final UrgenceListCallback myCallback, FirebaseAuth auth) {
        creatDialogNoInternet();
        database.getReference().child("data")
                .orderByChild("Device").equalTo(sqlLiteCacheHelper.getUser().getRole().toString())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                {
                    ArrayList<UrgenceModel> firstList = (ArrayList<UrgenceModel>) MySingleton.getInstance().getUrgenceArrayList().clone();
                    ArrayList<UrgenceModel> secondList;
                    MySingleton.getInstance().clearListUrgences();
                    System.out.println("#####++++++++++++++++++++++++++++++########\n");
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        System.out.println(dataSnapshot);
                        EmergencyFirebaseModel urgence = dataSnapshot.getValue(EmergencyFirebaseModel.class);
                        if(!urgence.getStatus().equals("close"))
                        MySingleton.getInstance().addUrgence(new UrgenceModel(
                                dataSnapshot.getKey(),
                                urgence.getPosition(),urgence.getDate(),urgence.getNiveauxDanger(),
                                urgence.getDescription(),urgence.getPositionName(),urgence.getTitle(),urgence.getNombreBlesses(),
                                urgence.getTakeBy(),urgence.getStatus(),urgence.getDevice()
                        ));
                        System.out.println(dataSnapshot);
                        System.out.println("ha data");
                        System.out.println(dataSnapshot.getKey());
                    }
                    secondList = (ArrayList<UrgenceModel>) MySingleton.getInstance().getUrgenceArrayList().clone();
                    // Remove all elements in firstList from secondList
                    secondList.removeAll(firstList);
                    // Show the list elements deferents
                    System.out.println("######### Result: " + secondList);
                    loadData = false;
                    progressBarDataAdmin.setVisibility(View.INVISIBLE);
                    int random = new Random().nextInt();
                    if (secondList.size() > 1) {
                        creatNotification("New emergencies", "there are " + secondList.size() + " new emergencies");
                        notificationManagerCompat.notify(random, notification);
                    } else if (secondList.size() == 1 ) {
                        if(!secondList.get(0).getTakeBy().equals(sqlLiteCacheHelper.getUser().getId())){
                            creatNotification(secondList.get(0).getTitle(),"Taken by other user");
                            notificationManagerCompat.notify(random, notification);
                        }else if(secondList.get(0).getStatus().equals("open")){
                            creatNotification(secondList.get(0).getTitle(), secondList.get(0).getPositionName());
                            notificationManagerCompat.notify(random, notification);
                        }
                    }
                }
                myCallback.onCallback(MySingleton.getInstance().getUrgenceArrayList());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println("^^^^^^^^^^^^^ ERREUR ^^^^^^^^^^^^^"+error);
            }
        });

//        database.getReference().child("emergency").orderByChild("role").equalTo("Admin").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                MySingleton.getInstance().clearListUrgences();
//                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    UrgenceModel urgence = dataSnapshot.getValue(UrgenceModel.class);
//                    MySingleton.getInstance().addUrgence(urgence);
//                }
//                myCallback.onCallback(MySingleton.getInstance().getUrgenceArrayList());
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    private void creatNotification(String title,String s){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notifChannel = new NotificationChannel("MyID", "My NotificationChannel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notifChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyID")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.img)
                //.setLargeIcon(getBitmapFromVectorDrawable(this, R.drawable.img))
                .setContentText(s);

        notification =builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(this);
    }
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    private void creatDialogNoInternet(){
        //test la connection internet
        if(!isNetworkAvailable()){
            Dialog dialog = new Dialog(ListUrgencesPage.this ,R.style.Dialoge);
            dialog.setContentView(R.layout.dialog_no_internet);
            TextView exit = dialog.findViewById(R.id.dialog_Exit);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.exit(0);
                }
            });

            TextView yesBtn,noBtn;
            dialog.show();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}


