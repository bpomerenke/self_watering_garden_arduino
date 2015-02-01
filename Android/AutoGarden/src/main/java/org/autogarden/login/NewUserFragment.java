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
import org.autogarden.dto.User;
import org.autogarden.service.PostGsonRequest;
import org.autogarden.service.RequestQueueSingleton;
import org.autogarden.service.Service;

public class NewUserFragment extends Fragment {

    private NewUserFragmentListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.new_user_fragment, container, false);
        rootView.findViewById(R.id.new_user_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (NewUserFragmentListener) activity;
    }

    private void createAccount() {
        String userName = ((TextView) getView().findViewById(R.id.new_user_username)).getText().toString();
        User user = new User(userName, null);
        PostGsonRequest<Void> request = new PostGsonRequest<>(Service.URL + "user", user, Void.class,
                new Response.Listener<Void>() {
                    @Override
                    public void onResponse(Void response) {
                        Toast.makeText(getActivity(), "Account Created. Please Login", Toast.LENGTH_LONG).show();
                        listener.newUserCreated();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(NewUserFragment.class.getSimpleName(), "VolleyError " + error, error.getCause());
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueueSingleton.getInstance(getActivity()).add(request);
    }
}
