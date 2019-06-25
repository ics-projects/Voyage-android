package com.example.voyage.ui.searchbus;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.voyage.R;
import com.example.voyage.data.models.Schedule;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchBusActivity extends AppCompatActivity {
    private SearchBusActivityViewModel viewModel;
    private List<Schedule> schedules;

    private Spinner originSpinner;
    private Spinner destinationSpinner;

    Button search_buses;
    private TextInputEditText datePickerTextInput;
    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);
        search_buses = findViewById(R.id.search_buses);

        originSpinner = findViewById(R.id.originSpinner);
        destinationSpinner = findViewById(R.id.destinationSpinner);
        TextInputLayout datePickerTextField = findViewById(R.id.select_date_text_input);

        datePickerTextField.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog();
            dpd.initialize((view, year, monthOfYear, dayOfMonth) -> {
                String yearText = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                datePickerTextInput.setText(yearText);
            }, mYear, mMonth, mDay);
        });

        viewModel = ViewModelProviders.of(this).get(SearchBusActivityViewModel.class);

        fetchSchedules();

        search_buses.setOnClickListener(view -> {
//            Intent intent = new Intent(getApplicationContext(), AvailableBusActivity.class);
//            startActivity(intent);
//            viewModel
        });
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
}
