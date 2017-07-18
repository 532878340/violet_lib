package com.violet.library.base.framework;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.violet.library.BaseApplication;
import com.violet.library.R;
import com.violet.library.manager.NetManager;


/**
 * 公用Activity，不包含任何业务，用于加载Fragment
 * Created by gjm on 2016/11/2.
 */
public class CommonActivity extends VioletBaseActivity implements Closeable {

    private static final String EXA_NAME = "name";
    private static final String EXA_ARGS = "args";

    /**
     * 启动公用的Activity
     * @param context 上下文对象
     * @param fragment Fragment
     */
    public static void start(Context context, Class<?> fragment) {
        start(context, fragment, null);
    }

    /**
     * 启动公用的Activity
     * @param from 源Fragment
     * @param fragment Fragment
     */
    public static void start4Result(Context context, Fragment from, Class<?> fragment) {
        start4Result(context, from, fragment, null);
    }

    /**
     * 启动公用的Activity
     * @param from 源Fragment
     * @param fragment Fragment
     */
    public static void start4Result(Context context, Fragment from, Class<?> fragment, Bundle args) {
        from.startActivityForResult(configIntent(context, fragment, args), 0);
    }

    /**
     * 启动公用的Activity
     * @param context 上下文对象
     * @param fragment Fragment
     * @param args Fragment参数
     */
    public static void start(Context context, Class<?> fragment, Bundle args) {
        context.startActivity(configIntent(context, fragment, args));
    }


    /**
     * 启动公用的Activity
     * @param context 上下文对象
     * @param fragment Fragment
     * @param id 参数id
     */
    public static void startWithId(Context context, Class<?> fragment, long id) {
        context.startActivity(configIntent(context, fragment, id));
    }

    public static Intent configIntent(Context context, Class<?> fragment) {
        return configIntent(context, fragment, null);
    }

    public static Intent configIntent(Context context, Class<?> fragment, Bundle args) {
        Intent intent = new Intent(context, CommonActivity.class);
        intent.putExtra(EXA_NAME, fragment.getName());
        intent.putExtra(EXA_ARGS, args);
        return intent;
    }

    public static Intent configIntent(Context context, Class<?> fragment, long id) {
        Intent intent = new Intent(context, CommonActivity.class);
        intent.putExtra(EXA_NAME, fragment.getName());
        Bundle args = new Bundle();
        args.putString(HP_Fragment.ARGS_ID, String.valueOf(id));
        intent.putExtra(EXA_ARGS, args);
        return intent;
    }


    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //固定竖屏(禁止横屏)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.base_activity_container);

        String fragmentName = getIntent().getStringExtra(EXA_NAME);
        Bundle args = getIntent().getBundleExtra(EXA_ARGS);
        if (!TextUtils.isEmpty(fragmentName)) {
            mFragment = Fragment.instantiate(this, fragmentName, args);
            // 添加Fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, mFragment,"parentfragment");
            ft.commit();
        } else {
            finish();
        }

//        registerReceiver(mNetStatusBroadcast,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
//        unregisterReceiver(mNetStatusBroadcast);
        super.onDestroy();
    }

    public void onClick(View v) {
        if (mFragment != null && mFragment instanceof View.OnClickListener) {
            ((View.OnClickListener) mFragment).onClick(v);
        }
    }

    @Override
    public void onBackPressed() {
        if(mFragment instanceof OnBackPressedListener
            && ((OnBackPressedListener) mFragment).onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void setClose(boolean close) {

    }

    @Override
    public boolean isClose() {
        return true;
    }

    /**
     * 网络监听
     */
    private BroadcastReceiver mNetStatusBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                if(mFragment != null && mFragment instanceof NetManager.NetEvent){
                    ((NetManager.NetEvent)mFragment).onNetChange(NetManager.getNetWorkState(BaseApplication.getInstance()));
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mFragment instanceof OnActivityResultListener){
            ((OnActivityResultListener) mFragment).onActivityResultHandler(requestCode,resultCode,data);
        }
    }
}
