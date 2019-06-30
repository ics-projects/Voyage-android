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

import com.example.voyage.R;
import com.example.voyage.data.Constants;
import com.example.voyage.data.models.Seat;
import com.example.voyage.ui.pay.PayActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class PickSeatActivity extends AppCompatActivity implements PickSeatAdapter.ItemClickListener {

    private static final String LOG_TAG = PickSeatActivity.class.getSimpleName();

    private ArrayList<Integer> pickedSeatIds = new ArrayList<>();

    private PickSeatViewModel viewModel;
    private PickSeatAdapter seatAdapter;

    private int intentIntegerTripId;
    private int intentIntegerPickPoint;
    private int intentIntegerDropPoint;

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
        intentIntegerTripId = pickedSeatActivityIntent.getIntExtra(Constants.TRIP_ID_INTENT_EXTRA, 0);
        intentIntegerPickPoint = pickedSeatActivityIntent.getIntExtra(Constants.TRIP_PICK_POINT_INTENT_EXTRA, 0);
        intentIntegerDropPoint = pickedSeatActivityIntent.getIntExtra(Constants.TRIP_DROP_POINT_INTENT_EXTRA, 0);
        int intentIntegerBusId = pickedSeatActivityIntent.getIntExtra(Constants.TRIP_BUS_ID_INTENT_EXTRA, 0);

        // Set up view model
        PickSeatViewModelFactory factory = new PickSeatViewModelFactory(getApplication(), intentIntegerBusId);
        viewModel = ViewModelProviders.of(this, factory).get(PickSeatViewModel.class);

        // fetch seats to be displayed
        fetchSeats();

        Button button = findViewById(R.id.proceed_to_payment_btn);
        button.setOnClickListener(v -> payActivityIntent());

    }

    private void payActivityIntent() {
        Intent payActivityIntent = new Intent(PickSeatActivity.this,
                PayActivity.class);
        payActivityIntent.putIntegerArrayListExtra(Seat.SEAT_SEAT_IDS_INTENT_EXTRA, pickedSeatIds);
        payActivityIntent.putExtra(Constants.TRIP_ID_INTENT_EXTRA, intentIntegerTripId);
        payActivityIntent.putExtra(Constants.TRIP_PICK_POINT_INTENT_EXTRA, intentIntegerPickPoint);
        payActivityIntent.putExtra(Constants.TRIP_DROP_POINT_INTENT_EXTRA, intentIntegerDropPoint);

        viewModel.navigateToPay(intentIntegerPickPoint, intentIntegerDropPoint, intentIntegerTripId,
                pickedSeatIds).observe(this, payDetails -> {
            if (payDetails != null) {
                String payUrl = payDetails.getPayUrl();
                String originName = payDetails.getStages().get(0).getName();
                String destinationName = payDetails.getStages().get(1).getName();
                String tripName = originName.concat(" - ").concat(destinationName);
                String time = payDetails.getTime();
                Log.d(LOG_TAG, "Time:    " + time);
                int totalPrice = payDetails.getTotalPrice();

                payActivityIntent.putExtra(Constants.PAY_URL_INTENT_EXTRA, payUrl);
                payActivityIntent.putExtra(Constants.PAY_TRIP_NAME_INTENT_EXTRA, tripName);
                payActivityIntent.putExtra(Constants.PAY_DEPARTURE_TIME_INTENT_EXTRA, time);
                payActivityIntent.putExtra(Constants.PAY_TOTAL_PRICE_INTENT_EXTRA, totalPrice);
                startActivity(payActivityIntent);
            }
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
