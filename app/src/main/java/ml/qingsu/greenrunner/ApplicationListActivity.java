package ml.qingsu.greenrunner;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.setupwizardlib.SetupWizardLayout;
import com.android.setupwizardlib.SetupWizardListLayout;
import com.android.setupwizardlib.view.NavigationBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Trumeet on 2017/6/21.
 * Step2 SetupWizard style activity
 * @author Trumeet
 */

public class ApplicationListActivity extends AppCompatActivity {
    private static final String EXTRA_LIST = ApplicationListActivity.class.getName()
            + ".EXTRA_LIST";
    private LoadAppTask mTask;
    private ArrayList<AppInfo> list;
    private SetupWizardListLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = new SetupWizardListLayout(this);
        layout.setHeaderText(R.string.app_name);
        layout.getNavigationBar().setNavigationBarListener(new NavigationBar.NavigationBarListener() {
            @Override
            public void onNavigateBack() {
                onBackPressed();
            }

            @Override
            public void onNavigateNext() {
                // don't allow
            }
        });
        layout.getNavigationBar().getNextButton().setVisibility(View.GONE);
        setContentView(Utils.initWizard(layout));
        if (savedInstanceState != null) {
            list = savedInstanceState.getParcelableArrayList(EXTRA_LIST);
        }
        mTask = new LoadAppTask();
        mTask.execute();
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        if (mTask != null) {
            outState.putParcelableArrayList(EXTRA_LIST,
                    list);
        }
        super.onSaveInstanceState(outState);
    }

    private class LoadAppTask extends AsyncTask<Void,
            Void, List<AppInfo>> {
        @Override
        protected List<AppInfo> doInBackground(Void... voids) {
            if (list != null && !list.isEmpty()) {
                return list;
            }
            ArrayList<AppInfo> appList = new ArrayList<>();
            PackageManager pm = ApplicationListActivity.this.getPackageManager();
            List<PackageInfo> packages =pm.getInstalledPackages(0);
            for(int i=0;i<packages.size();i++) {
                PackageInfo packageInfo = packages.get(i);
                AppInfo tmpInfo = new AppInfo();
                tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(pm)
                        .toString());
                tmpInfo.setPackageName(packageInfo.packageName);
                tmpInfo.setVersionName(packageInfo.versionName);
                tmpInfo.setVersionCode(packageInfo.versionCode);
                appList.add(tmpInfo);
            }
            AppInfo[] s=appList.toArray(new AppInfo[0]);
            Arrays.sort(s, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo appInfo, AppInfo t1) {
                    return appInfo.getAppName().compareTo(t1.getAppName());
                }
            });
            appList=new ArrayList<>(Arrays.asList(s));
            return appList;
        }

        @Override
        protected void onPreExecute () {
            layout.setHeaderText(R.string.step2);
            layout.setProgressBarShown(true);
        }

        @Override
        protected void onPostExecute (List<AppInfo> list) {
            final AppAdapter aa=new AppAdapter(list,
                    ApplicationListActivity.this);
            layout.getListView().setAdapter(aa);
            layout.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    startActivity(new Intent(ApplicationListActivity.this,
                            ProgressActivity.class)
                            .putExtra(ProgressActivity.EXTRA_APP,
                                    (AppInfo) adapterView.getItemAtPosition(i)));
                }
            });
            layout.setHeaderText(R.string.step2_2);
            layout.setProgressBarShown(false);
        }
    }

    @Override
    public void onDestroy () {
        if (mTask != null && !mTask.isCancelled()) {
            mTask.cancel(true);
        }
        super.onDestroy();
    }

    private class AppAdapter extends ArrayAdapter<AppInfo> {
        private final String TAG = AppAdapter.class.getSimpleName();

        private List<AppInfo> list;
        private LruCache<String, Drawable> mIconMemoryCaches;

        AppAdapter(List<AppInfo> list,
                   Context context) {
            super(context, 0, list);
            this.list = list;

            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int cacheSizes = maxMemory/5;

            mIconMemoryCaches = new LruCache<>(cacheSizes);
        }

        @Override
        @NonNull
        public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
            ViewHolder viewHolder;
            final AppInfo info = list.get(i);
            if(view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.radiobutton,
                        viewGroup, false);
                viewHolder = new AppAdapter.ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (AppAdapter.ViewHolder) view.getTag();
            }

            viewHolder.name.setText(info.getAppName());
            Drawable icon = getIconFromMemoryCache(info.getPackageName());
            if (icon != null) {
                viewHolder.icon.setImageDrawable(icon);
            } else {
                new AppAdapter.IconWorkerTask(viewHolder.icon)
                        .execute(info.getPackageName());
            }
            return view;
        }

        private void addDrawableToMemoryCache (@Nullable String pkg, @NonNull Drawable icon) {
            Log.d(TAG, "addDrawableToMemoryCache -> " + pkg + " -> " + icon);
            if (getIconFromMemoryCache(pkg) == null) {
                if (pkg == null) pkg = "";
                mIconMemoryCaches.put(pkg, icon);
            }
        }

        private Drawable getIconFromMemoryCache (@Nullable String pkg) {
            if (pkg == null) pkg = "";
            return mIconMemoryCaches.get(pkg);
        }

        private class IconWorkerTask extends AsyncTask<String, Void, Drawable> {
            private ImageView imageView;
            IconWorkerTask (ImageView imageView) {
                this.imageView = imageView;
            }

            @Override
            protected Drawable doInBackground(String... params) {
                String pkg = params[0];
                Drawable icon;
                if (pkg == null || pkg.equals("")) {
                    pkg = "";
                    icon = ContextCompat.getDrawable(getContext(),
                            R.mipmap.ic_launcher);
                } else {
                    try {
                        icon = getContext().getPackageManager()
                                .getApplicationIcon(pkg);
                    } catch (PackageManager.NameNotFoundException ignore) {
                        icon = ContextCompat.getDrawable(getContext(),
                                R.mipmap.ic_launcher);
                    }
                }
                if (icon == null) {
                    Log.w(TAG, "Save -> icon is null");
                    return null;
                }
                addDrawableToMemoryCache(pkg, icon);
                return icon;
            }

            @Override
            protected void onPostExecute (Drawable drawable) {
                if (imageView != null) {
                    imageView.setImageDrawable(drawable);
                }
            }
        }

        class ViewHolder{
            TextView name;
            ImageView icon;

            ViewHolder (View view) {
                name = (TextView)view.findViewById(R.id.textView);
                icon = (ImageView) view.findViewById(android.R.id.icon);
            }
        }
    }

}
