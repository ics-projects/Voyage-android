package com.example.voyage.ui.trips;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.voyage.R;
import com.example.voyage.data.Constants;
import com.example.voyage.ui.pickseat.PickSeatActivity;

public class TripsActivity extends AppCompatActivity implements TripsAdapter.ItemClickListener {

    private static final String LOG_TAG = TripsActivity.class.getSimpleName();

    private String intentStringOrigin;
    private String intentStringDestination;
    private String intentStringDate;

    private TripsAdapter busAdapter;


    TripsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_available_bus);

        ImageView backButton = findViewById(R.id.back_button);

        // Recyclerview
        RecyclerView recyclerView = findViewById(R.id.recyclerView_trips);
        recyclerView.setHasFixedSize(true);

        // LinearLayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // back navigation
        backButton.setOnClickListener(view -> NavUtils.navigateUpFromSameTask(this));

        // Adapter
        busAdapter = new TripsAdapter(this, this);
        recyclerView.setAdapter(busAdapter);

        // retrieve intent data
        Intent intent = getIntent();
        intentStringOrigin = intent.getStringExtra(Constants.TRIP_PICK_POINT_INTENT_EXTRA);
        intentStringDestination = intent.getStringExtra(Constants.TRIP_DROP_POINT_INTENT_EXTRA);
        intentStringDate = intent.getStringExtra(Constants.TRIP_DATE_INTENT_EXTRA);

        // Set up view model
        viewModel = ViewModelProviders.of(this).get(TripsViewModel.class);

        // fetch trips
        fetchTrips();
    }

    private void fetchTrips() {
        if (intentStringOrigin != null &&
                intentStringDestination != null &&
                intentStringDate != null
        ) {
            viewModel.getTrips(intentStringOrigin, intentStringDestination, intentStringDate)
                    .observe(this, trips -> busAdapter.setTrips(trips));
        }
    }

    @Override
    public void onItemClickListener(int tripId, int pickPoint, int dropPoint, int busId) {
        // Launch PickSeatActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(TripsActivity.this, PickSeatActivity.class);
        intent.putExtra(Constants.TRIP_ID_INTENT_EXTRA, tripId);
        intent.putExtra(Constants.TRIP_PICK_POINT_INTENT_EXTRA, pickPoint);
        intent.putExtra(Constants.TRIP_DROP_POINT_INTENT_EXTRA, dropPoint);
        intent.putExtra(Constants.TRIP_BUS_ID_INTENT_EXTRA, busId);
        startActivity(intent);
    }
}
