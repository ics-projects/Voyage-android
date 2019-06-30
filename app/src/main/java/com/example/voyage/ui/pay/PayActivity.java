package com.example.voyage.ui.pay;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.widget.Button;
import android.widget.TextView;

import com.example.voyage.R;
import com.example.voyage.data.models.Seat;
import com.example.voyage.data.models.Trip;
import com.example.voyage.ui.bookings.RecentBookingActivity;
import com.example.voyage.util.FormValidators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PayActivity extends AppCompatActivity {
    public static final String PAY_URL_INTENT_EXTRA = "PAY_URL";
    public static final String PAY_TRIP_NAME_INTENT_EXTRA = "PAY_TRIP_NAME";
    public static final String PAY_DEPARTURE_TIME_INTENT_EXTRA = "PAY_DEPARTURE_TIME";
    public static final String PAY_TOTAL_PRICE_INTENT_EXTRA = "PAY_TOTAL_PRICE";

    private TextInputLayout phoneNumberTextInput;
    private TextInputEditText phoneNumberEditText;

    private PayViewModel viewModel;

    private int intentIntegerTripId;
    private int intentIntegerPickPoint;
    private int intentIntegerDropPoint;
    private String intentPayUrl;

    private ArrayList<Integer> intentSeatIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa);

        TextView tripNameTextView = findViewById(R.id.trip_name_tv);
        TextView departureTimeTextView = findViewById(R.id.departure_time_tv);
        TextView totalPriceTextView = findViewById(R.id.total_price_tv);
        Button pay = findViewById(R.id.pay_button);
        phoneNumberTextInput = findViewById(R.id.phone_number_text_input);
        phoneNumberEditText = findViewById(R.id.phone_number_edit_Text);

        // retrieve PActivityIntent data
        Intent payActivityIntent = getIntent();
        intentIntegerTripId = payActivityIntent.getIntExtra(Trip.TRIP_ID_INTENT_EXTRA, 0);
        intentIntegerPickPoint = payActivityIntent.getIntExtra(Trip.TRIP_PICK_POINT_INTENT_EXTRA, 0);
        intentIntegerDropPoint = payActivityIntent.getIntExtra(Trip.TRIP_DROP_POINT_INTENT_EXTRA, 0);
        intentSeatIds = payActivityIntent.getIntegerArrayListExtra(Seat.SEAT_SEAT_IDS_INTENT_EXTRA);
        intentPayUrl = payActivityIntent.getStringExtra(PayActivity.PAY_URL_INTENT_EXTRA);
        String intentTripName = payActivityIntent.getStringExtra(PayActivity.PAY_TRIP_NAME_INTENT_EXTRA);
        String intentDepartureTime = payActivityIntent.getStringExtra(PayActivity.PAY_DEPARTURE_TIME_INTENT_EXTRA);
        int intentTotalPrice = payActivityIntent.getIntExtra(PayActivity.PAY_TOTAL_PRICE_INTENT_EXTRA, 0);

        viewModel = ViewModelProviders.of(this).get(PayViewModel.class);
        viewModel.payRequestStatus().observe(this, payRequestObserver);

        tripNameTextView.setText(intentTripName);
        setDepartureTimeTextView(departureTimeTextView, intentDepartureTime);
        totalPriceTextView.setText(String.valueOf(intentTotalPrice));

        pay.setOnClickListener(view -> {
            boolean validForm;
            Editable phoneNumber = phoneNumberEditText.getText();

            validForm = isValidForm(phoneNumber);

            if (validForm) {
                assert phoneNumber != null;
                String formattedPhoneNo = "254".concat(phoneNumber.toString());
                paymentRequest(intentPayUrl, formattedPhoneNo, intentIntegerTripId,
                        intentIntegerPickPoint, intentIntegerDropPoint, intentSeatIds);
            }
        });
    }

    private void setDepartureTimeTextView(TextView departureTimeTextView, String intentDepartureTime) {
        SimpleDateFormat originalTimeFormat =
                new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH);
        SimpleDateFormat screenTimeFormat = new SimpleDateFormat("yyyy-MM-dd 'at' h:mm a", Locale.ENGLISH);
        try {
            Date formattedDepartureTime = originalTimeFormat.parse(intentDepartureTime);
            String finalFormatDeparture = screenTimeFormat.format(formattedDepartureTime);
            departureTimeTextView.setText(finalFormatDeparture);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Observer<Integer> payRequestObserver = status -> {
        assert status != null;
        if (status != -1) {
            Intent intent = new Intent(getApplicationContext(), RecentBookingActivity.class);
            startActivity(intent);
        }
    };

    private void paymentRequest(String url, String phoneNumber, int tripId,
                                int pickPoint, int dropPoint, ArrayList<Integer> intentSeatIds) {
        viewModel.pay(url, phoneNumber, tripId, pickPoint, dropPoint, intentSeatIds);
    }

    private boolean isValidForm(Editable phoneNumber) {
        if (!FormValidators.isPhoneNumberValid(phoneNumber)) {
            phoneNumberTextInput.setError("invalid phone number");
            return false;
        } else {
            phoneNumberTextInput.setError(null);
        }

        return true;
    }
}
