package sk.dominika.dluhy.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import sk.dominika.dluhy.databases_objects.Debt;

public class DebtsDBHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyDebts.db";

    //table scheme
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

    //SQL statements
    private static String SQL_CREATE = "CREATE TABLE " + TableDebts.TABLE_NAME +
            " (" + "INTEGER PRIMARY KEY, " + TableDebts.COLUMN_NAME +
            " TEXT, " + TableDebts.COLUMN_SUM +
            " TEXT, " + TableDebts.COLUMN_NOTE +
            " TEXT, " + TableDebts.COLUMN_DATE_OF_CREATION +
            " TEXT, " + TableDebts.COLUMN_TIME_OF_CREATION +
            " TEXT, " + TableDebts.COLUMN_ALERT_DATE +
            " TEXT, " + TableDebts.COLUMN_ALERT_TIME + " TEXt)";
    private static String SQL_DELETE = "DROP TABLE IF EXISTS " + TableDebts.TABLE_NAME;

    public DebtsDBHandler(Context context) {
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
}
