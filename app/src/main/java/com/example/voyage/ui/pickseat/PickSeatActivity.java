package com.example.voyage.ui.pickseat;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.example.voyage.R;

public class PickSeatActivity extends AppCompatActivity {

    private PickSeatViewModel viewModel;
    private PickSeatAdapter seatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_book);

        // Recyclerview
        RecyclerView recyclerView = findViewById(R.id.recyclerView_seats);
        recyclerView.setHasFixedSize(true);

        // LinearLayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Adapter
        seatAdapter = new PickSeatAdapter(this);
        recyclerView.setAdapter(seatAdapter);

        // retrieve intent data
        Intent intent = getIntent();
//        int intentIntegerTripId = intent.getIntExtra("TRIP_ID", 0);
//        int intentIntegerPickPoint = intent.getIntExtra("TRIP_PICK_POINT", 0);
//        int intentIntegerDropPoint = intent.getIntExtra("TRIP_DROP_POINT", 0);
        int intentIntegerBusId = intent.getIntExtra("TRIP_BUS_ID", 0);

        // Set up view model
        PickSeatViewModelFactory factory = new PickSeatViewModelFactory(getApplication(), intentIntegerBusId);
        viewModel = ViewModelProviders.of(this, factory).get(PickSeatViewModel.class);

        // fetch seats to be displayed
        fetchSeats();
    }

    private void fetchSeats() {
        viewModel.getSeats().observe(this, seatRowCollection ->
                seatAdapter.setSeatRowCollection(seatRowCollection));
    }
}
