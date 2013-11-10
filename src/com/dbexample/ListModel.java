package com.dbexample;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ListModel extends BaseAdapter implements OnClickListener {

	private ListFragment activity;
	private ArrayList<DataHandler> data;
	private static LayoutInflater inflater = null;

	public ListModel(ListFragment a, ArrayList<DataHandler> d, Resources resLocal) {
		activity = a;
		data = d;

		inflater = (LayoutInflater)activity.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public static class ViewHolder{
		public TextView text;
		public CheckBox box;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;

		if (convertView == null){
			vi = inflater.inflate(R.layout.element, null);

			holder = new ViewHolder();
			holder.text = (TextView)vi.findViewById(R.id.list_text);
			holder.box = (CheckBox)vi.findViewById(R.id.list_check);

			vi.setTag(holder);
		}
		else
			holder = (ViewHolder)vi.getTag();

		if (data.size() > 0){
			DataHandler tempValues = new DataHandler(data.get(position));
			
			holder.text.setText(tempValues.title);
			holder.box.setId(tempValues.number);
			
			vi.setOnClickListener(new OnItemClickListener(position));
			vi.setOnLongClickListener(new OnItemLongClickListener(position));
		}
		return vi;
	}

	private class OnItemClickListener implements OnClickListener{           
		private int mPosition;

		OnItemClickListener(int position){
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {
			activity.onItemClick(mPosition);
		}         
	}
	
	private class OnItemLongClickListener implements OnLongClickListener{           
		private int mPosition;

		OnItemLongClickListener(int position){
			mPosition = position;
		}

		@Override
		public boolean onLongClick(View arg0) {
			activity.onItemLongClick(mPosition);
			return false;
		}         
	}

	@Override
	public void onClick(View v) {
	}
}

