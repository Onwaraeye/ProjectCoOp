package com.example.projectcoop;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;


public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<MonthYear> globalMonthYear;

    int setYear = Calendar.getInstance().get(Calendar.YEAR);
    int setMonth = Calendar.getInstance().get(Calendar.MONTH);
    MonthYear monthYear = new MonthYear(setYear,setMonth+1);


    public MutableLiveData<MonthYear> getGlobalMonthYear() {
        if (globalMonthYear == null){
            globalMonthYear = new MutableLiveData<>();
            globalMonthYear.setValue(monthYear);
        }
        return globalMonthYear;
    }

    public MutableLiveData<MonthYear> setDate(MonthYear my){
        MutableLiveData<MonthYear> my2 = new MutableLiveData<MonthYear>();
        my2.setValue(my);
        return my2;
    }
    public void setGlobalMonthYear(MutableLiveData<MonthYear> globalMonthYear) {
        this.globalMonthYear = globalMonthYear;
    }
}
