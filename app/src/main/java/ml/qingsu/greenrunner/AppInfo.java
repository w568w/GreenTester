package ml.qingsu.greenrunner;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Trumeet on 2017/6/20.
 * @author Trumeet
 */

public class AppInfo implements Parcelable {
    private String appName="";
    private String packageName="";
    private String versionName="";
    private int versionCode=0;

    public AppInfo () {}

    protected AppInfo(Parcel in) {
        appName = in.readString();
        packageName = in.readString();
        versionName = in.readString();
        versionCode = in.readInt();
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel in) {
            return new AppInfo(in);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(appName);
        parcel.writeString(packageName);
        parcel.writeString(versionName);
        parcel.writeInt(versionCode);
    }
}
