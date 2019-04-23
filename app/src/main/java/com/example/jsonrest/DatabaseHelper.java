package com.example.jsonrest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "json_rest_db";
    static final String TABLE_SERVERS = "servers";
    static final String CREATE_TABLE_SERVERS;
    static final String COLUMN_ID = "id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_IP = "ip";
    static final String COLUMN_PORT = "port";

    static {
        CREATE_TABLE_SERVERS = String.format("CREATE TABLE `%s`("
                + "`%s` INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "`%s` TEXT, "
                + "`%s` TEXT, "
                + "`%s` TEXT);", TABLE_SERVERS, COLUMN_ID, COLUMN_NAME, COLUMN_IP, COLUMN_PORT);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(CREATE_TABLE_SERVERS);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = String.format("DROP TABLE IF EXISTS `%s`", TABLE_SERVERS);
        db.execSQL(sql);

        onCreate(db);
    }

    public long insertServer(ServerDataModel server) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, server.name);
        values.put(COLUMN_IP, server.ip);
        values.put(COLUMN_PORT, server.port);

        // insert row
        long id = db.insert(TABLE_SERVERS, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public ServerDataModel getServer(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SERVERS,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_IP, COLUMN_PORT},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        ServerDataModel model = new ServerDataModel(
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_IP)),
                cursor.getString(cursor.getColumnIndex(COLUMN_PORT))
        );

        cursor.close();

        return model;
    }

    public ArrayList<ServerDataModel> getAllServers() {
        ArrayList<ServerDataModel> servers = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SERVERS,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_IP, COLUMN_PORT},
                null,
                null, null, null, null, null);
//
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " ORDER BY " +
//                Note.COLUMN_TIMESTAMP + " DESC";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);

        ServerDataModel model;
        if (cursor.moveToFirst()) {
            do {
                model = new ServerDataModel(
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_IP)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PORT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ID)));

                servers.add(model);
            } while (cursor.moveToNext());
        }

        db.close();

        return servers;
    }

    public int getServerCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SERVERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateServer(ServerDataModel server) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, server.name);
        values.put(COLUMN_IP, server.ip);
        values.put(COLUMN_PORT, server.port);

        // updating row
        int res = db.update(TABLE_SERVERS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(server.getId())});
        return res;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERVERS, null,
                null);
        db.close();
    }

    public void deleteServer(ServerDataModel server) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERVERS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(server.getId())});
        db.close();
    }
}
