package ml.qingsu.greenrunner;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import org.codepond.wizardroid.WizardStep;

/**
 * Created by Administrator on 17-6-19.
 */
public class Step4 extends WizardStep {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        WebView wv=new WebView(MyApplication.getInstance());
        wv.loadUrl("file://" + Environment.getExternalStorageDirectory().getPath() + "/result.html");
        return wv;

    }
}
