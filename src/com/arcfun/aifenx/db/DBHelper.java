package com.arcfun.aifenx.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "user.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_TOKEN = "tokens";
    private static final String CREATE_INFO = "create table if not exists tokens("
            + "id integer primary key autoincrement,token varchar(80))";
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_INFO);
    }

    public void insert(ContentValues values, String tableName) {
        if (db == null) {
            db = getWritableDatabase();
        }
        db.insert(tableName, null, values);
        db.close();
    }

    public Cursor query(String tableName) {
        if (db == null) {
            db = getWritableDatabase();
        }
        Cursor c = db.query(tableName, null, null, null, null, null, null);
        return c;
    }

    public Cursor rawQuery(String sql, String[] args) {
        if (db == null) {
            db = getWritableDatabase();
        }
        Cursor c = db.rawQuery(sql, args);
        return c;
    }

    public void execSQL(String sql) {
        if (db == null) {
            db = getWritableDatabase();
        }
        db.execSQL(sql);
    }

    public void delete(int id) {
        if (db == null) {
            db = getWritableDatabase();
        }
        db.delete(TABLE_TOKEN, "id=?", new String[] { String.valueOf(id) });
    }

    @Override
    public void close() {
        if (db != null)
            db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
