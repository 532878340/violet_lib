package com.violet.library.base.framework;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.violet.library.R;
import com.violet.library.app.windows.dialog.MaterialProgress;
import com.violet.library.app.windows.toast.ToastWidget;
import com.violet.library.manager.ConfigsManager;
import com.violet.library.manager.L;
import com.violet.library.manager.LeakCanaryManager;
import com.violet.library.manager.NetManager;
import com.violet.library.utils.StringUtils;
import com.violet.library.views.LoadStatusBox;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;
import com.zhy.http.okhttp.request.OkHttpRequest;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Map;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * description：Fragment基类
 * author：JimG on 16/10/8 17:18
 * e-mail：info@zijinqianbao.com
 */

public class HP_Fragment extends Fragment implements JSONResponse.ResponseListener, NetManager.NetEvent{
    public static final String TAG = "HP_Fragment";

    protected FragmentActivity mActivity;
    protected LayoutInflater mInflater;

    /**
     * 4种请求情境(初始化请求、下拉刷新、上拉加载、页面手动请求)
     */
    protected static final String TAG_INIT = "init";
    protected static final String TAG_REFRESH = "refresh";
    protected static final String TAG_APPEND = "append";
    protected static final String TAG_DIALOG = "dialog";

    /**
     * 公用的传参key
     */
    public static final String ARGS_ID = "id";

    /**
     * 是否需要包含title
     */
    private static final String ARGS_HAS_TITLE = "has_title";

    /**
     * 是否需要启用eventbus
     */
    private static final String ARGS_EVENT_BUS = "event_bus";

    /**
     * 标识当前fragment是否首次对用户可见
     */
    private boolean mFirstVisible = true;

    /**
     * 加载页UI
     */
    protected LoadStatusBox mLoadingView;

    /**
     * 标题栏
     */
    protected TitleBar mTitleBar;

    private MaterialProgress mProgress;

    /**
     * 由getContentView初始化，子类的业务View
     */
    protected View mContentView;

    /**
     * onResume时，是否需要刷新
     */
    protected boolean refreshFlag;

    /**
     * Fragment参数
     */
    private Bundle arguments;

    /**
     * 标识是否destory
     */
    private boolean isDestory;

    public static Bundle enableEventBus(Bundle args) {
        args.putBoolean(ARGS_EVENT_BUS, true);
        return args;
    }

    public static Bundle enableEventBus() {
        return enableEventBus(new Bundle());
    }

    public static Bundle putId(Bundle args, String id) {
        args.putString(ARGS_ID, id);
        return args;
    }

    /**
     * 设置无标题显示
     *
     * @return Bundle
     */
    public static Bundle configNoTitle() {
        return configTitleArgs(false);
    }

