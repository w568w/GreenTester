package ml.qingsu.greenrunner;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.codepond.wizardroid.WizardStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by w568w on 17-6-18.
 */
public class Step2 extends WizardStep {
    TextView tv;
    ListView lv;
    int selectPosition;
    public static AppInfo selected;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout ll= (LinearLayout) inflater.inflate(R.layout.step2,null);
        tv= (TextView) ll.findViewById(R.id.step2_textView);
        lv= (ListView) ll.findViewById(R.id.step2_listView);
        tv.setText(R.string.step2);
        ll.setPadding(5,5,5,5);
        return ll;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Context act=MyApplication.getInstance();
                    ArrayList<AppInfo> appList = new ArrayList<>();
                    PackageManager pm=act.getPackageManager();
                    List<PackageInfo> packages =pm.getInstalledPackages(0);
                    if(packages==null||packages.size()==0)
                    {
                        notifyIncomplete();
                        Toast.makeText(MyApplication.getInstance(),"请允许本应用读取已安装程序!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    for(int i=0;i<packages.size();i++) {
                        PackageInfo packageInfo = packages.get(i);
                        AppInfo tmpInfo =new AppInfo();
                        tmpInfo.appName = packageInfo.applicationInfo.loadLabel(pm).toString();
                        tmpInfo.packageName = packageInfo.packageName;
                        tmpInfo.versionName = packageInfo.versionName;
                        tmpInfo.versionCode = packageInfo.versionCode;
                        tmpInfo.packageInfo=packageInfo;
                        tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(pm);
                        appList.add(tmpInfo);
                    }
                    AppInfo[] s=appList.toArray(new AppInfo[0]);
                    Arrays.sort(s, new Comparator<AppInfo>() {
                        @Override
                        public int compare(AppInfo appInfo, AppInfo t1) {
                            return appInfo.appName.compareTo(t1.appName);
                        }
                    });
                    appList=new ArrayList<>(Arrays.asList(s));
                    final ArrayList<AppInfo> finalAppList = appList;
                    tv.post(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(R.string.step2_2);
                        }
                    });
                    lv.post(new Runnable() {
                        @Override
                        public void run() {

                            final AppAdapter aa=new AppAdapter(finalAppList);
                            lv.setAdapter(aa);

                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    //获取选中的参数
                                    selectPosition = position;
                                    aa.notifyDataSetChanged();
                                    selected = finalAppList.get(position);
                                }
                            });
                            selectPosition = 0;
                            aa.notifyDataSetChanged();
                            selected = finalAppList.get(0);
                            notifyCompleted();
                        }
                    });
                }
            }).start();
        }
    }
    private class AppAdapter extends BaseAdapter{
        private ArrayList<AppInfo> al;
        AppAdapter(ArrayList<AppInfo> a)
        {
            al=a;

        }
        @Override
        public int getCount() {
            return al.size();
        }

        @Override
        public Object getItem(int i) {
            return al.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if(view==null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.radiobutton, null);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView)view.findViewById(R.id.textView);
                viewHolder.select = (RadioButton)view.findViewById(R.id.radioButton);
                view.setTag(viewHolder);
            }
            else
            {
                viewHolder= (ViewHolder) view.getTag();
            }
            viewHolder.name.setText(al.get(i).appName);
            al.get(i).appIcon.setBounds(0,0,40,40);
            viewHolder.name.setCompoundDrawables(al.get(i).appIcon,null,null,null);
            if(selectPosition == i){
                viewHolder.select.setChecked(true);
            }
            else{
                viewHolder.select.setChecked(false);
            }
            return view;
        }
        public class ViewHolder{
            TextView name;
            RadioButton select;
        }
    }
    public class AppInfo {
        public String appName="";
        public String packageName="";
        public String versionName="";
        public int versionCode=0;
        public Drawable appIcon=null;
        public PackageInfo packageInfo;

    }
}
