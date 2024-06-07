package com.android.urgetruck.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Models.JobMilestone;
import com.android.urgetruck.UI.Models.MilestoneActionsTracking;

import java.util.ArrayList;
import java.util.List;

public class TrackVehicleRecyclerView extends RecyclerView.Adapter<TrackVehicleRecyclerView.ViewHolder> {

    private List<JobMilestone> milestones;
    private LayoutInflater mInflater;
    Context context;
    private List<MilestoneActionsTracking> list = new ArrayList<>();




    // data is passed into the constructor
    TrackVehicleRecyclerView(Context context, List<JobMilestone> milestones) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.milestones = milestones;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public TrackVehicleRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup
                                                                          parent, int viewType) {
        View view = mInflater.inflate(R.layout.track_vehicle, parent, false);
        return new TrackVehicleRecyclerView.ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        JobMilestone model = milestones.get(position);
        holder.tvMilestone.setText(model.getMilestone()+"-"+model.getMilestioneEvent());
        holder.tvLocationName.setText(model.getLocationName());

        if(model.getStatus().equals("Pending")){
            // holder.icStatus.setImageResource("");
            holder.icStatus.setVisibility(View.GONE);

        }else if(model.getStatus().equals("Completed")){
            holder.icStatus.setVisibility(View.VISIBLE);
            holder.icStatus.setImageResource(R.drawable.ic_tick_new_green);


        }else if(model.getStatus().equals("Open")){
            holder.icStatus.setVisibility(View.VISIBLE);
            holder.icStatus.setImageResource(R.drawable.ic_tick_new_orange);




        }
        boolean isExpandable = model.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        if (isExpandable){
            holder.mArrowImage.setImageResource(R.drawable.arrow_up);
        }else{
            holder.mArrowImage.setImageResource(R.drawable.arrow_down);
        }

        TrackVehicleChildAdapter adapter = new TrackVehicleChildAdapter(model.getMilestoneActionsTracking(),context);
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.nestedRecyclerView.setHasFixedSize(true);
        holder.nestedRecyclerView.setAdapter(adapter);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setExpandable(!model.isExpandable());
                list = model.getMilestoneActionsTracking();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });




    }

    // total number of cells
    @Override
    public int getItemCount() {
        return milestones.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMilestone;
        TextView tvLocationName;
        private LinearLayout linearLayout;
        private RelativeLayout expandableLayout;
        private ImageView mArrowImage;
        private ImageView icStatus;
        private RecyclerView nestedRecyclerView;

        ViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            tvLocationName = itemView.findViewById(R.id.tvLocationName);
            tvMilestone = itemView.findViewById(R.id.tvMilestone);
            mArrowImage = itemView.findViewById(R.id.arro_imageview);
            nestedRecyclerView = itemView.findViewById(R.id.child_rv);
            icStatus = itemView.findViewById(R.id.icStatus);
        }


    }

    // convenience method for getting data at click position

}
