package com.example.emergencysystem;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DetailsUrgence extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, RoutingListener {
    private Button bottomDialog;
    //google map object
    private GoogleMap mMap;

    //current and destination location objects
    Location myLocation = null;
    Location destinationLocation = null;
    protected LatLng start = null;
    protected LatLng end = null;
    boolean findRoute = true;

    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;

    //polyline object
    private List<Polyline> polylines = null;
    TextView takeBtn;

    //ifos
    String date,description,niveauxDanger,title,positionName,positionY,positionX,nombreBlesses,status,takeBy,myId,emergencyId;
    private String keyApiDerection="AIzaSyDQ2c_pOSOFYSjxGMwkFvCVWKjYOM9siow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_urgence);

        date=getIntent().getStringExtra("date");
        description=getIntent().getStringExtra("description");
        niveauxDanger=getIntent().getStringExtra("niveauxDanger");
        title=getIntent().getStringExtra("title");
        positionName=getIntent().getStringExtra("positionName");
        positionY=getIntent().getStringExtra("positionY");
        positionX=getIntent().getStringExtra("positionX");
        nombreBlesses=getIntent().getStringExtra("nombreBlesses");

        status=getIntent().getStringExtra("status");
        takeBy=getIntent().getStringExtra("takeBy");
        myId=getIntent().getStringExtra("myId");
        emergencyId=getIntent().getStringExtra("emergencyId");

        bottomDialog = findViewById(R.id.bottomDialog);

        bottomDialog.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                showBottomSheetDialog();
                                            }
                                        });
        //request location permission.
        requestPermision();
        //init google map fragment to show map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void requestPermision() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            locationPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if permission granted.
                    locationPermission = true;
                    getMyLocation();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    //to get user location
    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                myLocation=location;
                LatLng ltlng=new LatLng(location.getLatitude(),location.getLongitude());

                if(start==null || (Math.abs(start.latitude-location.getLatitude())>=0.0001 || Math.abs(start.longitude-location.getLongitude())>=0.0001)){
                    start=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                    end=new LatLng(Double.parseDouble(positionX),Double.parseDouble(positionY));
                    mMap.clear();
                    //start route finding
                    Findroutes(start,end);
                }
            }
        });

        //get destination location when user click on map positionX


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(locationPermission) {
            getMyLocation();
        }
    }

    // function to find Routes.
    public void Findroutes(LatLng Start, LatLng End)
    {
        if(Start==null || End==null) {
            Toast.makeText(DetailsUrgence.this,"Unable to get location", Toast.LENGTH_LONG).show();
        }
        else
        {
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key(keyApiDerection)  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    //Routing call back functions.
    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar= Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
//        Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
        //if(findRoute){
            findRoute=false;
            Toast.makeText(DetailsUrgence.this,"Finding Route...",Toast.LENGTH_LONG).show();
        //}

    }

    //If Route finding success..
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if(polylines!=null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(getResources().getColor(R.color.orangeColor));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);
                polylines.add(polyline);

            }
            else {

            }

        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        mMap.addMarker(endMarker);
        //camera zoom
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(startMarker.getPosition());
            builder.include(endMarker.getPosition());
        LatLngBounds bounds = builder.build();
        int padding = 100; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(start,end);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Findroutes(start,end);

    }

    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);
        TextView titleEmergency =bottomSheetDialog.findViewById(R.id.titleEmergency);
        TextView EmergencyDate =bottomSheetDialog.findViewById(R.id.EmergencyDate);
        TextView EmergencyLevelDanger =bottomSheetDialog.findViewById(R.id.EmergencyLevelDanger);
        TextView EmergencyNumberInjured =bottomSheetDialog.findViewById(R.id.EmergencyNumberInjured);
        TextView EmergencyDescription =bottomSheetDialog.findViewById(R.id.EmergencyDescription);
        TextView EmergencyPositionName =bottomSheetDialog.findViewById(R.id.EmergencyPositionName);
        takeBtn =bottomSheetDialog.findViewById(R.id.takeBtn);

        titleEmergency.setText(title);
        EmergencyDate.setText(date);
        switch(niveauxDanger){
            case "1":
                EmergencyLevelDanger.setText("Low");
                EmergencyLevelDanger.setTextColor(Color.GREEN);
                break;
            case "2":
                EmergencyLevelDanger.setText("Medium");
                EmergencyLevelDanger.setTextColor(Color.rgb(255,165,0));
                break;
            case "3":
                EmergencyLevelDanger.setText("High");
                EmergencyLevelDanger.setTextColor(Color.RED);
                break;
        }
        EmergencyNumberInjured.setText(nombreBlesses);
        EmergencyDescription.setText(description);
        EmergencyPositionName.setText(positionName);

        traitmentClickBtn();

        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.show();

    }

    private void traitmentClickBtn() {
        if(status.equals("open")){
            takeBtn.setText("Take this Emergency");
            takeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    status="inprogress";
                    takeBy=myId;
                    FirebaseDatabase database= FirebaseDatabase.getInstance();
                    database.getReference().child("data").child(emergencyId).child("status").setValue("inprogress");
                    database.getReference().child("data").child(emergencyId).child("takeBy").setValue(myId);
                    traitmentClickBtn();
                }
            });
        }else if(status.equals("inprogress")){
            if(takeBy.equals(myId)){
                takeBtn.setText("Close Emergency");
                takeBtn.setBackgroundResource(R.color.greenColor);
                takeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        status="close";
                        FirebaseDatabase database= FirebaseDatabase.getInstance();
                        database.getReference().child("data").child(emergencyId).child("status").setValue("close");
                        traitmentClickBtn();
                    }
                });

            }else {
                takeBtn.setText("In Progress");
                takeBtn.setTextColor(Color.GREEN);
                takeBtn.setBackgroundResource(R.color.white);
            }
        }else{
            takeBtn.setText("Closed");
            takeBtn.setTextColor(Color.GREEN);
            takeBtn.setBackgroundResource(R.color.white);
        }
    }
}



