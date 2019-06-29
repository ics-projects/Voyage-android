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

import com.example.voyage.R;
import com.example.voyage.data.models.Seat;
import com.example.voyage.data.models.Trip;
import com.example.voyage.ui.bookings.RecentBookingActivity;
import com.example.voyage.util.FormValidators;

import java.util.ArrayList;

public class PayActivity extends AppCompatActivity {
    public static final String PAY_URL_INTENT_EXTRA = "PAY_URL";

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

        phoneNumberTextInput = findViewById(R.id.phone_number_text_input);
        phoneNumberEditText = findViewById(R.id.phone_number_edit_Text);

        // retrieve PActivityIntent data
        Intent payActivityIntent = getIntent();
        intentIntegerTripId = payActivityIntent.getIntExtra(Trip.TRIP_ID_INTENT_EXTRA, 0);
        intentIntegerPickPoint = payActivityIntent.getIntExtra(Trip.TRIP_PICK_POINT_INTENT_EXTRA, 0);
        intentIntegerDropPoint = payActivityIntent.getIntExtra(Trip.TRIP_DROP_POINT_INTENT_EXTRA, 0);
        intentSeatIds = payActivityIntent.getIntegerArrayListExtra(Seat.SEAT_SEAT_IDS_INTENT_EXTRA);
        intentPayUrl = payActivityIntent.getStringExtra(PayActivity.PAY_URL_INTENT_EXTRA);

        viewModel = ViewModelProviders.of(this).get(PayViewModel.class);
        viewModel.payRequestStatus().observe(this, payRequestObserver);

        Button pay = findViewById(R.id.pay_button);
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
