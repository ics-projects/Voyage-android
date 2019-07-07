package com.example.voyage.ui.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText firstNameEditText;
    private TextInputLayout firstNameTextInput;

    private TextInputEditText lastNameEditText;
    private TextInputLayout lastNameTextInput;

    private TextInputEditText emailEditText;
    private TextInputLayout emailTextInput;

    private TextInputEditText passwordEditText;
    private TextInputLayout passwordTextInput;

    private TextInputEditText passwordConfirmEditText;
    private TextInputLayout passwordConfirmTextInput;

    private RegisterActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerButton = findViewById(R.id.register_button);
        Button cancelButton = findViewById(R.id.pay_page_cancel_button);

        firstNameEditText = findViewById(R.id.firstName_edit_text);
        firstNameTextInput = findViewById(R.id.firstName_text_input);

        lastNameEditText = findViewById(R.id.lastName_edit_text);
        lastNameTextInput = findViewById(R.id.lastName_text_input);

        emailTextInput = findViewById(R.id.emailAddress_text_input);
        emailEditText = findViewById(R.id.emailAddress_edit_text);

        passwordEditText = findViewById(R.id.password_edit_text);
        passwordTextInput = findViewById(R.id.password_text_input);

        passwordConfirmEditText = findViewById(R.id.passwordConfirm_edit_text);
        passwordConfirmTextInput = findViewById(R.id.passwordConfirm_text_input);

        viewModel = ViewModelProviders.of(this).get(RegisterActivityViewModel.class);
        viewModel.getUser().observe(this, userObserver);

        registerButton.setOnClickListener(v -> {
            boolean validForm;

            Editable firstName = firstNameEditText.getText();
            Editable lastName = lastNameEditText.getText();
            Editable email = emailEditText.getText();
            Editable password = passwordEditText.getText();
            Editable passwordConfirm = passwordConfirmEditText.getText();

            validForm = isValidForm(firstName, lastName, email, password, passwordConfirm);

            if (validForm) {
                assert firstName != null;
                assert lastName != null;
                assert email != null;
                assert password != null;
                assert passwordConfirm != null;
                registerUser(firstName.toString(), lastName.toString(), email.toString(),
                        password.toString(), passwordConfirm.toString());
            }
        });

        cancelButton.setOnClickListener(v -> finish());

        addOnKeyListeners();
    }

    private void registerUser(String firstName, String lastName, String email, String password,
                              String passwordConfirm) {
        viewModel.registerUser(firstName, lastName, email, password, passwordConfirm);
    }

    private boolean isValidForm(Editable firstName, Editable lastName, Editable email,
                                Editable password, Editable passwordConfirm) {

        if (!FormValidators.isNameValid(firstName)) {
            firstNameTextInput.setError(getString(R.string.error_name));
            return false;
        } else {
            firstNameTextInput.setError(null);
        }

        if (!FormValidators.isNameValid(lastName)) {
            lastNameTextInput.setError(getString(R.string.error_name));
            return false;
        } else {
            lastNameTextInput.setError(null);
        }

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

        if (!FormValidators.isPasswordConfirmValid(password, passwordConfirm)) {
            passwordConfirmTextInput.setError(getString(R.string.error_password_confirm));
            return false;
        } else {
            passwordConfirmTextInput.setError(null);
        }

        return true;
    }

    private void addOnKeyListeners() {
        firstNameEditText.setOnKeyListener((v, keyCode, event) -> {
            if (FormValidators.isNameValid(firstNameEditText.getText())) {
                firstNameTextInput.setError(null);
            }
            return false;
        });

        lastNameEditText.setOnKeyListener((v, keyCode, event) -> {
            if (FormValidators.isNameValid(lastNameEditText.getText())) {
                lastNameTextInput.setError(null);
            }
            return false;
        });

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

        passwordConfirmEditText.setOnKeyListener((v, keyCode, event) -> {
            if (FormValidators.isPasswordConfirmValid(
                    passwordEditText.getText(), passwordConfirmEditText.getText())) {
                passwordConfirmTextInput.setError(null);
            }
            return false;
        });
    }

    private Observer<VoyageUser> userObserver = voyageUser -> {
        Toast.makeText(RegisterActivity.this, "Registration Successful",
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), SearchBusActivity.class);
        startActivity(intent);

        finish();
    };
}
