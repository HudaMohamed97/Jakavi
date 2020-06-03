package com.cat.newname.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cat.newname.R;
import com.cat.newname.model_items.Patient_item;

import java.util.ArrayList;
import java.util.List;

public class Patient_adapter extends RecyclerView.Adapter<Patient_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<Patient_item> items;

    private Context mContext;

    public Patient_adapter(Context context, ArrayList<Patient_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Patient_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false);
        Patient_adapter.ViewHolder holder = new Patient_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Patient_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        holder.name.setText(items.get(position).getName());
        holder.dose.setText("Dose : " + items.get(position).getDose());
        holder.category.setText(items.get(position).getCategory());
        holder.hospital.setText(items.get(position).getHospital());
        holder.date.setText(items.get(position).getDate());
        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, dose, category, hospital, date;
        RelativeLayout parent_layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            dose = itemView.findViewById(R.id.dose);
            category = itemView.findViewById(R.id.category);
            hospital = itemView.findViewById(R.id.hospital);
            date = itemView.findViewById(R.id.date);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }
}