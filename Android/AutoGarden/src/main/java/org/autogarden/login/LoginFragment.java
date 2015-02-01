package org.autogarden.login;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.autogarden.R;
import org.autogarden.dto.AuthenticationResponse;
import org.autogarden.dto.User;
import org.autogarden.service.PostGsonRequest;
import org.autogarden.service.RequestQueueSingleton;
import org.autogarden.service.Service;
import org.autogarden.service.TokenManager;

import static com.android.volley.Response.ErrorListener;

public class LoginFragment extends Fragment {
    private LoginFragmentListener loginFragmentListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.login_fragment, container, false);
        rootView.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        rootView.findViewById(R.id.login_create_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFragmentListener.showNewUserFragment();
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        loginFragmentListener = (LoginFragmentListener) activity;
    }

    private void login() {
        String userName = ((TextView) getView().findViewById(R.id.login_username)).getText().toString();
        User user = new User(userName, null);
        PostGsonRequest<AuthenticationResponse> request = new PostGsonRequest<AuthenticationResponse>(Service.URL + "user/authenticate", user, AuthenticationResponse.class,
                new Response.Listener<AuthenticationResponse>() {
                    @Override
                    public void onResponse(AuthenticationResponse response) {
                        TokenManager.getInstance().setToken(response.getToken());
                        loginFragmentListener.loginSuccessful();
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LoginFragment.class.getSimpleName(), "VolleyError " + error, error.getCause());
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
        };
        RequestQueueSingleton.getInstance(getActivity()).add(request);
    }
}