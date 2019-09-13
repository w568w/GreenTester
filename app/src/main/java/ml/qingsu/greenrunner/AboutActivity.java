package ml.qingsu.greenrunner;

import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;

import com.danielstone.materialaboutlibrary.*;
import com.danielstone.materialaboutlibrary.items.*;
import com.danielstone.materialaboutlibrary.model.*;

import android.net.*;

public class AboutActivity extends MaterialAboutActivity {

    @Override
    protected MaterialAboutList getMaterialAboutList(Context p1) {


        return new MaterialAboutList.Builder()

                .addCard(new MaterialAboutCard.Builder()
                        .addItem(new MaterialAboutTitleItem.Builder()
                                .icon(R.drawable.logo)
                                .text(R.string.app_name)
                                .desc(R.string.about_desc)
                                .build())
                        .addItem(new MaterialAboutTitleItem.Builder()
                                .icon(new ColorDrawable(Color.TRANSPARENT))
                                .text(R.string.about_version)
                                .desc(getVersionName(this))
                                .setOnClickAction(new MaterialAboutItemOnClickAction() {

                                    @Override
                                    public void onClick() {
                                        browse("https://github.com/w568w/GreenTester");
                                    }
                                })
                                .build())
                        .addItem(new MaterialAboutTitleItem.Builder()
                                .icon(new ColorDrawable(Color.TRANSPARENT))
                                .text(R.string.about_builder)
                                .desc(R.string.about_build_desc)
                                .build())
                        .build())

                .addCard(new MaterialAboutCard.Builder()
                        .title(R.string.developer)
                        .addItem(
                                new MaterialAboutTitleItem.Builder()
                                        .icon(R.drawable.w568w)
                                        .text(R.string.w568w).desc(R.string.about_w568w_desc)
                                        .setOnClickAction(new MaterialAboutItemOnClickAction() {

                                            @Override
                                            public void onClick() {
                                                browse("http://w568w.ml/about.w568w.html");
                                            }
                                        })
                                        .build())
                        .addItem(
                                new MaterialAboutTitleItem.Builder()
                                        .icon(R.drawable.trumeet)
                                        .text(R.string.trumeet).desc(R.string.about_trumeet_desc)
                                        .setOnClickAction(new MaterialAboutItemOnClickAction() {

                                            @Override
                                            public void onClick() {
                                                browse("https://yuuta.moe/");
                                            }
                                        })
                                        .build())
                        .build())
                .addCard(new MaterialAboutCard.Builder()
                        .title(R.string.acknowledgement)
                        .addItem(
                                new MaterialAboutTitleItem.Builder()
                                        .icon(R.drawable.logo)
                                        .text(R.string.green_android).desc(R.string.about_green_android_desc)
                                        .setOnClickAction(new MaterialAboutItemOnClickAction() {

                                            @Override
                                            public void onClick() {
                                                browse("https://green-android.org/");
                                            }
                                        })
                                        .build())

                        .build())
                .build();
    }

    @Override
    protected CharSequence getActivityTitle() {
        // TODO: Implement this method
        return "About";
    }

    /**
     * 获取版本名称
     *
     * @param context 上下文
     * @return 版本名称
     */
    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void browse(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url)));
        } catch (ActivityNotFoundException ignore) {
        }

    }

}
