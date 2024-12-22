package com.example.carpoolingworkshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DrivesAdapter extends RecyclerView.Adapter<DrivesAdapter.ViewHolder> {
    private List<DriveModel> drivesList;
    private int rowLayout;
    private Context context;
    private DBHelper dbHelper;
    private OnItemClickListener listener;

    // INNER CLASS for ViewHolder (for each item in the list)
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_id;
        public TextView tv_fullname;
        public TextView tv_vehicle;
        public TextView tv_rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_id = itemView.findViewById(R.id.tv_id);
            tv_fullname = itemView.findViewById(R.id.tv_fullname);
            tv_vehicle = itemView.findViewById(R.id.tv_vehicle);
            tv_rating = itemView.findViewById(R.id.tv_rating);
        }
    }

    public DrivesAdapter(List<DriveModel> drivesList, int rowLayout, Context context, OnItemClickListener listener) {
        this.drivesList = drivesList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.dbHelper = new DBHelper(context);
        this.listener = listener;
    }

    // When the layout is created
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }

    // When the layout is filled with data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        fillViewData(holder, position);
        setupEventListeners(holder, position);
    }

    public void fillViewData(@NonNull ViewHolder holder, int position){
        DriveModel drive = drivesList.get(position);

        UserModel driver = dbHelper.getUser(drive.getDriver_id());

        holder.tv_id.setText(String.valueOf(driver.getId()));
        holder.tv_fullname.setText(driver.get_fullname());
        holder.tv_vehicle.setText("");
        VehicleModel vehicle = dbHelper.getVehicle(driver.getActive_vehicle());
        if(vehicle != null){
            holder.tv_vehicle.setText(vehicle.toString());
        }
        holder.tv_rating.setText(String.valueOf(driver.getRating()));
    }

    private void setupEventListeners(@NonNull ViewHolder holder, int position) {
        // Set click listener on the entire itemView
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position); // Notify the click
            }
        });
    }

    @Override
    public int getItemCount() {
        return drivesList == null ? 0 : drivesList.size();
    }
}
