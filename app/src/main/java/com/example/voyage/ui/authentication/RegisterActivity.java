package com.example.voyage.ui.authentication;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.example.voyage.R;
import com.example.voyage.auth.VoyageAuth;
import com.example.voyage.auth.VoyageUser;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

    VoyageAuth auth;

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

        auth = VoyageAuth.getInstance();

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
        auth.createUserWithCredentials(firstName, lastName, email, password, passwordConfirm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VoyageUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VoyageUser voyageUser) {
                        Log.d("User: " + voyageUser.token, LOG_TAG);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private boolean isValidForm(Editable firstName, Editable lastName, Editable email,
                                Editable password, Editable passwordConfirm) {

        if (!isNameValid(firstName)) {
            firstNameTextInput.setError(getString(R.string.error_name));
            return false;
        } else {
            firstNameTextInput.setError(null);
        }

        if (!isNameValid(lastName)) {
            lastNameTextInput.setError(getString(R.string.error_name));
            return false;
        } else {
            lastNameTextInput.setError(null);
        }

        if (!isEmailValid(email)) {
            emailTextInput.setError(getString(R.string.error_email));
            return false;
        } else {
            emailTextInput.setError(null);
        }

        if (!isPasswordValid(password)) {
            passwordTextInput.setError(getString(R.string.error_email));
            return false;
        } else {
            passwordTextInput.setError(null);
        }

        if (!isPasswordConfirmValid(passwordConfirm)) {
            passwordConfirmTextInput.setError(getString(R.string.error_email));
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
                if (isNameValid(firstNameEditText.getText())) {
                    firstNameTextInput.setError(null);
                }
                return false;
            }
        });

        lastNameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (isNameValid(lastNameEditText.getText())) {
                    lastNameTextInput.setError(null);
                }
                return false;
            }
        });

        emailEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (isEmailValid(emailEditText.getText())) {
                    emailTextInput.setError(null);
                }
                return false;
            }
        });

        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(null);
                }
                return false;
            }
        });

        passwordConfirmEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (isPasswordConfirmValid(passwordConfirmEditText.getText())) {
                    passwordConfirmTextInput.setError(null);
                }
                return false;
            }
        });
    }

    private boolean isNameValid(Editable text) {
        return false;
    }

    private boolean isEmailValid(Editable text) {
        return false;
    }

    private boolean isPasswordValid(Editable text) {
        return false;
    }

    private boolean isPasswordConfirmValid(Editable passwordConfirm) {
        return false;
    }
}
