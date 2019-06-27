package com.example.voyage.ui.pickseat;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.voyage.R;

public class PickSeatActivity extends AppCompatActivity {
    Button payment;

    private PickSeatActivityViewModel viewModel;
    private PickSeatAdapter seatAdapter;

    private int intentIntegerTripId;
    private int intentIntegerPickPoint;
    private int intentIntegerDropPoint;
    private int intentIntegerBusId;

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
        intentIntegerTripId = intent.getIntExtra("TRIP_ID", 0);
        intentIntegerPickPoint = intent.getIntExtra("TRIP_PICK_POINT", 0);
        intentIntegerDropPoint = intent.getIntExtra("TRIP_DROP_POINT", 0);
        intentIntegerBusId = intent.getIntExtra("TRIP_BUS_ID", 0);

        // Set up view model
        viewModel = ViewModelProviders.of(this).get(PickSeatActivityViewModel.class);

        // fetch seats to be displayed
        fetchSeats();

//        payment = findViewById(R.id.pay_seat);
//        payment.setOnClickListener(view -> {
//            Intent intent = new Intent(getApplicationContext(), MpesaActivity.class);
//            startActivity(intent);
//        });
    }

    private void fetchSeats() {
        viewModel.getSeats(intentIntegerBusId).observe(this, seats ->
                seatAdapter.setSeats(seats));
    }
}
