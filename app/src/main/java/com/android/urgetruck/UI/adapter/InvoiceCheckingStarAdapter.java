package com.android.urgetruck.UI.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Models.invoicecheckingstar.Product;
import com.android.urgetruck.UI.Models.invoicecheckingstar.ProductX;

import java.util.ArrayList;
import java.util.List;

public class InvoiceCheckingStarAdapter extends RecyclerView.Adapter<InvoiceCheckingStarAdapter.ViewHolder>{
    private List<Product> prList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    public InvoiceCheckingStarAdapter(Context context,List<Product> prList) {
        this.context = context;
        this.prList = prList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_checking_check_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // holder.setData(prList.get(position).getUrl(),prList.get(position).getName(),position,cm.get(position).getCourseDesc(),cm.get(position).getKey());
        holder.setData(prList.get(position).getProductName(),prList.get(position).getProductCategory(),prList.get(position).getProductTransactionCode());

        holder.invoiceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.invoiceCheckBox.setChecked(isChecked);
                if (onItemClickListener != null) {
                    if(isChecked)
                    {
                        onItemClickListener.onItemClick(new ProductX(prList.get(position).getProductTransactionCode()));
                    }
                    else {
                        onItemClickListener.onItemUnchecked(new ProductX(prList.get(position).getProductTransactionCode()));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return prList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView productName, productCategory,productTransactionCode;
        private CheckBox invoiceCheckBox;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            invoiceCheckBox=itemView.findViewById(R.id.checkBoxInvoice);
            productName=itemView.findViewById(R.id.tv_column_one);
            productCategory=itemView.findViewById(R.id.tv_column_two);
            productTransactionCode=itemView.findViewById(R.id.tv_column_three);


        }
        private void setData(String invoiceText,String productCategory,String productTransactionCode)
        {
            //Glide.with(itemView.getContext()).load(url).into(img);
            this.productName .setText(invoiceText);
            this.productCategory .setText(productCategory);
            this.productTransactionCode.setText(productTransactionCode);

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

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(ProductX product);
        void onItemUnchecked(ProductX product);
    }
}