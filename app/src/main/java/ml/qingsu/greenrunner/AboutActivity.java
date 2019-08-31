package ml.qingsu.greenrunner;
import com.danielstone.materialaboutlibrary.*;
import com.danielstone.materialaboutlibrary.model.*;
import android.content.*;
import com.danielstone.materialaboutlibrary.items.*;
import android.support.design.widget.*;

public class AboutActivity extends MaterialAboutActivity {

	@Override
	protected MaterialAboutList getMaterialAboutList(Context p1) {
		// TODO: Implement this method
		
		return new MaterialAboutList.Builder()
			.addCard(new MaterialAboutCard.Builder()
			
					 .addItem(new MaterialAboutTitleItem.Builder()
							  .icon(R.drawable.logo)
							  .text(R.string.app_name)
//							  .desc("一个还不错的应用评测工具")
							  .desc(R.string.about_desc)
							  .build())
					 .build())
			
			.addCard(new MaterialAboutCard.Builder()
					 .title("开发者")
					 .addItem(
						 new MaterialAboutTitleItem.Builder()
						 .icon(R.drawable.w568w)
						 
						 .text("@w568w").desc("The main developer")
						 .build())
					 .addItem(
					 	 new MaterialAboutTitleItem.Builder()
						 .text("@Trumeet").desc("Material UI")
						 .build())
					 .build()).build();
	}
 
	@Override
	protected CharSequence getActivityTitle() {
		// TODO: Implement this method
		return "About";
	}



}
