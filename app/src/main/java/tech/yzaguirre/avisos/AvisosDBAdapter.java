package tech.yzaguirre.avisos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.StringBuilderPrinter;

/**
 * Created by david on 22/04/16.
 */
public class AvisosDBAdapter {

    // Estos son los nombres de las columnas
    public static final String COL_ID = "_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMPORTANT = "important";

    // Estos son los indices correspondientes
    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;
    public static final int INDEX_IMPORTANT = INDEX_ID + 2;

    // Usado para logging
    public static final String TAG = "AVISOSDbAdapter";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public static final String DATABASE_NAME = "dba_remdrs";
    public static final String TABLE_NAME = "tbl_remdrs";
    public static final int DATABASE_VERSION = 1;

    private final Context ctx;

    // Declaración SQL usada para crear la base de datos
    public static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_CONTENT + " TEXT, " +
                    COL_IMPORTANT + " INTEGER );";

    public AvisosDBAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public void open() throws SQLException{
        dbHelper = new DatabaseHelper(ctx);
        db = dbHelper.getWritableDatabase();
    }
    public void close(){
        if (dbHelper != null){
            dbHelper.close();
        }
    }
    // CREATE
    public void createReminder(String name, boolean important){
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, name);
        values.put(COL_IMPORTANT, important?1:0);
        db.insert(TABLE_NAME, null, values);
    }
    public long createReminder(Aviso reminder){
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, reminder.getContent());
        values.put(COL_IMPORTANT, reminder.getImportant());

        return db.insert(TABLE_NAME, null, values);
    }
    public Aviso fetchReminderById(int id){
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_ID,
            COL_CONTENT, COL_IMPORTANT}, COL_ID + "=?",
            new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return new Aviso(cursor.getInt(INDEX_ID), cursor.getString(INDEX_CONTENT), cursor.getInt(INDEX_IMPORTANT));
    }
    public Cursor fetchAllReminders(){
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_ID, COL_CONTENT, COL_IMPORTANT
        }, null, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    // Update
    public void updateReminder(Aviso reminder){
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, reminder.getContent());
        values.put(COL_IMPORTANT, reminder.getImportant());
        db.update(TABLE_NAME, values, COL_ID + "=?", new String[]{String.valueOf(reminder.getId())});
    }
    // Delete
    public void deleteReminderById(int id){
        db.delete(TABLE_NAME, COL_ID + "=?", new String[]{id});
    }
    public void deleteAllReminders(){
        db.delete(TABLE_NAME, null, null);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Log.w(TAG, DATABASE_CREATE);
            sqLiteDatabase.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgarding database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
}
