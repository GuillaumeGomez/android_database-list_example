package com.dbexample;

public class DataHandler {
	public String 	title;
	public String 	description;
	public int		id;
	public int		number = 0;
	
	public DataHandler(int i, String t, String d){
		id = i;
		title = t;
		description = d;
	}
	
	public DataHandler(int i, String t, String d, int n){
		id = i;
		title = t;
		description = d;
		number = n;
	}
	
	public DataHandler(DataHandler d){
		id = d.id;
		title = d.title;
		description = d.description;
		number = d.number;
	}
}
