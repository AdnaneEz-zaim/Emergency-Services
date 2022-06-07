package com.example.emergencysystem.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    public ListUrgencesNormalUser(ArrayList<UrgenceModel> data, RecycleClickListener recycleClickListener) {
        this.data = data;
        this.recycleClickListener = recycleClickListener;
    }

    @NonNull
    @Override
    public ListUrgencesNormalUser.HolderUrgences onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.urgence_item,parent,false);
        return new ListUrgencesNormalUser.HolderUrgences(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListUrgencesNormalUser.HolderUrgences holder, int position) {
        //holder.productName.setText(data.get(position).getPrenom()+" "+data.get(position).getNom());
        //Picasso.get().load(data.get(position).getImage()).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class HolderUrgences extends RecyclerView.ViewHolder {
        TextView productName;
        ImageView productImage;
        public HolderUrgences(@NonNull View itemView) {
            super(itemView);
            // productName = itemView.findViewById(R.id.chatProductName);
            //productImage = itemView.findViewById(R.id.chatImageProduct);
            MapView mapView = (MapView)itemView.findViewById(R.id.mapitem);
            mapView.onCreate(null);
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    LatLng sydney = new LatLng(-10, 78);
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