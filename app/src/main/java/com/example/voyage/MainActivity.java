package com.example.voyage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.voyage.auth.VoyageAuth;
import com.example.voyage.fcm.VoyageMessagingService;
import com.example.voyage.ui.authentication.LoginActivity;
import com.example.voyage.ui.searchbus.SearchBusActivity;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        VoyageAuth auth = VoyageAuth.getInstance();

        // Create notification channel
        VoyageMessagingService.createNotificationChannel();

        if (auth.currentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), SearchBusActivity.class);
            startActivity(intent);
        }

        finish();
    }
}
