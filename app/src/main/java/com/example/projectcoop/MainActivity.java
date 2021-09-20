package com.example.projectcoop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.db.williamchart.Tooltip;
import com.db.williamchart.data.AxisType;
import com.db.williamchart.data.configuration.LineChartConfiguration;
import com.db.williamchart.pointtooltip.PointTooltip;
import com.db.williamchart.renderer.LineChartRenderer;
import com.db.williamchart.view.BarChartView;
import com.db.williamchart.view.LineChartView;
import com.example.projectcoop.adapter.AdapterPartnerSales;
import com.example.projectcoop.adapter.AdapterTotalSales;
import com.example.projectcoop.fragment.PartnerSalesReportFragment;
import com.example.projectcoop.fragment.TotalSalesFragment;
import com.example.projectcoop.models.Datum;
import com.example.projectcoop.models.Partner;
import com.example.projectcoop.models.Saleslist;
import com.example.projectcoop.models.SortMoneyProduct;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView Text_date;


    private RecyclerView listViewItemTotal,ListViewItemPartner;

    private AdapterTotalSales adapterTotalSales;
    private AdapterPartnerSales adapterPartnerSales;

    private Context mContext;

    int setYear = Calendar.getInstance().get(Calendar.YEAR);
    int setMonth = Calendar.getInstance().get(Calendar.MONTH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewItemTotal = findViewById(R.id.detail);
        ListViewItemPartner = findViewById(R.id.detail2);

        String date = getMonth(setMonth) + " " + setYear;
        Text_date = findViewById(R.id.text_date);
        Text_date.setText(date);

        Visibility();
        showDatePickerDialog();
        getListData(setYear,setMonth+1);
        getAllSales(setYear,setMonth);



        Exit();


    }


    public void showDatePickerDialog(){
        View calendar = findViewById(R.id.image_ic_calender);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog.show();
            }
        });
    }

    public String getMonth(int month){
        return new DateFormatSymbols().getMonths()[month];
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = getMonth(month) + " " + year;
        final MainActivityViewModel model = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        MonthYear monthYear = new MonthYear(year,month+1);
        MutableLiveData<MonthYear> my =  model.setDate(monthYear);
        model.setGlobalMonthYear(my);
        Log.e("MainActivity",String.valueOf(monthYear.getGlobalMonth()));
        getListData(year,month+1);
        Text_date.setText(date);
    }

    public void Visibility() {
        ImageView arrow = findViewById(R.id.arrow);
        RecyclerView detail = findViewById(R.id.detail);
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
        ImageView arrow2 = findViewById(R.id.arrow2);
        RecyclerView detail2 = findViewById(R.id.detail2);
        arrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detail2.getVisibility() == View.VISIBLE ){
                    detail2.setVisibility(View.GONE);
                    arrow2.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }else {
                    detail2.setVisibility(View.VISIBLE);
                    arrow2.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                }
            }
        });
    }

    public void Exit(){
        View exit = findViewById(R.id.imageViewLogout);
        exit.setOnClickListener((v) -> {
            finish();
            System.exit(0);
        });
    }

    public interface RetrofitInterfaceSalesList{

        @POST("api/ivt/partner")
        Call<Saleslist> requestSalesList(@Body Partner body);
    }

    public void getListData(int year,int month){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://35.240.178.202:8080/dashboriboon/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterfaceSalesList service = retrofit.create(RetrofitInterfaceSalesList.class);
        Map<String, Object> jsonParams = new ArrayMap<>();

        Partner partner = new Partner("webreport01","ek7cMZK7j9AN7ACCZx5BZ4N8wevUA4u7",month,year);
        Call<Saleslist> call = service.requestSalesList(partner);
        call.enqueue(new Callback<Saleslist>() {
            @Override
            public void onResponse(Call<Saleslist> call, Response<Saleslist> response) {
                Saleslist tempList = new Saleslist();
                tempList = response.body();
                if (Integer.parseInt(tempList.getStatus()) == 1) {
                    if (response.code() == 200) {

                        TextView timestamp = findViewById(R.id.text_timestamp);
                        timestamp.setText("ข้อมูล ณ เวลา " + tempList.getData().get(0).getTimeUpdate() + " น.");

                        //////////////////////////////////Total

                        int moneySumGarena = 0;
                        int moneySumUnitel = 0;
                        int moneySumEtl = 0;
                        int moneySumBeeline = 0;
                        int moneySumLaotelecom = 0;
                        int moneySumTruemoney = 0;
                        int moneySumlottory = 0;

                        List<SortMoneyProduct> list = new ArrayList<SortMoneyProduct>();
                        for (int i = 0; i < tempList.getData().size(); i++) {
                            moneySumGarena += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getGarena().getSaleTotal());
                            moneySumUnitel += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getUnitel().getSaleTotal());
                            moneySumEtl += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getEtl().getSaleTotal());
                            moneySumBeeline += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getBeeline().getSaleTotal());
                            moneySumLaotelecom += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getLaotelecom().getSaleTotal());
                            moneySumTruemoney += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getTruemoney().getSaleTotal());
                            moneySumlottory += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getLottory().getSaleTotal());
                        }
                        SortMoneyProduct garena = new SortMoneyProduct("Garena", String.valueOf(moneySumGarena));
                        SortMoneyProduct unital = new SortMoneyProduct("Unital", String.valueOf(moneySumUnitel));
                        SortMoneyProduct etl = new SortMoneyProduct("Etl", String.valueOf(moneySumEtl));
                        SortMoneyProduct beeline = new SortMoneyProduct("Beeline", String.valueOf(moneySumBeeline));
                        SortMoneyProduct laotelecom = new SortMoneyProduct("Laotelecom", String.valueOf(moneySumLaotelecom));
                        SortMoneyProduct truemoney = new SortMoneyProduct("Truemoney", String.valueOf(moneySumTruemoney));
                        SortMoneyProduct lottory = new SortMoneyProduct("Lottory", String.valueOf(moneySumlottory));

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
                        for (int i = 0; i < list.size(); i++) {
                            total += Integer.parseInt(list.get(i).getMoneyProduct());
                        }


                        TextView tx_totalSum = findViewById(R.id.text_total_sales);
                        tx_totalSum.setText(String.format("%,d", Long.parseLong(String.valueOf(total))) + " ₭");

                        adapterTotalSales = new AdapterTotalSales(MainActivity.this, list);
                        listViewItemTotal.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        listViewItemTotal.setAdapter(adapterTotalSales);

                        ////////////////////////////////////////////Partner

                        int high = 0;
                        String id_part = "";
                        for (int i = 0; i < tempList.getData().size(); i++) {
                            int testNum = Integer.parseInt(tempList.getData().get(i).getMoneyTotal());
                            if (high < testNum) {
                                high = testNum;
                                id_part = tempList.getData().get(i).getPartnerId();
                            }
                        }
                        TextView text_total_sales = findViewById(R.id.text_partner_sales);
                        TextView text_partID = findViewById(R.id.text_title_partner);
                        text_partID.setText("Partner " + id_part);
                        text_total_sales.setText(String.format("%,d", Long.parseLong(String.valueOf(high))) + " ₭");

                        Collections.sort(tempList.getData(), new Comparator<Datum>() {
                            @Override
                            public int compare(Datum o1, Datum o2) {
                                return Long.valueOf(o2.getMoneyTotal()).compareTo(Long.valueOf(o1.getMoneyTotal()));
                            }
                        });


                        adapterPartnerSales = new AdapterPartnerSales(MainActivity.this, tempList.getData());
                        ListViewItemPartner.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        ListViewItemPartner.setAdapter(adapterPartnerSales);

                        /////////////////////////////////////////////////////////LineChart

                        /*LineChartView lineChartView;
                        lineChartView = findViewById(R.id.lineChart);

                        int unt,etL,ltlc,grn,bl,tmn,ltr;
                        unt = (Integer.parseInt(unital.getMoneyProduct())/1000000);
                        etL = (Integer.parseInt(etl.getMoneyProduct())/1000000);
                        ltlc = (Integer.parseInt(laotelecom.getMoneyProduct())/1000000);
                        grn = (Integer.parseInt(garena.getMoneyProduct())/1000000);
                        bl = (Integer.parseInt(beeline.getMoneyProduct())/1000000);
                        tmn = (Integer.parseInt(truemoney.getMoneyProduct())/1000000);
                        ltr = (Integer.parseInt(lottory.getMoneyProduct())/1000000);


                        LinkedHashMap<String,Float> lc = new LinkedHashMap<>();


                        lc.put(unital.getProduct(), Float.parseFloat((String.valueOf(unt))));
                        lc.put(etl.getProduct(), Float.parseFloat(String.valueOf(etL)));
                        lc.put(laotelecom.getProduct(), Float.parseFloat(String.valueOf(ltlc)));
                        lc.put(garena.getProduct(), Float.parseFloat(String.valueOf(grn)));
                        lc.put(beeline.getProduct(), Float.parseFloat(String.valueOf(bl)));
                        lc.put(truemoney.getProduct(), Float.parseFloat(String.valueOf(tmn)));
                        lc.put(lottory.getProduct(), Float.parseFloat(String.valueOf(ltr)));


                        lineChartView.show(lc);*/

                        ///////////////////////////////////////////////BarChart

                        BarChartView barChartView;
                        barChartView = findViewById(R.id.barChart);

                        int unt,etL,ltlc,grn,bl,tmn,ltr;
                        unt = (Integer.parseInt(unital.getMoneyProduct())/1000000);
                        etL = (Integer.parseInt(etl.getMoneyProduct())/1000000);
                        ltlc = (Integer.parseInt(laotelecom.getMoneyProduct())/1000000);
                        grn = (Integer.parseInt(garena.getMoneyProduct())/1000000);
                        bl = (Integer.parseInt(beeline.getMoneyProduct())/1000000);
                        tmn = (Integer.parseInt(truemoney.getMoneyProduct())/1000000);
                        ltr = (Integer.parseInt(lottory.getMoneyProduct())/1000000);


                        LinkedHashMap<String,Float> lh = new LinkedHashMap<String,Float>();

                        lh.put(unital.getProduct(), Float.parseFloat((String.valueOf(unt))));
                        lh.put(etl.getProduct(), Float.parseFloat(String.valueOf(etL)));
                        lh.put(laotelecom.getProduct(), Float.parseFloat(String.valueOf(ltlc)));
                        lh.put(garena.getProduct(), Float.parseFloat(String.valueOf(grn)));
                        lh.put(beeline.getProduct(), Float.parseFloat(String.valueOf(bl)));
                        lh.put(truemoney.getProduct(), Float.parseFloat(String.valueOf(tmn)));
                        lh.put(lottory.getProduct(), Float.parseFloat(String.valueOf(ltr)));


                        barChartView.show(lh);



                    /*listData.clear();
                    for (int i = 0; i<tempList.getData().size() ; i++){
                        listData.add(tempList.getData().get(i));
                    }*/

                    /*String descrinption = response.body().getDescription();
                    listData = response.body().getData();
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("สำเร็จ");
                    builder.setMessage(descrinption);
                    builder.setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();*/
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("ไม่สำเร็จ.......");
                        builder.setMessage("code :" + response.code() + " \n" + response.message());
                        builder.setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }

                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("ไม่สำเร็จ");
                    builder.setMessage("ไม่พบรายการในเดือนนี้");
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

    public void getAllSales(int year,int month){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://35.240.178.202:8080/dashboriboon/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterfaceSalesList service = retrofit.create(RetrofitInterfaceSalesList.class);
        Map<String, Object> jsonParams = new ArrayMap<>();

        Partner partner = new Partner("webreport01","ek7cMZK7j9AN7ACCZx5BZ4N8wevUA4u7",month,year);
        Call<Saleslist> call = service.requestSalesList(partner);
        call.enqueue(new Callback<Saleslist>() {
            @Override
            public void onResponse(Call<Saleslist> call, Response<Saleslist> response) {
                Saleslist tempList = new Saleslist();
                tempList = response.body();
                if (Integer.parseInt(tempList.getStatus()) == 1) {
                    if (response.code() == 200) {

                        //////////////////////////////////Total

                        int moneySumGarena = 0;
                        int moneySumUnitel = 0;
                        int moneySumEtl = 0;
                        int moneySumBeeline = 0;
                        int moneySumLaotelecom = 0;
                        int moneySumTruemoney = 0;
                        int moneySumlottory = 0;

                        List<SortMoneyProduct> list = new ArrayList<SortMoneyProduct>();
                        for (int i = 0; i < tempList.getData().size(); i++) {
                            moneySumGarena += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getGarena().getSaleTotal());
                            moneySumUnitel += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getUnitel().getSaleTotal());
                            moneySumEtl += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getEtl().getSaleTotal());
                            moneySumBeeline += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getBeeline().getSaleTotal());
                            moneySumLaotelecom += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getLaotelecom().getSaleTotal());
                            moneySumTruemoney += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getTruemoney().getSaleTotal());
                            moneySumlottory += Integer.parseInt(tempList.getData().get(i).getDataGetDraw().getLottory().getSaleTotal());
                        }
                        SortMoneyProduct garena = new SortMoneyProduct("Garena", String.valueOf(moneySumGarena));
                        SortMoneyProduct unital = new SortMoneyProduct("Unital", String.valueOf(moneySumUnitel));
                        SortMoneyProduct etl = new SortMoneyProduct("Etl", String.valueOf(moneySumEtl));
                        SortMoneyProduct beeline = new SortMoneyProduct("Beeline", String.valueOf(moneySumBeeline));
                        SortMoneyProduct laotelecom = new SortMoneyProduct("Laotelecom", String.valueOf(moneySumLaotelecom));
                        SortMoneyProduct truemoney = new SortMoneyProduct("Truemoney", String.valueOf(moneySumTruemoney));
                        SortMoneyProduct lottory = new SortMoneyProduct("Lottory", String.valueOf(moneySumlottory));

                        list.add(garena);
                        list.add(unital);
                        list.add(etl);
                        list.add(beeline);
                        list.add(laotelecom);
                        list.add(truemoney);
                        list.add(lottory);

                        Long total = 0L;
                        for (int i = 0; i < list.size(); i++) {
                            total += Integer.parseInt(list.get(i).getMoneyProduct());
                        }

                        /////////////////////////////////////////////////////////LineChart

                        LineChartView lineChartView;
                        lineChartView = findViewById(R.id.lineChart);

                        LinkedHashMap<String,Float> lc = new LinkedHashMap<>();

                        for (int i = 0;tempList.getStatus().equals(1); i++){
                            lc.put(getMonth(month), Float.valueOf(total));
                        }


                        lineChartView.show(lc);



                    /*listData.clear();
                    for (int i = 0; i<tempList.getData().size() ; i++){
                        listData.add(tempList.getData().get(i));
                    }*/

                    /*String descrinption = response.body().getDescription();
                    listData = response.body().getData();
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("สำเร็จ");
                    builder.setMessage(descrinption);
                    builder.setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();*/
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("ไม่สำเร็จ.......");
                        builder.setMessage("code :" + response.code() + " \n" + response.message());
                        builder.setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }

                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("ไม่สำเร็จ");
                    builder.setMessage("ไม่พบรายการในเดือนนี้");
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


}