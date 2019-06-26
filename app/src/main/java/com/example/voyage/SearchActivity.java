package com.example.voyage;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class SearchActivity extends AppCompatActivity {
    private static final String LOG_TAG = SearchActivity.class.getSimpleName();

    private EditText dateEditText;

    private DatePickerListener datePickerListener = new DatePickerListener();
    final private Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);
        Button search_buses = findViewById(R.id.search_buses);
        search_buses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AvailableBusActivity.class);
                startActivity(intent);
            }
        });

        dateEditText = findViewById(R.id.select_date);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(LOG_TAG, "Datepicker selected");
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(SearchActivity.this, datePickerListener, year, month, day).show();
            }
        });

    }

    private class DatePickerListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String selectedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(c.getTime());

            Log.d(TAG, "onDateSet: " + selectedDate);
            dateEditText.setText(selectedDate);
        }
    }
}
