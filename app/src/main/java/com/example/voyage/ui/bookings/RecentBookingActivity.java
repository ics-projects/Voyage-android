package com.example.voyage.ui.bookings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.voyage.R;
import com.example.voyage.data.models.RecentBookings;

import java.util.ArrayList;

public class RecentBookingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_booking);
        recyclerView = (RecyclerView) findViewById(R.id.recent_bookings_recycler_view);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        ArrayList<RecentBookings> myDataset = new ArrayList<>();
        myDataset.add(new RecentBookings("Nairobi", "Mombasa",
                "12/8/2018", "4000"));
        myDataset.add(new RecentBookings("Nairobi", "Mombasa",
                "12/8/2018", "4000"));
        myDataset.add(new RecentBookings("Nairobi", "Mombasa",
                "12/8/2018", "4000"));
        myDataset.add(new RecentBookings("Nairobi", "Mombasa",
                "12/8/2018", "4000"));
        myDataset.add(new RecentBookings("Nairobi", "Mombasa",
                "12/8/2018", "4000"));
        myDataset.add(new RecentBookings("Nairobi", "Mombasa",
                "12/8/2018", "4000"));

        mAdapter = new RecentBookingAdapter(myDataset, RecentBookingActivity.this);
        recyclerView.setAdapter(mAdapter);

    }
}
