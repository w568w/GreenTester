package ml.qingsu.greenrunner;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.android.setupwizardlib.SetupWizardLayout;
import com.android.setupwizardlib.view.NavigationBar;
import android.support.v7.widget.*;
import android.widget.Button;
import android.widget.PopupMenu.*;
import android.view.*;

/**
 * Created by Trumeet on 2017/6/21.
 * Step1 SetupWizard style activity
 * @author Trumeet
 */

public class WelcomeActivity extends AppCompatActivity {
    SetupWizardLayout layout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = new SetupWizardLayout(this);

        layout.setHeaderText(R.string.app_name);
        layout.getNavigationBar().setNavigationBarListener(new NavigationBar.NavigationBarListener() {
				@Override
				public void onNavigateBack() {
					onBackPressed();
				}

				@Override
				public void onNavigateNext() {
					startActivity(new Intent(WelcomeActivity.this,
											 ApplicationListActivity.class));
				}
			});
        TextView textView = new TextView(this);
        textView.setText(R.string.step1);
        int h = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        int v = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        textView.setPadding(h, v, h, v);
        layout.addView(textView);
        setContentView(Utils.initWizard(layout));
		Button more=layout.getNavigationBar().getMoreButton();
		final PopupMenu menu=new PopupMenu(this, more);
//		menu.getMenu().add("About").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
//
//				@Override
//				public boolean onMenuItemClick(MenuItem p1) {
//					// TODO: Implement this method
//					try {
//						startActivity(new Intent(Intent.ACTION_VIEW,
//												 Uri.parse("https://github.com/w568w/GreenTester")));
//					}
//					catch (ActivityNotFoundException ignore) {}
//					return false;
//				}
//			});
		menu.getMenu().add("About").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

				@Override
				public boolean onMenuItemClick(MenuItem p1) {
					// TODO: Implement this method
					startActivity(new Intent(WelcomeActivity.this,AboutActivity.class));
					return false;
				}
			});
        layout.getNavigationBar().getMoreButton().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					menu.show();

				}
			});
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getPackageManager().resolveActivity(new Intent(Intent.ACTION_VIEW,
														   Uri.parse("https://github.com/w568w/GreenTester")),
												PackageManager.MATCH_DEFAULT_ONLY) == null) {
            layout.getNavigationBar().getMoreButton().setVisibility(View.GONE);
        } else {
            layout.getNavigationBar().getMoreButton().setVisibility(View.VISIBLE);
        }
    }
}
