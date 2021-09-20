package com.example.projectcoop.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.example.projectcoop.adapter.AdapterPartnerSales;
import com.example.projectcoop.models.Datum;
import com.example.projectcoop.models.Partner;
import com.example.projectcoop.models.Saleslist;

import java.util.ArrayList;
import java.util.Calendar;
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

public class PartnerSalesReportFragment extends Fragment {

    private RecyclerView listViewItem;
    private AdapterPartnerSales adapterPartnerSales;
    private List<Datum> listData  = new ArrayList<>();
    private Context mContext;
    private MainActivityViewModel monthYearLiveData;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        view =   inflater.inflate(R.layout.fragment_partner_sales_report, container, false);
        listViewItem = view.findViewById(R.id.detail2);
        mContext = getActivity();

        int setYear = Calendar.getInstance().get(Calendar.YEAR);
        int setMonth = Calendar.getInstance().get(Calendar.MONTH);
        getListData(view,setYear,setMonth+1);

        Visibility(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        monthYearLiveData = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        monthYearLiveData.getGlobalMonthYear().observe(getViewLifecycleOwner(), new Observer<MonthYear>() {
            @Override
            public void onChanged(MonthYear monthYear) {
                Log.e("PartnerFragment",String.valueOf(monthYear.getGlobalMonth()));
            }
        });

    }

    public void getListData(View view, int year , int month){
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

                    int high = 0;
                    String id_part = "";
                    for (int i=0;i<tempList.getData().size();i++){
                       int testNum =  Integer.parseInt(tempList.getData().get(i).getMoneyTotal());
                        if (high < testNum){
                            high = testNum;
                            id_part = tempList.getData().get(i).getPartnerId();
                        }
                    }

                    /*int total = 0;
                    for (int i=0;i<tempList.getData().size();i++){
                        total += Integer.parseInt(tempList.getData().get(i).getMoneyProfit());
                    }*/
                    TextView text_total_sales = view.findViewById(R.id.text_total_sales);
                    TextView text_partID = view.findViewById(R.id.text_title);
                    text_partID.setText("Partner " + id_part);
                    text_total_sales.setText(String.format("%,d",Long.parseLong(String.valueOf(high)))+" ₭");

                    Collections.sort(tempList.getData(), new Comparator<Datum>() {
                        @Override
                        public int compare(Datum o1, Datum o2) {
                            return Long.valueOf(o2.getMoneyTotal()).compareTo(Long.valueOf(o1.getMoneyTotal()));
                        }
                    });



                    adapterPartnerSales = new AdapterPartnerSales(mContext,tempList.getData());
                    listViewItem.setLayoutManager(new LinearLayoutManager(mContext));
                    listViewItem.setAdapter(adapterPartnerSales);


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

    }
}