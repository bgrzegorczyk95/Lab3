package com.example.lab3.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab3.R;
import com.example.lab3.ui.login.LoginViewModel;
import com.example.lab3.ui.login.LoginViewModelFactory;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "test@gmail.com:test123", "test2@gmail.com:test213",
    };

    private UserLogin userLogin = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                Boolean isValidUserName = validateEmail(usernameEditText);
                Boolean isValidPassword = validatePassword(passwordEditText);
                loginButton.setEnabled(isValidUserName && isValidPassword);
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin = new UserLogin(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                userLogin.execute((Void) null);
            }
        });
    }

    public boolean validatePassword(EditText password) {
        Boolean isValid = true;
        if (password.length() == 0) {
            password.setError("Pole wymagane");
            isValid = false;
        } else if (password.length() <= 2) {
            password.setError("Minimalna długośc hasła to 3 znaki");
            isValid = false;
        } else {
            isValid = true;
        }
        return isValid;
    }

    public boolean validateEmail(EditText email) {
        Boolean isValid = true;
        if (email.length() == 0) {
            email.setError("Pole wymagane");
            isValid = false;
        } else if (email.length() <= 2) {
            email.setError("Minimalna długośc maila to 3 znaki");
            isValid = false;
        } else {
            isValid = true;
        }
        return isValid;
    }

    public class UserLogin extends AsyncTask<Void, Void, Boolean> {
        private final String email;
        private final String password;

        UserLogin(String _email, String _password) {
            email = _email;
            password = _password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(email)) {
                    return pieces[1].equals(password);
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            userLogin = null;
            if (success) {
                Toast.makeText(getApplicationContext(), "Logowanie konta: "+ email, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Próba rejestracji: "+ email, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}