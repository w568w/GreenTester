package ml.qingsu.greenrunner;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.view.WindowManager;
import android.widget.TextView;

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
        FIR.init(this);
        //CrashHandler.getInstance().init(instance);
    }

    public static MyApplication getInstance(){
        return instance;
    }
    public static class CrashHandler implements Thread.UncaughtExceptionHandler {

        private static CrashHandler INSTANCE = new CrashHandler();
        private Context mContext;
        private CrashHandler() {
        }
        public static CrashHandler getInstance() {
            return INSTANCE;
        }
        public void init(Context context) {
            mContext = context;
            //设置该CrashHandler为程序的默认处理器
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
        @Override
        public void uncaughtException(Thread thread, final Throwable throwable) {

            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    StringBuilder sb=new StringBuilder();
                    sb.append(":(\n出了点小问题，请将截图发送给开发者\n");
                    sb.append(throwable.toString());
                    StackTraceElement[] ste=throwable.getStackTrace();
                    for(StackTraceElement s:ste)
                    {
                        sb.append("\n");
                        sb.append(s.toString());
                    }
                    TextView tv=new TextView(mContext);
                    tv.setSingleLine(false);
                    tv.setVerticalScrollBarEnabled(true);
                    tv.setMovementMethod(ScrollingMovementMethod.getInstance());
                    tv.setText(sb.toString());
                    AlertDialog a=new AlertDialog.Builder(mContext)
                            .setTitle("Ops!有地方出错了")
                            .setView(tv)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(0);
                                }
                            })
                            .create();
                    a.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    a.show();
                    Looper.loop();
                }
            }.start();
        }
    }
}
