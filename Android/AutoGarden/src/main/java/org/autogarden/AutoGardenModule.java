package org.autogarden;

import android.content.Context;

import com.android.volley.RequestQueue;

import org.autogarden.login.LoginFragment;
import org.autogarden.model.SensorModel;
import org.autogarden.model.UserModel;
import org.autogarden.model.WorkingSensorModel;
import org.autogarden.sensor.EditSensorActivity;
import org.autogarden.sensor.SensorDetailActivity;
import org.autogarden.sensor.SensorListActivity;
import org.autogarden.service.RequestQueueSingleton;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                SensorListActivity.class,
                SensorDetailActivity.class,
                SensorModel.class,
                LoginFragment.class,
                EditSensorActivity.class,
        },
        addsTo = BaseModule.class,
        complete = false
)
public class AutoGardenModule {
    private Context applicationContext;

    public AutoGardenModule(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Provides
    public DateFormatter dateFormatter() {
        return new DateFormatter();
    }

    @Provides
    public Context applicationContext() {
        return applicationContext;
    }

    @Provides
    @Singleton
    public SensorModel deviceModel(UserModel userModel, RequestQueue requestQueue) {
        return new SensorModel(userModel, requestQueue);
    }

    @Provides
    @Singleton
    public UserModel userModel(RequestQueue requestQueue) {
        return new UserModel(requestQueue);
    }

    @Provides
    @Singleton
    public RequestQueue requestQueue(Context context) {
        return RequestQueueSingleton.getInstance(context);
    }

    @Provides
    @Singleton
    public WorkingSensorModel getWorkingSensorModel() {
        return new WorkingSensorModel();
    }

}
