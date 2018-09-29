package com.arcfun.aifenx.ui;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

import com.arcfun.aifenx.data.GoodInfo;

import android.app.Activity;
import android.app.Application;

public class AFXApplication extends Application {
    private List<Activity> activities = new ArrayList<Activity>();
    private List<GoodInfo> mInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for (Activity activity : activities) {
            try {
                activity.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }

    public List<GoodInfo> getInfo() {
        return mInfo;
    }

    public void setInfo(List<GoodInfo> mInfo) {
        this.mInfo = mInfo;
    }

}