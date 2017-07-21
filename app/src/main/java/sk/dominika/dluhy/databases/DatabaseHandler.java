package sk.dominika.dluhy.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import sk.dominika.dluhy.databases_objects.Debt;
import sk.dominika.dluhy.databases_objects.Friend;

public class DatabaseHandler extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "MyDatabase.db";

        //table scheme_FRIENDS
        public static class TableFriends implements BaseColumns {
            public static final String TABLE_NAME = "myFriends";
            public static final String COLUMN_NAME = "name";
            public static final String COLUMN_EMAIL = "email";
        }

        // SQL statements_FRIENDS
        private static final String SQL_CREATE_FRIENDS = "CREATE TABLE " + TableFriends.TABLE_NAME +
                " (" + "INTEGER PRIMARY KEY, " + TableFriends.COLUMN_NAME +
                " TEXT, " + TableFriends.COLUMN_EMAIL + " TEXT)";
        private static final String SQL_DELETE_FRIENDS = "DROP TABLE IF EXISTS " + TableFriends.TABLE_NAME;

        //table scheme_DEBTS
        public static class TableDebts implements BaseColumns {
            public static final String TABLE_NAME = "myDebts";
            public static final String COLUMN_NAME = "name";
            public static final String COLUMN_SUM = "sum";
            public static final String COLUMN_NOTE = "note";
            public static final String COLUMN_DATE_OF_CREATION = "date_created";
            public static final String COLUMN_TIME_OF_CREATION = "time_created";
            public static final String COLUMN_ALERT_DATE = "date_alert";
            public static final String COLUMN_ALERT_TIME = "time_alert";
        }

        //SQL statements_DEBTS
        private static String SQL_CREATE_DEBTS = "CREATE TABLE " + TableDebts.TABLE_NAME +
                " (" + "INTEGER PRIMARY KEY, " + TableDebts.COLUMN_NAME +
                " TEXT, " + TableDebts.COLUMN_SUM +
                " TEXT, " + TableDebts.COLUMN_NOTE +
                " TEXT, " + TableDebts.COLUMN_DATE_OF_CREATION +
                " TEXT, " + TableDebts.COLUMN_TIME_OF_CREATION +
                " TEXT, " + TableDebts.COLUMN_ALERT_DATE +
                " TEXT, " + TableDebts.COLUMN_ALERT_TIME + " TEXt)";
        private static String SQL_DELETE_DEBTS = "DROP TABLE IF EXISTS " + TableDebts.TABLE_NAME;


        public DatabaseHandler(Context context) {
            super(context,DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_FRIENDS);
            db.execSQL(SQL_CREATE_DEBTS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_FRIENDS);
            db.execSQL(SQL_DELETE_DEBTS);
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

    //add new debt to database from NewDebtActivity
    public void addDebtToDatabase(Debt debt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableDebts.COLUMN_NAME, debt.getName());
        values.put(TableDebts.COLUMN_SUM, debt.getSum());
        values.put(TableDebts.COLUMN_NOTE, debt.getNote());
        values.put(TableDebts.COLUMN_DATE_OF_CREATION, "test");
        values.put(TableDebts.COLUMN_TIME_OF_CREATION, "test");
        values.put(TableDebts.COLUMN_ALERT_DATE, "test");
        values.put(TableDebts.COLUMN_ALERT_TIME, "test");

        //Insert Row
        db.insert(TableDebts.TABLE_NAME, null, values);
        db.close();
    }

        //delete whole database of friends
        public void deleteDatabase() {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TableFriends.TABLE_NAME, null, null);
        }
    }
