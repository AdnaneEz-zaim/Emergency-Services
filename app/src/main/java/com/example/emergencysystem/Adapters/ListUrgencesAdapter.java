package com.example.emergencysystem.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emergencysystem.Managers.CacheHelper;
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

public class ListUrgencesAdapter extends RecyclerView.Adapter<ListUrgencesAdapter.HolderUrgences> {
    ArrayList<UrgenceModel> data;
    RecycleClickListener recycleClickListener;
    String MyId;
    public ListUrgencesAdapter(ArrayList<UrgenceModel> data, RecycleClickListener recycleClickListener,String MyId) {
        this.data = data;
        this.recycleClickListener = recycleClickListener;
        this.MyId=MyId;
    }

    @NonNull
    @Override
    public HolderUrgences onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.urgence_item,parent,false);
        return new HolderUrgences(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderUrgences holder, int position) {
        holder.dateEmergency.setText(data.get(position).getDate());
        holder.DescriptionEmergency.setText(data.get(position).getDescription());
        holder.EmergencyTitle.setText(data.get(position).getTitle());
        holder.palceEmergencyName.setText(data.get(position).getPositionName());
        holder.InjuredNumber.setText(data.get(position).getNombreBlesses()+"");

        if(data.get(position).getStatus().equals("open")) {
            holder.status.setVisibility(View.INVISIBLE);
            //holder.status.setText(data.get(position).getDevice());
            holder.status2.setVisibility(View.VISIBLE);
        }else{
            holder.status.setVisibility(View.VISIBLE);
            holder.status2.setVisibility(View.INVISIBLE);
            if(data.get(position).getTakeBy().equals(MyId))
                holder.status.setText("Taken By Me");
            else
                holder.status.setText("Taken By Other");
        }

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
        holder.position.setPositionX(data.get(position).getPosition().getPositionX());
        holder.position.setPositionY(data.get(position).getPosition().getPositionY());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class HolderUrgences extends RecyclerView.ViewHolder {
        TextView EmergencyTitle,InjuredNumber,DescriptionEmergency,dateEmergency,palceEmergencyName,status,status2;
        Position position;
        ImageButton imageDangerLevel1,imageDangerLevel2,imageDangerLevel3;
        public HolderUrgences(@NonNull View itemView) {
            super(itemView);
            // productName = itemView.findViewById(R.id.chatProductName);
            //productImage = itemView.findViewById(R.id.chatImageProduct);7
            position = new Position(0,0);
            dateEmergency = itemView.findViewById(R.id.dateEmergency);
            DescriptionEmergency = itemView.findViewById(R.id.DescriptionEmergency);
            InjuredNumber = itemView.findViewById(R.id.InjuredNumber);
            EmergencyTitle = itemView.findViewById(R.id.EmergencyTitle);
            palceEmergencyName = itemView.findViewById(R.id.palceEmergencyName);
            status = itemView.findViewById(R.id.status);
            status2 = itemView.findViewById(R.id.status2);
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


