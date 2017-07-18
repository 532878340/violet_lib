package com.violet.library.base.framework;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.violet.library.R;
import com.violet.library.app.windows.toast.ToastWidget;
import com.violet.library.manager.ConstantsManager;
import com.violet.library.pulltorefresh.PullToRefreshBase;
import com.violet.library.pulltorefresh.PullToRefreshListView;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * description：数据列表基类
 * author：JimG on 16/10/10 16:11
 * e-mail：info@zijinqianbao.com
 */

public abstract class HP_ListFragment<T> extends HP_Fragment implements PullToRefreshBase.OnRefreshListener,AdapterView.OnItemClickListener{
    protected PullToRefreshListView mRefreshView;
    protected ListView mListView;

    /**
     * 标识当前页，用于分页请求
     */
    private int mCurrentPage = 1;

    private BaseListAdapter<T> mAdapter;

    protected List<T> mData = new ArrayList<>();

    @Override
    protected View getContentView() {
        mRefreshView = new PullToRefreshListView(mActivity);
        mRefreshView.setOnRefreshListener(this);
        mRefreshView.setPullLoadEnabled(isLoadMoreEnable());

        mListView = mRefreshView.getRefreshableView();
        initListView(mListView);

        mAdapter = new ListAdapter(mActivity,mData,getItemResource());
        mListView.setAdapter(mAdapter);

        return mRefreshView;
    }

    /**
     * 初始化listview样式
     * @param lv
     */
    protected void initListView(ListView lv) {
        lv.setSelector(new ColorDrawable());
        lv.setCacheColorHint(Color.TRANSPARENT);
        lv.setDivider(new ColorDrawable(getResources().getColor(R.color.divider)));
        lv.setDividerHeight(1);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void request(Map<String, String> args) {
        args.put("reqPageNum", String.valueOf(mCurrentPage));
        args.put("maxResults", String.valueOf(ConstantsManager.SINGLEPAGE_LENGTH));
        super.request(args);
    }

    @Override
    protected void performRefresh() {
        onPullDownToRefresh(mRefreshView);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        Map<String, String> params = getRequestParams();
        if (params != null) {
            params.put("reqPageNum", String.valueOf(mCurrentPage = 1));
            params.put("maxResults", String.valueOf(ConstantsManager.SINGLEPAGE_LENGTH));
            requestForRefresh(params);
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        Map<String, String> params = getRequestParams();
        if (params != null) {
            params.put("reqPageNum", String.valueOf(++ mCurrentPage));
            params.put("maxResults", String.valueOf(ConstantsManager.SINGLEPAGE_LENGTH));
            requestForAppend(params);
        }
    }

    @Override
    public void onNetSuccess(RequestCall requestCall, JSONObject response) {
        super.onNetSuccess(requestCall, response);
        response = response.optJSONObject("data");

        mRefreshView.setPullCompletedAndDate(true);
        String listJson = response.optString("list");
        int totalNum = response.optInt("recordCount");

        //每个接口返回字段可能不一致,如果需要,可复写此方法
        commonBindItems(requestCall, listJson, totalNum);
    }

    /**
     * 统一的数据处理
     * @param request
     * @param listJson json数组
     * @param totalNum 总数量
     */
    protected void commonBindItems(RequestCall request, String listJson, int totalNum) {
//        Class<T> cls = getItemType();
        Type type = getClass().getGenericSuperclass();
        ParameterizedType p = (ParameterizedType) type;
        Class<T> cls = (Class<T>) p.getActualTypeArguments()[0];
        if (!TextUtils.isEmpty(listJson) && cls != null) {
            final Object tag = request.getRequest().tag();
            mData = JSON.parseArray(listJson,cls);

            if(TAG_REFRESH.equals(tag) || TAG_INIT.equals(tag)){
                mAdapter.clearAdapter();
            }

            if (TAG_APPEND.equals(tag) && mData.isEmpty()) {
                ToastWidget.getInstance().warning("数据已加载完毕");
            }

            mAdapter.addData(mData);
//            if (mData.size() < ConstantsManager.SINGLEPAGE_LENGTH || totalNum == mAdapter.getCount()) {
//                // 数据请求完毕
//                mRefreshView.setHasMoreData(false);
//            }

            mRefreshView.setPullLoadEnabled(totalNum > mAdapter.getCount());
            mRefreshView.setHasMoreData(totalNum > mAdapter.getCount());
        }
    }


    @Override
    public void onNetFailed(Call call, Exception e) {
        super.onNetFailed(call, e);
        mRefreshView.setPullCompletedAndDate(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //ListView监听
    }

    /**
     * 是否开启PullToRefreshListView加载更多，默认开启
     * @return
     */
    protected boolean isLoadMoreEnable() {
        return true;
    }

    /**
     * 通用适配器
     */
    class ListAdapter extends BaseListAdapter<T>{
        public ListAdapter(Context context, List<T> list, int resId) {
            super(context, list, resId);
        }

        @Override
        public void clearAdapter() {
            super.clearAdapter();
            updateEmpty();
        }

        @Override
        protected void convertView(int position, View view, T type) {
            bindsView(position,view,type);
        }

        @Override
        public void addData(List<T> list) {
            super.addData(list);
            updateEmpty();
        }

        /**
         * 处理数据为空的情况
         */
        private void updateEmpty() {
            if (mData.isEmpty()) {
                mLoadingView.empty();
            } else {
                mLoadingView.success();
            }
        }
    }

    /**
     * 返回子item的布局资源
     * @return
     */

    public abstract int getItemResource();

    /**
     * 设置item的view与数据的绑定
     * 此处使用方法
     * {@link com.violet.library.utils.ViewHolderUtil#get(View, int)}
     */
    public void bindsView(int position, View view, T itemEntity){

    }

    /**
     * 子类覆盖此方法，进行统一的数据处理
     * @return
     */
//    protected abstract Class<T> getItemType();
}
