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


import java.util.HashMap;
import java.util.List;

public class ExitClearanceNewAdapter extends RecyclerView.Adapter<ExitClearanceNewAdapter.ViewHolder>{
    private List<InvoiceDetail> invoiceDetailsList;
    private Context context;
    private RecyclerView recyclerView;
    private OnItemClickListener onItemClickListener;

    private HashMap<String, Boolean> checkedStateMap = new HashMap<>();

    public ExitClearanceNewAdapter(Context context, List<InvoiceDetail> prList ,RecyclerView recyclerView) {
        this.context = context;
        this.invoiceDetailsList = prList;
        this.recyclerView = recyclerView;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_checking_check_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
       // holder.setData(prList.get(position).getUrl(),prList.get(position).getName(),position,cm.get(position).getCourseDesc(),cm.get(position).getKey());

        if(invoiceDetailsList.get(pos).isVerified()==true)
        {
            holder.verifiedBarcode.setVisibility(View.VISIBLE);
            setVerifiedData(holder,pos);
        }
        //setDefaultCheckState(holder);


        holder.setData(invoiceDetailsList.get(pos).getInvoiceNumber(), invoiceDetailsList.get(pos).getStatus());
    /*    holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(new InvoiceDetail(invoiceDetailsList.get(pos).getInvoiceNumber(),false,true,
                        "Success", invoiceDetailsList.get(pos).getVehicelTransactionId()));
                notifyItemChanged(pos);
            }
        });*/
        holder.verifyBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onVerifyBarcodeClick(pos);

            }
        });
       /* holder.invoiceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.invoiceCheckBox.setChecked(isChecked);
                if (onItemClickListener != null) {
                    if(isChecked)
                    {
                        onItemClickListener.onItemClick(new InvoiceDetail(invoiceDetailsList.get(position).getInvoiceNumber(),false,true,
                                "Success", invoiceDetailsList.get(position).getVehicelTransactionId()));
                    }
                    else {
                        onItemClickListener.onItemUnchecked(new InvoiceDetail(invoiceDetailsList.get(position).getInvoiceNumber(),false,false,
                                "Failed", invoiceDetailsList.get(position).getVehicelTransactionId()));
                    }
                }
            }
        });*/
    }

/*    private void setDefaultCheckState(ViewHolder holder) {
        for(InvoiceDetail invoiceDetail:invoiceDetailsList)
        {
            if(invoiceDetail.isVerified()==true)
            {
                //holder.invoiceCheckBox.setChecked(true);
                holder.verifyBarcoded.setVisibility(View.VISIBLE);
                notifyItemChanged(holder.getAdapterPosition());
            }
        }


    }*/
    private void setVerifiedData(ViewHolder holder, int position) {
        /*for(InvoiceDetail invoiceDetail:invoiceDetailsList)
        {
            if(invoiceDetail.isVerified()==true || holder.verifyBarcoded.getVisibility()==View.VISIBLE)
            {
                //holder.invoiceCheckBox.setChecked(true);
                onItemClickListener.onItemClick(new InvoiceDetail(invoiceDetailsList.get(position).getInvoiceNumber(),false,true,
                        "Pending", invoiceDetailsList.get(position).getVehicelTransactionId()));
            }
        }*/


        if (position != RecyclerView.NO_POSITION && holder.verifiedBarcode.getVisibility() == View.VISIBLE) {
            onItemClickListener.onItemClick(new InvoiceDetail(invoiceDetailsList.get(position).getInvoiceNumber(),false,true,
                    "Pending", invoiceDetailsList.get(position).getVehicelTransactionId()));
        }
    }

    public void markCheckboxByTag(String scannedTag) {
        for (int i = 0; i < invoiceDetailsList.size(); i++) {
            InvoiceDetail invoiceDetail = invoiceDetailsList.get(i);
            if (invoiceDetail.getInvoiceNumber().equals(scannedTag)) {
                // Access the ViewHolder for the item and check its checkbox
              /*  ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                if (viewHolder != null) {
                    viewHolder.verifyBarcoded.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                    notifyItemChanged(i);
                }*/

                ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                if (viewHolder != null) {
                    viewHolder.verifiedBarcode.setVisibility(View.VISIBLE);
                    viewHolder.verifyBarcode.setVisibility(View.GONE);
                    onItemClickListener.onItemClick(new InvoiceDetail(
                            invoiceDetail.getInvoiceNumber(),
                            false, true, "Pending", invoiceDetail.getVehicelTransactionId()));

                    notifyItemChanged(i);
                    break;
                }
                break;
            }
        }
    }


/*
    public void markCheckboxByTag(String scannedTag) {
        for (int i = 0; i < invoiceDetailsList.size(); i++) {
            InvoiceDetail invoiceDetail = invoiceDetailsList.get(i);
            if (invoiceDetail.getInvoiceNumber().equals(scannedTag)) {
                // Access the ViewHolder for the item and check its checkbox
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
                if (viewHolder instanceof ViewHolder) {

                    ((ViewHolder) viewHolder).invoiceCheckBox.setChecked(true);
                    // Notify the adapter that the data has changed for this specific position
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }
*/


    @Override
    public int getItemCount() {
        return invoiceDetailsList.size();
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
   /*     public void checkCheckBox() {
            invoiceCheckBox.setChecked(true);
        }*/
 /*  public void checkCheckBox(int position) {
       invoiceCheckBox.setChecked(true);
       // Optionally, you can also update the data set and notify the adapter about the change
       invoiceDetailsList.get(position).setChecked(true);
       recyclerView.getAdapter().notifyItemChanged(position);
   }*/
        private void setData(String invoiceNumber,String status )
        {
            //Glide.with(itemView.getContext()).load(url).into(img);
            this.invoiceNumber .setText(invoiceNumber);
            //this.status .setText(status);


           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(itemView.getContext(),setActivity.class);
                    i.putExtra("title",title);
                    i.putExtra("position",position);
                    i.putExtra("key",key);
                    itemView.getContext().startActivity(i);
                }
            });*/

        }
    }
/*    public void markCheckboxByTag(String scannedTag) {
        for (int i = 0; i < invoiceDetailsList.size(); i++) {
            InvoiceDetail invoiceDetail = invoiceDetailsList.get(i);
            if (invoiceDetail.getInvoiceNumber().equals(scannedTag)) {
                // Access the ViewHolder for the item and check its checkbox
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
                if (viewHolder instanceof ViewHolder) {
                    ((ViewHolder) viewHolder).invoiceCheckBox.setChecked(true);
                    break;
                }
                notifyItemChanged(i);
            }
        }
    }*/
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(InvoiceDetail product);
        void onItemUnchecked(InvoiceDetail product);

        void onVerifyBarcodeClick(int position);
    }
}