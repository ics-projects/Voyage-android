package com.example.voyage.ui.trips;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.example.voyage.R;
import com.example.voyage.ui.pickseat.PickSeatActivity;

public class AvailableBusActivity extends AppCompatActivity implements AvailableBusAdapter.ItemClickListener {

    private static final String LOG_TAG = AvailableBusActivity.class.getSimpleName();

    private String intentStringOrigin;
    private String intentStringDestination;
    private String intentStringDate;

    private AvailableBusAdapter busAdapter;


    AvailableBusActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_available_bus);

        // Recyclerview
        RecyclerView recyclerView = findViewById(R.id.recyclerView_trips);
        recyclerView.setHasFixedSize(true);

        // LinearLayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Adapter
        busAdapter = new AvailableBusAdapter(this, this);
        recyclerView.setAdapter(busAdapter);

        // retrieve intent data
        Intent intent = getIntent();
        intentStringOrigin = intent.getStringExtra("TRIP_ORIGIN");
        intentStringDestination = intent.getStringExtra("TRIP_DESTINATION");
        intentStringDate = intent.getStringExtra("TRIP_DATE");

        // Set up view model
        viewModel = ViewModelProviders.of(this).get(AvailableBusActivityViewModel.class);

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
        Intent intent = new Intent(AvailableBusActivity.this, PickSeatActivity.class);
        intent.putExtra("TRIP_ID", tripId);
        intent.putExtra("TRIP_PICK_POINT", pickPoint);
        intent.putExtra("TRIP_DROP_POINT", dropPoint);
        intent.putExtra("TRIP_BUS_ID", busId);
        startActivity(intent);
    }
}
