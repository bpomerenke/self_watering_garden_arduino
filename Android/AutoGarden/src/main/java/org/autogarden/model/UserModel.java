package org.autogarden.model;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.autogarden.dto.User;
import org.autogarden.service.PostGsonRequest;
import org.autogarden.service.Service;

import javax.inject.Inject;

public class UserModel {
    private String userName;
    private User user;
    private RequestQueue requestQueue;

    @Inject
    public UserModel(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void fetchUser(final ModelCallback<User> callback) {
        if (user == null) {
            PostGsonRequest<User> request = new PostGsonRequest<>(Service.URL + "user?userName=" + userName, null, User.class,
                    new Response.Listener<User>() {
                        @Override
                        public void onResponse(User response) {
                            callback.success(response);
                            user = response;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(UserModel.class.getSimpleName(), "VolleyError " + error, error.getCause());
                            callback.fail();
                        }
                    });
            requestQueue.add(request);
        } else {
            callback.success(user);
        }
    }
}
