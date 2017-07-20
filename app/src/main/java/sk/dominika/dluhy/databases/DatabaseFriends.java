package sk.dominika.dluhy.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class DatabaseFriends {

    private DatabaseFriends() {
    }

    public static class TableFriends implements BaseColumns {
        public static final String TABLE_NAME = "myFriends";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";
    }

    private static final String SQL_CREATE = "CREATE TABLE" + TableFriends.TABLE_NAME +
            " (" + TableFriends._ID + " INTEGER PRIMARY KEY," + TableFriends.COLUMN_NAME +
            " TEXT," + TableFriends.COLUMN_EMAIL + " TEXT)";
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TableFriends.TABLE_NAME;

    public class FriendsDBHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "MyFriends.db";

        public FriendsDBHelper(Context context) {
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
    }
}
