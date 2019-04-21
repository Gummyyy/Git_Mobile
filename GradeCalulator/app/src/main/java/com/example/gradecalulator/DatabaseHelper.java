package com.example.gradecalulator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // database helper class for doing some sql command
    public DatabaseHelper(Context context) {
        super(context, "login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL("create table user(FName text, LName text, username text primary key, password text, Email text, Phone text, Program text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
    }

    ///check for login
    public Boolean toLogin(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user where username=? and password=?", new String[]{username, password});
        if(cursor.getCount() > 0) return true;
        else return false;
    }

    ///checking if username exists
    public Boolean checkUser(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user where username = ?", new String[]{username});
        if(cursor.getCount() > 0) return false;
        else return true;
    }

    ///checking if email exists
    public Boolean checkEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user where email=?", new String[]{email});
        if(cursor.getCount() > 0) return false;
        else return true;
    }

    ///inserting
    public boolean insert(String username, String password, String email, String fname, String lname, String phone, String program){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("email", email);
        contentValues.put("Fname", fname);
        contentValues.put("Lname", lname);
        contentValues.put("Phone", phone);
        contentValues.put("Program", program);

        long ins = db.insert("user", null, contentValues);
        if(ins == -1) return false;
        else return true;
    }

    public Cursor getInfo(String s1){
         String[] COLUMNS = {"username","FName","LName","Email","Phone","Program"};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("user", COLUMNS , "username = ?", new String[]{s1}, null, null,null);

        return cursor;
    }
}

