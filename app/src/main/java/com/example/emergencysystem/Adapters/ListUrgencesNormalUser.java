package com.example.emergencysystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.emergencysystem.Models.Position;
import com.example.emergencysystem.Models.RecycleClickListener;
import com.example.emergencysystem.Models.UrgenceModel;
import com.example.emergencysystem.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ListUrgencesNormalUser extends RecyclerView.Adapter<ListUrgencesNormalUser.HolderUrgences> {
    ArrayList<UrgenceModel> data;
    RecycleClickListener recycleClickListener;
    Context context;

    public ListUrgencesNormalUser(ArrayList<UrgenceModel> data, RecycleClickListener recycleClickListener,Context context) {
        this.data = data;
        this.recycleClickListener = recycleClickListener;
        this.context=context;
    }

    @NonNull
    @Override
    public ListUrgencesNormalUser.HolderUrgences onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.urgence_item,parent,false);
        return new ListUrgencesNormalUser.HolderUrgences(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListUrgencesNormalUser.HolderUrgences holder, int position) {
        //Picasso.get().load(data.get(position).getImage()).into(holder.productImage);
        holder.dateEmergency.setText(data.get(position).getDate());
        holder.DescriptionEmergency.setText(data.get(position).getDescription());
        holder.EmergencyTitle.setText(data.get(position).getTitle());
        holder.palceEmergencyName.setText(data.get(position).getPositionName());
        holder.InjuredNumber.setText(data.get(position).getNombreBlesses()+"");

        if(data.get(position).getNiveauxDanger()==3) {
            //holder.imageDangerLevel.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.redColor)));
            holder.imageDangerLevel1.setVisibility(View.INVISIBLE);
            holder.imageDangerLevel2.setVisibility(View.INVISIBLE);
            holder.imageDangerLevel3.setVisibility(View.VISIBLE);
        }
        else if(data.get(position).getNiveauxDanger()==2){
            holder.imageDangerLevel1.setVisibility(View.INVISIBLE);
            holder.imageDangerLevel2.setVisibility(View.VISIBLE);
            holder.imageDangerLevel3.setVisibility(View.INVISIBLE);
        }else{
            holder.imageDangerLevel1.setVisibility(View.VISIBLE);
            holder.imageDangerLevel2.setVisibility(View.INVISIBLE);
            holder.imageDangerLevel3.setVisibility(View.INVISIBLE);
        }
        System.out.println("===> ha la positioon li jab "+data.get(position).getPosition().getPositionX());
        holder.position.setPositionX(data.get(position).getPosition().getPositionX());
        holder.position.setPositionY(data.get(position).getPosition().getPositionY());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class HolderUrgences extends RecyclerView.ViewHolder {
        TextView EmergencyTitle,InjuredNumber,DescriptionEmergency,dateEmergency,palceEmergencyName;
        Position position;
        ImageButton imageDangerLevel1,imageDangerLevel2,imageDangerLevel3;
        public HolderUrgences(@NonNull View itemView) {
            super(itemView);
            // productName = itemView.findViewById(R.id.chatProductName);
            //productImage = itemView.findViewById(R.id.chatImageProduct);
            position = new Position(0,0);
            dateEmergency = itemView.findViewById(R.id.dateEmergency);
            DescriptionEmergency = itemView.findViewById(R.id.DescriptionEmergency);
            InjuredNumber = itemView.findViewById(R.id.InjuredNumber);
            EmergencyTitle = itemView.findViewById(R.id.EmergencyTitle);
            palceEmergencyName = itemView.findViewById(R.id.palceEmergencyName);
            imageDangerLevel1 = itemView.findViewById(R.id.imageDangerLevel1);
            imageDangerLevel2 = itemView.findViewById(R.id.imageDangerLevel2);
            imageDangerLevel3 = itemView.findViewById(R.id.imageDangerLevel3);


            MapView mapView = (MapView)itemView.findViewById(R.id.mapitem);
            mapView.onCreate(null);
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    LatLng sydney = new LatLng(position.getPositionX(), position.getPositionY());
                    googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15f));
                    mapView.onResume();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recycleClickListener.onItemClickListener(getAdapterPosition());
                }
            });
        }
    }
}