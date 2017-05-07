package com.app.infideap.instaclone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements
        SignupFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener {

    private static final Object LOGIN = 1;
    private static final Object SIGNUP = 2;
    private Fragment fragment;
    private String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextView textView = (TextView) findViewById(R.id.textView_cert_action);
        textView.setTag(LOGIN);

        View view = findViewById(R.id.layout_cert_action);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView.getTag().equals(LOGIN)) {
                    textView.setText(R.string.signupcaption);
                    textView.setTag(SIGNUP);
                    fragment = LoginFragment.newInstance(null, null);
                    displayFragment(R.id.container_login, fragment);

                } else {
                    textView.setText(R.string.logincaption);
                    textView.setTag(LOGIN);
                    fragment = SignupFragment.newInstance(null, null);
                    displayFragment(R.id.container_login, fragment);
                }

                Log.e(TAG, "Equal : "+(fragment instanceof SignupFragment));

            }
        });
        fragment = SignupFragment.newInstance(null, null);
        displayFragment(R.id.container_login, fragment);
    }

    private void displayFragment(int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(id, fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLogInSuccss(View v) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    /**
     * Method need to trigger after sign up process complete.
     */
    @Override
    public void onSignUpSuccess() {
        fragment = LoginFragment.newInstance(null,null);
        displayFragment(R.id.container_login, fragment);
    }

    @Override
    public void onBackPressed() {
        Log.e(TAG, "Equal : "+(fragment instanceof SignupFragment));
        if (fragment instanceof SignupFragment) {
            SignupFragment signupFragment = (SignupFragment) fragment;
            if (signupFragment.canGoBack()) {
                signupFragment.back();
                return;
            }
        }
        super.onBackPressed();
    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
}
