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

public class AvailableBusActivity extends AppCompatActivity {

    private static final String LOG_TAG = AvailableBusActivity.class.getSimpleName();

    private String intentStringOrigin;
    private String intentStringDestination;
    private String intentStringDate;

    private RecyclerView recyclerView;
    private AvailableBusAdapter mAdapter;


    AvailableBusActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_available_bus);

        // Recyclerview
        recyclerView = findViewById(R.id.recyclerView_trips);
        recyclerView.setHasFixedSize(true);

        // LinearLayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Adapter
        mAdapter = new AvailableBusAdapter(this);
        recyclerView.setAdapter(mAdapter);

        // retrieve intent data
        Intent intent = getIntent();
        intentStringOrigin = intent.getStringExtra("TRIP_ORIGIN");
        intentStringDestination = intent.getStringExtra("TRIP_DESTINATION");
        intentStringDate = intent.getStringExtra("TRIP_DATE");

        // Set up view model
        viewModel = ViewModelProviders.of(this).get(AvailableBusActivityViewModel.class);

        // fetch trips
        fetchTrips();

//        // click listener for card
//        CardView cardView = findViewById(R.id.cardView);
//        cardView.setOnClickListener(view -> {
//            Intent intent1 = new Intent(getApplicationContext(), BookActivity.class);
//            startActivity(intent1);
//
//        });
    }

    private void fetchTrips() {
        if (intentStringOrigin != null &&
                intentStringDestination != null &&
                intentStringDate != null
        ) {
            viewModel.getTrips(intentStringOrigin, intentStringDestination, intentStringDate)
                    .observe(this, trips -> mAdapter.setTrips(trips));
        }
    }


}
