package com.example.emergencysystem.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.emergencysystem.Models.Role;
import com.example.emergencysystem.Models.User;

import java.util.ArrayList;

public class CacheHelper extends SQLiteOpenHelper {

    /**
     * Version constant to increment when the database should be rebuilt
     */
    private static final int VERSION = 1;

    /**
     * Name of database file
     */
    private static final String NAME = "cache.db";

    /**
     * @param context
     */
    public CacheHelper(final Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        String sql= "CREATE TABLE IF NOT EXISTS USERS ( id TEXT ,prenom TEXT ,nom TEXT ,role TEXT ,email TEXT )";
        // la creation de la table users
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
                          final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USERS");
        onCreate(db);
    }

    public void addUser(User user,String role){
        removeUser();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",user.getId());
        contentValues.put("prenom",user.getPrenom());
        contentValues.put("nom",user.getNom());
        contentValues.put("role",role);
        contentValues.put("email",user.getEmail());

        sqLiteDatabase.insert("USERS",null,contentValues);
        sqLiteDatabase.close();
    }

    public User getUser(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor= sqLiteDatabase.rawQuery("select * from USERS",null);
        if(cursor.moveToFirst()){
            User user = new User();
            user.setEmail(cursor.getString(4));
            user.setId(cursor.getString(0));
            user.setNom(cursor.getString(2));
            user.setPrenom(cursor.getString(1));
            if(cursor.getString(3).equals(Role.user.toString())) {
                user.setRole(Role.user);
            }
            else {
                user.setRole(Role.Admin);
            }
            return user;
        }
        return null;
    }

    public ArrayList<User> getListUsers(){
        ArrayList<User> users = new ArrayList<User>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor= sqLiteDatabase.rawQuery("select * from USERS",null);
        if(cursor.moveToFirst()){
            do{
                User user = new User();
                user.setEmail(cursor.getString(4));
                user.setId(cursor.getString(0));
                user.setNom(cursor.getString(2));
                user.setPrenom(cursor.getString(1));
                if(cursor.getString(3).equals(Role.user.toString()))
                    user.setRole(Role.user);
                else
                    user.setRole(Role.Admin);
                users.add(user);
            }while(cursor.moveToNext());

            return users;
        }
        return users;
    }

    public void removeUser(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM USERS");
        sqLiteDatabase.close();
    }

}
