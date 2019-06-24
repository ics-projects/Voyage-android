package com.example.voyage.ui.searchbus;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.voyage.AvailableBusActivity;
import com.example.voyage.R;
import com.example.voyage.data.models.Schedule;

import java.util.ArrayList;
import java.util.List;

public class SearchBusActivity extends AppCompatActivity {
    private SearchBusActivityViewModel viewModel;
    private List<Schedule> schedules;

    private Spinner originSpinner;
    private Spinner destinationSpinner;

    private ArrayAdapter<String> originSpinnerAdapter;
    private ArrayAdapter<String> destinationSpinnerAdapter;

    Button search_buses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);
        search_buses = findViewById(R.id.search_buses);

        originSpinner = findViewById(R.id.originSpinner);
        destinationSpinner = findViewById(R.id.destinationSpinner);

        viewModel = ViewModelProviders.of(this).get(SearchBusActivityViewModel.class);

        fetchSchedules();

        search_buses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AvailableBusActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchSchedules() {
        viewModel.getSchedules().observe(this, new Observer<List<Schedule>>() {
            @Override
            public void onChanged(@Nullable List<Schedule> returnedSchedules) {
                schedules = returnedSchedules;
                setSpinnerData();
            }
        });
    }

    private void setSpinnerData() {
        ArrayList<String> originSpinnerItems = new ArrayList<>();
        ArrayList<String> destinationSpinnerItems = new ArrayList<>();

        for (Schedule schedule : schedules) {
            originSpinnerItems.add(schedule.getOrigin());
            destinationSpinnerItems.add(schedule.getDestination());
        }

        originSpinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, originSpinnerItems);
        originSpinner.setAdapter(originSpinnerAdapter);

        destinationSpinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, destinationSpinnerItems);
        destinationSpinner.setAdapter(destinationSpinnerAdapter);
    }
}
