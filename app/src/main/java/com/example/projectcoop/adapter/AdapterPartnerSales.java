package com.example.projectcoop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectcoop.R;
import com.example.projectcoop.models.Datum;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterPartnerSales extends RecyclerView.Adapter<AdapterPartnerSales.AdapterPartnerSalesHolder> {

    private Context mContext;
    List<Datum> partnerSales;

    public AdapterPartnerSales(Context mContext, List<Datum> partnerSales){
        this.mContext = mContext;
        this.partnerSales = partnerSales;
    }

    @NonNull
    @NotNull
    @Override
    public AdapterPartnerSales.AdapterPartnerSalesHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_partner_sales,parent,false);
        return new AdapterPartnerSalesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterPartnerSales.AdapterPartnerSalesHolder holder, int position) {
        try {
            String partnerID = partnerSales.get(position).getPartnerId();
            String partnerProfit = partnerSales.get(position).getMoneyProfit();
            String partnerTotal = partnerSales.get(position).getMoneyTotal();

            holder.text_Partner.setText(partnerID);
            holder.text_partner_profit.setText("กำไร "+String.format("%,d",Long.parseLong(partnerProfit))+" ₭");
            holder.text_partner_sales.setText("ยอดขาย "+String.format("%,d",Long.parseLong(partnerTotal))+" ₭");

        }catch (NullPointerException e){ }
    }

    @Override
    public int getItemCount() { return partnerSales.size(); }

    public class AdapterPartnerSalesHolder extends RecyclerView.ViewHolder {
        TextView text_Partner,text_partner_profit,text_partner_sales;

        public AdapterPartnerSalesHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            text_Partner = itemView.findViewById(R.id.text_partner);
            text_partner_profit = itemView.findViewById(R.id.text_partner_profit);
            text_partner_sales = itemView.findViewById(R.id.text_partner_sales);

        }
    }
}
