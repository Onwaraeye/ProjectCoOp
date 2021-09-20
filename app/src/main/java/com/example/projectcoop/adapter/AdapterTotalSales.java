package com.example.projectcoop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectcoop.R;
import com.example.projectcoop.models.DataGetDraw;
import com.example.projectcoop.models.Datum;
import com.example.projectcoop.models.SortMoneyProduct;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterTotalSales extends RecyclerView.Adapter<AdapterTotalSales.AdapterTotalSalesHolder> {

    private Context mContext;
    List<SortMoneyProduct> totalSales ;

    public AdapterTotalSales(Context mContext, List<SortMoneyProduct> totalSales){
        this.mContext = mContext;
        this.totalSales = totalSales;
    }

    @NonNull
    @Override
    public AdapterTotalSales.AdapterTotalSalesHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_total_sales, parent,false);
        return new AdapterTotalSalesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull AdapterTotalSales.AdapterTotalSalesHolder holder, int position) {
        try{
            String product = totalSales.get(position).getProduct();
            String moneyproduct = totalSales.get(position).getMoneyProduct();

            holder.text_product.setText(product);
            holder.text_sales.setText(String.format("%,d",Long.parseLong(moneyproduct))+" ₭");

        }catch (NullPointerException e){ }

    }

    @Override
    public int getItemCount() { return totalSales.size(); }

    public class AdapterTotalSalesHolder extends RecyclerView.ViewHolder {
        TextView text_product,text_sales;


        public AdapterTotalSalesHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            text_product = itemView.findViewById(R.id.text_product);
            text_sales = itemView.findViewById(R.id.text_product_sales);
        }
    }
}
