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
            public static final String COLUMN_FIRSTNAME = "firstname";
            public static final String COLUMN_LASTNAME = "lastname";
            public static final String COLUMN_EMAIL = "email";
        }

        // SQL statements_FRIENDS
        private static final String SQL_CREATE_FRIENDS = "CREATE TABLE " + TableFriends.TABLE_NAME +
                " (" + TableFriends._ID + " INTEGER PRIMARY KEY, " + TableFriends.COLUMN_FIRSTNAME +
                " TEXT, " + TableFriends.COLUMN_LASTNAME +
                " TEXT, " + TableFriends.COLUMN_EMAIL + " TEXT)";
        private static final String SQL_DELETE_FRIENDS = "DROP TABLE IF EXISTS " + TableFriends.TABLE_NAME;

        //table scheme_DEBTS
        public static class TableDebts implements BaseColumns {
            public static final String TABLE_NAME = "myDebts";
            public static final String COLUMN_FRIEND_ID = "id_friend";
            public static final String COLUMN_FRIEND_NAME = "friend_name";
            public static final String COLUMN_SUM = "sum";
            public static final String COLUMN_NOTE = "note";
            public static final String COLUMN_DATE_OF_CREATION = "date_created";
            public static final String COLUMN_TIME_OF_CREATION = "time_created";
            public static final String COLUMN_ALERT_DATE = "date_alert";
            public static final String COLUMN_ALERT_TIME = "time_alert";
            public static final String COLUMN_PAID = "paid";
        }

        //SQL statements_DEBTS
        private static String SQL_CREATE_DEBTS = "CREATE TABLE " + TableDebts.TABLE_NAME +
                " (" + TableDebts._ID + " INTEGER PRIMARY KEY, " + TableDebts.COLUMN_FRIEND_ID +
                " TEXT, " + TableDebts.COLUMN_FRIEND_NAME +
                " TEXT, " + TableDebts.COLUMN_SUM +
                " REAL, " + TableDebts.COLUMN_NOTE +
                " TEXT, " + TableDebts.COLUMN_DATE_OF_CREATION +
                " TEXT, " + TableDebts.COLUMN_TIME_OF_CREATION +
                " TEXT, " + TableDebts.COLUMN_ALERT_DATE +
                " TEXT, " + TableDebts.COLUMN_ALERT_TIME +
                " TEXT, " + TableDebts.COLUMN_PAID + " TEXT)";
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
        //returns friend's ID
        public long addFriendToDatabase(Friend friend) {

            // Gets the data repository in write mode
            SQLiteDatabase db = this.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(TableFriends.COLUMN_FIRSTNAME, friend.getFirstName());
            values.put(TableFriends.COLUMN_LASTNAME, friend.getLastName());
            values.put(TableFriends.COLUMN_EMAIL, friend.getEmail());

            //Insert Row and return new ID
            long newId = db.insert(TableFriends.TABLE_NAME, null, values);

//            //get id of friend
//            String selectQuery = "SELECT * FROM " + TableFriends.TABLE_NAME;
//            Cursor cursor = db.rawQuery(selectQuery, null);
//            cursor.moveToLast();

            db.close();
            return newId;
        }

        //read all friends from database
        public List<Friend> getFriendsFromDatabase() {
            List<Friend> list_friends = new ArrayList<Friend>();
            SQLiteDatabase dbFriends = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TableFriends.TABLE_NAME;
            Cursor cursor = dbFriends.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do{
                    //cursor get FIRSTNAME, LASTNAME, EMAIL
                    Friend fr = new Friend(
                            cursor.getString(cursor.getColumnIndex("firstname")),
                            cursor.getString(cursor.getColumnIndex("lastname")),
                            cursor.getString(cursor.getColumnIndex("email")));
                    fr.setId(new Long(cursor.getString(0)));
                    list_friends.add(fr);
                }while (cursor.moveToNext());
            }
            cursor.close();
            dbFriends.close();
            return list_friends;
        }

    //add new debt to database from NewDebtActivity
    public void addDebtToDatabase(Debt debt) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TableDebts.COLUMN_FRIEND_ID, debt.getId_friend());
        values.put(TableDebts.COLUMN_FRIEND_NAME, debt.getFriend_name());
        values.put(TableDebts.COLUMN_SUM, debt.getSum());
        values.put(TableDebts.COLUMN_NOTE, debt.getNote());
        values.put(TableDebts.COLUMN_DATE_OF_CREATION, "test");
        values.put(TableDebts.COLUMN_TIME_OF_CREATION, "test");
        values.put(TableDebts.COLUMN_ALERT_DATE, "test");
        values.put(TableDebts.COLUMN_ALERT_TIME, "test");
        values.put(TableDebts.COLUMN_PAID, "false");

        //Insert Row
        db.insert(TableDebts.TABLE_NAME, null, values);
        db.close();
    }

    //read all debts from database

    /**
     * Get whole database of my debts
     * @return list of all my debts
     */
    public List<Debt> getMyDebtsFromDatabase(){
        List<Debt> list_debts = new ArrayList<Debt>();
        SQLiteDatabase dbDebts = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TableDebts.TABLE_NAME;
        Cursor cursor = dbDebts.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do{
                //cursor get ID OD FRIEND, NAME OF FRIEND, SUM, NOTE
                Debt d = new Debt(
                        cursor.getInt(cursor.getColumnIndex("id_friend")),
                        cursor.getString(cursor.getColumnIndex("friend_name")),
                        cursor.getFloat(cursor.getColumnIndex("sum")),
                        cursor.getString(cursor.getColumnIndex("note")));
                list_debts.add(d);
            }while (cursor.moveToNext());
        }
        cursor.close();
        dbDebts.close();
        return list_debts;
    }

    /**
     * Finds the friend through ID in database.
     * @param friend_id
     * @return Firstname of friend.
     */
    public String getNameFromDatabase(long friend_id) {
        String name = "";
        SQLiteDatabase dbFriends = this.getReadableDatabase();
        String selectQuery = "SELECT " + DatabaseHandler.TableFriends.COLUMN_FIRSTNAME + " FROM "
                + DatabaseHandler.TableFriends.TABLE_NAME + " WHERE " + DatabaseHandler.TableFriends._ID
                + "='" + friend_id +"'";
        Cursor cursor = dbFriends.rawQuery(selectQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex("firstname"));
        }

        return name;
    }
}
