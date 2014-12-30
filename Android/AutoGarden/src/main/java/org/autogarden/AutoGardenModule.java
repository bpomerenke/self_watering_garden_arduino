package org.autogarden;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                DeviceListActivity.class,
                DeviceDetailActivity.class
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
}
