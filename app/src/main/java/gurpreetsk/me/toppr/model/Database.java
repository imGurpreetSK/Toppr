package gurpreetsk.me.toppr.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Gurpreet on 25/09/16.
 */

public class Database extends SQLiteOpenHelper {

    public static final int TABLE_VERSION = 1;

    public static final String TABLE_NAME = "TOPPR";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_EXPERIENCE = "EXPERIENCE";
    public static final String COLUMN_IMAGE = "IMAGE";
    public static final String COLUMN_CATEGORY = "CATEGORY";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_FAVORITE = "isFAVORITE";

    public static final String QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " TEXT PRIMARY KEY," +
            COLUMN_NAME + " TEXT, " +
            COLUMN_IMAGE + " TEXT," +
            COLUMN_CATEGORY + " TEXT, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_EXPERIENCE + " TEXT, " +
            COLUMN_FAVORITE + " TEXT);";

    public Database(Context context) {
        super(context, TABLE_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void update(String value, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(COLUMN_FAVORITE, value);

        db.update(TABLE_NAME, newValues, "id=" + id, null);
    }

    public void addEvents(Data events) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, events.getId());
            values.put(COLUMN_NAME, events.getName());
            values.put(COLUMN_IMAGE, events.getImage());
            values.put(COLUMN_CATEGORY, events.getCategory());
            values.put(COLUMN_DESCRIPTION, events.getDescription());
            values.put(COLUMN_EXPERIENCE, events.getExperience());
            values.put(COLUMN_FAVORITE, events.getFav());

            // Inserting Row
            db.insert(TABLE_NAME, null, values);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Data> getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Data> data = null;
        try {
            data = new ArrayList<>();
            String QUERY = "SELECT * FROM " + TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    Data events = new Data(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                    data.add(events);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return data;
    }

}
