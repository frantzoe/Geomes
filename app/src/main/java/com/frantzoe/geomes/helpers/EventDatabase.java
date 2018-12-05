package com.frantzoe.geomes.helpers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.frantzoe.geomes.models.Event;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;


public class EventDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "eventsdb";

    private static final String TAB_EVENT = "event";
    private static final String COL_EVENT_ID = "_id";
    private static final String COL_EVENT_UID = "uid";
    private static final String COL_EVENT_LAT = "latitude";
    private static final String COL_EVENT_LON = "longitude";
    private static final String COL_EVENT_DIR = "direction";
    private static final String COL_EVENT_CON = "confirmed";
    private static final String COL_EVENT_PHO = "phone";
    private static final String COL_EVENT_DAT = "date";

    public EventDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + TAB_EVENT + "("
                + COL_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_EVENT_UID + " TEXT, "
                + COL_EVENT_LAT + " REAL, "
                + COL_EVENT_LON + " REAL, "
                + COL_EVENT_DIR + " TEXT, "
                + COL_EVENT_CON + " INTEGER, "
                + COL_EVENT_PHO + " TEXT, "
                + COL_EVENT_DAT + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        //
    }

    public List<Event> getConfirmedEvents() throws SQLException {
        List<Event> events = null;
        Cursor cursor = getReadableDatabase().query(false, TAB_EVENT, new String[] {COL_EVENT_ID, COL_EVENT_UID, COL_EVENT_LAT, COL_EVENT_LON, COL_EVENT_DIR, COL_EVENT_CON, COL_EVENT_PHO, COL_EVENT_DAT},
                COL_EVENT_CON + " = 1", null, null, null, null, null);
        if (cursor != null) {
            events = new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                events.add(new Event(
                        cursor.getInt(cursor.getColumnIndex(COL_EVENT_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_EVENT_UID)),
                        cursor.getDouble(cursor.getColumnIndex(COL_EVENT_LAT)),
                        cursor.getDouble(cursor.getColumnIndex(COL_EVENT_LON)),
                        cursor.getString(cursor.getColumnIndex(COL_EVENT_DIR)),
                        cursor.getInt(cursor.getColumnIndex(COL_EVENT_CON)) == 1,
                        cursor.getString(cursor.getColumnIndex(COL_EVENT_PHO)),
                        cursor.getString(cursor.getColumnIndex(COL_EVENT_DAT)),
                        null
                ));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return events;
    }

    public List<Event> getEventsByDir(String direction) throws SQLException {
        List<Event> events = null;
        Cursor cursor = getReadableDatabase().query(false, TAB_EVENT, new String[] {COL_EVENT_ID, COL_EVENT_UID, COL_EVENT_LAT, COL_EVENT_LON, COL_EVENT_DIR, COL_EVENT_CON, COL_EVENT_PHO, COL_EVENT_DAT},
                COL_EVENT_DIR + " LIKE '" + direction + "'", null, null, null, null, null);
        if (cursor != null) {
            events = new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                events.add(new Event(
                        cursor.getInt(cursor.getColumnIndex(COL_EVENT_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_EVENT_UID)),
                        cursor.getDouble(cursor.getColumnIndex(COL_EVENT_LAT)),
                        cursor.getDouble(cursor.getColumnIndex(COL_EVENT_LON)),
                        cursor.getString(cursor.getColumnIndex(COL_EVENT_DIR)),
                        cursor.getInt(cursor.getColumnIndex(COL_EVENT_CON)) == 1,
                        cursor.getString(cursor.getColumnIndex(COL_EVENT_PHO)),
                        cursor.getString(cursor.getColumnIndex(COL_EVENT_DAT)),
                        null
                ));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return events;
    }

    public Event getEvent (String uid, String dir) {
        Event event = null;
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM "
                        + TAB_EVENT
                        + " WHERE "
                        + COL_EVENT_UID + " LIKE " + uid + " AND " + COL_EVENT_DIR + " LIKE " + dir, null
        );
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            event = new Event(
                    cursor.getInt(cursor.getColumnIndex(COL_EVENT_ID)),
                    uid,
                    cursor.getDouble(cursor.getColumnIndex(COL_EVENT_LAT)),
                    cursor.getDouble(cursor.getColumnIndex(COL_EVENT_LON)),
                    cursor.getString(cursor.getColumnIndex(COL_EVENT_DIR)),
                    cursor.getInt(cursor.getColumnIndex(COL_EVENT_CON)) == 1,
                    cursor.getString(cursor.getColumnIndex(COL_EVENT_PHO)),
                    cursor.getString(cursor.getColumnIndex(COL_EVENT_DAT)),
                    null
            );
            cursor.close();
        }
        return event;
    }

    public void deleteEvent(String uid, String dir) {
        //final String whereClause = dir == null ? COL_EVENT_UID + " LIKE ?" : COL_EVENT_UID + " LIKE ? AND " + COL_EVENT_DIR + " LIKE ?";
        final String whereClause = COL_EVENT_UID + " LIKE ? AND " + COL_EVENT_DIR + " LIKE ?";
        final String[] whereArgs = {uid, dir};
        getReadableDatabase().delete(TAB_EVENT, whereClause, whereArgs);
    }

    public long getEventsCount() {
        return DatabaseUtils.queryNumEntries(getReadableDatabase(), TAB_EVENT);
    }

    public void addEvent(String uid, double lat, double lon, String dir, boolean con, String pho, String dat){
        final ContentValues values = new ContentValues();
        values.put(COL_EVENT_UID, uid);
        values.put(COL_EVENT_LAT, lat);
        values.put(COL_EVENT_LON, lon);
        values.put(COL_EVENT_DIR, dir);
        values.put(COL_EVENT_CON, con);
        values.put(COL_EVENT_PHO, pho);
        values.put(COL_EVENT_DAT, dat);
        getWritableDatabase().insert(TAB_EVENT, null, values);
    }

    public void confirmEvent(String uid, String dir){
        final ContentValues values = new ContentValues();
        final String whereClause = COL_EVENT_UID + " LIKE ? AND " + COL_EVENT_DIR + " LIKE ?";
        final String[] whereArgs = {uid, dir};
        values.put(COL_EVENT_CON, 1);
        getWritableDatabase().update(TAB_EVENT, values, whereClause, whereArgs);
    }
}
