package sk.dominika.dluhy.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import sk.dominika.dluhy.databases_objects.Friend;

public class FriendsDBHandler extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "MyFriends.db";

        //table scheme
        public static class TableFriends implements BaseColumns {
            public static final String TABLE_NAME = "myFriends";
            public static final String COLUMN_NAME = "name";
            public static final String COLUMN_EMAIL = "email";
        }

        // SQL statements
        private static final String SQL_CREATE = "CREATE TABLE " + TableFriends.TABLE_NAME +
                " (" + "INTEGER PRIMARY KEY, " + TableFriends.COLUMN_NAME +
                " TEXT, " + TableFriends.COLUMN_EMAIL + " TEXT)";
        private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TableFriends.TABLE_NAME;


        public FriendsDBHandler(Context context) {
            super(context,DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE);
            onCreate(db);
        }

        //add my new friend from AddFriendActivity
        public void addFriendToDatabase(Friend friend) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TableFriends.COLUMN_NAME, friend.getName());
            values.put(TableFriends.COLUMN_EMAIL, friend.getEmail());

            //Insert Row
            db.insert(TableFriends.TABLE_NAME, null, values);
            db.close();
        }

        //read all friends from database
        public List<Friend> getFriendFromDatabase() {
            List<Friend> list = new ArrayList<Friend>();
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TableFriends.TABLE_NAME;
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do{
                    Friend fr = new Friend(cursor.getString(1), cursor.getString(2));
                    list.add(fr);
                }while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return list;
        }

        //delete whole database of friends
        public void deleteDatabase() {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TableFriends.TABLE_NAME, null, null);
        }
    }
