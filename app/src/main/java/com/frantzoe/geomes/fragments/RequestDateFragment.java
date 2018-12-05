package com.frantzoe.geomes.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.frantzoe.geomes.R;
import com.frantzoe.geomes.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class RequestDateFragment extends Fragment {

    private String dateString;
    private boolean dateSelected;

    private TextView txtDatePick;
    private TextView txtDayLet;
    private TextView txtDayNum;
    private TextView txtMonth;
    private TextView txtYear;

    public RequestDateFragment() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_request_date, container, false);
        txtDatePick = rootView.findViewById(R.id.txtDatePick);
        txtDayLet = rootView.findViewById(R.id.txtDayLet);
        txtDayNum = rootView.findViewById(R.id.txtDayNum);
        txtMonth = rootView.findViewById(R.id.txtMonth);
        txtYear = rootView.findViewById(R.id.txtYear);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utilities.shortFormat, Locale.FRENCH);
                        try {Date date = simpleDateFormat.parse(dayOfMonth + "/" + (month + 1) + "/" + year);
                            dateString = Utilities.formatDate(date, Utilities.shortFormat, Locale.FRENCH);
                            final String[] dateSplit = Utilities.formatDate(date, Utilities.longFormat, Locale.FRENCH).split(" ");
                            txtDayLet.setText(dateSplit[0]);
                            txtDayNum.setText(dateSplit[1]);
                            txtMonth.setText(dateSplit[2]);
                            txtYear.setText(dateSplit[3]);
                            dateSelected = true;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    public boolean isDateSelected() {
        return dateSelected;
    }

    public String getDateString() {
        return dateString;
    }
}
