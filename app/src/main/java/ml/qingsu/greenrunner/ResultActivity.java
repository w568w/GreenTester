package ml.qingsu.greenrunner;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.setupwizardlib.SetupWizardLayout;
import com.android.setupwizardlib.view.NavigationBar;

/**
 * Created by Trumeet on 2017/6/21.
 * @author Trumeet
 */

public class ResultActivity extends AppCompatActivity {
    public static final String EXTRA_APP = ResultActivity.class
            .getName() + ".EXTRA_APP";
    public static final String EXTRA_RESULT = ResultActivity.class
            .getName() + ".EXTRA_RESULT";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfo info = getIntent().getParcelableExtra(EXTRA_APP);
        AppResult result = getIntent().getParcelableExtra(EXTRA_RESULT);
        if (info == null || result == null) {
            finish();
            return;
        }
        SetupWizardLayout layout = new SetupWizardLayout(this);
        layout.setHeaderText(R.string.step4);
        layout.getNavigationBar().setNavigationBarListener(new NavigationBar.NavigationBarListener() {
            @Override
            public void onNavigateBack() {
                onBackPressed();
            }

            @Override
            public void onNavigateNext() {
                ActivityCompat.finishAffinity(ResultActivity.this);
            }
        });
        layout.getNavigationBar().getNextButton().setText(R.string.exit);
        setContentView(Utils.initWizard(layout));
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(buildItem(getString(R.string.item_target_nougat),
                result.getTargetNougat(),
                linearLayout));
        linearLayout.addView(buildItem(getString(R.string.item_privacy_permission),
                result.getPrivacyPermission(),
                linearLayout));
        linearLayout.addView(buildItem(getString(R.string.item_special_receiver),
                result.getSpecialReceiver(),
                linearLayout));
        linearLayout.addView(buildItem(getString(R.string.item_background_limit),
                result.getBackgroundLimit(),
                linearLayout));
        linearLayout.addView(buildItem(getString(R.string.item_alarm_limit),
                result.getAlarmLimit(),
                linearLayout));
        linearLayout.addView(buildItem(getString(R.string.item_ad),
                result.getAd(),
                linearLayout));
        linearLayout.addView(buildItem(getString(R.string.item_protect),
                result.getProtect(),
                linearLayout));
        linearLayout.addView(buildFooter(info, result, linearLayout));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(linearLayout);
    }

    private View buildItem (String title,
                            int status,
                            @Nullable ViewGroup viewGroup) {
        View view = getLayoutInflater().inflate(R.layout.item_result,
                viewGroup, false);
        ImageView icon = (ImageView) view.findViewById(android.R.id.icon);
        TextView name = (TextView)view.findViewById(R.id.textView);
        TextView statusTv = (TextView)view.findViewById(android.R.id.text2);
        Drawable drawable = null;
        int color = 0;
        String statusStr = "";
        switch (status) {
            case AppResult.STATUS_PASS :
                drawable = ContextCompat.getDrawable(this,
                        R.drawable.ic_check_black_24dp);
                color = Color.parseColor("#009688");
                statusStr = getString(R.string.pass);
                break;
            case AppResult.STATUS_NOT_PASS:
                drawable = ContextCompat.getDrawable(this,
                        R.drawable.ic_pan_tool_black_24dp);
                color = Color.parseColor("#F44336");
                statusStr = getString(R.string.not_pass);
                break;
            case AppResult.STATUS_FAIL:
                statusStr = getString(R.string.fail);
                break;
        }
        name.setText(title);
        statusTv.setText(statusStr);
        if (drawable == null) {
            icon.setImageDrawable(null);
        } else {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, color);
            icon.setImageDrawable(drawable);
        }
        return view;
    }

    private View buildFooter (AppInfo info,
                              AppResult result, @Nullable ViewGroup viewGroup) {
        View view = getLayoutInflater().inflate(R.layout.result_footer,
                viewGroup, false);
        TextView textView = (TextView)view.findViewById(android.R.id.text2);
        textView.setText(Html.fromHtml(getString(R.string.item_footer,
                info.getAppName(),
                String.valueOf((int) (((float) result.getGood()) / 7 * 100)))));
        return view;
    }
}
