package ml.qingsu.greenrunner;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.setupwizardlib.SetupWizardLayout;
import com.android.setupwizardlib.view.NavigationBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trumeet on 2017/6/21.
 *
 * @author Trumeet
 */

public class ProgressActivity extends AppCompatActivity {
    private static final String TAG = ProgressActivity.class.getSimpleName();

    public static final String EXTRA_APP = ProgressActivity.class
            .getName() + ".EXTRA_APP";

    private CheckTask mTask;
    private SetupWizardLayout layout;

    private AppInfo info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = getIntent().getParcelableExtra(EXTRA_APP);
        if (info == null) {
            finish();
            return;
        }
        layout = new SetupWizardLayout(this);
        layout.setHeaderText(R.string.step3);
        layout.getNavigationBar().setVisibility(View.GONE);
        setContentView(Utils.initWizard(layout));
        mTask = new CheckTask();
        mTask.execute(info);
    }

    @Override
    public void onDestroy() {
        if (mTask != null && !mTask.isCancelled()) {
            mTask.cancel(true);
        }
        super.onDestroy();
    }

    private void setResultAndFinish(AppResult result) {
        startActivity(new Intent(ProgressActivity.this,
                ResultActivity.class)
                .putExtra(ResultActivity.EXTRA_APP, info)
                .putExtra(ResultActivity.EXTRA_RESULT, result));
        finish();
    }

    private class CheckTask extends AsyncTask<AppInfo,
            Void, AppResult> {


        @Override
        protected void onPreExecute() {
            layout.setProgressBarShown(true);
        }

        @Override
        protected void onPostExecute(AppResult result) {
            layout.setProgressBarShown(false);
            setResultAndFinish(result);
        }

        @Override
        protected AppResult doInBackground(AppInfo... params) {
            AppInfo info = params[0];
            AppResult result = new AppResult();
            int good = result.getGood();

            try {
                PackageInfo packageInfo = getPackageManager()
                        .getPackageInfo(info.getPackageName()
                                , PackageManager.GET_PERMISSIONS);
                if (!contains(packageInfo.requestedPermissions
                        , Manifest.permission.READ_PHONE_STATE) && !contains(packageInfo.requestedPermissions
                        , Manifest.permission.READ_EXTERNAL_STORAGE) && !contains(packageInfo.requestedPermissions
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    good++;
                    result.setPrivacyPermission(AppResult.STATUS_PASS);
                } else {
                    result.setPrivacyPermission(AppResult.STATUS_NOT_PASS);
                }

                if (packageInfo.applicationInfo.targetSdkVersion >= 26) {
                    good++;
                    result.setTargetNougat(AppResult.STATUS_PASS);
                } else {
                    result.setTargetNougat(AppResult.STATUS_NOT_PASS);
                }

                ArrayList<String> al = getActivities(info.getPackageName());
                if (contains(al, "Ad") || contains(al, "AD") || contains(al, "ad") || contains(al, "Mob")) {
                    result.setAd(AppResult.STATUS_NOT_PASS);
                } else if (contain(packageInfo.services, "AD")
                        || contain(packageInfo.services, "Ad")
                        || contain(packageInfo.services, "Mob")
                        || contain(packageInfo.services, "ad")) {
                    result.setAd(AppResult.STATUS_NOT_PASS);
                } else {
                    result.setAd(AppResult.STATUS_PASS);
                    good++;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                result.setPrivacyPermission(AppResult.STATUS_FAIL);
                result.setTargetNougat(AppResult.STATUS_FAIL);
                result.setAd(AppResult.STATUS_FAIL);
            }

            if (isRecevier("android.net.conn.CONNECTIVITY_CHANGE", info.getPackageName()) ||
                    isRecevier("android.hardware.action.NEW_PICTURE", info.getPackageName()) ||
                    isRecevier("android.hardware.action.NEW_VIDEO", info.getPackageName()) ||
                    isRecevier("android.net.wifi.SCAN_RESULTS", info.getPackageName()) ||
                    isRecevier("android.intent.action.USER_PRESENT", info.getPackageName()) ||
                    isRecevier("android.intent.action.ACTION_POWER_DISCONNECTED", info.getPackageName())) {
                result.setSpecialReceiver(AppResult.STATUS_NOT_PASS);
            } else {
                good++;
                result.setSpecialReceiver(AppResult.STATUS_PASS);
            }

            // TODO: getRunningServices had already deprecated after 5.0
            // Above 5.0,it will always be PASS.
            ActivityManager mActivityManager = (ActivityManager) MyApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> a = mActivityManager.getRunningServices(Integer.MAX_VALUE);
            if (!contain(a, info.getPackageName())) {
                good++;
                result.setBackgroundLimit(AppResult.STATUS_PASS);
            } else {
                result.setBackgroundLimit(AppResult.STATUS_NOT_PASS);
            }

            if (ShellUtils.isRoot()) {
                ShellUtils.CommandResult cr = ShellUtils.execCommand("dumpsys alarm", true, true);
                if (cr.result == 0) {
                    String top = String_Between(cr.successMsg, "Top Alarms:", "Alarm Stats:");
                    if (!top.contains(info.getPackageName())) {
                        good++;
                        result.setAlarmLimit(AppResult.STATUS_PASS);
                    } else {
                        result.setAlarmLimit(AppResult.STATUS_NOT_PASS);
                    }
                } else {
                    Log.w(TAG, "Result -> " + cr.result);
                    Log.w(TAG, "Success -> " + cr.successMsg);
                    Log.w(TAG, "Error -> " + cr.errorMsg);
                    good++;
                    result.setAlarmLimit(AppResult.STATUS_FAIL);
                }
            } else {
                Log.w(TAG, "No root");
                good++;
                result.setAlarmLimit(AppResult.STATUS_FAIL);
            }

            try {
                System.out.println("start fuck");
                ApkUtils au = new ApkUtils(MyApplication.getInstance(),
                        info.getPackageName());
                boolean contained = false;
                for (String key : Data.PROTECT_KEY_WORDS) {
                    if (au.contains(key)) {
                        contained = true;
                        break;
                    }
                }
                if (!contained) {
                    good++;
                    result.setProtect(AppResult.STATUS_PASS);
                } else {
                    result.setProtect(AppResult.STATUS_NOT_PASS);
                }
            } catch (Exception e) {
                e.printStackTrace();
                good++;
                result.setProtect(AppResult.STATUS_FAIL);
            }

            result.setGood(good);
            return result;
        }
    }

    public static String String_Between(String str, String leftstr, String rightstr) {
        if (!str.contains(leftstr) || !str.contains(rightstr))
            return "";
        int i = str.indexOf(leftstr) + leftstr.length();
        return str.substring(i, str.indexOf(rightstr, i));
    }

    private boolean contain(List<ActivityManager.RunningServiceInfo> array, String obj) {
        if (array == null) return false;
        for (ActivityManager.RunningServiceInfo o : array) {
            if (!o.foreground && o.service.getPackageName().equals(obj))
                return true;
        }
        return false;
    }

    private <T> boolean contains(T[] a, T o) {
        if (a == null) return false;
        for (T b : a) {
            if (o.equals(b))
                return true;
        }
        return false;
    }

    private boolean contain(ServiceInfo[] a, String obj) {
        if (a == null) return false;
        for (ServiceInfo si : a) {
            if (si.name.contains(obj) && si.enabled)
                return true;
        }
        return false;
    }

    private boolean contains(ArrayList a, Object o) {
        if (a == null) return false;
        for (Object b : a) {
            if (o.equals(b))
                return true;
        }
        return false;
    }

    private boolean isRecevier(String action, String packageName) {
        List<ResolveInfo> mApps;
        Intent intent = new Intent(action);
        PackageManager pm = MyApplication.getInstance().getPackageManager();
        mApps = pm.queryBroadcastReceivers(intent, PackageManager.MATCH_ALL);
        if (mApps == null) return false;
        for (ResolveInfo ri : mApps) {
            if (ri.activityInfo.packageName.equals(packageName))
                return true;
        }
        return false;
    }

    public static ArrayList<String> getActivities(String pn) {
        ArrayList<String> result = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setPackage(pn);
        for (ResolveInfo info : MyApplication.getInstance().getPackageManager().queryIntentActivities(intent, 0)) {
            result.add(info.activityInfo.name);
        }
        return result;
    }
}
