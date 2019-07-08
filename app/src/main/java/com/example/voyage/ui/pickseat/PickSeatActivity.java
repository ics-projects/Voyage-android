package com.example.voyage.ui.pickseat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voyage.R;
import com.example.voyage.data.Constants;
import com.example.voyage.data.models.Seat;
import com.example.voyage.ui.pay.PayActivity;
import com.example.voyage.ui.trips.TripsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class PickSeatActivity extends AppCompatActivity implements PickSeatAdapter.ItemClickListener {

    private static final String LOG_TAG = PickSeatActivity.class.getSimpleName();

    private ArrayList<Integer> pickedSeatIds = new ArrayList<>();

    private PickSeatViewModel viewModel;
    private PickSeatAdapter seatAdapter;

    private int intentIntegerTripId;
    private int intentIntegerPickPoint;
    private int intentIntegerDropPoint;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_pick_seat);

        //add tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText("Bus Seats");
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


        // initiate the progress bar
        progressBar = findViewById(R.id.pick_seat_activity_indeterminate_bar);

        // Recyclerview
        recyclerView = findViewById(R.id.recyclerView_seats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.GONE);

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
        String intentStringDate = pickedSeatActivityIntent.getStringExtra(Constants.TRIP_DATE_INTENT_EXTRA);
        int intentIntegerBusId = pickedSeatActivityIntent.getIntExtra(Constants.TRIP_BUS_ID_INTENT_EXTRA, 0);

        // Set up view model
        PickSeatViewModelFactory factory = new PickSeatViewModelFactory(intentIntegerBusId);
        viewModel = ViewModelProviders.of(this, factory).get(PickSeatViewModel.class);

        // fetch seats to be displayed
        fetchSeats();

        Button proceedToPaymentBtn = findViewById(R.id.proceed_to_payment_btn);
        proceedToPaymentBtn.setOnClickListener(v -> payActivityIntent());

        // back navigation
        Intent resultIntent = new Intent(this, TripsActivity.class);
        resultIntent.putExtra(Constants.TRIP_PICK_POINT_INTENT_EXTRA, String.valueOf(intentIntegerPickPoint));
        resultIntent.putExtra(Constants.TRIP_DROP_POINT_INTENT_EXTRA, String.valueOf(intentIntegerDropPoint));
        resultIntent.putExtra(Constants.TRIP_DATE_INTENT_EXTRA, intentStringDate);
        setResult(Activity.RESULT_OK, resultIntent);
        ImageView backButton = findViewById(R.id.book_seat_back_button);
        backButton.setOnClickListener(view -> finish());

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
        viewModel.getSeats().observe(this, seatRowCollection -> {
            if (seatRowCollection != null) {
                seatAdapter.setSeatRowCollection(seatRowCollection);
            }
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        });

    }

    @Override
    public void onItemClickListener() {
        pickedSeatIds = (ArrayList<Integer>) seatAdapter.getSelectedItems();
        Log.d(LOG_TAG, "Items: " +
                Arrays.toString(pickedSeatIds.toArray()));
    }
}