    /**
     * 设置是否包含标题栏
     *
     * @param hasTitle true有标题栏，false无标题栏
     */
    public static Bundle configTitleArgs(boolean hasTitle) {
        Bundle args = new Bundle();
        args.putBoolean(ARGS_HAS_TITLE, hasTitle);
        return args;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
        mInflater = LayoutInflater.from(mActivity);
        arguments = getArguments();
        L.e("fragment init:" + getClass().getName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (arguments != null && arguments.getBoolean(ARGS_EVENT_BUS, false)) {
            EventBus.getDefault().register(this);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boolean hasTitle = hasTitleDefault();
        if (getArguments() != null) {
            hasTitle = getArguments().getBoolean(ARGS_HAS_TITLE, hasTitle);
        }
        // 根据参数加载不同的布局文件
        int layoutRes = hasTitle ? R.layout.base_fragment : R.layout.base_fragment_no_title;
        View layout = inflater.inflate(layoutRes, container, false);

        mLoadingView = (LoadStatusBox) layout.findViewById(R.id.loadStatusBox);
        setLoadingClickListener();
        // 初始化不可见
        mLoadingView.success();

        mTitleBar = new TitleBar(layout);
        ViewGroup contentContainer = (ViewGroup) layout.findViewById(R.id.contentContainer);
        mContentView = getContentView();
        if (mContentView == null) {
            final int contentRes = getContentRes();
            if (contentRes > 0) {
                mContentView = View.inflate(mActivity, contentRes, null);
            }
        }

        if (mContentView != null) {
            contentContainer.addView(mContentView, 0);
        }

        // butterKnife初始化控件
        ButterKnife.bind(this, mContentView);

        initViewOrData();
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化加载
        if (mFirstVisible && getUserVisibleHint()) {
            initRequest();
            mFirstVisible = false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mFirstVisible && isVisibleToUser && isVisible()) {
            initRequest();
            mFirstVisible = false;
        }
    }

    /**
     * 默认（未调用{@link #configTitleArgs(boolean)}情况下，是否添加title
     *
     * @return 是否含有title
     */
    protected boolean hasTitleDefault() {
        return true;
    }

    /**
     * 获取Fragment需要显示的View
     *
     * @return 获取根view
     */
    protected View getContentView() {
        return null;
    }

    /**
     * 获取Fragment需要显示的View的资源文件
     *
     * @return
     */
    @LayoutRes
    protected int getContentRes() {
        return 0;
    }

    /**
     * 处理特殊的初始化需求，无需求可不重写
     */
    protected void initViewOrData() {

    }

    /**
     * 设置LoadingView点击事件（重新请求）
     */
    private void setLoadingClickListener() {
        mLoadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request(getRequestParams());
            }
        });
    }

    /**
     * 初始化界面,自动调用网络请求
     */
    private void initRequest() {
        //自动请求开启且初始化参数不为空,则开始网络请求
        if (isAutoRequest() && getRequestParams() != null) {
//            performRequest(getRequestParams(), TAG_INIT);

            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_PHONE_STATE},PERMISSIONS_REQUEST_READ_PHONE);
            }else{
                request(getRequestParams());
            }
        }
    }

    private static final int PERMISSIONS_REQUEST_READ_PHONE = 999;//读取手机信息权限

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                request(getRequestParams());
            } else {
                ToastWidget.getInstance().warning("Permission Denied");
            }
        }
    }

    /**
     * 界面网络请求是否自动打开
     *
     * @return
     */
    protected boolean isAutoRequest() {
        return ConfigsManager.AUTO_REQUEST;
    }

    protected void setTitle(int title) {
        mTitleBar.setTitleBar(title);
    }

    protected void setTitle(CharSequence title) {
        mTitleBar.setTitleBar(title);
    }

    protected void setTitle(boolean showBack, int title) {
        mTitleBar.setTitleBar(showBack, title);
    }

    protected void setTitle(boolean showBack, CharSequence title) {
        mTitleBar.setTitleBar(showBack, title);
    }

    /**
     * 设置具有返回按钮的标题
     *
     * @param title
     */
    protected void setTitleWithBack(CharSequence title) {
        mTitleBar.setTitleBar(true, title);
    }

    /**
     * 获取请求参数，子类需要重写此方法，提供请求参数
     *
     * @return 请求参数
     */
    public Map<String, String> getRequestParams() {
        return null;
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
        if (refreshFlag) {
            performRefresh();
            refreshFlag = false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isDestory = true;

        //调试模式,开启内存检测
        if (ConfigsManager.DEBUG_ENABLE) {
            LeakCanaryManager.getInstance().getRefWatcher().watch(this);
        }

        if (arguments != null && arguments.getBoolean(ARGS_EVENT_BUS, false)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 初始化请求
     *
     * @param args
     */
    public void request(Map<String, String> args) {
        performRequest(args, TAG_INIT);
    }

    /**
     * 刷新请求
     *
     * @param args
     */
    public void requestForRefresh(Map<String, String> args) {
        performRequest(args, TAG_REFRESH);
    }

    /**
     * 加载更多请求
     *
     * @param args
     */
    public void requestForAppend(Map<String, String> args) {
        performRequest(args, TAG_APPEND);
    }

    /**
     * 此请求方式不处理LoadingView等UI逻辑，只发起网络请求，并显示对话框
     *
     * @param args
     */
    public void requestShowDialog(Map<String, String> args) {
        performRequest(args, TAG_DIALOG);
        if (mProgress == null) {
            mProgress = new MaterialProgress(mActivity);
        }
        mProgress.show();
    }

    /**
     * 刷新UI显示数据（默认实现为重新初始化请求）
     */
    protected void performRefresh() {
        request(getRequestParams());
    }

    /**
     * 网络请求
     *
     * @param args
     * @param tag
     */
    private void performRequest(Map<String, String> args, String tag) {
        if (args == null) {
            throw new NullPointerException("params can not be null,please try again!");
        }

        OkHttpRequestBuilder requestBuilder = null;
        RequestCall requestCall;

        NetManager.METHOD method = NetManager.METHOD.valueOf(args.get("method"));
        switch (method) {
            case GET://get请求
                requestBuilder = OkHttpUtils.get();
                break;
            case POST://post请求
                requestBuilder = OkHttpUtils.post();
                break;
        }

        requestBuilder = NetManager.buildRequestParams(mActivity, requestBuilder, args, tag);
        requestCall = requestBuilder.build();

        String paramStr = StringUtils.getUrlParamsByMap(args);
        if(NetManager.requestSet.contains(paramStr)){
            L.d(TAG,"重复请求 不处理");
            return;
        }else if(!TextUtils.isEmpty(paramStr)){
            NetManager.requestSet.add(paramStr);
        }

        JSONResponse response = new JSONResponse();
        response.setRequestCall(requestCall);
        response.setResponseListener(this);

        requestCall.execute(response);

        //显示Loadingview
        if (TAG_INIT.equals(tag)) {
            mLoadingView.loading();
        }
    }

    /**
     * 网络请求成功,有数据返回
     *
     * @param requestCall
     * @param response
     * @param id          {@link OkHttpResponse.ResponseListener}
     */
    @Override
    public void onSuccess(RequestCall requestCall, JSONObject response, int id) {
        handlerRepeatSubmit(requestCall);

        if(isDestory) return;

        ParentEntity parentEntity = new Gson().fromJson(response.toString(), ParentEntity.class);
        String tag = (String) requestCall.getRequest().tag();

        if (parentEntity == null) return;

        if(parentEntity.go2Login()){
            Log.d(TAG, "onSuccess: 跳转登录界面");
            try {
                Class cls = Class.forName("com.deelon.zijinqianbao.userinterface.fragment.user.LoginFragment");
                CommonActivity.start(mActivity,cls,enableEventBus());
                mActivity.finish();
                return;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        //处理loadingview
        if (TAG_INIT.equals(tag)) {
            if (parentEntity.isSuccessful()) {//获取数据正常,显示UI界面
                mLoadingView.success();
            } else {//获取数据不正常,显示loading失败界面
                mLoadingView.failed();
            }
        }

        //不论请求结果,隐藏对话框
        if (TAG_DIALOG.equals(tag)) {
            if (mProgress != null) mProgress.dismiss();
        }

        //方法回调处理
        if (parentEntity.isSuccessful()) {
            onNetSuccess(requestCall, response);
        } else {
            onNetIllegal(requestCall, response);
        }
    }

    /**
     * 数据返回失败,报错了
     *
     * @param call
     * @param e
     * @param id   {@link OkHttpResponse.ResponseListener}
     */
    @Override
    public void onFailed(Call call, Exception e, int id) {
        handlerRepeatSubmit(null);
        if(isDestory) return;

        String tag = (String) call.request().tag();

        //初始化失败,显示加载失败界面
        if (TAG_INIT.equals(tag)) {
            mLoadingView.failed();
        }

        //隐藏对话框
        if (TAG_DIALOG.equals(tag)) {
            if (mProgress != null) mProgress.dismiss();
        }

        onNetFailed(call, e);
    }

    /**
     * 数据正常返回,子类处理
     *
     * @param requestCall
     * @param response
     */
    public void onNetSuccess(RequestCall requestCall, JSONObject response) {

    }

    /**
     * 数据非正常返回,子类处理
     *
     * @param requestCall
     * @param response
     */
    public void onNetIllegal(RequestCall requestCall, JSONObject response) {
        if (response != null) {
            //弹窗提示,必要时子类可处理
            ToastWidget.getInstance().warning(response.optString("description"));
        }
    }

    /**
     * 网络请求失败,此方法回调
     *
     * @param call
     * @param e
     */
    public void onNetFailed(Call call, Exception e) {

    }

    @Override
    public void onNetChange(int netStatus) {
        if (netStatus == NetManager.NETWORK_NONE) {
            mLoadingView.failed();
        } else if (getRequestParams() != null) {
//            request(getRequestParams());
        }
    }

    /**
     * 处理重复提交
     * @param requestCall
     * @return
     */
    private void handlerRepeatSubmit(RequestCall requestCall){
        if(requestCall != null){
            String requestMap = null;
            if(requestCall.getOkHttpRequest() != null){
                OkHttpRequest okHttpRequest = requestCall.getOkHttpRequest();

                try {
                    Field filed = OkHttpRequest.class.getDeclaredField("params");
                    filed.setAccessible(true);
                    Map<String, String> param = (Map<String, String>) filed.get(okHttpRequest);
                    requestMap = StringUtils.getUrlParamsByMap(param);

                    L.d(TAG, "handlerRepeatSubmit: " + requestMap);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            if(!TextUtils.isEmpty(requestMap) && NetManager.requestSet.contains(requestMap)){
                NetManager.requestSet.remove(requestMap);
            }
        }else{
            NetManager.requestSet.clear();
        }
    }
}
