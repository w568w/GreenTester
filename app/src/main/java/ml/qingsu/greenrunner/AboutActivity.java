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
							  .text("版本")
							  .desc(getVersionName(this))
							  .setOnClickAction(new MaterialAboutItemOnClickAction(){

									  @Override
									  public void onClick() {
										  browse("https://github.com/w568w/GreenTester");
									  }
								  })
							  .build())
					 .addItem(new MaterialAboutTitleItem.Builder()
							  .icon(new ColorDrawable(Color.TRANSPARENT))
							  .text("构建")
							  .desc("AIDE\nVersion 3.2.190809 (1908090002)\nCommit #eb8d5626ff8b7781e3a2ec6c3990785697adb99f")
							  .build())
					 .build())

			.addCard(new MaterialAboutCard.Builder()
					 .title("开发者")
					 .addItem(
						 new MaterialAboutTitleItem.Builder()
						 .icon(R.drawable.w568w)
						 .text("@w568w").desc("The main developer")
						 .setOnClickAction(new MaterialAboutItemOnClickAction(){

								 @Override
								 public void onClick() {
									 browse("http://w568w.ml/about.w568w.html");
								 }
							 })
						 .build())
					 .addItem(
					 	 new MaterialAboutTitleItem.Builder()
						 .icon(R.drawable.trumeet)
						 .text("@Trumeet").desc("Material UI")
						 .setOnClickAction(new MaterialAboutItemOnClickAction(){

								 @Override
								 public void onClick() {
									 browse("https://yuuta.moe/");
								 }
							 })
						 .build())
					 .build())
//			.addCard(new MaterialAboutCard.Builder()
//					 .addItem(new MaterialAboutTitleItem.Builder()
//							  .icon(R.drawable.logo)
//							  .text(R.string.app_name)
//							  .desc(R.string.about_desc)
//							  .build())
//					 .build())
			.build();
	}

	@Override
	protected CharSequence getActivityTitle() {
		// TODO: Implement this method
		return "About";
	}
    /**
	 * 获取版本名称
	 * @param context 上下文
	 * @return 版本名称
	 */
	public static String getVersionName(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		}
		catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	} 
	public void browse(String url){
		try {
			startActivity(new Intent(Intent.ACTION_VIEW,
									 Uri.parse(url)));
		}
		catch (ActivityNotFoundException ignore) {}
		
	}

}
