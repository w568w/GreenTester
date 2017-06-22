package ml.qingsu.greenrunner;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Trumeet on 2017/6/21.
 * @author Trumeet
 */

public class AppResult implements Parcelable {
    public static final int STATUS_PASS = 0;
    public static final int STATUS_NOT_PASS = 1;
    public static final int STATUS_FAIL = 2;
    
    private int good;
    private int targetNougat = STATUS_NOT_PASS;
    private int privacyPermission = STATUS_NOT_PASS;
    private int specialReceiver = STATUS_NOT_PASS;
    private int backgroundLimit = STATUS_NOT_PASS;
    private int alarmLimit = STATUS_NOT_PASS;
    private int ad = STATUS_NOT_PASS;
    private int protect = STATUS_NOT_PASS;

    public AppResult () {
    }

    protected AppResult(Parcel in) {
        good = in.readInt();
        targetNougat = in.readInt();
        privacyPermission = in.readInt();
        specialReceiver = in.readInt();
        backgroundLimit = in.readInt();
        alarmLimit = in.readInt();
        ad = in.readInt();
        protect = in.readInt();
    }

    public static final Creator<AppResult> CREATOR = new Creator<AppResult>() {
        @Override
        public AppResult createFromParcel(Parcel in) {
            return new AppResult(in);
        }

        @Override
        public AppResult[] newArray(int size) {
            return new AppResult[size];
        }
    };

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public int getTargetNougat() {
        return targetNougat;
    }

    public void setTargetNougat(int targetNougat) {
        this.targetNougat = targetNougat;
    }

    public int getPrivacyPermission() {
        return privacyPermission;
    }

    public void setPrivacyPermission(int privacyPermission) {
        this.privacyPermission = privacyPermission;
    }

    public int getSpecialReceiver() {
        return specialReceiver;
    }

    public void setSpecialReceiver(int specialReceiver) {
        this.specialReceiver = specialReceiver;
    }

    public int getBackgroundLimit() {
        return backgroundLimit;
    }

    public void setBackgroundLimit(int backgroundLimit) {
        this.backgroundLimit = backgroundLimit;
    }

    public int getAlarmLimit() {
        return alarmLimit;
    }

    public void setAlarmLimit(int alarmLimit) {
        this.alarmLimit = alarmLimit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(good);
        dest.writeInt(targetNougat);
        dest.writeInt(privacyPermission);
        dest.writeInt(specialReceiver);
        dest.writeInt(backgroundLimit);
        dest.writeInt(alarmLimit);
        dest.writeInt(ad);
        dest.writeInt(protect);
    }

    public int getAd() {
        return ad;
    }

    public void setAd(int ad) {
        this.ad = ad;
    }

    public int getProtect() {
        return protect;
    }

    public void setProtect(int protect) {
        this.protect = protect;
    }
}
