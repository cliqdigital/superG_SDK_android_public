package com.cliqdigital.supergsdk.utils;

public class Event {

	//private variables
	String _guid;
	String _date;
	String _name;
	String _body;

	// Empty constructor
	public Event(){
	}

	// constructor
	public Event(String guid, String date, String name, String body ){
		this._guid = guid;
		this._date = date;
		this._name = name;
		this._body = body;
	}

	// getting guid
	public String getGuid(){
		return this._guid;
	}

	// setting guid
	public void setGuid(String guid){
		this._guid = guid;
	}

	// getting date
	public String getDate(){
		return this._date;
	}

	// setting date
	public void setDate(String date){
		this._date = date;
	}    

	// getting name
	public String getName(){
		return this._name;
	}

	// setting name
	public void setName(String name){
		this._name = name;
	} 

	// getting body
	public String getBody(){
		return this._body;
	}

	// setting guid
	public void setBody(String body){
		this._body = body;
	}    

}