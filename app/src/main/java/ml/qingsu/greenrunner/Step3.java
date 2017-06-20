package ml.qingsu.greenrunner;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.codepond.wizardroid.WizardStep;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class Step3 extends WizardStep {
    TextView tv;
    private static final String TAG="aa";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tv = new TextView(MyApplication.getInstance());
        tv.setText(R.string.step3);
        tv.setPadding(5,5,5,5);
        Log.d(TAG, "onCreateView() called with: " + "inflater = [" + inflater + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");
        return tv;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint() called with: " + "isVisibleToUser = [" + isVisibleToUser + "]");
             if (isVisibleToUser) {
            notifyIncomplete();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyApplication.CrashHandler.getInstance().init(MyApplication.getInstance());
                    int good = 0;
                    PackageManager pm = MyApplication.getInstance().getPackageManager();
                    StringBuilder sb = new StringBuilder();
                    sb.append("<head><meta name=\"viewport\" content=\"width=device-width,initial-scale=1\"/><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head>");
                    sb.append("<h1>测试应用:");
                    sb.append(Step2.selected.appName);
                    sb.append("</h1>");
                    sb.append("<p>(优化)Target SDK Version >= 24&emsp;");
                    if (Step2.selected.packageInfo.applicationInfo.targetSdkVersion >= 24) {
                        sb.append("Passed");
                        good++;
                    } else {
                        sb.append("Failed");
                    }
                    sb.append("</p><hr />");

                    //=============================

                    sb.append("<p>(隐私)READ_PHONE_STATE权限请求&emsp;");
                    try {
                        Step2.selected.packageInfo = pm.getPackageInfo(Step2.selected.packageName, PackageManager.GET_PERMISSIONS);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (!contains(Step2.selected.packageInfo.requestedPermissions, Manifest.permission.READ_PHONE_STATE)) {
                        sb.append("Passed");
                        good++;
                    } else {
                        sb.append("Failed");
                    }
                    sb.append("</p><hr />");
                    //=============================
                    sb.append("<p>(省电、优化)是否注册特殊广播&emsp;");
                    if (isRecevier("android.net.conn.CONNECTIVITY_CHANGE", Step2.selected.packageName) ||
                            isRecevier("android.hardware.action.NEW_PICTURE", Step2.selected.packageName) ||
                            isRecevier("android.hardware.action.NEW_VIDEO", Step2.selected.packageName) ||
                            isRecevier("android.net.wifi.SCAN_RESULTS", Step2.selected.packageName) ||
                            isRecevier("android.intent.action.USER_PRESENT", Step2.selected.packageName) ||
                            isRecevier("android.intent.action.ACTION_POWER_DISCONNECTED", Step2.selected.packageName)) {
                        sb.append("Failed");
                    } else {
                        sb.append("Passed");
                        good++;
                    }
                    sb.append("</p><hr />");
                    //=============================
                    sb.append("<p>(优化)后台服务限制&emsp;");
                    ActivityManager mActivityManager = (ActivityManager) MyApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningServiceInfo> a = mActivityManager.getRunningServices(999);
                    if (!contain(a, Step2.selected.packageName)) {
                        sb.append("Passed");
                        good++;
                    } else {
                        sb.append("Failed");
                    }
                    sb.append("</p><hr />");
                    //=============================
                    sb.append("<p>(省电)Alarm限制&emsp;");
                    if(ShellUtils.isRoot())
                    {
                        ShellUtils.CommandResult cr=ShellUtils.execCommand("dumpsys alarm",true,true);
                        if(cr.result==0)
                        {
                            String top=String_Between(cr.successMsg,"Top Alarms:","Alarm Stats:");
                            if(!top.contains(Step2.selected.packageName))
                            {
                                sb.append("Passed");
                                good++;
                            }
                            else
                            {
                                sb.append("Failed");
                            }
                        }
                        else
                        {
                            good++;
                            sb.append("Root执行失败,无法检测");
                        }
                    }
                    else
                    {
                        good++;
                        sb.append("本应用没有Root权限,无法检测");
                    }
                    sb.append("</p><hr />");

                    sb.append("<h2>综上所述，评定 "+Step2.selected.appName+" "+good*20+"%符合绿色公约要求。</h2><hr /><a href=\"http://green-android.org\">绿色公约官网</a><p>By w568w</p>");
                    try {
                        File_WritetoSDFrom_bytes(sb.toString().getBytes("UTF-8"),"result.html");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    tv.post(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(R.string.step4);
                            notifyCompleted();
                        }
                    });
                }
            }).start();
        }
    }
    public static String String_Between(String str, String leftstr, String rightstr)
    {
        if(!str.contains(leftstr)||!str.contains(rightstr))
            return "";
        int i = str.indexOf(leftstr) + leftstr.length();
        return str.substring(i, str.indexOf(rightstr,i));
    }
    public static void File_WritetoSDFrom_bytes(byte[] data,String filename){
        File f=new File(Environment.getExternalStorageDirectory().getPath()+"/"+filename);
        if(f.exists())
            f.delete();
        if(!f.exists())
        {
            try {
                f.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
            FileOutputStream fops = new FileOutputStream(f);
            fops.write(data);
            fops.flush();
            fops.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean contain(List<ActivityManager.RunningServiceInfo> array, String obj) {
        if(array==null)return false;
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

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called with: " + "");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called with: " + "");
    }
}
