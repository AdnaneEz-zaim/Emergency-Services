package com.example.emergencysystem;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.emergencysystem.Adapters.ListUrgencesAdapter;
import com.example.emergencysystem.Adapters.ListUrgencesNormalUser;
import com.example.emergencysystem.GlobalVariables.MySingleton;
import com.example.emergencysystem.Models.RecycleClickListener;
import com.example.emergencysystem.Models.UrgenceModel;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emergencysystem.databinding.ActivityAddUrgenceBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddUrgence extends AppCompatActivity implements RecycleClickListener{

    private AppBarConfiguration appBarConfiguration;
    private ActivityAddUrgenceBinding binding;
    String[] courses = { "Select level danger", "Low", "Medium", "High"};
    String currentPhotoPath;

    EditText edtTitleEmergency,edtdescreption,edtNbrInjured;
    Spinner spino;


    RecyclerView recyclerViewUrgences;

    LinearLayoutManager linearLayoutManagerUrgences;
    ListUrgencesNormalUser urgencesAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddUrgenceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_add_urgence);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                showBottomSheetDialog();
            }
        });

        recyclerViewUrgences = findViewById(R.id.listUrgencesClient);

        linearLayoutManagerUrgences = new LinearLayoutManager(AddUrgence.this,LinearLayoutManager.VERTICAL,false);
        urgencesAdapter = new ListUrgencesNormalUser(MySingleton.getInstance().getUrgenceArrayList(),this,getApplicationContext());
        recyclerViewUrgences.setLayoutManager(linearLayoutManagerUrgences);
        recyclerViewUrgences.setAdapter(urgencesAdapter);

    }

    @Override
    public void onItemClickListener(int position) {
        //switchToMaps(position);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_add_urgence);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void showBottomSheetDialog() {
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
                    Toast.makeText(AddUrgence.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent switchToArView = new Intent(AddUrgence.this, PositionUrgence.class);
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}


//    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//// Ensure that there's a camera activity to handle the intent
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                        // Create the File where the photo should go
//                        File photoFile = null;
//                        try {
//                        photoFile = createImageFile();
//                        } catch (IOException ex) {
//                        // Error occurred while creating the File
//                        }
//                        // Continue only if the File was successfully created
//                        if (photoFile != null) {
//                        Uri photoURI = FileProvider.getUriForFile(AddUrgence.this, "com.example.android.fileprovider",
//                        photoFile);
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                        startActivityForResult(takePictureIntent, 2);
//                        }
//                        }