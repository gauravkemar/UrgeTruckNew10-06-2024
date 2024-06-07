package com.android.urgetruck.UI;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Models.MilestoneActionsTracking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class TrackVehicleChildAdapter extends RecyclerView.Adapter<TrackVehicleChildAdapter.NestedViewHolder> {

    private List<MilestoneActionsTracking> mList;
    Context context;

    public TrackVehicleChildAdapter(List<MilestoneActionsTracking> mList, Context context) {
        this.mList = mList;
        this.context=context;
    }

    @NonNull
    @Override
    public NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_child, parent, false);
        return new NestedViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NestedViewHolder holder, int position) {
        if (mList.get(position).getStatus().equalsIgnoreCase("Completed")) {
            //holder.tvlabel.setCompoundDrawables(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_tick_black, context.getTheme()),null,null,null);
            holder.ivStatus.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_tick_small_green, context.getTheme()));
        }
        else if (mList.get(position).getStatus().equalsIgnoreCase("Open") || mList.get(position).getStatus().equalsIgnoreCase("ReOpen")) {
            //holder.tvlabel.setTextColor(Color.parseColor("#FF3B00"));
            holder.ivStatus.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_open_small_orange, context.getTheme()));
        }
        else if (mList.get(position).getStatus().equals("Cancelled")) {
            //holder.tvlabel.setTextColor(Color.parseColor("#FF0000"));
            holder.ivStatus.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_close_small_red, context.getTheme()));
        }
        else if (mList.get(position).getStatus().equals("Pending")) {
            //holder.tvlabel.setTextColor(Color.parseColor("#005EFF"));
            holder.ivStatus.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pending_small_blue, context.getTheme()));
        }
        else if (mList.get(position).getStatus().equals("Failed")) {
            //holder.tvlabel.setTextColor(Color.parseColor("#005EFF"));
            holder.ivStatus.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_failed_small_blue, context.getTheme()));
        }
        holder.tvlabel.setText(mList.get(position).getMilestoneAction());
        //holder.tvStatus.setText(mList.get(position).getStatus());
        String datetime="";
        if(mList.get(position).getCompletionTime()==null)
            datetime="NA";
        else
        {
            try {
                datetime = formattedDate(mList.get(position).getCompletionTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        holder.tvStatus.setText(datetime);
        Log.e("Size", "" + mList.size());
    }

    public String formattedDate(String inputDate) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        //SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
        Date date = inputFormat.parse(inputDate);

        String formattedDate = outputFormat.format(date);
        Log.e("datetime",formattedDate);
        return formattedDate;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class NestedViewHolder extends RecyclerView.ViewHolder {
        private TextView tvlabel;
        private TextView tvStatus;
        private ImageView ivStatus;

        public NestedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvlabel = itemView.findViewById(R.id.label);
            tvStatus = itemView.findViewById(R.id.status);
            ivStatus = itemView.findViewById(R.id.iv_status);
        }
    }


}