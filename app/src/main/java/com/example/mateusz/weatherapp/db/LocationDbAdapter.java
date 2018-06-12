package com.example.mateusz.weatherapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocationDbAdapter {
    private static final String DEBUG_TAG = "SqLiteLocationManager";

    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "location.db";
    private static final String DB_LOCATION_TABLE = "locations";

    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int ID_COLUMN = 0;
    public static final String KEY_LONGITUDE = "longitude";
    public static final String LONGITUDE_OPTIONS = "DECIMAL(5,2) NOT NULL";
    public static final int LONGITUDE_COLUMN = 1;
    public static final String KEY_LATITUDE = "latitude";
    public static final String LATITUDE_OPTIONS = "DECIMAL(5,2) NOT NULL";
    public static final int LATITUDE_COLUMN = 2;
    public static final String KEY_CITY = "city";
    public static final String CITY_OPTIONS = "TEXT NOT NULL UNIQUE";
    public static final int CITY_COLUMN = 3;

    private static final String DB_CREATE_LOCATION_TABLE =
            "CREATE TABLE " + DB_LOCATION_TABLE + "( " +
                    KEY_ID + " " + ID_OPTIONS + ", " +
                    KEY_LONGITUDE + " " + LONGITUDE_OPTIONS + ", " +
                    KEY_LATITUDE + " " + LATITUDE_OPTIONS + ", " +
                    KEY_CITY + " " + CITY_OPTIONS +
                    ");";
    private static final String DROP_LOCATION_TABLE =
            "DROP TABLE IF EXISTS " + DB_LOCATION_TABLE;

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_LOCATION_TABLE);

            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table " + DB_LOCATION_TABLE + " ver." + DB_VERSION + " created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_LOCATION_TABLE);

            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table " + DB_LOCATION_TABLE + " updated from ver." + oldVersion + " to ver." + newVersion);
            Log.d(DEBUG_TAG, "All data is lost.");

            onCreate(db);
        }

    }

    public LocationDbAdapter(Context context) {
        this.context = context;
    }

    public LocationDbAdapter open() {
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertLocation(double longitude, double latitude, String cityName) {
        ContentValues newLocationValues = new ContentValues();
        newLocationValues.put(KEY_LONGITUDE, longitude);
        newLocationValues.put(KEY_LATITUDE, latitude);
        newLocationValues.put(KEY_CITY, cityName);
        return db.insert(DB_LOCATION_TABLE, null, newLocationValues);
    }

    public boolean deleteLocation(long id) {
        String where = KEY_ID + "=" + id;
        return db.delete(DB_LOCATION_TABLE, where, null) > 0;
    }

    public Cursor getAllLocations() {
        String[] columns = {KEY_ID, KEY_LONGITUDE, KEY_LATITUDE, KEY_CITY};
        return db.query(DB_LOCATION_TABLE, columns, null, null, null, null, null);
    }

    public LocationModel getLocation(long id) {
        String[] columns = {KEY_ID, KEY_LONGITUDE, KEY_LATITUDE, KEY_CITY};
        String where = KEY_ID + " = " + id;
        Cursor cursor = db.query(DB_LOCATION_TABLE, columns, where, null, null, null, null);
        LocationModel location = null;
        if (cursor != null && cursor.moveToFirst()) {
            Double longitude = cursor.getDouble(LONGITUDE_COLUMN);
            Double latitude = cursor.getDouble(LATITUDE_COLUMN);
            String city = cursor.getString(CITY_COLUMN);
            location = new LocationModel(id, longitude, latitude, city);
        }
        return location;
    }

    public int getLocationID(String cityName) {
        String[] columns = {KEY_ID};
        String where = KEY_CITY + "=" + "\'" + cityName + "\'";
        Cursor cursor = db.query(DB_LOCATION_TABLE, columns, where, null, null, null, null);
        int id = 0;
        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getInt(ID_COLUMN);
        }
        return id;
    }

    public int getSize() {
        Cursor cursor = getAllLocations();
        return cursor.getCount();
    }
}
