package com.example.voyage.ui.searchbus;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.voyage.R;
import com.example.voyage.data.Constants;
import com.example.voyage.data.models.Schedule;
import com.example.voyage.ui.trips.TripsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class SearchBusActivity extends AppCompatActivity {

    private EditText dateEditText;
    final private Calendar c = Calendar.getInstance();
    private DatePickerListener datePickerListener = new DatePickerListener();

    private SearchBusActivityViewModel viewModel;
    private List<Schedule> schedules;

    private Spinner originSpinner;
    private Spinner destinationSpinner;
    private ProgressBar progressBar;
    private ScrollView scrollView;
    private Button searchBuses;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_search);

        // initiate the progress bar
        progressBar = findViewById(R.id.search_activity_indeterminate_bar);

        scrollView = findViewById(R.id.screen_scroll_view);
//        scrollView.setVisibility(View.GONE);

        searchBuses = findViewById(R.id.search_buses);
        originSpinner = findViewById(R.id.originSpinner);
        destinationSpinner = findViewById(R.id.destinationSpinner);
        dateEditText = findViewById(R.id.select_date);

        viewModel = ViewModelProviders.of(this).get(SearchBusActivityViewModel.class);

        fetchSchedules();

        dateEditText.setOnClickListener(view -> {
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(SearchBusActivity.this, datePickerListener, year, month, day).show();
        });

        searchBuses.setEnabled(false);
        searchBuses.setOnClickListener(view -> {
            String origin = (String) originSpinner.getSelectedItem();
            String destination = (String) destinationSpinner.getSelectedItem();

            String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                    .format(c.getTime());

            if (selectedDate != null) {
                Intent intent = new Intent(this, TripsActivity.class);
                intent.putExtra(Constants.TRIP_PICK_POINT_INTENT_EXTRA, origin);
                intent.putExtra(Constants.TRIP_DROP_POINT_INTENT_EXTRA, destination);
                intent.putExtra(Constants.TRIP_DATE_INTENT_EXTRA, selectedDate);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerNetworkCallback();
    }

    @Override
    protected void onPause() {
        super.onPause();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    private void fetchSchedules() {
        viewModel.getSchedules().observe(this, returnedSchedules -> {
            if (returnedSchedules != null) {
                schedules = returnedSchedules;
                setSpinnerData();
                searchBuses.setEnabled(true);
            } else {
                searchBuses.setEnabled(false);
            }
            scrollView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        });
    }

    private void setSpinnerData() {
        ArrayList<String> originSpinnerItems = new ArrayList<>();
        ArrayList<String> destinationSpinnerItems = new ArrayList<>();

        for (Schedule schedule : schedules) {
            originSpinnerItems.add(schedule.getOrigin());
            destinationSpinnerItems.add(schedule.getDestination());
        }

        ArrayAdapter<String> originSpinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, originSpinnerItems);
        originSpinner.setAdapter(originSpinnerAdapter);

        ArrayAdapter<String> destinationSpinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, destinationSpinnerItems);
        destinationSpinner.setAdapter(destinationSpinnerAdapter);
    }

    private class DatePickerListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String selectedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(c.getTime());

            dateEditText.setText(selectedDate);
        }
    }

    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        private Handler handler = new Handler();

        @Override
        public void onAvailable(Network network) {
            handler.post(() -> {
                scrollView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                fetchSchedules();
            });
        }

        @Override
        public void onLost(Network network) {
            handler.post(() -> {
                Toast.makeText(
                        SearchBusActivity.this,
                        "Network connectivity lost",
                        Toast.LENGTH_LONG).show();
                searchBuses.setEnabled(false);
            });
        }
    };

    private void registerNetworkCallback() {
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }
}
