package com.dbexample;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ListFragment extends Fragment {
	public  ArrayList<DataHandler> listViewValues = new ArrayList<DataHandler>();

	private DatabaseHandler	db = null;
	private View view;
	private ArrayList<Integer>	ar = new ArrayList<Integer>();
	
	public static final String[] tables = {"db1", "db2"};
	public static int pos = 0;

	public static String getTableName() {
		if (pos == 1)
			pos = 0;
		else
			pos = 1;
		return tables[pos];
	}

	public int addCheck(int pos) {
		ar.add(pos);
		return ar.size();
	}

	public int removeCheck(int pos) {
		ar.remove(Integer.valueOf(pos));
		return ar.size();
	}

	public int getChecked() {
		return ar.size();
	}

	public ArrayList<DataHandler>	getCheckedItem() {
		ArrayList<DataHandler> list = new ArrayList<DataHandler>();

		for (Integer i : ar) {
			list.add(listViewValues.get(i));
		}
		ar.clear();
		return list;
	}

	public void removeItem(DataHandler d) {
		db.deleteData(d.id);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DatabaseHandler(this.getActivity(), ListFragment.getTableName());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.list_viewer, container, false);

		this.drawAct();
		return view;
	}

	public void deleteEntry(DataHandler v){
		db.deleteData(v.id);
		listViewValues.remove(v);
		this.drawAct();
	}

	public void drawAct() {
		if (db != null) {
			listViewValues = db.getValues();
			ListView list = (ListView)view.findViewById(R.id.list);
			list.setAdapter(new ListModel(this, listViewValues, getResources()));
		}
	}

	public void addValueToDb(String t, String d){
		db.addValue(t, d);
		this.drawAct();
	}

	public void onItemClick(int pos){
		DataHandler d = listViewValues.get(pos);
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

		builder.setMessage(d.description);
		builder.setTitle(d.title);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});
		builder.create().show();
	}

	public void onItemLongClick(int pos){
	}
}