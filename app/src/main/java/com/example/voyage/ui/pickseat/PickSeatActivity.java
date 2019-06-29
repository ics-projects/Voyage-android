package com.example.voyage.ui.pickseat;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.voyage.MpesaActivity;
import com.example.voyage.R;

import java.util.ArrayList;
import java.util.Arrays;

public class PickSeatActivity extends AppCompatActivity implements PickSeatAdapter.ItemClickListener {

    private static final String LOG_TAG = PickSeatActivity.class.getSimpleName();
    private PickSeatViewModel viewModel;
    private PickSeatAdapter seatAdapter;

    private ArrayList<Integer> pickedSeatIds = new ArrayList<>();

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
        seatAdapter = new PickSeatAdapter(this, this);
        recyclerView.setAdapter(seatAdapter);

        // retrieve pickedSeatActivityIntent data
        Intent pickedSeatActivityIntent = getIntent();
//        int intentIntegerTripId = pickedSeatActivityIntent.getIntExtra("TRIP_ID", 0);
//        int intentIntegerPickPoint = pickedSeatActivityIntent.getIntExtra("TRIP_PICK_POINT", 0);
//        int intentIntegerDropPoint = pickedSeatActivityIntent.getIntExtra("TRIP_DROP_POINT", 0);
        int intentIntegerBusId = pickedSeatActivityIntent.getIntExtra("TRIP_BUS_ID", 0);

        // Set up view model
        PickSeatViewModelFactory factory = new PickSeatViewModelFactory(getApplication(), intentIntegerBusId);
        viewModel = ViewModelProviders.of(this, factory).get(PickSeatViewModel.class);

        // fetch seats to be displayed
        fetchSeats();

        Button button = findViewById(R.id.proceed_to_payment_btn);
        button.setOnClickListener(v -> {
            Intent payActivityIntent = new Intent(PickSeatActivity.this,
                    MpesaActivity.class);
            payActivityIntent.putIntegerArrayListExtra("PICKED_SEATS", pickedSeatIds);
            startActivity(payActivityIntent);
        });

    }

    private void fetchSeats() {
        viewModel.getSeats().observe(this, seatRowCollection ->
                seatAdapter.setSeatRowCollection(seatRowCollection));
    }

    @Override
    public void onItemClickListener() {
        pickedSeatIds = (ArrayList<Integer>) seatAdapter.getSelectedItems();
        Log.d(LOG_TAG, "Items: " +
                Arrays.toString(pickedSeatIds.toArray()));
    }
}
