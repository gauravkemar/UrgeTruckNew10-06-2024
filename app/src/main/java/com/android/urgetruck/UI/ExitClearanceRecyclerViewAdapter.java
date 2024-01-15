package com.android.urgetruck.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Models.Product;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.Scanner;

import java.util.ArrayList;
import java.util.List;

public class ExitClearanceRecyclerViewAdapter extends RecyclerView.Adapter<ExitClearanceRecyclerViewAdapter.ViewHolder>

    {

        private List<Product> product;
        private LayoutInflater mInflater;
        private EMDKManager emdkManager = null;
        private BarcodeManager barcodeManager = null;
        private Scanner scanner = null;
        private ArrayList<Boolean> checkList;

        // data is passed into the constructor
        ExitClearanceRecyclerViewAdapter(Context context, List<Product> product,ArrayList<Boolean> checkList){
        this.mInflater = LayoutInflater.from(context);
        this.product = product;
        this.checkList = checkList;
    }

        // inflates the cell layout from xml when needed
        @Override
        @NonNull
        public ExitClearanceRecyclerViewAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup
        parent,int viewType){
        View view = mInflater.inflate(R.layout.exitclearance_recyclerviewitem, parent, false);
        return new ExitClearanceRecyclerViewAdapter.ViewHolder(view);
    }

        // binds the data to the TextView in each cell
        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder (@NonNull ViewHolder holder,int position){
        holder.tvProductName.setText(product.get(position).getProductName());
        holder.tvBatchNumber.setText(product.get(position).getBatchNumber());

        if(checkList.get(position)){
           holder.tvVerifyBarcode.setText("Verified");
           holder.tvVerifyBarcode.setTextColor(Color.parseColor("#FFFFFF"));
           holder.tvVerifyBarcode.setBackgroundColor(Color.parseColor("#FF4CAF50"));
        }

        }

        private void verifyBarcode() {
        }

        // total number of cells
        @Override
        public int getItemCount () {
        return product.size();
    }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvProductName;
            TextView tvBatchNumber;
            TextView tvVerifyBarcode;

            ViewHolder(View itemView) {
                super(itemView);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                tvBatchNumber = itemView.findViewById(R.id.tvBatchNumber);
                tvVerifyBarcode = itemView.findViewById(R.id.tvVerifyBarcode);
            }


        }

        // convenience method for getting data at click position


    }


