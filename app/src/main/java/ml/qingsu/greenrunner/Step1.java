package ml.qingsu.greenrunner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.codepond.wizardroid.WizardStep;

/**
 * Created by Administrator on 17-6-18.
 */
public class Step1 extends WizardStep {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView v = new TextView(getActivity());
        v.setPadding(5,5,5,5);
        v.setText(R.string.step1);

        return v;
    }
}
