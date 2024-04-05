package com.example.lectorqr.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.lectorqr.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class DbUser extends DBHelper{
    Context context;

    public DbUser(@Nullable Context context) {
        super(context);
        this.context=context;
    }
    public void MailDB(String CURP){
        DBHelper dbHelper = new DBHelper(context);

        SQLiteDatabase db=dbHelper.getWritableDatabase();

        UrDt user = null;
        Cursor cursorUser = null;

        cursorUser=db.rawQuery("SELECT * FROM "+TABLE_MAIL+" WHERE Curp=" +"'"+CURP+"'", null);
        if(cursorUser.moveToFirst()){
            do{
                user = new UrDt();
                user.setRemail(cursorUser.getString(2));
                user.setSemail(cursorUser.getString(3));
                user.setPswd(cursorUser.getString(4));
            } while (cursorUser.moveToNext());
        }
        cursorUser.close();
    }

    public void DataDB(String CURP){
        DBHelper dbHelper = new DBHelper(context);

        SQLiteDatabase db=dbHelper.getWritableDatabase();

        UrDt user = null;
        Cursor cursorUser = null;

        cursorUser=db.rawQuery("SELECT * FROM "+TABLE_DATA+" WHERE CURP=" +"'"+CURP+"'", null);
        if(cursorUser.moveToFirst()){
            do{
                user = new UrDt();
                user.setNombre(cursorUser.getString(1));
                user.setApellido_P(cursorUser.getString(2));
                user.setApellido_M(cursorUser.getString(3));
                user.setNum(cursorUser.getString(4));
                user.setArea(cursorUser.getString(5));
                user.setCorreo(cursorUser.getString(6));
                user.setCelular(cursorUser.getString(7));
            } while (cursorUser.moveToNext());
        }
        cursorUser.close();
    }
}
