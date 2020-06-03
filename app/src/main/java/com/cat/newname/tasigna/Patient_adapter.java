package com.cat.newname.tasigna;

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
import com.example.catapplication.models.PatientRepData;

import java.util.ArrayList;
import java.util.List;


public class Patient_adapter extends RecyclerView.Adapter<Patient_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<PatientRepData> items;
    private OnItemClickListener onItemClickListener;

    private Context mContext;

    public Patient_adapter(Context context, ArrayList<PatientRepData> items) {
        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patien_tasignat, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");
        holder.name.setText(items.get(position).getDoctor_name());
        holder.dose.setText(items.get(position).getProduct_name());
        holder.category.setText(items.get(position).getCategory_name());
        holder.hospital.setText(items.get(position).getHospital_name());
    }

    @Override
    public int getItemCount() {
        Log.i("hhhh", "" + items.size());
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClicked(ViewHolder.this.getAdapterPosition());
                }
            });
        }
    }


    interface OnItemClickListener {
        void onItemClicked(int position);


    }

    void setOnItemListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}