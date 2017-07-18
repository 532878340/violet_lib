package com.violet.library.base.framework;

import android.view.View;

import com.violet.library.pulltorefresh.PullToRefreshBase;
import com.violet.library.pulltorefresh.PullToRefreshScrollView;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

/**
 * description：嵌套PullToRefreshScrollView
 * author：JimG on 16/10/11 11:57
 * e-mail：info@zijinqianbao.com
 */

public abstract class HP_ScrollFragment extends HP_Fragment implements PullToRefreshBase.OnRefreshListener{
    protected PullToRefreshScrollView mRefreshView;

    @Override
    protected final View getContentView() {
        mRefreshView = new PullToRefreshScrollView(mActivity);
        mRefreshView.setOnRefreshListener(this);
        View layout = getLayoutView();
        if (layout != null) {
            mRefreshView.getRefreshableView().addView(layout);
        }
        return mRefreshView;
    }

    /**
     * 获取业务布局View，用于填充到ScrollView中
     * @return
     */
    public abstract View getLayoutView();

    /**
     * 获取请求参数
     * @return
     */
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public void onNetSuccess(RequestCall requestCall, JSONObject response) {
        super.onNetSuccess(requestCall, response);
        mRefreshView.setPullCompletedAndDate(true);
    }

    @Override
    public void onNetFailed(Call call, Exception e) {
        super.onNetFailed(call, e);
        mRefreshView.setPullCompletedAndDate(false);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        Map<String, String> params = getRequestParams();
        if (params != null) {
            requestForRefresh(params);
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    protected void performRefresh() {
        requestForRefresh(getRequestParams());
    }
}
