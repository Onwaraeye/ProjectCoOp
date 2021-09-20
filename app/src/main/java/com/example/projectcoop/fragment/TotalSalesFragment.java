package com.example.projectcoop.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectcoop.MainActivity;
import com.example.projectcoop.MainActivityViewModel;
import com.example.projectcoop.MonthYear;
import com.example.projectcoop.R;
import com.example.projectcoop.adapter.AdapterTotalSales;
import com.example.projectcoop.models.Datum;
import com.example.projectcoop.models.Partner;
import com.example.projectcoop.models.Saleslist;
import com.example.projectcoop.models.SortMoneyProduct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class TotalSalesFragment extends Fragment {

    private RecyclerView listViewItem;
    private AdapterTotalSales adapterTotalSales;
    private List<Datum> listData  = new ArrayList<>();
    private Context mContext;
    private MainActivityViewModel monthYearLiveData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =   inflater.inflate(R.layout.fragment_total_sales, container, false);
        listViewItem = view.findViewById(R.id.detail);
        mContext = getActivity();


        monthYearLiveData = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        getListData(view,monthYearLiveData.getGlobalMonthYear().getValue().getGlobalYear(),monthYearLiveData.getGlobalMonthYear().getValue().getGlobalMonth());

        Visibility(view);
        return view;
    }

    public void getListData(View view, int year, int month){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://35.240.178.202:8080/dashboriboon/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MainActivity.RetrofitInterfaceSalesList service = retrofit.create(MainActivity.RetrofitInterfaceSalesList.class);
        Map<String, Object> jsonParams = new ArrayMap<>();

        Partner partner = new Partner("webreport01","ek7cMZK7j9AN7ACCZx5BZ4N8wevUA4u7",month,year);
        Call<Saleslist> call = service.requestSalesList(partner);
        call.enqueue(new Callback<Saleslist>() {
            @Override
            public void onResponse(Call<Saleslist> call, Response<Saleslist> response) {
                if(response.code() == 200) {

                    Saleslist tempList = new Saleslist();
                    tempList = response.body();
                    int moneySumGarena = 0;
                    int moneySumUnitel = 0;
                    int moneySumEtl = 0;
                    int moneySumBeeline = 0;
                    int moneySumLaotelecom = 0;
                    int moneySumTruemoney = 0;
                    int moneySumlottory = 0;

                    List<SortMoneyProduct> list = new ArrayList<SortMoneyProduct>();
                    for (int i = 0; i<tempList.getData().size(); i++){
                        moneySumGarena += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getGarena().getSaleTotal());
                        moneySumUnitel += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getUnitel().getSaleTotal());
                        moneySumEtl += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getEtl().getSaleTotal());
                        moneySumBeeline += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getBeeline().getSaleTotal());
                        moneySumLaotelecom += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getLaotelecom().getSaleTotal());
                        moneySumTruemoney += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getTruemoney().getSaleTotal());
                        moneySumlottory += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getLottory().getSaleTotal());
                    }
                    SortMoneyProduct garena = new SortMoneyProduct("Garena",String.valueOf(moneySumGarena));
                    SortMoneyProduct unital = new SortMoneyProduct("Unital",String.valueOf(moneySumUnitel));
                    SortMoneyProduct etl = new SortMoneyProduct("Etl",String.valueOf(moneySumEtl));
                    SortMoneyProduct beeline = new SortMoneyProduct("Beeline",String.valueOf(moneySumBeeline));
                    SortMoneyProduct laotelecom = new SortMoneyProduct("Laotelecom",String.valueOf(moneySumLaotelecom));
                    SortMoneyProduct truemoney = new SortMoneyProduct("Truemoney",String.valueOf(moneySumTruemoney));
                    SortMoneyProduct lottory = new SortMoneyProduct("Lottory",String.valueOf(moneySumlottory));

                    list.add(garena);
                    list.add(unital);
                    list.add(etl);
                    list.add(beeline);
                    list.add(laotelecom);
                    list.add(truemoney);
                    list.add(lottory);

                    Collections.sort(list, new Comparator<SortMoneyProduct>() {
                        @Override
                        public int compare(SortMoneyProduct o1, SortMoneyProduct o2) {
                            return Long.valueOf(o2.getMoneyProduct()).compareTo(Long.valueOf(o1.getMoneyProduct()));
                        }
                    });

                    Long total = 0L;
                    for (int i=0;i<list.size();i++){
                        total += Integer.parseInt(list.get(i).getMoneyProduct());
                    }


                    TextView tx_totalSum = view.findViewById(R.id.text_total_sales);
                    tx_totalSum.setText(String.format("%,d",Long.parseLong(String.valueOf(total)))+" ₭");

                    adapterTotalSales = new AdapterTotalSales(mContext,list);
                    listViewItem.setLayoutManager(new LinearLayoutManager(mContext));
                    listViewItem.setAdapter(adapterTotalSales);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("ไม่สำเร็จ.......");
                    builder.setMessage("code :" + response.code()+" \n"+response.message());
                    builder.setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<Saleslist> call, Throwable t) {

            }
        });
    }


    public interface RetrofitInterfaceSalesList{
        @POST("api/ivt/partner")
        Call<Saleslist> requestSalesList(@Body Partner body);
    }

    public void Visibility(View view) {
        ImageView arrow = view.findViewById(R.id.arrow);
        RecyclerView detail = view.findViewById(R.id.detail);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detail.getVisibility() == View.VISIBLE) {
                    detail.setVisibility(View.GONE);
                    arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                } else {
                    detail.setVisibility(View.VISIBLE);
                    arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                }
            }
        });
    }
}

