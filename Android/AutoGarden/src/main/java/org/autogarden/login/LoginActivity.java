package org.autogarden.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import org.autogarden.R;
import org.autogarden.sensor.SensorListActivity;

public class LoginActivity extends ActionBarActivity implements LoginFragmentListener, NewUserFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction = fragmentTransaction.add(R.id.container, new LoginFragment());
            fragmentTransaction.commit();
        }
    }

    @Override
    public void loginSuccessful() {
        startActivity(new Intent(this, SensorListActivity.class));
    }

    @Override
    public void showNewUserFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction = fragmentTransaction.replace(R.id.container, new NewUserFragment());
        fragmentTransaction = fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void newUserCreated() {
        onBackPressed();
    }
}