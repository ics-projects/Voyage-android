package com.example.voyage.ui.pickseat;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.voyage.R;
import com.example.voyage.data.models.Seat;

import java.util.List;

public class PickSeatActivity extends AppCompatActivity {
    Button payment;
    private int intentIntegerTripId;
    private PickSeatActivityViewModel viewModel;
    private PickSeatAdapter seatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_book);

        // retrieve intent data
        Intent intent = getIntent();
        intentIntegerTripId = intent.getIntExtra("TRIP_ID", 0);

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
        viewModel.getSeats(intentIntegerTripId).observe(this, new Observer<List<Seat>>() {
            @Override
            public void onChanged(@Nullable List<Seat> seats) {
                seatAdapter.setSeats(seats);
            }
        });
    }
}
