package com.example.voyage.ui.authentication;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.voyage.R;
import com.example.voyage.ui.searchbus.SearchBusActivity;
import com.example.voyage.auth.VoyageUser;
import com.example.voyage.util.FormValidators;

public class LoginActivity extends AppCompatActivity {

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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validForm;

                Editable email = emailEditText.getText();
                Editable password = passwordEditText.getText();

                validForm = isValidForm(email, password);

                if (validForm) {
                    assert email != null;
                    assert password != null;
                    loginUser(email.toString(), password.toString());
                }
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
        emailEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (FormValidators.isEmailValid(emailEditText.getText())) {
                    emailTextInput.setError(null);
                }
                return false;
            }
        });

        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (FormValidators.isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(null);
                }
                return false;
            }
        });
    }

    private Observer<VoyageUser> userObserver = new Observer<VoyageUser>() {
        @Override
        public void onChanged(@Nullable VoyageUser voyageUser) {
            Toast.makeText(LoginActivity.this, "Login Successful",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), SearchBusActivity.class);
            startActivity(intent);
        }
    };
}
