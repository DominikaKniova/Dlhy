package sk.dominika.dluhy.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import sk.dominika.dluhy.database_models.Debt;
import sk.dominika.dluhy.database_models.Friend;

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
        private static final String SQL_CREATE_FRIENDS =
                "CREATE TABLE " + TableFriends.TABLE_NAME +
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
        private static String SQL_CREATE_DEBTS =
                "CREATE TABLE " + TableDebts.TABLE_NAME +
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

        /**
         * Adds item_friend (Friend object) to database from AddFriendActivity.
         * @param friend The object to add.
         * @return ID of item_friend.
         */
        public long addFriendToDatabase(Friend friend) {

            // Gets the data repository in write mode
            SQLiteDatabase db = this.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(TableFriends.COLUMN_FIRSTNAME, friend.getFirstName());
            values.put(TableFriends.COLUMN_LASTNAME, friend.getLastName());
            values.put(TableFriends.COLUMN_EMAIL, friend.getEmail());

            //Insert Row and return new ID
            long newIdFriend = db.insert(TableFriends.TABLE_NAME, null, values);

            db.close();
            return newIdFriend;
        }

        /**
         * Gets whole database of my friends.
         * @return list of all my friends.
         */
        public List<Friend> getFriendsFromDatabase() {
                List<Friend> list_friends = new ArrayList<Friend>();
                SQLiteDatabase dbFriends = this.getReadableDatabase();
                String selectQuery = "SELECT * FROM " + TableFriends.TABLE_NAME;
                Cursor cursor = dbFriends.rawQuery(selectQuery, null);

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do{
                        //cursor gets FIRSTNAME, LASTNAME, EMAIL
                        Friend fr = new Friend(
                                cursor.getString(cursor.getColumnIndex("firstname")),
                                cursor.getString(cursor.getColumnIndex("lastname")),
                                cursor.getString(cursor.getColumnIndex("email")));
                        fr.setId(cursor.getString(cursor.getColumnIndex(TableFriends._ID)));
                        list_friends.add(fr);
                    }while (cursor.moveToNext());
                }
                cursor.close();
                dbFriends.close();
                return list_friends;
            }

        /**
         * Adds item_debt_all (Debt object) to database from NewDebtActivity
         * @param debt Object to be added.
         * @return ID of item_debt_all.
         */
        public long addDebtToDatabase(Debt debt) {
                // Gets the data repository in write mode
                SQLiteDatabase db = this.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                //values.put(TableDebts.COLUMN_FRIEND_ID, debt.getId_friend());
                //values.put(TableDebts.COLUMN_FRIEND_NAME, debt.getFriend_name());
                values.put(TableDebts.COLUMN_SUM, debt.getSum());
                values.put(TableDebts.COLUMN_NOTE, debt.getNote());
                values.put(TableDebts.COLUMN_DATE_OF_CREATION, "test");
                values.put(TableDebts.COLUMN_TIME_OF_CREATION, "test");
                values.put(TableDebts.COLUMN_ALERT_DATE, "test");
                values.put(TableDebts.COLUMN_ALERT_TIME, "test");
                values.put(TableDebts.COLUMN_PAID, "false");

                //Insert Row and return new ID
                long newIDdebt = db.insert(TableDebts.TABLE_NAME, null, values);

                db.close();

                return newIDdebt;

            }

            /**
             * Gets whole database of my debts
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
                        //cursor gets ID OD FRIEND, NAME OF FRIEND, SUM, NOTE
//                        Debt d = new Debt(
//                                cursor.getString(cursor.getColumnIndex("id_friend")),
//                                cursor.getString(cursor.getColumnIndex("friend_name")),
//                                cursor.getFloat(cursor.getColumnIndex("sum")),
//                                cursor.getString(cursor.getColumnIndex("note")));
//                        list_debts.add(d);
                    }while (cursor.moveToNext());
                }
                cursor.close();
                dbDebts.close();

                return list_debts;
            }

            /**
             * Finds the item_friend through ID in database.
             * @param friend_id
             * @return Firstname of item_friend.
             */
            public String getNameFromDatabase(long friend_id) {
                String name = "";
                SQLiteDatabase dbFriends = this.getReadableDatabase();
                String selectQuery =
                        "SELECT " + TableFriends.COLUMN_FIRSTNAME +
                        " FROM " + TableFriends.TABLE_NAME +
                        " WHERE " + TableFriends._ID + "='" + friend_id +"'";
                Cursor cursor = dbFriends.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    name = cursor.getString(cursor.getColumnIndex("firstname"));
                }

                cursor.close();
                dbFriends.close();

                return name;
            }

    /**
     * Finds item_friend based ib id.
     * @param id
     * @return Bundle object containing name, email of item_friend and the overall sum of our debts.
     */
    public Bundle getFriend(long id) {
        Bundle bundle = new Bundle();

        SQLiteDatabase dFriends = this.getReadableDatabase();
        String selectQuery =
                        "SELECT " + TableFriends.COLUMN_FIRSTNAME + ", "
                                + TableFriends.COLUMN_LASTNAME +
                        " FROM " + TableFriends.TABLE_NAME +
                        " WHERE " + TableFriends._ID + "='" + id + "'";
        Cursor cursor = dFriends.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            bundle.putString("firstname", cursor.getString(cursor.getColumnIndex(TableFriends.COLUMN_FIRSTNAME)));
            bundle.putString("lastname", cursor.getString(cursor.getColumnIndex(TableFriends.COLUMN_LASTNAME)));
//            bundle.putString("email", cursor.getString(cursor.getColumnIndex("email")));
//            bundle.putString("sum", cursor.getString(cursor.getColumnIndex("overall_sum")));
        }

        cursor.close();
        dFriends.close();

        return bundle;
    }

    /**
     * Gets only the debts created with the item_friend
     * @param friend_id
     * @return list of debts shared with the item_friend
     */
    public List<Debt> getOurDebtsWithFriend(long friend_id) {
        List<Debt> list_debts = new ArrayList<Debt>();
        SQLiteDatabase dbDebts = getReadableDatabase();
        String selectQuery =
                "SELECT * FROM " + TableDebts.TABLE_NAME +
                " WHERE " + TableDebts.COLUMN_FRIEND_ID + "='" + friend_id + "'";
        Cursor cursor = dbDebts.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
//                Debt d = new Debt(
//                        cursor.getString(cursor.getColumnIndex("id_friend")),
//                        cursor.getString(cursor.getColumnIndex("friend_name")),
//                        cursor.getFloat(cursor.getColumnIndex("sum")),
//                        cursor.getString(cursor.getColumnIndex("note"))
//                );
//                list_debts.add(d);
            }while (cursor.moveToNext());
        }

        cursor.close();
        dbDebts.close();

        return list_debts;
    }
}
