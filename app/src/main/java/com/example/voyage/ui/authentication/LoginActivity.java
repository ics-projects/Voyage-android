package com.example.voyage.ui.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.voyage.R;
import com.example.voyage.auth.VoyageUser;
import com.example.voyage.ui.searchbus.SearchBusActivity;
import com.example.voyage.util.FormValidators;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private TextInputEditText emailEditText;
    private TextInputLayout emailTextInput;

    private TextInputEditText passwordEditText;
    private TextInputLayout passwordTextInput;
    private LoginActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);

        emailTextInput = findViewById(R.id.emailAddress_text_input);
        emailEditText = findViewById(R.id.emailAddress_edit_text);

        passwordEditText = findViewById(R.id.password_edit_text);
        passwordTextInput = findViewById(R.id.password_text_input);

        viewModel = ViewModelProviders.of(this).get(LoginActivityViewModel.class);
        viewModel.getUser().observe(this, userObserver);

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            boolean validForm;

            Editable email = emailEditText.getText();
            Editable password = passwordEditText.getText();

            validForm = isValidForm(email, password);

            if (validForm) {
                assert email != null;
                assert password != null;
                loginUser(email.toString(), password.toString());
            }
        });

        addOnKeyListeners();
    }

    private void loginUser(String email, String password) {
        viewModel.loginUser(email, password);
    }

    private boolean isValidForm(Editable email, Editable password) {

        if (!FormValidators.isEmailValid(email)) {
            emailTextInput.setError(getString(R.string.error_email));
            return false;
        } else {
            emailTextInput.setError(null);
        }

        if (!FormValidators.isPasswordValid(password)) {
            passwordTextInput.setError(getString(R.string.error_password));
            return false;
        } else {
            passwordTextInput.setError(null);
        }

        return true;
    }

    private void addOnKeyListeners() {
        emailEditText.setOnKeyListener((v, keyCode, event) -> {
            if (FormValidators.isEmailValid(emailEditText.getText())) {
                emailTextInput.setError(null);
            }
            return false;
        });

        passwordEditText.setOnKeyListener((v, keyCode, event) -> {
            if (FormValidators.isPasswordValid(passwordEditText.getText())) {
                passwordTextInput.setError(null);
            }
            return false;
        });
    }

    private void generateFcmToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(LOG_TAG, "getInstanceId failed", task.getException());
                return;
            }

            // Get new Instance ID token
            String token = task.getResult().getToken();
//            PreferenceUtilities.saveFcmToken(this, token);
            Log.d(LOG_TAG, "token: " + token);
        });
    }

    private Observer<VoyageUser> userObserver = voyageUser -> {
//        generateFcmToken();

        Toast.makeText(LoginActivity.this, "Login Successful",
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), SearchBusActivity.class);
        startActivity(intent);

        finish();
    };
}
