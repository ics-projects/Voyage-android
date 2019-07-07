package com.example.voyage.ui.pay;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.example.voyage.R;
import com.example.voyage.data.Constants;
import com.example.voyage.data.models.Seat;
import com.example.voyage.ui.bookings.RecentBookingActivity;
import com.example.voyage.util.FormValidators;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PayActivity extends AppCompatActivity {

    private TextInputLayout phoneNumberTextInput;
    private TextInputEditText phoneNumberEditText;

    private PayViewModel viewModel;

    private String intentPayUrl;
    private String intentTripName;
    private String intentDepartureTime;
    private int intentIntegerTripId;
    private int intentIntegerPickPoint;
    private int intentIntegerDropPoint;
    private int intentTotalPrice;

    private ArrayList<Integer> intentSeatIds;

    private View.OnClickListener navigationOnClickListener = (view) -> cancelAlertDialog();

    private ProgressBar progressBar;
    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_pay);

        // initiate the progress bar
        progressBar = findViewById(R.id.pay_activity_indeterminate_bar);
        progressBar.setVisibility(View.GONE);

        // main layout
        mainLayout = findViewById(R.id.main_layout);

        // initialise views
        ImageView backButton = findViewById(R.id.pay_page_back_button);
        Button cancelButton = findViewById(R.id.pay_page_cancel_button);
        TextView tripNameTextView = findViewById(R.id.trip_name_tv);
        TextView departureTimeTextView = findViewById(R.id.departure_time_tv);
        TextView totalPriceTextView = findViewById(R.id.total_price_tv);
        Button pay = findViewById(R.id.pay_page_pay_button);
        phoneNumberTextInput = findViewById(R.id.phone_number_text_input);
        phoneNumberEditText = findViewById(R.id.phone_number_edit_Text);

        // retrieve activityIntent data
        retrieveIntentData();

        // initialise view model
        viewModel = ViewModelProviders.of(this).get(PayViewModel.class);

        // set screen data
        tripNameTextView.setText(intentTripName);
        setDepartureTimeTextView(departureTimeTextView, intentDepartureTime);
        totalPriceTextView.setText(String.valueOf(intentTotalPrice));

        // On click listeners
        backButton.setOnClickListener(navigationOnClickListener);
        cancelButton.setOnClickListener(navigationOnClickListener);

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

    private void retrieveIntentData() {
        Intent payActivityIntent = getIntent();
        intentIntegerTripId = payActivityIntent.getIntExtra(Constants.TRIP_ID_INTENT_EXTRA, 0);
        intentIntegerPickPoint = payActivityIntent.getIntExtra(Constants.TRIP_PICK_POINT_INTENT_EXTRA, 0);
        intentIntegerDropPoint = payActivityIntent.getIntExtra(Constants.TRIP_DROP_POINT_INTENT_EXTRA, 0);
        intentSeatIds = payActivityIntent.getIntegerArrayListExtra(Seat.SEAT_SEAT_IDS_INTENT_EXTRA);
        intentPayUrl = payActivityIntent.getStringExtra(Constants.PAY_URL_INTENT_EXTRA);
        intentTripName = payActivityIntent.getStringExtra(Constants.PAY_TRIP_NAME_INTENT_EXTRA);
        intentDepartureTime = payActivityIntent.getStringExtra(Constants.PAY_DEPARTURE_TIME_INTENT_EXTRA);
        intentTotalPrice = payActivityIntent.getIntExtra(Constants.PAY_TOTAL_PRICE_INTENT_EXTRA, 0);
    }

    @Override
    public void onBackPressed() {
        cancelAlertDialog();
    }

    private void cancelAlertDialog() {
        new AlertDialog.Builder(PayActivity.this)
                .setTitle(getString(R.string.cancel_payment_dialog_title))
                .setMessage(getString(R.string.cancel_payment_confirmation_dialog))
                .setPositiveButton(android.R.string.yes, (dialog, which) ->
                        NavUtils.navigateUpFromSameTask(PayActivity.this))
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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

    private void paymentRequest(String url, String phoneNumber, int tripId,
                                int pickPoint, int dropPoint, ArrayList<Integer> intentSeatIds) {

        mainLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        LiveData<Integer> status =
                viewModel.pay(url, phoneNumber, tripId, pickPoint, dropPoint, intentSeatIds);
        status.removeObservers(this);

        status.observe(this, statusInteger -> {
            if (statusInteger != null) {
                if (statusInteger != -1) {
                    Intent intent = new Intent(getApplicationContext(), RecentBookingActivity.class);
                    startActivity(intent);
                    finish();
                    progressBar.setVisibility(View.GONE);
                } else {
                    mainLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
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
