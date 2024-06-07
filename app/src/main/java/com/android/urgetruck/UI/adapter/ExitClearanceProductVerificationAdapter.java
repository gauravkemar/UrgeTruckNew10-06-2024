package com.android.urgetruck.UI.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Models.exitclearancenew.InvoiceDetail;
import com.android.urgetruck.UI.Models.exitclearancenew.ProductDetail;

import java.util.HashMap;
import java.util.List;

public class ExitClearanceProductVerificationAdapter extends RecyclerView.Adapter<ExitClearanceProductVerificationAdapter.ViewHolder>{
    private List<ProductDetail> productDetails;

    private ExitClearanceProductVerificationAdapter.OnItemClickListener onItemClickListener;



    public ExitClearanceProductVerificationAdapter(Context context, List<ProductDetail> prList ,RecyclerView recyclerView) {
        this.productDetails = prList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_checking_check_list_item,parent,false);
        return new ExitClearanceProductVerificationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        // holder.setData(prList.get(position).getUrl(),prList.get(position).getName(),position,cm.get(position).getCourseDesc(),cm.get(position).getKey());

        if(productDetails.get(pos).isVerified)
        {
            holder.verifiedBarcode.setVisibility(View.VISIBLE);
            holder.verifyBarcode.setVisibility(View.GONE);
            //setVerifiedData(holder,pos);
        }
        else {
            holder.verifyBarcode.setVisibility(View.VISIBLE);
        }
        //setDefaultCheckState(holder);
        holder.setData(productDetails.get(pos).batchNumber);
        holder.verifyBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onVerifyBarcodeClick(productDetails.get(pos).getBatchNumber(),pos);
            }
        });

    }


    public void markCheckboxByTag(String scannedTag) {
        for (int i = 0; i < productDetails.size(); i++) {
            if (productDetails.get(i).getBatchNumber().equals(scannedTag)) {
                // Update the data in the dataset
                productDetails.get(i).setIsVerified(true);
                // Notify the adapter that the data at position i has changed
                notifyItemChanged(i);
                break;
            }
        }
    }
    @Override
    public int getItemCount() {
        return productDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView invoiceNumber, verifiedBarcode,verifyBarcode;
        //private CheckBox invoiceCheckBox;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            //invoiceCheckBox=itemView.findViewById(R.id.checkBoxInvoice);
            //invoiceNumber=itemView.findViewById(R.id.tvVerifyBarcode);
            invoiceNumber=itemView.findViewById(R.id.tvInvoiceNumber);
            verifiedBarcode=itemView.findViewById(R.id.tvVerifiedBarcode);
            verifyBarcode=itemView.findViewById(R.id.tvVerifyBarcode);
            //status=itemView.findViewById(R.id.tv_column_two);
            //productTransactionCode=itemView.findViewById(R.id.tv_column_three);


        }

        private void setData(String invoiceNumber )
        {
            //Glide.with(itemView.getContext()).load(url).into(img);
            this.invoiceNumber .setText(invoiceNumber);

        }
    }

    public void setOnItemClickListener(ExitClearanceProductVerificationAdapter.OnItemClickListener listener) {
        onItemClickListener = listener;
    }
    public interface OnItemClickListener {


        void onVerifyBarcodeClick(String invoicNo,int position);
    }
}

