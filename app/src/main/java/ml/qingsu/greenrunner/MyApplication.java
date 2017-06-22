package ml.qingsu.greenrunner;

import android.app.Application;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;

import im.fir.sdk.FIR;

/**
 * Created by w568w on 17-6-19.
 */
public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;//存储引用
        CaocConfig.Builder.create()
                .errorDrawable(R.drawable.doge_lv)
                .showErrorDetails(true)
                .apply();
        FIR.init(this);
    }

    public static MyApplication getInstance(){
        return instance;
    }
}
