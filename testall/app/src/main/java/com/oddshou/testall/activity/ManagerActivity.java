package com.oddshou.testall.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.oddshou.testall.R;

import java.util.ArrayList;
import java.util.List;

public class ManagerActivity extends AppCompatActivity {

    private final static ArrayList<PackageInfo> mPackageInfoList = new ArrayList<PackageInfo>();
    private static PackageManager mPackageManager;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        Toolbar mytooToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytooToolbar);

        mPackageManager = getPackageManager();
        loadLocalApps();    //加载本机应用数据
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MyAdapter());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_manager_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                return true;
            case R.id.settings1:
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewholder> {


        @Override
        public MyAdapter.MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewholder myViewholder = new MyViewholder(
                    LayoutInflater.from(ManagerActivity.this).inflate(R.layout.viewholder_manageractivity, parent, false)
            );
            return myViewholder;


        }

        @Override
        public void onBindViewHolder(final MyAdapter.MyViewholder holder, int position) {
            final PackageInfo packageInfo = mPackageInfoList.get(position);
            holder.icon.setBackground(packageInfo.applicationInfo.loadIcon(getPackageManager()));
            holder.showName.setText(packageInfo.applicationInfo.loadLabel(getPackageManager()));
            holder.packageName.setText(packageInfo.packageName);
            holder.uninstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uninstallPack(packageInfo.packageName);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPackageInfoList.size();
        }

        private void uninstallPack(String packName) {
            Intent intent = new Intent();
            Uri uri = Uri.parse(packName);
            intent.setData(uri);
            startActivity(intent);
        }

        class MyViewholder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView packageName;
            TextView showName;
            Button uninstall;

            public MyViewholder(View itemView) {
                super(itemView);
                icon = (ImageView) itemView.findViewById(R.id.icon);
                packageName = (TextView) itemView.findViewById(R.id.packageName);
                showName = (TextView) itemView.findViewById(R.id.showName);
                uninstall = (Button) itemView.findViewById(R.id.uninstall);
            }
        }
    }

    // 因为后台需要查询应用更新， 有可能会同时操作 loadLocalApps 中的数据结构
    // ，所以方法需要synchronized,保证互斥访问mPackageInfoMap，mActivityInfoMap
    private void loadLocalApps() {
        // 加载前先清理
        mPackageInfoList.clear();
        if (null == mPackageManager) {
            mPackageManager = getPackageManager();
        }
        List<PackageInfo> packageInfoes = mPackageManager
                .getInstalledPackages(PackageManager.GET_SIGNATURES);
        for (PackageInfo rInfo : packageInfoes) {
            if ((rInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                mPackageInfoList.add(rInfo);
            }
        }

    }

}
