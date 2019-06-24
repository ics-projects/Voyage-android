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

public class RegisterActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegisterActivity.class.getSimpleName();

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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

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
        firstNameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (FormValidators.isNameValid(firstNameEditText.getText())) {
                    firstNameTextInput.setError(null);
                }
                return false;
            }
        });

        lastNameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (FormValidators.isNameValid(lastNameEditText.getText())) {
                    lastNameTextInput.setError(null);
                }
                return false;
            }
        });

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

        passwordConfirmEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (FormValidators.isPasswordConfirmValid(
                        passwordEditText.getText(), passwordConfirmEditText.getText())) {
                    passwordConfirmTextInput.setError(null);
                }
                return false;
            }
        });
    }

    private Observer<VoyageUser> userObserver = new Observer<VoyageUser>() {
        @Override
        public void onChanged(@Nullable VoyageUser voyageUser) {
            Toast.makeText(RegisterActivity.this, "Registration Successful",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), SearchBusActivity.class);
            startActivity(intent);
        }
    };
}
