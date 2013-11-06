package com.cliqdigital.supergsdk.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "superG_Events";
 
    // Events table name
    private static final String TABLE_EVENTS = "events";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists events(guid text, date text, name text, body text)");
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
// 
//        // Create tables again
//        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new event
    void addEvent(Event event ) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put("guid", event.getGuid());
        values.put("date", event.getDate());        
        values.put("name", event.getName());
        values.put("body", event.getBody());        
        // Inserting Row
        db.insert(TABLE_EVENTS, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single event
    Event getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_EVENTS, new String[] { "guid",
                "date", "name", "body" }, "guid" + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Event event = new Event(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
        db.close(); // Closing database connection        
        // return event
        return event;
    }
     
    // Getting All Events
    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<Event>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setGuid(cursor.getString(0));
                event.setDate(cursor.getString(1));
                event.setName(cursor.getString(2));
                event.setBody(cursor.getString(3));
                // Adding event to list
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        db.close(); // Closing database connection
        // return event list
        return eventList;
    }
 
    // Updating single event
    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put("guid", event.getGuid());
        values.put("date", event.getDate());
        values.put("name", event.getName());
        values.put("body", event.getBody());        
        db.close(); // Closing database connection
        // updating row
        return db.update(TABLE_EVENTS, values, "guid" + " = ?",new String[] { String.valueOf(event.getGuid()) });
    }
 
    // Deleting single event
    public void deleteEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, "guid" + " = ?",
                new String[] { String.valueOf(event.getGuid()) });
        db.close();
    }
    
    // Deleting all events
    public void deleteAllEvents() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM events");
        db.close();
    }    
 
 
    // Getting events Count
    public int getEventsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close(); // Closing database connection
        // return count
        return cursor.getCount();
    }
 
}