package com.one.daybook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by one on 4/19/2016.
 */
public class FolderListDB {

    private MyHelper m;
    SQLiteDatabase s;
    public FolderListDB(Context c){
        m=new MyHelper(c,"mydatabase",null,1);
    }
    public void openDB(){
        s=m.getWritableDatabase();
    }
    public void insertIntoDB(String s1){
        ContentValues cv=new ContentValues();
        cv.put("sub",s1);
        s.insert("folder",null,cv);

    }
    public Cursor displayDB(){
        Cursor c=s.query("folder",null,null,null,null,null,null);
        //Cursor c=s.query("folder",null,null,null,"parfolder",null,null);


        return  c;

    }

    private class MyHelper extends SQLiteOpenHelper {

        public MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table folder(_id integer primary key,sub text);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
