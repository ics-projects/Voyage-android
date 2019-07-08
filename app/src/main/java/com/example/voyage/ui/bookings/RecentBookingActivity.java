package com.example.voyage.ui.bookings;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voyage.R;
import com.example.voyage.data.models.Booking;

import java.util.List;
import java.util.Objects;

public class RecentBookingActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private RecentBookingAdapter adapter;
    private ProgressBar progressBar;
    private TextView noBookingsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recent_booking);


        //add tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        mTitle.setText("Recent Trips");

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // initiate the progress bar
        progressBar = findViewById(R.id.bookings_activity_indeterminate_bar);

        // text view to show when no data is available
        noBookingsTextView = findViewById(R.id.no_bookings_text_view);
        noBookingsTextView.setVisibility(View.GONE);

        // back navigation
        ImageView backButton = findViewById(R.id.bookings_back_button);
        backButton.setOnClickListener(view -> NavUtils.navigateUpFromSameTask(this));

        // recycler view
        recyclerView = findViewById(R.id.recent_bookings_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.GONE);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // initialise adapter
        adapter = new RecentBookingAdapter(this);
        recyclerView.setAdapter(adapter);

        // initialise view model
        RecentBookingViewModel viewModel = ViewModelProviders.of(this).get(RecentBookingViewModel.class);
        viewModel.getBookings().observe(this, bookingsObserver);

    }

    private Observer<List<Booking>> bookingsObserver = bookings -> {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        if (bookings != null) {
            adapter.setData(bookings);
            if (bookings.size() == 0) {
                noBookingsTextView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                noBookingsTextView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }

        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    };
}
