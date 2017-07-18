package com.violet.library.base.framework;

import java.util.List;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author new7
 * adapter父类
 * @param <T>
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
	protected List<T> list;
	protected LayoutInflater mInflater;
	protected Context context;
	private int layoutId;
	
	public BaseListAdapter(Context context, List<T> list ,@LayoutRes int resId) {
		this.context = context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
		layoutId = resId;
	}
	
	public void addData(@NonNull List<T> list){
		this.list.addAll(list);
		notifyDataSetChanged();
	}
	
	public List<T> getData(){
		return list;
	}
	
	public void removeData(List<T> list){
		this.list.removeAll(list);
		notifyDataSetChanged();
	}
	
	public void clearAdapter(){
		list.clear();
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public T getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(layoutId,parent,false);
		}

		convertView(position,convertView,getItem(position));

		return convertView;
	}

	/**
	 * item处理
	 * @param view
	 * @param type
     */
	protected abstract void convertView(int position,View view,T type);
}
