package com.texttrans.translator.app_data.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.texttrans.translator.app_data.model.HistItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseReferHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "youtranslatehistory.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static final String DB_PATH_SUFFIX = "/databases/";
    static Context ctx;

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public DatabaseReferHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
        ctx = context;
    }

    public void CopyDataBaseFromAsset() throws IOException {
        InputStream open = ctx.getAssets().open(DATABASE_NAME);
        String databasePath = getDatabasePath();
        File file = new File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!file.exists()) {
            file.mkdir();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(databasePath);
        byte[] bArr = new byte[1024];
        while (true) {
            int read = open.read(bArr);
            if (read > 0) {
                fileOutputStream.write(bArr, 0, read);
            } else {
                fileOutputStream.flush();
                fileOutputStream.close();
                open.close();
                return;
            }
        }
    }

    private static String getDatabasePath() {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        File databasePath = ctx.getDatabasePath(DATABASE_NAME);
        if (!databasePath.exists()) {
            try {
                CopyDataBaseFromAsset();
                System.out.println("Copying success from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }
        return SQLiteDatabase.openDatabase(databasePath.getPath(), (SQLiteDatabase.CursorFactory) null, 268435472);
    }

    public void addSearchHistory(String str, String str2, String str3, String str4) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Lang1", str);
        contentValues.put("String1", str2);
        contentValues.put("Lang2", str3);
        contentValues.put("String2", str4);
        long insert = writableDatabase.insert("History", (String) null, contentValues);
        writableDatabase.close();
    }

    public List<HistItem> getSearchHistory() {
        ArrayList arrayList = new ArrayList();
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT * FROM History", (String[]) null);
        if (rawQuery.moveToFirst()) {
            do {
                HistItem histItem = new HistItem ();
                histItem.setId(rawQuery.getString(0));
                histItem.setLan1(rawQuery.getString(1));
                histItem.setStr1(rawQuery.getString(2));
                histItem.setLan2(rawQuery.getString(3));
                histItem.setStr2(rawQuery.getString(4));
                arrayList.add(histItem);
            } while (rawQuery.moveToNext());
        }
        return arrayList;
    }

    public boolean Delete_History_id(String str) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append("id=");
        sb.append(str);
        return writableDatabase.delete("History", sb.toString(), (String[]) null) > 0;
    }
}
