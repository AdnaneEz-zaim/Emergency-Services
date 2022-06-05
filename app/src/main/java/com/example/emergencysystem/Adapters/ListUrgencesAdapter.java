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

import java.util.ArrayList;

public class ListUrgencesAdapter extends RecyclerView.Adapter<ListUrgencesAdapter.HolderUrgences> {
    ArrayList<UrgenceModel> data;
    RecycleClickListener recycleClickListener;

    public ListUrgencesAdapter(ArrayList<UrgenceModel> data, RecycleClickListener recycleClickListener) {
        this.data = data;
        this.recycleClickListener = recycleClickListener;
    }

    @NonNull
    @Override
    public HolderUrgences onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.urgence_item,parent,false);
        return new HolderUrgences(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderUrgences holder, int position) {
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recycleClickListener.onItemClickListener(getAdapterPosition());
                }
            });
        }
    }
}


