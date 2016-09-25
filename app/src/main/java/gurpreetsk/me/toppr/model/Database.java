package gurpreetsk.me.toppr.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gurpreet on 25/09/16.
 */

public class Database extends SQLiteOpenHelper {

    public static final String name = "toppr";
    public static final int version = 1;

    public static final String TABLE_NAME = "FAVORITE";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_EXPERIENCE = "EXPERIENCE";
    public static final String COLUMN_IMAGE = "IMAGE";
    public static final String COLUMN_CATEGORY = "CATEGORY";
    public static final String COLUMN_ID = "ID";

    public static final String QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " TEXT PRIMARY KEY," +
            COLUMN_NAME + " TEXT, " +
            COLUMN_CATEGORY + " TEXT, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_EXPERIENCE + " TEXT, " +
            COLUMN_IMAGE + " TEXT);";

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
