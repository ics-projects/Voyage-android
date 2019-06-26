package com.example.voyage.ui.searchbus;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.voyage.R;
import com.example.voyage.data.models.Schedule;
import com.example.voyage.ui.trips.AvailableBusActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class SearchBusActivity extends AppCompatActivity {
    private static final String LOG_TAG = SearchBusActivity.class.getSimpleName();

    private EditText dateEditText;
    final private Calendar c = Calendar.getInstance();
    private DatePickerListener datePickerListener = new DatePickerListener();

    private SearchBusActivityViewModel viewModel;
    private List<Schedule> schedules;

    private Spinner originSpinner;
    private Spinner destinationSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_search);

        Button search_buses = findViewById(R.id.search_buses);
        originSpinner = findViewById(R.id.originSpinner);
        destinationSpinner = findViewById(R.id.destinationSpinner);

        viewModel = ViewModelProviders.of(this).get(SearchBusActivityViewModel.class);

        fetchSchedules();

        dateEditText = findViewById(R.id.select_date);

        dateEditText.setOnClickListener(view -> {
            Log.d(LOG_TAG, "Datepicker selected");
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(SearchBusActivity.this, datePickerListener, year, month, day).show();
        });

        search_buses.setOnClickListener(view -> {
            String origin = (String) originSpinner.getSelectedItem();
            String destination = (String) destinationSpinner.getSelectedItem();

            String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                    .format(c.getTime());

            if (selectedDate != null) {
                Intent intent = new Intent(this, AvailableBusActivity.class);
                intent.putExtra("TRIP_ORIGIN", origin);
                intent.putExtra("TRIP_DESTINATION", destination);
                intent.putExtra("TRIP_DATE", selectedDate);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 2) {
            Toast.makeText(this, "No trips found", Toast.LENGTH_LONG).show();
        }
    }

    private void fetchSchedules() {
        viewModel.getSchedules().observe(this, returnedSchedules -> {
            schedules = returnedSchedules;
            setSpinnerData();
        });
    }

    private void setSpinnerData() {
        ArrayList<String> originSpinnerItems = new ArrayList<>();
        ArrayList<String> destinationSpinnerItems = new ArrayList<>();

        for (Schedule schedule : schedules) {
            originSpinnerItems.add(schedule.getOrigin());
            destinationSpinnerItems.add(schedule.getDestination());
        }

        ArrayAdapter<String> originSpinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, originSpinnerItems);
        originSpinner.setAdapter(originSpinnerAdapter);

        ArrayAdapter<String> destinationSpinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, destinationSpinnerItems);
        destinationSpinner.setAdapter(destinationSpinnerAdapter);
    }

    private class DatePickerListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String selectedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(c.getTime());

            dateEditText.setText(selectedDate);
        }
    }
}
