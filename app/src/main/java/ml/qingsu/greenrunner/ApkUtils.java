package ml.qingsu.greenrunner;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

/**
 * Created by w568w on 17-6-21.
 * 参考http://blog.csdn.net/do2jiang/article/details/8492329
 */
public class ApkUtils {
    ArrayList<String> clazz = new ArrayList<>();

    ApkUtils(Context con, String packageName) throws Exception {
        PackageManager packagemgr = con.getPackageManager();
        List<PackageInfo> packageList = packagemgr.getInstalledPackages(0);
        if (packageList == null || packageList.size() == 0)
            throw new Exception("Fuck U!Give me Quan Xian!");
        String path = packagemgr.getApplicationInfo(packageName, 0).sourceDir;
        DexFile dexFile = null;
        try {
            dexFile = new DexFile(path);// get it

            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                clazz.add(entries.nextElement());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dexFile != null)
                    dexFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean contains(String partOfClazzName) {
        for (String str : clazz) {
            if (str.contains(partOfClazzName))
                return true;
        }
        return false;
    }
}
